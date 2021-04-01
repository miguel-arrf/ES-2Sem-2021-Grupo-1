package metric_extraction;

import java.util.ArrayList;

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

    public ArrayList<Method> getClass_methods() {
        return class_methods;
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
}