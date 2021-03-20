package metric_extraction;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ExtractionWorker implements Runnable {

    private ClassMetrics metrics;
    private final File class_file;

    public ExtractionWorker(File class_file) {
        this.class_file = class_file;
    }

    @Override
    public void run() {
        try {
            int loc_class = extractLOC_Class();
            ArrayList<Method> class_methods = extractClassMethods();
            int nom_class = class_methods.size();
            int wmc_class = 0;
            if(nom_class != 0) {
                for(Method method : class_methods) {
                    method.calculateMethodMetrics();
                    wmc_class += method.getCyclo_method();
                }
            }
            metrics = new ClassMetrics(loc_class, nom_class, wmc_class, class_methods);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Method> extractClassMethods() throws FileNotFoundException {
        Scanner scanner = new Scanner(class_file);
        ArrayList<Method> class_methods = new ArrayList<>();
        while(scanner.hasNextLine()) {
            //to be coded...
        }
        scanner.close();
        return class_methods;
    }

    private int extractLOC_Class() throws FileNotFoundException {
        Scanner scanner = new Scanner(class_file);
        int counter = 0;
        while(scanner.hasNextLine()) {
            counter++;
            scanner.nextLine();
        }
        scanner.close();
        return counter;
    }

    public ClassMetrics getMetrics() {
        return this.metrics;
    }
}
