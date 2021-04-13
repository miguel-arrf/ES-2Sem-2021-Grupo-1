package metric_extraction;

import org.apache.poi.xssf.usermodel.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MetricExtractor {

    private final ExecutorService threadPool;
    private ArrayList<File> source_code = new ArrayList<>();
    private final String exported_file_name;
    private final String destination_directory;

    public MetricExtractor(File project_directory, String destination_directory) {
        getFilesFromProjectDirectory(project_directory);
        this.threadPool = Executors.newFixedThreadPool(5);
        this.exported_file_name = project_directory.getName() + "_metrics.xlsx";
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
            System.out.println("ERROR: No source code files found in given directory. No metrics extracted.");
        } else {
            ArrayList<ExtractionWorker> workers = new ArrayList<>();
            for(File class_file : source_code) {
                ExtractionWorker runnable = new ExtractionWorker(class_file);
                threadPool.execute(runnable);
                workers.add(runnable);
            }
            threadPool.shutdown();
            threadPool.awaitTermination(5, TimeUnit.SECONDS);
            ArrayList<ClassMetrics> results = new ArrayList<>();
            for(ExtractionWorker worker : workers) {
                ArrayList<ClassMetrics> metrics = worker.getMetrics();
                results.addAll(metrics);
            }
            exportResultsToFile(results);
        }
    }

    private void exportResultsToFile(ArrayList<ClassMetrics> results) {
        String SheetName = "Code Smells";
        try {
            XSSFWorkbook workBook = new XSSFWorkbook();
            XSSFSheet mySheet = workBook.createSheet(SheetName);

            String[] excelData = new String[11];
            XSSFFont boldFont= workBook.createFont();
            boldFont.setBold(true);
            excelData[0] = "MethodID";
            excelData[1] = "package";
            excelData[2] = "class";
            excelData[3] = "method";
            excelData[4] = "NOM_Class";
            excelData[5] = "LOC_Class";
            excelData[6] = "WMC_Class";
            excelData[7] = "is_God_Class";
            excelData[8] = "LOC_method";
            excelData[9] = "CYCLO_method";
            excelData[10] = "is_Long_Method";
            for(int i = 0;i < 1; i++)
            {
                XSSFRow myRow = mySheet.createRow(i);
                for(int j=0;j<11;j++)
                {
                    XSSFCell myCell = myRow.createCell(j);
                    myCell.setCellValue(excelData[j]);
                }
            }
            //Adicionar lista 'results' que contém as métricas de cada ficheiro .java

            String directoryName = destination_directory;
            File file = new File(directoryName);

            if (!file.exists()) {
                if (file.mkdir())
                    System.out.println("Directory is created!");
                else
                    System.out.println("Failed to create directory!");
            }

            String relativePath = directoryName + System.getProperty("file.separator") + exported_file_name;

            FileOutputStream out = new FileOutputStream(new File(relativePath));
            workBook.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
