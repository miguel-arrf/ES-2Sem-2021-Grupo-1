package test;

import java.io.*;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

public class CreateExcelFileExample1 {
    public static void main(String[] args) throws FileNotFoundException, IOException {
//creating an instance of Workbook class
        Workbook wb = new HSSFWorkbook();
//creates an excel file at the specified location
        OutputStream fileOut = new FileOutputStream("C:\\Users\\Henrique\\Desktop\\demo\\BankStatement.xlsx");
        System.out.println("Excel File has been created successfully.");
        wb.write(fileOut);
    }
}