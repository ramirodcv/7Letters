package ComboChecker;


import FileParser.FileParser;
import utils.LetterNodeTree;

import static utils.Constants.*;

public class CheckStarter {
    private int mGates = 4;

    public CheckStarter() {
        LetterNodeTree tree = LetterNodeTree.getTree();

        if(!FileParser.parse("C:\\Users\\deoca\\CodingProjects\\7Letters\\7LettersV1.0\\src\\TextFiles\\englishWords.txt", tree))
        {
            return;
        }

        ComboChecker[] checkers = new ComboChecker[mGates];
        checkers[0] = new ComboChecker(0, 1, tree, this);
        checkers[1] = new ComboChecker(1, 2, tree, this);
        checkers[2] = new ComboChecker(2, 4, tree, this);
        checkers[3] = new ComboChecker(4, NUM_LETTERS - 6, tree, this);

        int initGates = mGates;
        for(int i = 0; i < initGates; i++) {
            new Thread(checkers[i]).start();
        }

        waitForFinish();

        System.out.println(tree.getMaxCount());
        System.out.println(tree.getBestCombo());
    }

    public synchronized void done() {
        if(--mGates == 0) {
            notifyAll();
        }
    }

    public synchronized void waitForFinish() {
        while(mGates > 0) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
    }
}
