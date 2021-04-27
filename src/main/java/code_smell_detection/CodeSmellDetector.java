package code_smell_detection;

import metric_extraction.ClassMetrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CodeSmellDetector {

    private ArrayList<ClassMetrics> metrics;
    private ArrayList<CodeSmell> codeSmells;
    private HashMap<String, ArrayList<String>> results = new HashMap<>();

    public CodeSmellDetector(ArrayList<ClassMetrics> metrics, ArrayList<CodeSmell> codeSmells) {
        this.metrics = metrics;
        this.codeSmells = codeSmells;
        for(CodeSmell codeSmell : codeSmells) {
            results.put(codeSmell.getName(), new ArrayList<>());
        }
        results.put("NoCodeSmellDetected", new ArrayList<>());
    }

    public HashMap<String, ArrayList<String>> getResults() {
        return results;
    }

    public void runDetection() throws InterruptedException {
        ArrayList<DetectionWorker> workers = new ArrayList<>();
        ExecutorService threadPool = Executors.newFixedThreadPool(codeSmells.size());
        for(CodeSmell codeSmell: codeSmells) {
            DetectionWorker worker = new DetectionWorker(codeSmell, metrics);
            threadPool.execute(worker);
            workers.add(worker);
        }
        threadPool.shutdown();
        threadPool.awaitTermination(60, TimeUnit.SECONDS);
        for(DetectionWorker worker : workers) {
            results.get(worker.getCodeSmell().getName()).addAll(worker.getResults());
            results.get("NoCodeSmellDetected").addAll(worker.getUndetectedSmells());
        }
        //printResults();
    }

    private void printResults() {
        for(String codeSmell : results.keySet()) {
            ArrayList<String> values = results.get(codeSmell);
            if(values.isEmpty()) {
                System.out.println("The code smell " + codeSmell + " was not detected in the code");
            } else {
                System.out.println("The code smell " + codeSmell + " was found in:");
                for(String value : values) System.out.println(value);
            }
        }
    }


}
