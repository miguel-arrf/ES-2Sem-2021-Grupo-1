package code_smell_detection;

import metric_extraction.ClassMetrics;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CodeSmellDetector {

    private ArrayList<ClassMetrics> metrics;
    private ArrayList<CodeSmell> codeSmells;

    public CodeSmellDetector(ArrayList<ClassMetrics> metrics, ArrayList<CodeSmell> codeSmells) {
        this.metrics = metrics;
        this.codeSmells = codeSmells;
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
            //Work in progress...
        }
    }


}
