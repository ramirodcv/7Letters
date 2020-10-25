package utils;

import ComboChecker.Combo;

public class Constants {

    public static final int LETTERS_PER_COMBO = 6;

    // letters by usage
    public static final char[] LETTERS = {
            'e', 'a', 'r', 'i', 'o',
            't', 'n', 's', 'l', 'c',
            'u', 'd', 'p', 'm', 'h',
            'g', 'b', 'f', 'y', 'w',
            'k', 'v', 'w', 'z', 'j',
            'q'
    };
    public static final int NUM_LETTERS = LETTERS.length;


    public static final int[] LETTER_FREQ = {
            1, 16, 9, 11, 0,
            17, 15, 14, 3, 24,
            20, 8, 13, 6, 4,
            12, 25, 2, 7, 5,
            10, 21, 19, 22, 18, 23
    };

    public enum TXT_FILES {
        ENGLISH_WORDS;

        String[] mFilePaths = {
                "7LettersV1.0\\src\\TextFiles\\englishWords.txt",
                "this is not a valid file"
        };


        Combo[] mAnswers = {
              new Combo("eartslp", 622),
              null
        };

        public String file() {
            return mFilePaths[this.ordinal()];
        }

        public Combo answer() {
            return mAnswers[this.ordinal()];
        }
    }
}
