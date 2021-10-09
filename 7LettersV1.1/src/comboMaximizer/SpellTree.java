package comboMaximizer;

public class SpellTree {
    public static final int BAD_VALUE = -1;
    public static final char MIN_CHAR = 'a';
    public static final char MAX_CHAR = 'z';

    /**
     * Get the number of unique letters in a bit word
     * @param word a word in bit form
     * @return the number of letters in the bit word
     */
    public static int length(int word) {
        int length = 0;
        while(word > 0) {
            if((word & 1) == 1) { length++; }
            word >>>= 1;
        }
        return length;
    }

    /**
     * Convert the given letter to lowercase if it is a letter
     * @param c the character to convert
     * @return the letter in lowercase form
     */
    public static char toLower(char c) {
        // if capital letter
        if(c >= 'A' && c <= 'Z') {
            return (char) (c - 'A' + 'a');
        }
        // not capital letter
        return c;
    }

    // number of soft resets performed
    // hard reset: re-instantiating root_
    // soft reset: same root_
    private int resetCount_;
    // available letters to use when spelling
    private Letters letters_;
    // root Node
    private Node root_;
    // max word length (can be anything, but set lower for better optimization)
    private int maxLetters_;



    /**
     * Instantiate a SpellTree that counts word spellings with the given letters up to the maxLetter count
     * i.e. 'a', 'c', and 't' spell "act", "at", and ... with maxLetter length=3
     * @param letters letters to account for in spelling checks
     * @param maxLetters the maximum number of letters to account for in spelling checks
     */
    public SpellTree(String letters, int maxLetters) {
        resetCount_ = 0;
        letters_ = new Letters(letters);
        maxLetters_ = Math.min(maxLetters, letters_.size());
        root_ = new Node(letters_.size());
    }

    /**
     * Instantiate a SpellTree that counts word spellings with the given letters (maxLetters = letters.size())
     * @param letters letters to account for in the spelling checks
     */
    public SpellTree(String letters) {
        resetCount_ = 0;
        letters_ = new Letters(letters);
        maxLetters_ = letters_.size();
        root_ = new Node(letters_.size());
    }



    /**
     * Reset this spell tree with new letters and letter length (essentially new instance)
     * @param letters letters to account for in spelling checks
     * @param maxLetters the maximum number of letters to account for in spelling checks
     */
    public void reset(String letters, int maxLetters) {
        // softReset
        if(letters_.equals(letters_) && maxLetters_ >= maxLetters) {
            resetCount_++;
            return;
        }
        // hard-reset
        resetCount_ = 0;
        letters_.set(letters);
        maxLetters_ = Math.min(maxLetters, letters_.size());
        synchronized (root_) {
            root_ = new Node(letters_.size());
        }
    }

    /**
     * Reset this spell tree with a new letter length (clears existing data)
     * @param maxLetters the maximum number of letters to account for in spelling checks
     */
    public void reset(int maxLetters) {
        // softReset
        if(maxLetters_ >= maxLetters) {
            resetCount_++;
            return;
        }
        // hard-reset
        resetCount_ = 0;
        maxLetters_ = maxLetters;
        synchronized (root_) {
            root_ = new Node(letters_.size());
        }
    }

    /**
     * Reset this spell tree with new letters (clears existing data)
     * @param letters the letters to use in the new reset structure
     */
    public void reset(String letters) {
        // soft-reset
        if(letters_.equals(letters)) {
            resetCount_++;
            return;
        }
        // hard-reset
        letters_.set(letters);
        synchronized (root_) {
            root_ = new Node(letters_.size());
        }
    }

    /**
     * Reset this spell tree (clears existing data)
     * @param hardReset if true the tree structure will be deconstructed
     */
    public void reset(boolean hardReset) {
        if(!hardReset) {
            resetCount_++;
            return;
        }
        resetCount_ = 0;
        synchronized (root_) {
            root_ = new Node(letters_.size());
        }
    }

    /**
     * Reset this spell tree (clears existing data)
     */
    public void reset() {
        // soft-reset
        resetCount_++;
    }


    /**
     * Get the maximum number of letters to consider when adding words
     * @return maximum number of letters considered by this SpellTree
     */
    public int getMaxLetters() {
        return maxLetters_;
    }



