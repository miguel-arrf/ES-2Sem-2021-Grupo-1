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

    public static void main(String[] args) throws InterruptedException, IOException {
        File class_file = new File("C:\\Users\\Henrique\\IdeaProjects\\MongoToMongo\\src\\main\\java\\org\\pisid\\MongoToMongo.java");
        ExtractionWorker extractor = new ExtractionWorker(class_file);
        extractor.run();


        String destination_directory = System.getProperty("user.dir") + "\\src\\main\\Created_Excels\\";
        File project_directory = new File("C:\\Users\\Henrique\\IdeaProjects\\MongoToMongo\\src\\");

        MetricExtractor me = new MetricExtractor(project_directory, destination_directory);
        me.executeExtraction();
    }
}
