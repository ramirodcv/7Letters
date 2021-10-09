package test;

import comboMaximizer.SpellTree;

public class SpellTreeTest {
    public static final int TEST_COUNT = 10;

    public static final String LETTERS = "eariotnslcudpmhgbfywkvwzjq";

    public static final int MAX_LETTERS = 7;

    public static final String[] ADDITIONS = new String[] {
            "aaaaabbbbbbbccccdededaade",
            "abcdabcdbdcabbdbcabd",
            "abcabcabcba",
            "abababa",
            "aaaaaa"
    };

    public static final String[] COUNTS = new String[] {
            "abcde",
            "abcd",
            "abc",
            "ab",
            "A"
    };

    public static void main(String[] args) {
        SpellTree spellTree = null;
        for(int i = 0; i < TEST_COUNT; i++) {
            if(spellTree == null) {
                spellTree = new SpellTree(LETTERS, MAX_LETTERS);
            } else {
                spellTree.reset(true);
            }

            for(String s : ADDITIONS) {
                spellTree.add(s);
            }

            for(String s : COUNTS) {
                System.out.println(s + ": " + spellTree.count(s));
            }
        }
    }
}
