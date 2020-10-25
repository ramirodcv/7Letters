package utils;

import ComboChecker.Combo;

public class Constants {

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
        NULL,
        ENGLISH_WORDS7,
        ENGLISH_WORDS6;

        String[] mFilePaths = {
                "this is not a valid file",
                "7LettersV1.0\\src\\TextFiles\\englishWords.txt",
                "7LettersV1.0\\src\\TextFiles\\englishWords.txt"
        };


        Combo[] mAnswers = {
              null,
              new Combo("eartslp", 622),
              new Combo("eartslp", 622),
        };

        int[] mLetterPerCombo = {
                0,
                7,
                6
        };

        public String file() {
            return mFilePaths[this.ordinal()];
        }

        public Combo answer() {
            return mAnswers[this.ordinal()];
        }

        public int comboSize() {
            return mLetterPerCombo[this.ordinal()];
        }
    }
}
