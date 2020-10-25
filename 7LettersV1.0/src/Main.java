import ComboChecker.ComboScorer;
import ComboChecker.Combo;
import utils.Constants;

public class Main {

    public static final boolean showCompBar = true;

    public static void main(String[] args) {
        String completionBar = "-------------------";
        int runsPerTest = Constants.RUNS_PER_TESTS;

        // set the scorer
        ComboScorer scorer = new ComboScorer(4);

        // get the tests
        Constants.TXT_FILES[] tests = Constants.TXT_FILES.values();
        Combo[][] results = new Combo[tests.length][runsPerTest];
        long[] totalTime = new long[tests.length];
        int totalRuns = runsPerTest * tests.length;

        if(showCompBar) {
            System.out.print("progress: <" + completionBar + ">\r");
        }
        long startTime = System.currentTimeMillis();   // start timer

        // for each test
        for(int i = 0; i < tests.length; i++) {

            // run the tests
            for(int j = 0; j < runsPerTest; j++) {
                results[i][j] = scorer.getBestCombo(tests[i].file(), tests[i].comboSize());

                // handle completion bar
                if(showCompBar) {
                    if((runsPerTest * i + j + 1) % (totalRuns / completionBar.length()) == 0) {
                        completionBar = completionBar.replaceFirst("-", "=");
                        System.out.print("progress: <" + completionBar + ">\r");
                    }
                }
            }

            // record time
            totalTime[i] = System.currentTimeMillis() - startTime;
            startTime = System.currentTimeMillis();
        }

        if(showCompBar) {
            System.out.println();
            System.out.println();
        }

        System.out.print("checking correctness...\r");
        System.out.println("results: " + getNumCorrect(results, tests) + " out of " + (tests.length * runsPerTest) + " tests were passed.");
        printResults(results, tests, totalTime);
    }

    /**
     * check if strings use the same letters (params cannot carry copies of the same char)
     * @param s1 String to compare to another
     * @param s2 String to compare to another
     * @return returns true if both Strings contain the same letters
     */
    public static boolean containSameChars(String s1, String s2) {
        boolean equal = true;
        for(int i = 0; i < s1.length(); i++) {
            if(s2.indexOf(s1.charAt(i)) == -1) {
                equal = false;
                break;
            }
        }
        return equal;
    }

    public static int getNumCorrect(Combo[][] results, Constants.TXT_FILES[] tests) {
        // get number of correct answers
        int correct = 0;
        for(int i = 0; i < tests.length; i++) {
            for(int j = 0; j < results[0].length; j++) {
                if(results[i][j] == null || tests[i].answer() == null) {
                    if(results[i][j] == null && tests[i].answer() == null) {
                        correct++;
                    }
                } else if(
                        containSameChars(results[i][j].combo, tests[i].answer().combo) &&
                                results[i][j].count == tests[i].answer().count
                ) {
                    correct++;
                }
            }
        }
        return correct;
    }

    public static void printResults(Combo[][] results, Constants.TXT_FILES[] tests, long[] totalTime) {
        for(int i = 0; i < tests.length; i++) {
            System.out.println("\t" + tests[i].file() + ":");
            if(results[i][0] == null) {
                System.out.println("\t\tnull");
            } else {
                System.out.println("\t\tcomboSize: " + tests[i].comboSize());
                System.out.println("\t\t" + results[i][0].toString());
            }
            System.out.println("\t\taverage time: " + (toSec(totalTime[i]) / results[0].length) + " sec");
            System.out.println();
        }
    }

    public static double toSec(long time) {
        return (1.0 * time)/(1000);
    }
}