    /**
     * Add a word to the tree structure
     * @param word bit representation of a word (use toInt() for conversion)
     * @param wordLength the length of the word
     * @return true if and only if the word was added
     */
    private boolean add(int word, int wordLength) {
        // word too long do not add
        if(wordLength > maxLetters_) {
            return false;
        }

        int index = 0;
        Node current = root_;
        // while there letters still in the word
        while (word > 0) {
            // word has the letter activated
            if((word & 1) == 1) {
                // get next current and increment counts
                synchronized (root_) {
                    current = current.getChild(index, true);
                    current.incrementPass();
                }
                // reset index
                index = -1;
            }
            // increment word and index
            index++;
            word >>>= 1;
        }
        current.incrementCount();
        return true;
    }

    /**
     * Add a word to the tree structure
     * @param word bit representation of a word (use toInt() for conversion)
     * @param checkLength if true, the length of the word will be checked
     * @return true if and only if the word was added
     */
    public boolean add(int word, boolean checkLength) {
        if(!checkLength) {
            return add(word, 0);
        }
        return add(word, length(word));
    }

    /**
     * Add a word to the tree structure (length will be checked by default)
     * @param word bit representation of a word (use toInt() for conversion)
     * @return true if and only if the word was added
     */
    public boolean add(int word) {
        return add(word, length(word));
    }

    /**
     * Add a word to the tree structure
     * @param word String representation of a word
     * @param checkLength if true, the length of the word will be checked
     * @return true if and only if the word was added
     */
    public boolean add(String word, boolean checkLength) {
        int bitWord = toInt(word, checkLength);
        if(bitWord == BAD_VALUE) { return false; }
        return add(bitWord, 0);
    }

    /**
     * Add a word to the tree structure (length will be checked by default)
     * @param word String representation of a word
     * @return true if and only if the word was added
     */
    public boolean add(String word) {
        int bitWord = toInt(word, true);
        if(bitWord == BAD_VALUE) { return false; }
        return add(bitWord, 0);
    }



    /**
     * Count the number of words that can be spelled given the combination of letters (in bit form)
     * @param combo the combination of letters in bit form (use toInt())
     * @return the number of words that can be spelled with the given combination of letters
     */
    public int count(int combo) {
        return countHelper(root_, combo);
    }

    /**
     * Count the number of words that can be spelled given the combination of letters
     * @param combo the combination of letters as a String
     * @return the number of words that can be spelled with the given combination of letters
     */
    public int count(String combo) {
        return countHelper(root_, toInt(combo));
    }

    /**
     * recursively gets the counts of for this combo for the subtree formed by node
     * @param node the root Node of the subtree to get counts for
     * @param combo the combination to get counts for in bit form
     * @return
     */
    private int countHelper(Node node, int combo) {
        // get the counts for node


        int count = node.getCount_();
        int index = 0;
        Node next;
        // while combo has at least 1 unchecked letter
        while(combo > 0) {
            // letter is used in the combo
            if((combo & 1) == 1) {
                // get the next node
                next = node.getChild(index, false);
                // recurse if node exists
                if(next != null) {
                    count += countHelper(next, combo >>> 1);
                }
            }
            // increment count and combo
            combo >>>= 1;
            index++;
        }
        return count;
    }



    /**
     * Get the given word as a String
     * @param word the word as a string
     * @param checkLength when true, this function will return BAD_VALUE if the word has more letters than maxLetters
     * @return the wird in bit form
     */
    public int toInt(String word, boolean checkLength) {
        int res = 0;
        int letter = 0;
        int count = 0;

        // for each letter in word
        for(int i = 0; i < word.length(); i++) {
            letter = letters_.toIndex(word.charAt(i));
            // if the letter is valid
            if(letter != BAD_VALUE) {
                // break if too long
                if(checkLength && count == maxLetters_ && ((1 << letter) & res) == 0) { return BAD_VALUE; }
                // add to the result
                res |= (1 << letter);
            }
        }
        return res;
    }

    /**
     * Get the given word as in bit form (ignores chars unspecified in instantiation or reset)
     * @param word the word as a String
     * @return the word in bit form
     */
    public int toInt(String word) {
        return toInt(word, false);
    }

    /**
     * Get the given character in bit form
     * @param c the character to convert
     * @return the bit representation of the character
     */
    public int toInt(char c) {
        return letters_.toIndex(c);
    }

    /**
     * Convert the given word in bit form to a String
     * @param word the word in bit form
     * @return the word in String form
     */
    public String toString(int word) {
        String res = "";
        int index = 0;
        int letter = 0;

        // while there are letters in the bit word
        while(word > 0) {
            // if the letter is in the bit word
            if((word & 1) == 1) {
                // get the letter
                letter = letters_.toChar(index);
                // add it to the result if it was specified in instantiation or reset
                if(letter != BAD_VALUE) { res += (char) letter; }
            }
            // increment index and the word
            index++;
            word >>>= 1;
        }
        return res;
    }

