package MetricExtraction;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Abstract data structure which holds a Java class's information and metrics, as well as a list of its method's with their own information and metrics
 */
public class ClassMetrics {

    private final int loc_class;
    private final int nom_class;
    private final int wmc_class;
    private final String class_package;
    private final String class_name;
    private final ArrayList<Method> class_methods;

    /**
     * Constructs an instance of ClassMetrics
     * @param class_name Name of the class
     * @param class_package Name of the class's package
     * @param loc_class LOC_Class metric
     * @param nom_class NOM_Class metric
     * @param wmc_class WMC_Class metric
     * @param class_methods List of the class's methods
     */
    public ClassMetrics(String class_name, String class_package, int loc_class, int nom_class, int wmc_class, ArrayList<Method> class_methods) {
        this.loc_class = loc_class;
        this.nom_class = nom_class;
        this.wmc_class = wmc_class;
        this.class_methods = class_methods;
        this.class_package = class_package;
        this.class_name = class_name;
    }

    /**
     * Gets the total number of lines of code metric in the class
     * @return Number of lines of code in the class as int
     */
    public int getLoc_class() {
        return loc_class;
    }

    /**
     * Gets the total number of methods metric in the class
     * @return Number of methods in the class as int
     */
    public int getNom_class() {
        return nom_class;
    }

    /**
     * Gets the cyclomatic complexity metric of the class
     * @return Cyclomatic complexity of the class as int
     */
    public int getWmc_class() {
        return wmc_class;
    }

    /**
     * Gets the name of the class
     * @return Name of the class as String
     */
    public String getClass_name() {
        return class_name;
    }

    /**
     * Gets the name of the package to which the class belongs to
     * @return Name of the class's package as String
     */
    public String getClass_package() {
        return class_package;
    }

    /**
     * Returns a list of Method instances corresponding to the class methods
     * @return An ArrayList of the class methods as instances of Method
     */
    public ArrayList<Method> getClass_methods() {
        return class_methods;
    }

    /**
     * Creates a data structure that relates each method to its class's metrics
     * @return A matrix of String associating the class's metrics to each method
     */
    public String[][] getMetrics_by_method() {
        String[][] values = new String[class_methods.size()][8];
        for (int i = 0; i < class_methods.size(); i++) {
            for (int j = 0; j < 8; j++) {
                values[i] = new String[]{class_package, class_name, class_methods.get(i).getMethod_name(),
                        String.valueOf(nom_class), String.valueOf(loc_class), String.valueOf(wmc_class), String.valueOf(class_methods.get(i).getLoc_method()),
                        String.valueOf(class_methods.get(i).getCyclo_method())};
            }
        }
        return values;
    }

    /**
     * Maps the class's metrics for the functionality of code smell detection
     * @return A HashMap, mapping the name of each metric of the class to its numeric value.
     */
    public HashMap<String, Integer> getMetricsForDetection() {
        HashMap<String, Integer> metrics = new HashMap<>();
        metrics.put("LOC_Class", getLoc_class());
        metrics.put("NOM_Class", getNom_class());
        metrics.put("WMC_Class", getWmc_class());
        return metrics;
    }
}