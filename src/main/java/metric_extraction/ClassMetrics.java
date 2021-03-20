package metric_extraction;

import java.util.ArrayList;

public class ClassMetrics {

    private final int loc_class;
    private final int nom_class;
    private final int wmc_class;
    private final ArrayList<Method> class_methods;

    public ClassMetrics(int loc_class, int nom_class, int wmc_class, ArrayList<Method> class_methods) {
        this.loc_class = loc_class;
        this.nom_class = nom_class;
        this.wmc_class = wmc_class;
        this.class_methods = class_methods;
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

}
