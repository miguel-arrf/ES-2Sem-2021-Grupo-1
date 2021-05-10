package CodeSmellDetection;

import MetricExtraction.MetricExtractor;
import RuleEditor.ConditionBlock;
import RuleEditor.LogicBlock;
import RuleEditor.MetricBlock;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;

public class CodeSmellDetectorTest {

    private static CodeSmellDetector codeSmellDetector;

    @Test
    void getResults() {
        assertNotNull(codeSmellDetector.getResults());
    }

    @Test
    void runDetection() throws InterruptedException {
        codeSmellDetector.runDetection();
        assertFalse(codeSmellDetector.getResults().isEmpty());
    }

    @BeforeAll
    static void setUp() throws InterruptedException {
        JFXPanel jfxPanel = new JFXPanel();

        String directory_src = "C:\\Users\\mferr\\Downloads\\jasml_0.10\\";
        File file = new File(directory_src);
        MetricExtractor extractor = new MetricExtractor(file);
        extractor.executeExtraction();

        ArrayList<CodeSmell> smells = new ArrayList<>();

        RuleNode left_node = new RuleNode(new ConditionBlock(RuleOperator.GREATER, new MetricBlock("WMC_Class"), "50"));
        RuleNode right_node = new RuleNode(new ConditionBlock(RuleOperator.GREATER, new MetricBlock("NOM_Class"), "10", null));
        RuleNode rule = new RuleNode(new LogicBlock(RuleOperator.OR), left_node, right_node);
        CodeSmell god_class = new CodeSmell("isGodClass", rule, true);
        smells.add(god_class);

        left_node = new RuleNode(new ConditionBlock(RuleOperator.GREATER, new MetricBlock("LOC_Method"), "50"));
        right_node = new RuleNode(new ConditionBlock(RuleOperator.GREATER, new MetricBlock("CYCLO_Method"), "10", null));
        rule = new RuleNode(new LogicBlock(RuleOperator.OR), left_node, right_node);
        CodeSmell long_method = new CodeSmell("isLongMethod", rule, false);
        smells.add(long_method);

        codeSmellDetector = new CodeSmellDetector(extractor.getResults(), smells);
    }

}
