package CodeSmellDetection;

import g1.ISCTE.ProjectInfo;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Used to apply rules created by the user to the selected xlsx file.
 */
public class RuleApplier {

    private final XSSFSheet mySheet;
    private final XSSFWorkbook myWorkbook;
    private final String path;
    private final HashMap<String, ArrayList<String>> rules;
    private final ArrayList<CodeSmell> codeSmellsList;

    /**
     * Instantiates a new Rule applier.
     *
     * @param rules key - code smell name; set - method/class name in which the code smell is present
     * @param path  path to the xlsx file
     * @throws IOException the io exception
     */
    public RuleApplier(HashMap<String, ArrayList<String>> rules, String path, ArrayList<CodeSmell> codeSmellsList) throws IOException {
        this.rules = rules;
        this.codeSmellsList = codeSmellsList;
        myWorkbook = ProjectInfo.createWorkbook(path);
        mySheet = myWorkbook.getSheetAt(0);
        this.path = path;
    }

    /**
     * Resets the xlsx file to its original state prior to the detection of code smells
     */
    public void resetMetricsTable() {
        int lastcell = mySheet.getRow(0).getLastCellNum();
        for (int x = lastcell; x > 8; x--) {
            removeColumn(x);
        }
    }

    /**
     * After the code smell values had been calculated, this method resets the table and then adds one column for each
     * code smell and sets the value for each method/class to true or false depending on the presence of the respective
     * code smell
     *
     * @throws IOException the io exception
     */
    public void processRules() throws IOException {
        resetMetricsTable();
        XSSFRow titleRow = mySheet.getRow(0);
        int lastcell = titleRow.getLastCellNum();
        String[] rulesKeys = rules.keySet().toArray(new String[0]);

        int offset = 0;

        for (int x = lastcell; x < lastcell + rulesKeys.length; x++) {
            if(!rulesKeys[x-lastcell].equals("NoCodeSmellDetected")){
                addColumn(rulesKeys[x - lastcell], x - offset);
            }else{
                offset = 1;
            }

        }
        FileOutputStream excelCreator = new FileOutputStream(path);
        myWorkbook.write(excelCreator);
        excelCreator.close();
    }

    /**
     * Adds a column to the sheet.
     *
     * @param title the title of the column.
     * @param nColumn the number of the column.
     */
    private void addColumn(String title, int nColumn){

        int nLinhas = mySheet.getLastRowNum();

        XSSFRow currentRow = mySheet.getRow(0);

        XSSFCell myCell = currentRow.createCell(nColumn);
        myCell.setCellValue(title);

        for(int y= 1; y != nLinhas; y++){
            currentRow =  mySheet.getRow(y);
            myCell = currentRow.createCell(nColumn);

            if(codeSmellsList != null){
                if (!rules.get(title).isEmpty() && !codeSmellsList.stream().filter(p -> p.getName().equals(title)).findFirst().get().isClassSmell()) {
                    if (isCodeSmell(currentRow.getCell(3).getStringCellValue(), title)) {
                        myCell.setCellValue("TRUE");
                    } else {
                        myCell.setCellValue("FALSE");
                    }
                } else if (!rules.get(title).isEmpty() && codeSmellsList.stream().filter(p -> p.getName().equals(title)).findFirst().get().isClassSmell()) {
                    if (isCodeSmell(currentRow.getCell(2).getStringCellValue(), title)) {
                        myCell.setCellValue("TRUE");
                    } else {
                        myCell.setCellValue("FALSE");
                    }
                } else {
                    myCell.setCellValue("FALSE");
                }
            }else{
                myCell.setCellValue("FALSE");
            }


        }
    }

    /**
     * Removes a given column.
     *
     * @param nColumn the column to be removed.
     */
    private void removeColumn(int nColumn){
        int nLinhas = mySheet.getLastRowNum();
        XSSFRow currentRow;

        for(int y = 0; y != nLinhas; y++){
            currentRow = mySheet.getRow(y);
            XSSFCell oldCell = currentRow.getCell(nColumn);
            if (oldCell != null)
                currentRow.removeCell( oldCell );
        }
    }


    /**
     * Checks if a given method (or class) in the sheet contains the given CodeSmell applied by the rule.
     *
     * @param smellTarget the method (or class) name.
     * @param codeSmell the CodeSmell to check.
     * @return if the method (or class) contains the CodeSmell.
     */
    private Boolean isCodeSmell(String smellTarget, String codeSmell){
        for(String stringWithBar : rules.get(codeSmell)){
            String name = stringWithBar.split("/")[0];
            if(smellTarget.equals(name)) {
                return true;
            }
        }
        return false;
    }


}