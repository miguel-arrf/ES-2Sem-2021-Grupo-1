package g1.ISCTE;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;

public class ProjectInfo {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public ProjectInfo(XSSFWorkbook workbook) {
        this.workbook = workbook;
        this.sheet = workbook.getSheetAt(0);
    }

    public int packageCounter() {
        Iterator<Row> itr = sheet.iterator();
        ArrayList<String> packageList = new ArrayList<String>();
        itr.next();
        while (itr.hasNext()) {
            Row row = itr.next();
            Cell cell = row.getCell(1);
            if(!packageList.contains(cell.getStringCellValue()) && cell.getCellType() == Cell.CELL_TYPE_STRING) {
                packageList.add(cell.getStringCellValue());
            }
        }
        return packageList.size();
    }

    public int classCounter() {
        Iterator<Row> itr = sheet.iterator();
        ArrayList<String> classList = new ArrayList<String>();
        itr.next();
        while (itr.hasNext()) {
            Row row = itr.next();
            Cell cell = row.getCell(2);
            if(!classList.contains(cell.getStringCellValue()) && cell.getCellType() == Cell.CELL_TYPE_STRING) {
                classList.add(cell.getStringCellValue());
            }
        }
        return classList.size();
    }

    public int methodCounter() {
        Iterator<Row> itr = sheet.iterator();
        ArrayList<String> methodList = new ArrayList<String>();
        itr.next();
        while (itr.hasNext()) {
            Row row = itr.next();
            Cell cell = row.getCell(3);
            if(!methodList.contains(cell.getStringCellValue()) && cell.getCellType() == Cell.CELL_TYPE_STRING) {
                methodList.add(cell.getStringCellValue());
            }
        }
        return methodList.size();
    }

    public int lineCounter() {
        Iterator<Row> itr = sheet.iterator();
        int counter = 0;
        itr.next();
        while (itr.hasNext()) {
            Row row = itr.next();
            Cell cell = row.getCell(8, Row.CREATE_NULL_AS_BLANK);
            if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                counter += cell.getNumericCellValue();
            }
        }
        return counter;
    }

    protected static String[] getMainMetricsInfo(File file) {
        try{
            FileInputStream fip = new FileInputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(fip);
            ProjectInfo metricsinfo = new ProjectInfo(workbook);

            String[] metrics = {
                    Integer.toString(metricsinfo.packageCounter()) ,
                    Integer.toString(metricsinfo.classCounter()),
                    Integer.toString(metricsinfo.methodCounter()),
                    Integer.toString(metricsinfo.lineCounter())
            };

            return metrics;
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
        return null;

    }


    //apenas para testar
    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\mferr\\Downloads\\teste\\Code_Smells (1).xlsx");
        FileInputStream fip = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fip);
        ProjectInfo metricsinfo = new ProjectInfo(workbook);
        System.out.println(metricsinfo.packageCounter());
        System.out.println(metricsinfo.classCounter());
        System.out.println(metricsinfo.methodCounter());
        System.out.println(metricsinfo.lineCounter());

    }

}






