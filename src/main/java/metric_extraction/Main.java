package metric_extraction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main {

    public static void main( String[] args) throws InterruptedException, IOException {
//        File project = new File("D:\\Users\\Diogo Simões\\Documents\\ISCTE\\2º ano\\PCD\\Projeto\\2019_2020\\Projeto2");
//        MetricExtractor extractor = new MetricExtractor(project, "D:\\Users\\Diogo Simões\\Documents\\ISCTE\\3º ano\\ES\\Projeto");
//        extractor.executeExtraction();
        File class_file = new File("D:\\Users\\Diogo Simões\\Documents\\ISCTE\\2º ano\\PCD\\Projeto\\2019_2020\\Projeto2\\src\\Searching.java");
        ExtractionWorker extractor = new ExtractionWorker(class_file);
        extractor.run();
    }
}
