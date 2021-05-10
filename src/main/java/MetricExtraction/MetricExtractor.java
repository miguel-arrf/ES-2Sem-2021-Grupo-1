package MetricExtraction;

import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Class that extracts code metrics from all Java class files contained in a user-specified source code directory. After extraction, the results are stored and exported into an .xlsx file format
 */
public class MetricExtractor {

    private final ExecutorService threadPool;
    private final ArrayList<File> source_code = new ArrayList<>();
    private final String exported_file_name;
    private final String destination_directory;
    private final ArrayList<ClassMetrics> metrics = new ArrayList<>();

    /**
     * Constructs an instance of MetricExtractor
     * @param project_directory The source code project's directory
     */
    public MetricExtractor(File project_directory) {
        String destination_directory1 = null;
        getFilesFromProjectDirectory(project_directory);
        this.threadPool = Executors.newFixedThreadPool(5);
        this.exported_file_name = project_directory.getName() + "_metrics.xlsx";

        try{
            Path temp = Files.createTempDirectory("CreatedExcel");
            File file = temp.toFile();
            destination_directory1 = file.getPath();
        }catch (IOException e){
            System.err.println("It wasn't possible to create excel.");
        }

        this.destination_directory = destination_directory1;
    }

    /**
     * Given a project directory, extracts all its Java class files for metric extraction, storing them in the designated attribute
     * @param project The directory of the source code project
     */
    private void getFilesFromProjectDirectory(File project) {
        for (File file : project.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".java")) {
                source_code.add(file);
            } else if (file.isDirectory()) {
                getFilesFromProjectDirectory(file);
            }
        }
    }

    /**
     * Gets the directory on which to save the extraction results
     * @return The destination directory for the extraction results
     */
    public String getFinalPath() { return destination_directory + "/" + exported_file_name ; }

    /**
     * If there's source code present in the source directory, executes metric extraction process on the source code, and exports results to file
     * @throws InterruptedException in case of timout.
     */
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
            threadPool.awaitTermination(60, TimeUnit.SECONDS);
            for(ExtractionWorker worker : workers) {
                metrics.addAll(worker.getMetrics());
            }
            exportResultsToFile();
        }
    }

    /**
     * Exports metric extraction results to .xlsx file format
     */
    private void exportResultsToFile() {
        try {
            XSSFWorkbook workBook = new XSSFWorkbook();
            XSSFSheet mySheet = workBook.createSheet("Code Smells");
            XSSFFont boldFont = workBook.createFont();
            boldFont.setBold(true);
            String[] metricName = new String[11];
            metricName[0] = "MethodID";
            metricName[1] = "package";
            metricName[2] = "class";
            metricName[3] = "method";
            metricName[4] = "NOM_Class";
            metricName[5] = "LOC_Class";
            metricName[6] = "WMC_Class";
            metricName[7] = "LOC_Method";
            metricName[8] = "CYCLO_Method";

            XSSFRow currentRow = mySheet.createRow(0);

            for (int j = 0; j < 9; j++) {
                XSSFCell myCell = currentRow.createCell(j);
                myCell.setCellValue(metricName[j]);
            }

            int sumID = 0;
            //Iterar pelo numero de classes
            for(int i=1; i < metrics.size(); i++) {
                String[][] methods = metrics.get(i).getMetrics_by_method();

                //Iterar pelo numero de methodos (começa no um porque a primeira row já está ocupada)
                for (String[] method : methods) {
                    sumID++;
                    currentRow = mySheet.createRow(sumID);

                    //inserir no excel
                    //Para ter MethodID
                    XSSFCell myCell = currentRow.createCell(0);
                    myCell.setCellValue(sumID);

                    for (int z = 1; z < 9; z++) {
                        myCell = currentRow.createCell(z);
                        myCell.setCellValue(method[z - 1]);

                    }
                }
            }

            File file = new File(destination_directory);

            if (!file.exists()) {
                if (file.mkdir())
                    System.out.println("Directory is created!");
                else
                    System.out.println("Failed to create directory!");
            }

            String relativePath = destination_directory + System.getProperty("file.separator") + exported_file_name;

            FileOutputStream excelCreator = new FileOutputStream(relativePath);
            workBook.write(excelCreator);
            excelCreator.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the extracted metrics from the source code
     * @return An ArrayList of ClassMetrics instances
     */
    public ArrayList<ClassMetrics> getResults() {
        return metrics;

    }
}
