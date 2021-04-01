package code_smell_detection;

import metric_extraction.ClassMetrics;
import metric_extraction.Method;

import java.util.ArrayList;
import java.util.Map;

public class DetectionWorker implements Runnable {

    private CodeSmell codeSmell;
    private ArrayList<ClassMetrics> metrics;
    private Map<Object, Boolean> results;

    public DetectionWorker(CodeSmell codeSmell, ArrayList<ClassMetrics> metrics) {
        this.codeSmell = codeSmell;
        this.metrics = metrics;
    }

    @Override
    public void run() {
        if(codeSmell.isClassSmell()) {
            for(ClassMetrics classMetrics : metrics) {
                //Work in progress...
            }
        } else {
            ArrayList<Method> methods = extractMethods();
            for(Method method : methods) {
                //Work in progress...
            }
        }
    }

    private ArrayList<Method> extractMethods() {
        ArrayList<Method> methods = new ArrayList<>();
        for(ClassMetrics classMetrics : metrics) {
            methods.addAll(classMetrics.getClass_methods());
        }
        return methods;
    }

    public Map<Object, Boolean> getResults() {
        return results;
    }

}
