package metric_extraction;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MetricExtractor {

    private final ExecutorService threadPool;
    private ArrayList<File> source_code;
    private final String project_name;
    private final String destination_directory;

    public MetricExtractor(File project, String destination_directory) {
        getFilesFromProjectDirectory(project);
        this.threadPool = Executors.newFixedThreadPool(5);
        this.project_name = project.getName();
        this.destination_directory = destination_directory;
    }

    private void getFilesFromProjectDirectory(File project) {
        for(File file :project.listFiles()) {
            if(file.isFile() && file.getName().endsWith(".java")) {
                source_code.add(file);
            } else if(file.isDirectory()) {
                getFilesFromProjectDirectory(file);
            }
        }
    }

    public void executeExtraction() throws InterruptedException {
        if(source_code.isEmpty()) {
            System.out.println("ERROR: No source code files found in given directory.");
        } else {
            ArrayList<ExtractionWorker> workers = new ArrayList<>();
            for(File class_file : source_code) {
                ExtractionWorker runnable = new ExtractionWorker(class_file);
                threadPool.execute(runnable);
                workers.add(runnable);
            }
            threadPool.shutdown();
            threadPool.awaitTermination(5, TimeUnit.SECONDS);
            ArrayList<int[]> results = new ArrayList<>();
            for(ExtractionWorker worker : workers) {
                results.add(worker.getMetrics());
            }
            exportResultsToFile(results);
        }
    }

    private void exportResultsToFile(ArrayList<int[]> results) {
        File results_file = new File(destination_directory.concat("\\") + project_name + "_metrics.xlsx");
        //Criar o ficheiro Excel com base na lista 'results' que contém as métricas de cada ficheiro .java
    }

}