    /**
     * Check if the given letter is in the letter range
     * @param c the letter to check
     * @return returns true if and only if the character is in the given range
     */
    private boolean isUsable(int c) {
        return c >= MIN_CHAR && c <= MAX_CHAR;
    }



    /**
     * Stores Letters (put most used letters 1st for faster runtime)
     */
    private class Letters {
        private String indexToChar_; // used to go from index values to char values
        private int[] charToIndex_;  // used to go from char values to index values.

        /**
         * Instantiate a letters object (put letters in order from most to least used to optimize runtime)
         * @param letters
         */
        private Letters(String letters) {
            charToIndex_ = new int[MAX_CHAR - MIN_CHAR + 1];
            set(letters);
        }

        /**
         * Set this object to represent the given letters
         * @param letters the letters to represent
         */
        private void set(String letters) {
            if(letters.length() == 0) {
                throw new IllegalArgumentException("Cannot initialize a Spell Tree using an empty letter String.");
            }

            // zero global variables
            indexToChar_ = "";
            for(int i = 0; i < MAX_CHAR - MIN_CHAR + 1; i++) {
                charToIndex_[i] = BAD_VALUE;
            }

            // use letters param to initialize global variables
            int usedLetters = 0;
            int c;
            for(int i = 0; i < letters.length(); i++) {

                c = toLower(letters.charAt(i)) - MIN_CHAR;
                // if usable letter and has not been used yet
                if(isUsable(c + MIN_CHAR) && ((1 << c) & usedLetters) == 0) {
                    usedLetters |= (1 << c);
                    charToIndex_[c] = indexToChar_.length();
                    indexToChar_ += (char) (c + MIN_CHAR);
                }
            }
        }

        /**
         * Convert the given index to a character
         * @param i the index (bit index)
         * @return returns the ASCII value of the character (BAD_VALUE for unknown index)
         */
        private int toChar(int i) {
            if(i < 0 || i >= indexToChar_.length()) {
                return BAD_VALUE;
            }
            return indexToChar_.charAt(i);
        }

        /**
         * Convert the given character to an index
         * @param c the character to convert
         * @return the index of the given character
         */
        private int toIndex(char c) {
            int i = toLower(c) - MIN_CHAR;
            // if bad index
            if(i < 0 || i >= charToIndex_.length) {
                return BAD_VALUE;
            }
            return charToIndex_[i];
        }

        /**
         * returns the number of letters represented by this object
         * @return the number of letters in this object
         */
        private int size() {
            return indexToChar_.length();
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof Letters) {
                return indexToChar_.equals(((Letters) obj).indexToChar_);
            }
            return indexToChar_.equals(obj);
        }
    }

    /**
     * Node to represent a letter in the SpellTree
     */
    private class Node {
        private Node[] children_;
        private int passed_;
        private int count_;
        private int resetNum_; // the resetCount_ this Node's data relate to

        /**
         * Instantiate a Node
         * @param childCount the number of children this Node has
         */
        private Node(int childCount) {
            children_ = new Node[childCount];
            softReset();
        }

        /**
         * get the child of this Node
         * @param i the index of the child
         * @param makeChild if and only if true, make a new child Node if one does not exist
         * @return the specified child Node
         */
        private synchronized Node getChild(int i, boolean makeChild) {
            if(children_[i] == null && makeChild) {
                children_[i] = new Node(children_.length - i - 1);
            }
            return children_[i];
        }

        /**
         * Increment the number of words that passed this Node
         */
        private synchronized void incrementPass() {
            if(resetNum_ != resetCount_) {
                softReset();
            }
            passed_++;
        }

        /**
         * Increment the number of count of words that are represented by this Node
         */
        private synchronized void incrementCount() {
            if(resetNum_ != resetCount_) {
                softReset();
            }
            count_++;
        }

        /**
         * Get the number of words that passed through this Node
         * @return the number of words that passed through this Node
         */
        private synchronized int getPassed() {
            if(resetNum_ != resetCount_) {
                softReset();
            }
            return passed_;
        }

        /**
         * Get the number of words that are represented by this Node
         * @return the number of words that represent each Node
         */
        private synchronized int getCount_() {
            if(resetNum_ != resetCount_) {
                softReset();
            }
            return count_;
        }

        /**
         * Soft-reset this Node
         */
        private void softReset() {
            passed_ = 0;
            count_ = 0;
            resetNum_ = resetCount_;
        }
    }
}
