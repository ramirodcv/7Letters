package FileParser;

import utils.LetterNodeTree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static utils.Constants.LETTERS_PER_COMBO;
import static utils.Constants.LETTER_FREQ;

public class FileParser {
    public static boolean parse(String file, LetterNodeTree tree) {
        try {
            byte[] letters = Files.readAllBytes(Paths.get(file));
            int word = 0;
            int letterCount = 0;
            int bitIndex = 0;

            for(int i = 0; i < letters.length; i++) {

                // lowercase
                if(letters[i] >= 'a' && letters[i] <= 'z') {
                    bitIndex = 1 << (LETTER_FREQ[letters[i] - 'a']);
                    if((bitIndex & word) == 0) {
                        word |= bitIndex;
                        letterCount++;
                    }

                } else if (letters[i] >= 'A' && letters[i] <= 'Z') { // upper case
                    bitIndex = 1 << (LETTER_FREQ[letters[i] - 'A']);
                    if((bitIndex & word) == 0) {
                        word |= bitIndex;
                        letterCount++;
                    }

                    // blank space found handle token (word)
                } else if (letters[i] == ' ' || letters[i] == '\n') {
                    if(letterCount <= LETTERS_PER_COMBO) {
                        tree.add(word);
                    }
                    word = 0;
                    letterCount = 0;
                }
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
