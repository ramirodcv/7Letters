import ComboChecker.ComboScorer;
import ComboChecker.Combo;
import utils.Constants;

public class Main {

    // show the completion bar (may slow runtime)
    public static final boolean showCompBar = true;

    public static void main(String[] args) {
        String completionBar = "-------------------";

        // set the scorer
        ComboScorer scorer = new ComboScorer(Constants.THREADS_PER_TEST);

        // get the tests
        int runsPerTest = Constants.RUNS_PER_TESTS;
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

        // show stats
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

    /**
     * gets how many results were correct
     * @param results results found using 7 lettersV1.0 data structures
     * @param tests the test answers
     * @return the number of results that where correct
     */
    public static int getNumCorrect(Combo[][] results, Constants.TXT_FILES[] tests) {
        int correct = 0;
        for(int i = 0; i < tests.length; i++) {
            for(int j = 0; j < results[0].length; j++) {

                // handle null file
                if(results[i][j] == null || tests[i].answer() == null) {
                    // correct if result and test are null
                    if(results[i][j] == null && tests[i].answer() == null) {
                        correct++;
                    }
                } else if( // if the found combo was correct and the count is correct
                        containSameChars(results[i][j].combo, tests[i].answer().combo) &&
                                results[i][j].count == tests[i].answer().count
                ) {
                    correct++;
                }
            }
        }
        return correct;
    }

    /**
     * prints the results of testing
     * @param results the results found by 7 lettersV1.0 data structures
     * @param tests the test answers
     * @param totalTime the time used for each set of tests
     */
    public static void printResults(Combo[][] results, Constants.TXT_FILES[] tests, long[] totalTime) {
        // for each test
        for(int i = 0; i < tests.length; i++) {
            // print name
            System.out.println("\t" + tests[i].file() + ":");

            // print results
            if(results[i][0] == null) {
                System.out.println("\t\tnull");
            } else {
                System.out.println("\t\tcomboSize: " + tests[i].comboSize());
                System.out.println("\t\t" + results[i][0].toString());
            }

            // print time
            System.out.println("\t\taverage time: " + (toSec(totalTime[i]) / results[0].length) + " sec");
            System.out.println();
        }
    }

    /**
     * converts milliseconds into seconds
     * @param time time to be converted
     * @return the parameter in seconds
     */
    public static double toSec(long time) {
        return (1.0 * time)/(1000);
    }
}
