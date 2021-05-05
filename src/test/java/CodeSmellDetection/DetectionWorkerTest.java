package CodeSmellDetection;

import RuleEditor.ConditionBlock;
import RuleEditor.LogicBlock;
import RuleEditor.MetricBlock;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class DetectionWorkerTest {

    private static DetectionWorker detectionWorker;

    @BeforeAll
    static void setUp() {
        JFXPanel jfxPanel = new JFXPanel();
        RuleNode left_node = new RuleNode(new ConditionBlock(RuleOperator.GREATER, new MetricBlock("WMC_Class"), "50"));
        RuleNode right_node = new RuleNode(new ConditionBlock(RuleOperator.GREATER, new MetricBlock("NOM_Class"), "10", null));
        RuleNode rule = new RuleNode(new LogicBlock(RuleOperator.OR), left_node, right_node);
        CodeSmell god_class = new CodeSmell("isGodClass", rule, true);
        detectionWorker = new DetectionWorker(god_class, new ArrayList<>());
    }

    @Test
    void getCodeSmell() {
        assertNotNull(detectionWorker.getCodeSmell());
    }

    @Test
    void getUndetectedSmells() {
        assertNotNull(detectionWorker.getUndetectedSmells());
    }

    @Test
    void run() {
        detectionWorker.run();
        assertFalse(detectionWorker.getResults().isEmpty() && detectionWorker.getResults().isEmpty());
    }

    @Test
    void getResults() {
        assertNotNull(detectionWorker.getResults());
    }
}
