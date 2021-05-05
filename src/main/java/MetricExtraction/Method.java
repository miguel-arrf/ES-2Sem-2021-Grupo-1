package MetricExtraction;

import java.util.HashMap;
import java.util.Scanner;

/**
 * Represents a Java method from a class, its information and metrics
 */
public class Method {


    private int loc_method;
    private int cyclo_method;
    private final String method_name;
    private final String method;
    private String class_name;

    /**
     * Constructs an instance of Method
     * @param method
     * @param method_name
     */
    public Method(String method, String method_name) {
        this.method = method;
        this.method_name = method_name;
        this.class_name = "";
    }

    /**
     * Executes the methods responsible for calculating the method's metrics and stores them in the class's designated attributes
     */
    public void calculateMethodMetrics() {
        loc_method = calculateLoc_Method();
        cyclo_method = calculateCyclo_Method();
    }

    /**
     * Calculates the value of the cyclomatic complexity of the method
     * @return The cyclomatic complexity of the method as int
     */
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

    /**
     * Calculates the total number of the method's lines of code
     * @return The total number of the method's lines of code as int
     */
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

    /**
     * Gets the cyclomatic complexity metric of the method
     * @return Cyclomatic complexity of the method as int
     */
    public int getCyclo_method() {
        return cyclo_method;
    }

    /**
     * Gets the total number of lines of code metric in the method
     * @return Number of lines of code in the method as int
     */
    public int getLoc_method() {
        return loc_method;
    }

    /**
     * Gets the method's name
     * @return Name of the method as String
     */
    public String getMethod_name() {
        return method_name;
    }

    /**
     * Gets the method's body
     * @return Method's body as String
     */
    public String getMethod() {
        return method;
    }

    /**
     * Gets the name of the method's class
     * @return Method's class name as String
     */
    public String getClass_name() {
        return class_name;
    }

    /**
     * Sets the name of the method's class
     * @param class_name The name of the class the method belongs to
     */
    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    /**
     * Maps the method's metrics for the functionality of code smell detection
     * @return A HashMap, mapping the name of each metric of the method to its numeric value
     */
    public HashMap<String, Integer> getMetricsForDetection() {
        HashMap<String, Integer> metrics = new HashMap<>();
        metrics.put("CYCLO_Method", getCyclo_method());
        metrics.put("LOC_Method", getLoc_method());
        return metrics;
    }
}