package ComboChecker;


import utils.LetterNodeTree;

import static utils.Constants.LETTERS_PER_COMBO;
import static utils.Constants.NUM_LETTERS;

public class ComboChecker implements Runnable {


    private int mStart;
    private int mEnd;
    private LetterNodeTree mTree;
    private ComboScorer mStarter;

    public ComboChecker(int start, int end, LetterNodeTree tree, ComboScorer starter) {
        mStart = start;
        mEnd = end;
        mTree = tree;
        mStarter = starter;
    }

    /**
     * get the max count for this checker
     */
    @Override
    public void run() {
        // combo: each letter is ranked by their frequency index
        int[] combo = new int[LETTERS_PER_COMBO];

        // make the first combo: {0, 1, 2, 3,...}
        for (int i = 0; i < LETTERS_PER_COMBO; i++) {
            combo[i] = i + mStart;
        }

        // while there are more combos to check
        while(combo[0] < mEnd) {
            mTree.scoreCombo(combo);

            // find the next combo
            // see: https://www.baeldung.com/java-combinations-algorithm
            int t = LETTERS_PER_COMBO - 1;
            while (t != 0 && combo[t] == NUM_LETTERS - LETTERS_PER_COMBO + t) {
                t--;
            }
            combo[t]++;
            for(int x = t + 1; x < LETTERS_PER_COMBO; x++) {
                combo[x] = combo[x - 1] + 1;
            }
        }
        mStarter.done(); // inform starter
    }

    /**
     * Converts from an array of integers into a bit representation, where
     * the elements determine which bits are active
     * @param combo array to be converted in to a bit
     *              {0, 1, 3} -> ...001011
     * @return The combo represented as a bit
     */
    private static  int arrayToInt(int[] combo) {
        int letters = 0;

        // for each
        for(int i = 0; i < LETTERS_PER_COMBO; i++) {
            letters |= (1 << combo[i]);
        }
        return letters;
    }
}
