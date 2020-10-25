import ComboChecker.ComboScorer;
import ComboChecker.Combo;
import utils.Constants;

public class Main {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        int testCount = 100;
        ComboScorer scorer = new ComboScorer(4);


        Constants.TXT_FILES[] tests = makeTests(testCount);
        Combo[] results = new Combo[testCount];

        System.out.println("starting");
        for (int i = 0; i < testCount; i++) {
            results[i] = scorer.getBestCombo(tests[i].file());
        }

        System.out.println("best combination of letters: " + results[0]);

        System.out.println("total tests: " + testCount);
        System.out.println("average time: " + (1.0 * System.currentTimeMillis() - startTime)/(1000 * testCount) + " seconds");
    }

    public static Constants.TXT_FILES[] makeTests(int testCount) {
        Constants.TXT_FILES[] tests = new Constants.TXT_FILES[testCount];
        for(int i = 0; i < testCount; i++) {
            tests[i] = Constants.TXT_FILES.ENGLISH_WORDS;
        }
        return tests;
    }
}
