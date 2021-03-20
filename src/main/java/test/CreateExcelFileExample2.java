package test;

import java.io.*;
public class CreateExcelFileExample2
{
    public static void main(String[]args)
    {
        try
        {
            String filename = "C:\\Users\\Henrique\\Desktop\\demo\\CustomersDetail.xlsx";
            FileOutputStream fileOut = new FileOutputStream(filename);
            fileOut.close();
            System.out.println("Excel file has been generated successfully.");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}