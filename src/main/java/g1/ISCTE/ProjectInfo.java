package g1.ISCTE;

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

/**
 * The type Project info.
 */
public class ProjectInfo {

    private XSSFSheet sheet;
    private final int NUM_OF_COLUMNS = 11;

    /**
     * Create an excel workbook.
     *
     * @param path the path
     * @return the xssf workbook
     * @throws IOException the io exception
     */
    public static XSSFWorkbook createWorkbook(String path) throws IOException {
        File file = new File(path);
        FileInputStream fip = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fip);
        return workbook;
    }

    /**
     * Instantiates a new Project info.
     *
     * @param workbook the workbook
     */
    public ProjectInfo(XSSFWorkbook workbook) {
        this.sheet = workbook.getSheetAt(0);
    }

    /**
     * Count the different number of packages represented in the excel sheet.
     *
     * @return int number of packages
     */
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

    /**
     * Count the different number of classes represented in the excel sheet.
     *
     * @return the int
     */
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

    /**
     * Count the different number of methods represented in the excel sheet.
     *
     * @return the int
     */
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

    /**
     * Count the number of project total lines of code, using the information given in the excel sheet.
     *
     * @return int number of lines of code
     */
    public int lineCounter() {
        Iterator<Row> itr = sheet.iterator();
        int counter = 0;
        itr.next();
        while (itr.hasNext()) {
            Row row = itr.next();
            Cell cell = row.getCell(8, Row.CREATE_NULL_AS_BLANK);
            try{
                counter += Integer.parseInt(cell.getStringCellValue());
            }catch (NumberFormatException numberFormatException){
                System.err.println("There was a problem while extracting data from metrics.");
            }
        }
        return counter;
    }

    /**
     * Given the table represented in excel, process the info to an ArrayList.
     *
     * Note: blank cells are returned as a String = ""
     *
     * @return the metrics table
     */
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
            table.add(line);
        }
        return table;
    }

    /**
     * Get main metrics info string [ ].
     *
     * @return the string [ ]
     */
    public String[] getMainMetricsInfo() {

        String[] metrics = new String[4];
        metrics[0] = Integer.toString(packageCounter());
        metrics[1] = Integer.toString(classCounter());
        metrics[2] = Integer.toString(methodCounter());
        metrics[3] = Integer.toString(lineCounter());

        return metrics;
    }


}

