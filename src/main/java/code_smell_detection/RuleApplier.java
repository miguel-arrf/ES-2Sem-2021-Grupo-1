package code_smell_detection;

import g1.ISCTE.ProjectInfo;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class RuleApplier {

    private  XSSFSheet mySheet;
    private HashMap<String, ArrayList<String>> rules;

    public RuleApplier(HashMap<String, ArrayList<String>> rules, String path) throws IOException {
        this.rules = rules;
        mySheet = ProjectInfo.createWorkbook(path).getSheetAt(0);

    }

    public void mandar(){
        XSSFRow titleRow = mySheet.getRow(0);
        int lastcell = titleRow.getLastCellNum() + 1;

        for(int i =0; i!=rules.keySet().toArray().length; i++){
            System.out.println("rulekeys:" + rules.keySet().toArray()[i]);
        }

        String[] rulesKeys = rules.keySet().toArray(new String[rules.size()]);

        for (int x = lastcell;x < lastcell + rulesKeys.length; x++) {
            addColumn(rulesKeys[x - lastcell], x);
        }
    }

    private void addColumn(String title, int nColumn){
        int nLinhas =  mySheet.getLastRowNum();

        XSSFRow currentRow = mySheet.getRow(1);
        System.out.println("currentRow.getLastCellNum()= " + currentRow.getLastCellNum());

        XSSFCell myCell = currentRow.createCell(nColumn);
        myCell.setCellValue(title);
        System.out.println("titulo: " + title);

        for(int y= 1; y != nLinhas; y++){
            currentRow =  mySheet.getRow(y);

            if(isCodeSmell(currentRow.getCell(3).getRawValue(), title)){
                myCell.setCellValue("TRUE");
            }else{
                myCell.setCellValue("FALSE");
            }
        }
    }

    private Boolean isCodeSmell(String methodName, String title){
        for(String methodTrue : rules.get(title)){
            if(methodName.equals(methodTrue))
                return true;
        }
        return false;
    }



}
