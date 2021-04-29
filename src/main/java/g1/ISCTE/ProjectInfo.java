package g1.ISCTE;

import com.github.javaparser.UnicodeEscapeProcessingProvider;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class ProjectInfo {

    private XSSFSheet sheet;
    private int NUM_OF_COLUMNS = 11;

    public static XSSFWorkbook createWorkbook(String path) throws IOException {
        File file = new File(path);
        FileInputStream fip = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fip);
        return workbook;
    }

    public ProjectInfo(XSSFWorkbook workbook) {
        this.sheet = workbook.getSheetAt(0);
    }

    public int packageCounter() {
        Iterator<Row> itr = sheet.iterator();
        ArrayList<String> packageList = new ArrayList<>();
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
        ArrayList<String> classList = new ArrayList<>();
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
        ArrayList<String> methodList = new ArrayList<>();
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

    //tirar o printArray quando for implementado na GUI
    //Nota: os espaços em branco devolve como String = ""
    public ArrayList<ArrayList<String>> getMetricsTable() {
        ArrayList<ArrayList<String>> table = new ArrayList<>();
        DataFormatter dataFormatter = new DataFormatter();
        for(Row row : sheet) {
            ArrayList<String> line = new ArrayList<>(NUM_OF_COLUMNS);
            for(int column=0; column < row.getLastCellNum(); column++) {
                Cell cell = row.getCell(column);
                String cellValue = dataFormatter.formatCellValue(cell);
                line.add(cellValue);
            }
            printArray(line);
            table.add(line);
        }
        return table;
    }

    private void printArray(ArrayList<String> list) {
        for (String cell : list) {
            System.out.println(cell);
        }
    }


    String[] getMainMetricsInfo(){

        String[] metrics = new String[4];
        metrics[0] = Integer.toString(packageCounter());
        metrics[1] = Integer.toString(classCounter());
        metrics[2] = Integer.toString(methodCounter());
        metrics[3] = Integer.toString(lineCounter());

        return metrics;
    }


    //apenas para testar
    public static void main(String[] args) throws IOException {

        File file = new File("C:\\Users\\Antonio Martins\\Downloads\\Code_Smells.xlsx");

        FileInputStream fip = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fip);
        ProjectInfo metricsinfo = new ProjectInfo(workbook);
        metricsinfo.getMetricsTable();
        System.out.println(metricsinfo.packageCounter());
        System.out.println(metricsinfo.classCounter());
        System.out.println(metricsinfo.methodCounter());
        System.out.println(metricsinfo.lineCounter());

    }

}

