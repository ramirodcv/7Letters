import ComboChecker.CheckStarter;

public class Main {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        new CheckStarter();
        System.out.println("time: " + (1.0 * System.currentTimeMillis() - startTime)/1000 + " seconds");
    }
}
