package metric_extraction;

import java.util.HashMap;
import java.util.Scanner;

public class Method {

    private int loc_method;
    private int cyclo_method;
    private final String method_name;
    private final String method;

    public Method(String method, String method_name) {
        this.method = method;
        this.method_name = method_name;
    }

    public void calculateMethodMetrics() {
        loc_method = calculateLoc_Method();
        cyclo_method = calculateCyclo_Method();
    }

    private int calculateCyclo_Method() {
        Scanner scanner = new Scanner(method);
        int counter = 1;
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            line = line.replaceAll("\\s+","");
            if(line.startsWith("if") || line.startsWith("case") || line.startsWith("for") || line.startsWith("while"))
                counter++;
        }
        scanner.close();
        return counter;
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

    public String getMethod_name() {
        return method_name;
    }

    public String getMethod() {
        return method;
    }

    public HashMap<String, Integer> getMetricsForDetection() {
        HashMap<String, Integer> metrics = new HashMap<>();
        metrics.put("CYCLO_Method", getCyclo_method());
        metrics.put("LOC_Method", getLoc_method());
        return metrics;
    }
}