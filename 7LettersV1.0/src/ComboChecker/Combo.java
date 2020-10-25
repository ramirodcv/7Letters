package ComboChecker;

/**
 * represents a combination of seven letters and its score
 */
public class Combo {
    public String combo;
    public int count;


    public Combo(String combo, int count) {
        this.combo = combo;
        this.count = count;
    }

    @Override
    public String toString() {
        return "Combo{" +
                "combo='" + combo + '\'' +
                ", count=" + count +
                '}';
    }
}
