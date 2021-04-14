package metric_extraction;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import code_smell_detection.*;

public class Main {

    public static void main( String[] args) throws InterruptedException, IOException {
        File class_file = new File("C:\\Users\\andre\\IdeaProjects\\ES-2Sem-2021-Grupo-1\\src");
        MetricExtractor extractor = new MetricExtractor(class_file, class_file.getName());
        extractor.executeExtraction();
        ArrayList<CodeSmell> smells = new ArrayList<CodeSmell>();
        RuleCondition left_condition = new RuleCondition("WMC_Class", 50, RuleOperator.GREATER);
        RuleCondition right_condition = new RuleCondition("NOM_Class", 10, RuleOperator.GREATER);
        RuleNode left_node = new RuleNode(left_condition);
        RuleNode right_node = new RuleNode(right_condition);
        RuleTree rule = new RuleTree(RuleOperator.OR, left_node, right_node, true);
        CodeSmell god_class = new CodeSmell("isGodClass", rule);
        smells.add(god_class);
        CodeSmellDetector detector = new CodeSmellDetector(extractor.getResults(), smells);
        detector.runDetection();
    }
}
