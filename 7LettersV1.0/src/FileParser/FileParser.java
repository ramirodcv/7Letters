package FileParser;

import utils.LetterNodeTree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


import static utils.Constants.LETTER_FREQ;

public class FileParser {
    /**
     * parses the file into tree
     * @param file the .txt file to parse into bit words (should be a valid Paths object)
     * @param tree the tree to store bit words in
     * @return true if and only if parse was successful
     */
    public static boolean parse(String file, LetterNodeTree tree, int comboSize) {
        // try parse
        try {
            byte[] letters = Files.readAllBytes(Paths.get(file));
            int word = 0;
            int letterCount = 0;
            int bitIndex = 0;

            // used to convert from input to bits
            // ' ' means do not convert
            char charToBit;
            for(int i = 0; i < letters.length; i++) {

                charToBit = ' '; // default to no convert

                // lowercase
                if(letters[i] >= 'a' && letters[i] <= 'z') {
                    charToBit = 'a';

                //upper case
                } else if (letters[i] >= 'A' && letters[i] <= 'Z') {
                    charToBit = 'A';

                // blank space found handle token (word)
                } else if (letters[i] == ' ' || letters[i] == '\n') {
                    if(letterCount <= comboSize) {
                        tree.add(word);
                    }
                    word = 0;
                    letterCount = 0;
                }

                // convert if needed
                if(charToBit != ' ') {
                    bitIndex = 1 << (LETTER_FREQ[letters[i] - charToBit]);
                    if((bitIndex & word) == 0) {
                        word |= bitIndex;
                        letterCount++;
                    }
                }
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
