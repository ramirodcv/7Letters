import ComboChecker.ComboScorer;
import ComboChecker.Combo;
import utils.Constants;

public class Main {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        String completionBar = "----------";
        int runsPerTest = 100;

        // set the scorer
        ComboScorer scorer = new ComboScorer(4);

        // get the tests
        Constants.TXT_FILES[] tests = Constants.TXT_FILES.values();
        Combo[][] results = new Combo[tests.length][runsPerTest];

        int totalRuns = runsPerTest * tests.length;

        System.out.print("progress: <" + completionBar + ">\r");
        for(int i = 0; i < tests.length; i++) {
            for(int j = 0; j < runsPerTest; j++) {
                results[i][j] = scorer.getBestCombo(tests[i].file(), tests[i].comboSize());

                // handle completion bar
                if((runsPerTest * i + j + 1) % (totalRuns / 10) == 0) {
                    completionBar = completionBar.replaceFirst("-", "=");
                    System.out.print("progress: <" + completionBar + ">\r");
                }
            }
        }

        System.out.println();
        System.out.println();

        System.out.println("average time: " + (1.0 * System.currentTimeMillis() - startTime)/(1000 * runsPerTest) + " seconds");
        System.out.print("checking correctness...\r");
        System.out.println("results: " + getNumCorrect(results, tests) + " out of " + (tests.length * runsPerTest) + " tests were passed.");
        printResults(results, tests);
    }

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
                if(results[i][j] == null || tests[i] == null) {
                    if(results[i][j] == null && tests[i] == null) {
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

    public static void printResults(Combo[][] results, Constants.TXT_FILES[] tests) {
        for(int i = 0; i < tests.length; i++) {
            System.out.println("\t" + tests[i].file() + ":");
            if(results[i][0] == null) {
                System.out.println("\t\tnull");
            } else {
                System.out.println("\t\tcomboSize: " + tests[i].comboSize());
                System.out.println("\t\t" + results[i][50].toString());
            }
            System.out.println();
        }
    }
}
