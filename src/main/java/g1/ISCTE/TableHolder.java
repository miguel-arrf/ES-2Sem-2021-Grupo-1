package g1.ISCTE;

import javafx.beans.property.SimpleStringProperty;

public class TableHolder {

    private final SimpleStringProperty NOM_class;
    private final SimpleStringProperty LOC_class;
    private final SimpleStringProperty WMC_class;
    private final SimpleStringProperty LOC_method;
    private final SimpleStringProperty CYCLO_method;

    public TableHolder(String nom_class, String loc_class, String wmc_class, String loc_method, String cyclo_method) {
        this.NOM_class = new SimpleStringProperty(nom_class);
        this.LOC_class = new SimpleStringProperty(loc_class);
        this.WMC_class = new SimpleStringProperty(wmc_class);
        this.LOC_method = new SimpleStringProperty(loc_method);
        this.CYCLO_method = new SimpleStringProperty(cyclo_method);
    }

    public String getNOM_class() {
        return NOM_class.get();
    }

    public String getLOC_class() {
        return LOC_class.get();
    }

    public String getWMC_class() {
        return WMC_class.get();
    }

    public String getLOC_method() {
        return LOC_method.get();
    }

    public String getCYCLO_method() {
        return CYCLO_method.get();
    }

}