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

    private  XSSFSheet mySheet;
    private XSSFWorkbook myWorkbook;
    private String path;
    private HashMap<String, ArrayList<String>> rules;

    public RuleApplier(HashMap<String, ArrayList<String>> rules, String path) throws IOException {
        this.rules = rules;
        myWorkbook = ProjectInfo.createWorkbook(path);
        mySheet = myWorkbook.getSheetAt(0);
        this.path = path;

    }

    public void mandar() throws IOException {
        XSSFRow titleRow = mySheet.getRow(0);
        int lastcell = titleRow.getLastCellNum();

        for(int i =0; i!=rules.keySet().toArray().length; i++){
            System.out.println("rulekeys:" + rules.keySet().toArray()[i]);
        }

        String[] rulesKeys = rules.keySet().toArray(new String[rules.size()]);

        for (int x = lastcell;x < lastcell + rulesKeys.length; x++) {
            addColumn(rulesKeys[x - lastcell], x);
        }
        FileOutputStream excelCreator = new FileOutputStream(path);
        myWorkbook.write(excelCreator);
        excelCreator.close();
    }

    private void addColumn(String title, int nColumn){
        int nLinhas = mySheet.getLastRowNum();

        XSSFRow currentRow = mySheet.getRow(0);
        System.out.println("currentRow.getLastCellNum()= " + currentRow.getLastCellNum());

        XSSFCell myCell = currentRow.createCell(nColumn);
        myCell.setCellValue(title);
        System.out.println("titulo: " + title);

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

    private Boolean isCodeSmell(String smellTarget, String codeSmell){
        for(String stringWithBar : rules.get(codeSmell)){
            String name = stringWithBar.split("/")[0];
            System.out.println(name);
            if(smellTarget.equals(name)) {
                return true;
            }
        }
        return false;
    }

    private Boolean isMethodSmell(String stringWithBar) {
        if(stringWithBar.split("/").length > 1) {
            return true;
        } else {
            return false;
        }
    }



}
