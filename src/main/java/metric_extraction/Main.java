package metric_extraction;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import code_smell_detection.*;

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

//    Andre main
//
//    public static void main(String[] args) throws InterruptedException, IOException {
//
//        String directory_src = System.getProperty("user.dir") + "\\src";
//        File class_file = new File(directory_src);
//        MetricExtractor extractor = new MetricExtractor(class_file, class_file.getName());
//        extractor.executeExtraction();
//        ArrayList<CodeSmell> smells = new ArrayList<CodeSmell>();
//        RuleCondition left_condition = new RuleCondition("WMC_Class", 50, RuleOperator.GREATER);
//        RuleCondition right_condition = new RuleCondition("NOM_Class", 10, RuleOperator.GREATER);
//        RuleNode left_node = new RuleNode(left_condition);
//        RuleNode right_node = new RuleNode(right_condition);
//        RuleTree rule = new RuleTree(RuleOperator.OR, left_node, right_node, true);
//        CodeSmell god_class = new CodeSmell("isGodClass", rule);
//        smells.add(god_class);
//        CodeSmellDetector detector = new CodeSmellDetector(extractor.getResults(), smells);
//        detector.runDetection();
//    }


}