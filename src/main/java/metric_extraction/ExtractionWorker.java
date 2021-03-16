package metric_extraction;

import java.io.File;

public class ExtractionWorker implements Runnable {

    private int[] metrics;
    private File class_file;

    public ExtractionWorker(File class_file) {
        this.class_file = class_file;
    }

    @Override
    public void run() {
        //Analisar class_file, extrair as métricas e colocar resultados obtidos no vetor de inteiros 'metrics'
    }

    public int[] getMetrics() {
        return this.metrics;
    }
}
