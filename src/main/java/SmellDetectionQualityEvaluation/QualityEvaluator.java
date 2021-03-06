package SmellDetectionQualityEvaluation;

import CodeSmellDetection.CodeSmell;
import CodeSmellDetection.CodeSmellDetector;
import MetricExtraction.MetricExtractor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Given the results of code smell detection upon the project 'jasml', compares the results obtained by this application's execution with the manually inputed results in the given .xlsx file, via extraction of the required columns from the original file.
 * The comparison results in an abstract data structure representing the quality evaluation of the application's performance.
 */
public class QualityEvaluator {

    private QualityEvaluation evaluation;

    private ArrayList<CodeSmell> codeSmells = new ArrayList<>();

    private final File projectFile;

    public QualityEvaluator(File file){
        this.projectFile = file;
    }

    /**
     * Runs the quality evaluation process
     */
    public void run() {
        try {
            HashMap<String, ArrayList<String>> detection_results = initializeData();
            HashMap<String, ArrayList<String>> comparison_data = getDataForComparison();
            performEvaluation(detection_results, comparison_data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the evaluation resultant from the execution
     * @return An instance of QualityEvaluation
     */
    public QualityEvaluation getEvaluation() {
        return evaluation;
    }

    /**
     * Performs the evaluation by comparing the obtained data to the reference data
     * @param detection_results The results obtained from the application's execution of code smell detection
     * @param comparison_data The reference results obtained from the .xlsx file
     */
    private void performEvaluation(HashMap<String, ArrayList<String>> detection_results, HashMap<String, ArrayList<String>> comparison_data) {
        ConfusionMatrix confusionMatrix = new ConfusionMatrix();
        ArrayList<String> consoleOutputs = new ArrayList<>();
        ArrayList<String> undetectedCodeSmells = detection_results.get("NoCodeSmellDetected");

        ArrayList<String> allowedMethods = new ArrayList<>();
        allowedMethods.add("isLongMethod");
        allowedMethods.add("isGodClass");

        for(String codeSmell : detection_results.keySet()) {
            if(!codeSmell.equals("NoCodeSmellDetected") && allowedMethods.contains(codeSmell)) {
                ArrayList<String> detected = detection_results.get(codeSmell);
                ArrayList<String> reference = comparison_data.get(codeSmell);

                for (String value : detected) {
                    String aux = value;
                    if(value.contains("/")) aux = value.split("/")[0];

                    if (reference.contains(value)) {
                        confusionMatrix.incrementTruePositives();
                        consoleOutputs.add(aux + " | " + codeSmell + " | True Positive");

                    } else {
                        confusionMatrix.incrementFalsePositives();
                        consoleOutputs.add(aux + " | " + codeSmell + " | False Positive");
                    }
                }

                for (String value : undetectedCodeSmells) {
                    String aux = value;
                    if(value.contains("/")) aux = value.split("/")[0];

                    if (reference.contains(value)) {
                        confusionMatrix.incrementFalseNegatives();
                        consoleOutputs.add(aux + " | " + codeSmell + " | False Negative");

                    } else {
                        confusionMatrix.incrementTrueNegatives();
                        consoleOutputs.add(aux + " | " + codeSmell + " | True Negative");
                    }
                }
            }
        }
        evaluation = new QualityEvaluation(confusionMatrix, consoleOutputs);
    }

    /**
     * Transfer the content of a InputStream to a given file.
     *
     * @param input the InputStream to transfer the content of.
     * @param file the to transfer the content to.
     * @throws IOException throws IOException if the transfer fails.
     */
    private static void copyInputStreamToFileJava9(InputStream input, File file)
            throws IOException {

        // append = false
        try (OutputStream output = new FileOutputStream(file, false)) {
            input.transferTo(output);
        }

    }

    /**
     * Creates a temporary file to store the XLSX file.
     *
     * @return the temporary file to store the XLSX file.
     */
    private File loadCodeSmellsFile(){
        try{
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = classloader.getResourceAsStream("Code_Smells.xlsx");

            Path temp = Files.createTempFile("Code_Smells", ".xlsx");
            File file = temp.toFile();
            copyInputStreamToFileJava9(inputStream, file);

            return file;
        }catch (IOException e){
            return null;
        }

    }


    /**
     * Gets the reference data from the .xlsx file in the shape of a map data structure
     * @return A HashMap mapping the name of the code smell to a list of where it was detected
     */
    private HashMap<String, ArrayList<String>> getDataForComparison() {
        try {
            File file = loadCodeSmellsFile();

            XSSFWorkbook workBook = new XSSFWorkbook(OPCPackage.open(file));
            XSSFSheet sheet = workBook.getSheet("Code Smells");
            HashMap<String, ArrayList<String>> data = new HashMap<>();
            data.put("isGodClass", new ArrayList<>());
            data.put("isLongMethod", new ArrayList<>());

            for(int i = 1; i < sheet.getLastRowNum(); i++) {
                XSSFRow currentRow = sheet.getRow(i);
                String className = currentRow.getCell(2).getStringCellValue();
                String methodName = currentRow.getCell(3).getStringCellValue();
                boolean isGodClass = currentRow.getCell(7).getBooleanCellValue();
                boolean isLongMethod = currentRow.getCell(10).getBooleanCellValue();
                if(isGodClass) data.get("isGodClass").add(className);
                if(isLongMethod) data.get("isLongMethod").add(methodName);
            }

            return data;
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets the data obtained from the application's execution of code smell detection
     * @return A HashMap mapping the name of the code smell to a list of where it was detected
     * @throws InterruptedException if the metrics extraction fails.
     */
    private HashMap<String, ArrayList<String>> initializeData() throws InterruptedException {
       File java_project = projectFile;
        MetricExtractor extractor = new MetricExtractor(java_project);
        extractor.executeExtraction();

        List<CodeSmell> smells = codeSmells.stream().filter(p -> {
            if(p.getName().equals("isGodClass") && p.isClassSmell()){
                return true;
            }
            return p.getName().equals("isLongMethod") && !p.isClassSmell();

        }).collect(Collectors.toList());

        CodeSmellDetector detector = new CodeSmellDetector(extractor.getResults(), smells);
        detector.runDetection();

        return detector.getResults();
    }


    /**
     * Sets the list of code smells to the given one
     * @param codeSmells List of code smells
     */
    public void setCodeSmells(ArrayList<CodeSmell> codeSmells) {
        this.codeSmells = codeSmells;
    }

}
