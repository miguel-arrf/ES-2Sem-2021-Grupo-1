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

    public String[][] getMetrics_by_method() {
        String[][] values = new String[class_methods.size()][10];
        for (int i = 0; i < class_methods.size(); i++) {
            for (int j = 0; j < 10; j++) {
                values[i] = new String[]{class_package, class_name, class_methods.get(i).getName(),
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
        System.out.println("LOC_Class: " + loc_class);
        System.out.println("WMC_Class: " + wmc_class);
        System.out.println("NOM_Class: " + nom_class);
        System.out.println("Class methods: ");
        for (Method m : class_methods) {
            System.out.println("  Name: " + m.getMethod_name());
            System.out.println("  LOC_Method: " + m.getLoc_method());
            System.out.println("  CYCLO_Method: " + m.getCyclo_method());
            System.out.println("---------------------");
        }
        System.out.println("END OF CLASS");
    }
}