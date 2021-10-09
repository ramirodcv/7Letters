package utils;

import ComboChecker.Combo;

public class TextFile {
    public String name;
    public String path;
    public Combo answer;
    public int lettersPerCombo;

    public TextFile(String name, String path, Combo answer, int letterPerCombo) {
        this.name = name;
        this.path = path;
        this.answer = answer;
        this.lettersPerCombo = letterPerCombo;
    }
}
