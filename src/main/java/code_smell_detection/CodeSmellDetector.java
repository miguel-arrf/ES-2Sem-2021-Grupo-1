package code_smell_detection;

import metric_extraction.ClassMetrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Given a list of code smells and the code metrics extracted from source code, detects if the code smells are present or not in the code, by evaluating the truth value of the code smell's rules using the code's metrics as parameters to the rules' evaluation. The result is a list of instances of String, corresponding to the names of the classes/methods where the code smells was detected, as well as a list of the same nature with the names of those where they weren't detected
 */
public class CodeSmellDetector {

    private ArrayList<ClassMetrics> metrics;
    private ArrayList<CodeSmell> codeSmells;
    private HashMap<String, ArrayList<String>> results = new HashMap<>();

    /**
     * Constructs an instance of CodeSmellDetector
     * @param metrics The metrics extracted from the source code
     * @param codeSmells A list of code smells to run detection of in the source code
     */
    public CodeSmellDetector(ArrayList<ClassMetrics> metrics, ArrayList<CodeSmell> codeSmells) {
        this.metrics = metrics;
        this.codeSmells = codeSmells;
        for(CodeSmell codeSmell : codeSmells) {
            results.put(codeSmell.getName(), new ArrayList<>());
        }
        results.put("NoCodeSmellDetected", new ArrayList<>());
    }

    /**
     * Gets the code smells' names mapped to lists of the names of classes/methods where the smells were detected
     * @return A HashMap mapping the code smells' names to lists of the names of classes/methods where the smells were detected
     */
    public HashMap<String, ArrayList<String>> getResults() {
        return results;
    }

    /** Runs the detection of the code smells' presence or absence in the source code, and generates the results
     * @throws InterruptedException
     */
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
        printResults();
    }

    /**
     * Prints the results of the code smells' detection on the console
     */
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
