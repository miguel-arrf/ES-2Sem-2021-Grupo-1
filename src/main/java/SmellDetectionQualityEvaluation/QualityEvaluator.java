package SmellDetectionQualityEvaluation;

import RuleEditor.ConditionBlock;
import RuleEditor.LogicBlock;
import RuleEditor.MetricBlock;
import CodeSmellDetection.CodeSmell;
import CodeSmellDetection.CodeSmellDetector;
import CodeSmellDetection.RuleNode;
import CodeSmellDetection.RuleOperator;
import MetricExtraction.MetricExtractor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Given the results of code smell detection upon the project 'jasml', compares the results obtained by this application's execution with the manually inputed results in the given .xlsx file, via extraction of the required columns from the original file.
 * The comparison results in an abstract data structure representing the quality evaluation of the application's performance.
 */
public class QualityEvaluator {

    private QualityEvaluation evaluation;

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
        ArrayList<String> undetected = detection_results.get("NoCodeSmellDetected");
        for(String key : detection_results.keySet()) {
            if(!key.equals("NoCodeSmellDetected")) {
                ArrayList<String> detected = detection_results.get(key);
                ArrayList<String> reference = comparison_data.get(key);
                for (String value : detected) {
                    if (reference.contains(value)) {
                        confusionMatrix.incrementTruePositives();
                        consoleOutputs.add(value + " | " + key + " | True Positive");
                    } else {
                        confusionMatrix.incrementFalsePositives();
                        consoleOutputs.add(value + " | " + key + " | False Positive");
                    }
                }
                for (String value : undetected) {
                    if (reference.contains(value)) {
                        confusionMatrix.incrementFalseNegatives();
                        consoleOutputs.add(value + " | " + key + " | False Negative");
                    } else {
                        confusionMatrix.incrementTrueNegatives();
                        consoleOutputs.add(value + " | " + key + " | True Negative");
                    }
                }
            }
        }
        evaluation = new QualityEvaluation(confusionMatrix, consoleOutputs);
    }

    /**
     * Gets the reference data from the .xlsx file in the shape of a map data structure
     * @return A HashMap mapping the name of the code smell to a list of where it was detected
     */
    private HashMap<String, ArrayList<String>> getDataForComparison() {
        try {
            String directory_src = System.getProperty("user.dir") + "\\Code_Smells.xlsx";
            XSSFWorkbook workBook = new XSSFWorkbook(OPCPackage.open(new File(directory_src)));
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
     * @throws InterruptedException
     */
    private HashMap<String, ArrayList<String>> initializeData() throws InterruptedException {
       File java_project = getDefaultProject();
        MetricExtractor extractor = new MetricExtractor(java_project, java_project.getName());
        extractor.executeExtraction();

        ArrayList<CodeSmell> smells = initializeCodeSmells();
        CodeSmellDetector detector = new CodeSmellDetector(extractor.getResults(), smells);
        detector.runDetection();

        return detector.getResults();
    }

    /**
     * Gets the file for the default project's folder.
     *
     * @return the file for the default project's folder.
     */
    public static File getDefaultProject(){
        String directory_src = System.getProperty("user.dir") + "\\jasml_0.10";
        return new File(directory_src);
    }

    /**
     * Instantiates the code smells present in the reference data to be detected by the application, and returns a list containing them
     * @return A list of code smells
     */
    private static ArrayList<CodeSmell> initializeCodeSmells() {
        ArrayList<CodeSmell> smells = new ArrayList<CodeSmell>();

        RuleNode left_node = new RuleNode(new ConditionBlock(RuleOperator.GREATER, new MetricBlock("WMC_Class"), "50"));
        RuleNode right_node = new RuleNode(new ConditionBlock(RuleOperator.GREATER, new MetricBlock("NOM_Class"), "10", null));
        RuleNode rule = new RuleNode(new LogicBlock(RuleOperator.OR), left_node, right_node);
        CodeSmell god_class = new CodeSmell("isGodClass", rule, true);
        smells.add(god_class);

        left_node = new RuleNode(new ConditionBlock(RuleOperator.GREATER, new MetricBlock("LOC_Method"), "50"));
        right_node = new RuleNode(new ConditionBlock(RuleOperator.GREATER, new MetricBlock("CYCLO_Method"), "10", null));
        rule = new RuleNode(new LogicBlock(RuleOperator.OR), left_node, right_node);
        CodeSmell long_method = new CodeSmell("isLongMethod", rule, false);
        smells.add(long_method);

        return smells;
    }

}
