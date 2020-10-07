package utils;

import utils.Constants;

import static utils.Constants.*;

public class LetterNodeTree {
    private static LetterNodeTree self = null;
    private LetterNode mHead = new LetterNode();

    private int mMaxCount = 0;
    private int mBestCombo = 0;

    private LetterNodeTree() {
        mHead.addBranches(Constants.NUM_LETTERS);
    }

    public static LetterNodeTree getTree() {
        if(self == null) {
            self = new LetterNodeTree();
        }
        return self;
    }

    public void add(int word) {
        int index = 0;
        int branchCount = NUM_LETTERS - 1;
        LetterNode current = mHead;

        while(word > 0) {
            if((word & 1) == 1) {
                current = current.passWord(index, branchCount);
                index = -1;
            }
            index++;
            branchCount--;
            word >>>= 1;
        }
        current.mCount++;
    }

    public void scoreCombo(int[] combo) {
        if(mHead.mNextNodes[combo[0]] == null) {
            return;
        }
        if(mHead.mNextNodes[combo[0]].mWordsPassed > mMaxCount) {
            int count = getCount(mHead, arrayToInt(combo));
            synchronized (this) {
                if(count > mMaxCount) {
                    mMaxCount = count;
                    mBestCombo = arrayToInt(combo);
                }
            }
        }
    }

    private int getCount(LetterNode node, int word) {
        int count = node.mCount;
        int index = 0;

        while(word > 0) {
            if((node.mNextNodes[index] != null) && (word & 1) == 1) {
                word >>>= 1;
                count += getCount(node.mNextNodes[index], word);
            } else {
                word >>>= 1;
            }
            index++;
        }
        return count;
    }

    private static  int arrayToInt(int[] combo) {
        int letters = 0;

        // for each
        for(int i = 0; i < LETTERS_PER_COMBO; i++) {
            letters |= (1 << combo[i]);
        }
        return letters;
    }

    public String getBestCombo() {
        return intToString(mBestCombo);
    }

    public int getMaxCount() {
        return mMaxCount;
    }

    public static String intToString(int letters) {
        String s = "";
        for (int i = 0; i < NUM_LETTERS; i ++) {
            if((letters & 1 << i) >= 1) {
                s+= LETTERS[i];
            }
        }
        return s;
    }

    protected class LetterNode {
        protected LetterNode[] mNextNodes;
        protected int mCount;
        protected int mWordsPassed;

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


