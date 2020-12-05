package ComboChecker;


import FileParser.FileParser;
import utils.LetterNodeTree;

import static utils.Constants.*;

/**
 * finds the best combo of letters for the given .txt file
 */
public class ComboScorer {
    private int mNumGates;
    private int mGatesLeft;
    private LetterNodeTree mTree = LetterNodeTree.getTree();


    /**
     *
     * @param numGates the number of threads to run during the search. Recommended: 4 (if searching all 26 letters).
     *                 Will default to 4 if given an in less than 1 or greater than 24
     */
    public ComboScorer(int numGates) {
        mNumGates = (numGates < 1 || numGates > 24)?  4  :  numGates;
        mGatesLeft = mNumGates;
    }

    /**
     * Search a .txt file for combination of letters to spell the most words
     * @param file the .txt file to find the best combo for
     * @return A Combo object containing information about the best combo found. Returns null if file is not found
     */
    public Combo getBestCombo(String file, int comboSize) {
        // return null if file not found
        if(!FileParser.parse(file, mTree, comboSize))
        {
            return null;
        }

        mTree.setComboSize(comboSize);

        // make the checkers and set the range of letters they will check
        ComboChecker[] checkers = new ComboChecker[mNumGates];
        for(int i = 0; i < mNumGates - 1; i++) {
            checkers[i] = new ComboChecker(i, i + 1, mTree, comboSize, this);
        }
        // create the last checker
        checkers[mNumGates - 1] = new ComboChecker(mNumGates - 2, NUM_LETTERS - comboSize - 1, mTree, comboSize, this);

        // reset gates and start the ComboChecker threads
        mGatesLeft = mNumGates;
        for(int i = 0; i < mNumGates; i++) {
            new Thread(checkers[i]).start();
        }

        waitForFinish();

        Combo bestCombo = new Combo(mTree.getBestCombo(), mTree.getMaxCount());
        mTree.empty();
        return bestCombo;

    }

    /**
     * Threads call this method when they finish.
     * THis is bad practice, but Mr. King said this would be the fastest and easiest
     * way to implement threads.
     */
    public synchronized void done() {
        if(--mGatesLeft == 0) {
            notifyAll();
        }
    }

    /**
     * stay in this method until all gates have been passed
     */
    private synchronized void waitForFinish() {
        while(mGatesLeft > 0) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
    }
}
