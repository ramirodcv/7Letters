package utils;

import java.util.Arrays;

import static utils.Constants.*;

/**
 * this tree is inspired by a bit tree, except it represents sets of chars.
 */
public class LetterNodeTree {
    // stores self. see constructor for more info
    private static LetterNodeTree self = null;
    private LetterNode mHead = new LetterNode();
    private int mComboSize;

    private int mMaxCount = 0;
    private int mBestCombo = 0;

    private LetterNodeTree() {
        empty();
    }

    // returns a stored object
    // this class is instantiated once at most
    public static LetterNodeTree getTree() {
        if(self == null) {
            self = new LetterNodeTree();
        }
        return self;
    }

    /**
     * empty this tree of stored strings
     */
    public void empty() {
        mHead.addBranches(NUM_LETTERS);
        mMaxCount = 0;
        mBestCombo = 0;
    }

    public void setComboSize(int comboSize) {
        mComboSize = comboSize;
    }

    /**
     * add a new word to the tree
     * @param word the word to be added to the tree (should be in bit form)
     */
    public void add(int word) {
        int index = 0;
        int branchCount = NUM_LETTERS - 1; // number of branches the next node should have
        LetterNode current = mHead;

        // while the word is not processed
        while(word > 0) {
            if((word & 1) == 1) {
                // move down the correct branch
                current = current.passWord(index, branchCount);
                // the next node has 1 less branch. Decrement index.
                index = -1;
            }
            index++;
            branchCount--;
            word >>>= 1;
        }
        current.mCount++;
    }

    /**
     * scores a combo of letters
     * @param combo each index represents a letter based on Constants.LETTERS
     */
    public void scoreCombo(int[] combo) {
        // check if the branch to go down has been crated
        if(mHead.mNextNodes[combo[0]] == null) {
            return;
        }

        // only score the combo if it has potential to beat mMaxCount
        if(mHead.mNextNodes[combo[0]].mWordsPassed > mMaxCount) {
            int count = getCount(mHead, arrayToInt(combo));

            // check if new mMaxCount has been found
            synchronized (this) {
                if(count > mMaxCount) {
                    mMaxCount = count;
                    mBestCombo = arrayToInt(combo);
                }
            }
        }
    }

    // acts recursively to count how many words the combo can make
    private int getCount(LetterNode node, int combo) {
        int count = node.mCount;
        int index = 0;

        // while the combo is not fully processed
        while(combo > 0) {
            // travel down a branch, because it represents the combo
            if((node.mNextNodes[index] != null) && (combo & 1) == 1) {
                combo >>>= 1;
                count += getCount(node.mNextNodes[index], combo);

            // branch does not represent the combo
            } else {
                combo >>>= 1;
            }
            index++;
        }
        return count;
    }

    // returns the array as a bit representation of letters
    private int arrayToInt(int[] combo) {
        int letters = 0;
        // for each
        for(int i = 0; i < mComboSize; i++) {
            letters |= (1 << combo[i]);
        }
        return letters;
    }

    // get the best combo as a string
    public String getBestCombo() {
        return intToString(mBestCombo);
    }

    public int getMaxCount() {
        return mMaxCount;
    }

    // returns the bit representation of letters as a string
    public static String intToString(int letters) {
        String s = "";
        for (int i = 0; i < NUM_LETTERS; i ++) {
            if((letters & 1 << i) >= 1) {
                s+= LETTERS[i];
            }
        }
        return s;
    }

    // nodes of the tree
    // the number of branches for each node depends on how many letters have been searched by prior nodes.
    // i.e if 'e' (the most frequent letter) is represented by the parent node,
    // then this node will not have a branch representing 'e'
    protected class LetterNode {
        protected LetterNode[] mNextNodes; // branches
        protected int mCount;       // words ending at this node
        protected int mWordsPassed; // words passed through this node

        protected void addBranches(int numBranches) {
            mNextNodes = new LetterNode[numBranches];
        }

        protected LetterNode passWord(int index, int nextBranchCount) {
            if(mNextNodes[index] == null) {
                mNextNodes[index] = new LetterNode();
            }

            if(mNextNodes[index].mNextNodes == null) {
                mNextNodes[index].addBranches(nextBranchCount);
            }
            mWordsPassed++;
            return mNextNodes[index];
        }
    }
}


