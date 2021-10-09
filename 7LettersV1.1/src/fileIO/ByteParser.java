package fileIO;

import comboMaximizer.SpellTree;

public class ByteParser implements Runnable {
    public static final int WHITESPACE = (1 << ' ') | (1 << '\t') | (1 << '\n');

    byte[] bytes_;
    int start_;
    int finish_;
    SpellTree spellTree_;

    public ByteParser(byte[] bytes, int start, int finish, SpellTree spellTree) {
        bytes_ = bytes;
        start_ = start;
        finish_ = finish;
        spellTree_ = spellTree;
    }

    @Override
    public void run() {
        synchronized (spellTree_) {
            int word = 0;
            int letter = 0;
            boolean isWhiteSpace;

            for(int i = start_; i < finish_; i++) {
                isWhiteSpace = isWhiteSpace(bytes_[i]);

                // try to add newly made word
                if(isWhiteSpace && word != 0) {

                // continue making word
                } else if(!isWhiteSpace) {
                    letter = spellTree_.toInt((char) bytes_[i]);
                    if(letter != SpellTree.BAD_VALUE) {

                    }
                }
            }
        }
    }

    public boolean isWhiteSpace(byte b) {
        return (WHITESPACE & (1 << b)) == 1;
    }
}
