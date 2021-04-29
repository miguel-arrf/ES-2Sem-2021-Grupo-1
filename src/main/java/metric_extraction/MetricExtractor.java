package metric_extraction;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellType;


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
    private ArrayList<ClassMetrics> metrics = new ArrayList<>();

    public MetricExtractor(File project_directory, String destination_directory) {
        getFilesFromProjectDirectory(project_directory);
        this.threadPool = Executors.newFixedThreadPool(5);
        this.exported_file_name = project_directory.getName() + "_metrics.xlsx";
        this.destination_directory = destination_directory;
    }

    private void getFilesFromProjectDirectory(File project) {
        for (File file : project.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".java")) {
                source_code.add(file);
            } else if (file.isDirectory()) {
                getFilesFromProjectDirectory(file);
            }
        }
    }

    public String getFinalPath() { return destination_directory + "/" + exported_file_name ; }

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

    private void exportResultsToFile() {
        try {
            XSSFWorkbook workBook = new XSSFWorkbook();
            XSSFSheet mySheet = workBook.createSheet("Code Smells");
//            Investigar:
//            https://poi.apache.org/apidocs/4.0/org/apache/poi/hslf/usermodel/HSLFTextShape.html
//            TextShape.setTextAutofit(TextAutofit.SHAPE);

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
            metricName[7] = "is_God_Class";
            metricName[8] = "LOC_method";
            metricName[9] = "CYCLO_method";
            metricName[10] = "is_Long_Method";

            XSSFRow currentRow = mySheet.createRow(0);

            for (int j = 0; j < 11; j++) {
                XSSFCell myCell = currentRow.createCell(j);
                myCell.setCellValue(metricName[j]);
            }

            int sumID = 0;
            //Iterar pelo numero de classes
            for(int i=1; i < metrics.size(); i++) {
                String[][] methods = metrics.get(i).getMetrics_by_method();

                //Iterar pelo numero de methodos (começa no um porque a primeira row já está ocupada)
                for (int j=0; j < methods.length; j++) {
                    sumID++;
                    currentRow = mySheet.createRow(sumID);
                    String[] oneMethod = methods[j];

                    //inserir no excel
                        //Para ter MethodID
                    XSSFCell myCell = currentRow.createCell(0);
                    myCell.setCellValue(sumID);

                    for(int z=1; z < 11; z++) {
                        myCell = currentRow.createCell(z);
                        myCell.setCellValue(oneMethod[z-1]);

                        //Adicionei aqui este código para que, caso seja um valor numérico, assim seja interpretado.
                        /*if(z == 8){
                            //myCell.setCellValue(Integer.parseInt(oneMethod[z-1]));
                            //System.out.print("antes: " + myCell.getStringCellValue());

                            //myCell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
                            //System.out.print(" depois: " + myCell.getNumericCellValue());
                            //System.out.println();
                        }*/


                    }
                }
            }

            //Adicionar lista 'results' que contém as métricas de cada ficheiro .java









            //
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ClassMetrics> getResults() {
        return metrics;
    }
}
