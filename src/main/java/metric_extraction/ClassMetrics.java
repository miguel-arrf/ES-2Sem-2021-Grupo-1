package metric_extraction;

import java.util.ArrayList;
import java.util.HashMap;

public class ClassMetrics {

    private final int loc_class;
    private final int nom_class;
    private final int wmc_class;
    private final String class_package;
    private final String class_name;
    private final ArrayList<Method> class_methods;

    public ClassMetrics(String class_name, String class_package, int loc_class, int nom_class, int wmc_class, ArrayList<Method> class_methods) {
        this.loc_class = loc_class;
        this.nom_class = nom_class;
        this.wmc_class = wmc_class;
        this.class_methods = class_methods;
        this.class_package = class_package;
        this.class_name = class_name;
    }

    public int getLoc_class() {
        return loc_class;
    }

    public int getNom_class() {
        return nom_class;
    }

    public int getWmc_class() {
        return wmc_class;
    }

    public String getClass_name() {
        return class_name;
    }

    public String getClass_package() {
        return class_package;
    }

    public ArrayList<Method> getClass_methods() {
        return class_methods;
    }

    public String[][] getMetrics_by_method() {
        String[][] values = new String[class_methods.size()][10];
        for (int i = 0; i < class_methods.size(); i++) {
            for (int j = 0; j < 10; j++) {
                values[i] = new String[]{class_package, class_name, class_methods.get(i).getMethod_name(),
                        String.valueOf(nom_class), String.valueOf(loc_class), String.valueOf(wmc_class), "is_god_class", String.valueOf(class_methods.get(i).getLoc_method()),
                        String.valueOf(class_methods.get(i).getCyclo_method()), "islongmethod"};
            }
        }
//        {"method id ", class_package, class_name, "method" , nom_class, loc_class, wmc_class, "is_god_class", class_methods, "islongmethod" }
        return values;
    }

    public void printInfo() {
        System.out.println("Package: " + class_package);
        System.out.println("Class: " + class_name);
        System.out.println("LOC_Class: " + getLoc_class());
        System.out.println("WMC_Class: " + getWmc_class());
        System.out.println("NOM_Class: " + getNom_class());
        System.out.println("Class methods: ");
        for(Method m : getClass_methods()) {
            System.out.println("  Name: " + m.getMethod_name());
            System.out.println("  LOC_Method: " + m.getLoc_method());
            System.out.println("  CYCLO_Method: " + m.getCyclo_method());
            System.out.println("---------------------");
        }
        System.out.println("END OF CLASS");
    }

    public HashMap<String, Integer> getMetricsForDetection() {
        HashMap<String, Integer> metrics = new HashMap<>();
        metrics.put("LOC_Class", getLoc_class());
        metrics.put("NOM_Class", getNom_class());
        metrics.put("WMC_Class", getWmc_class());
        return metrics;
    }
}