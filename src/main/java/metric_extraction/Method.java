package metric_extraction;

import java.util.Scanner;

public class Method {

    private int loc_method;
    private int cyclo_method;
    private final String method;

    public Method(String method) {
        this.method = method;
    }

    public void calculateMethodMetrics() {
        loc_method = calculateLoc_Method();
        cyclo_method = calculateCyclo_Method();
    }

    private int calculateCyclo_Method() {
        return 0;
    }

    private int calculateLoc_Method() {
        Scanner scanner = new Scanner(method);
        int counter = 0;
        while(scanner.hasNextLine()) {
            counter++;
            scanner.nextLine();
        }
        scanner.close();
        return counter;
    }

    public int getCyclo_method() {
        return cyclo_method;
    }

    public int getLoc_method() {
        return loc_method;
    }

}

