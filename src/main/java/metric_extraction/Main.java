package metric_extraction;

import java.io.File;
import java.util.ArrayList;

import RuleEditor.LogicBlock;
import RuleEditor.ConditionBlock;
import RuleEditor.MetricBlock;
import code_smell_detection.*;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

//    public static void main(String[] args) throws InterruptedException, IOException {
//        File class_file = new File("C:\\Users\\Henrique\\IdeaProjects\\MongoToMongo\\src\\main\\java\\org\\pisid\\MongoToMongo.java");
//        ExtractionWorker extractor = new ExtractionWorker(class_file);
//        extractor.run();
//
//
//        String destination_directory = System.getProperty("user.dir") + "\\src\\main\\Created_Excels\\";
//        File project_directory = new File("C:\\Users\\Henrique\\IdeaProjects\\MongoToMongo\\src\\");
//
//        MetricExtractor me = new MetricExtractor(project_directory, destination_directory);
//        me.executeExtraction();
//    }
//
//    Andre main

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception {
        String directory_src = System.getProperty("user.dir") + "\\src";
        File class_file = new File(directory_src);
        MetricExtractor extractor = new MetricExtractor(class_file, class_file.getName());
        extractor.executeExtraction();
        ArrayList<CodeSmell> smells = new ArrayList<CodeSmell>();

        RuleNode left_node = new RuleNode(new ConditionBlock(RuleOperator.GREATER, new MetricBlock("WMC_Class"), "50"));
        RuleNode right_node = new RuleNode(new ConditionBlock(RuleOperator.GREATER, new MetricBlock("NOM_Class"), "10", null));

        RuleNode rule = new RuleNode(new LogicBlock(RuleOperator.OR), left_node, right_node);

        CodeSmell god_class = new CodeSmell("isGodClass", rule, true);
        smells.add(god_class);
        CodeSmellDetector detector = new CodeSmellDetector(extractor.getMetrics(), smells);
        detector.runDetection();

    }
}