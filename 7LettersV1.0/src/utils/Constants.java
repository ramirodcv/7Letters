package utils;

import ComboChecker.Combo;

public class Constants {

    public static final int RUNS_PER_TESTS = 5;
    public static final int THREADS_PER_TEST = 4;

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

    public static final TextFile[] TESTS = {
            new TextFile(
                    "Null",
                    "this is not a valid file",
                    null,
                    0
            ),
            new TextFile(
                    "English Words",
                    "TextFiles\\englishWords.txt",
                    null,  // answer not verified
                    10
            ),
            new TextFile(
                    "English Words",
                    "TextFiles\\englishWords.txt",
                    null,  // answer not verified
                    9
            ),
            new TextFile(
                    "English Words",
                    "TextFiles\\englishWords.txt",
                    null,  // answer not verified
                    8
            ),
            new TextFile(
                    "English Words",
                    "TextFiles\\englishWords.txt",
                    new Combo("eartslp", 622),
                    7
            ),
            new TextFile(
                    "English Words",
                    "TextFiles\\englishWords.txt",
                    new Combo("eartsp", 345),
                    6
            ),
            new TextFile(
                    "Simple",
                    "TextFiles\\englishWords.txt",
                    new Combo("cat", 4),
                    3
            )
    };
}
