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

public class RuleApplier {

    private final XSSFSheet mySheet;
    private final XSSFWorkbook myWorkbook;
    private final String path;
    private final HashMap<String, ArrayList<String>> rules;

    public RuleApplier(HashMap<String, ArrayList<String>> rules, String path) throws IOException {
        this.rules = rules;
        myWorkbook = ProjectInfo.createWorkbook(path);
        mySheet = myWorkbook.getSheetAt(0);
        this.path = path;
    }

    private void resetMetricsTable() {
        int lastcell = mySheet.getRow(0).getLastCellNum();

        for (int x = lastcell; x > 8; x--) {
            removeColumn(x);
        }
    }

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

    private void addColumn(String title, int nColumn){

        int nLinhas = mySheet.getLastRowNum();

        XSSFRow currentRow = mySheet.getRow(0);

        XSSFCell myCell = currentRow.createCell(nColumn);
        myCell.setCellValue(title);

        for(int y= 1; y != nLinhas; y++){
            currentRow =  mySheet.getRow(y);
            myCell = currentRow.createCell(nColumn);

            if (!rules.get(title).isEmpty() && isMethodSmell(rules.get(title).get(0))) {
                if (isCodeSmell(currentRow.getCell(3).getStringCellValue(), title)) {
                    myCell.setCellValue("TRUE");
                } else {
                    myCell.setCellValue("FALSE");
                }
            } else if (!rules.get(title).isEmpty() && !isMethodSmell(rules.get(title).get(0))) {
                if (isCodeSmell(currentRow.getCell(2).getStringCellValue(), title)) {
                    myCell.setCellValue("TRUE");
                } else {
                    myCell.setCellValue("FALSE");
                }
            } else {
                myCell.setCellValue("FALSE");
            }
        }
    }

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


    private Boolean isCodeSmell(String smellTarget, String codeSmell){
        for(String stringWithBar : rules.get(codeSmell)){
            String name = stringWithBar.split("/")[0];
            if(smellTarget.equals(name)) {
                return true;
            }
        }
        return false;
    }

    private Boolean isMethodSmell(String stringWithBar) {
        return stringWithBar.split("/").length > 1;
    }



}
