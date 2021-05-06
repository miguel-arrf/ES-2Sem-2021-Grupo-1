package CodeSmellDetection;

import MetricExtraction.ClassMetrics;
import RuleEditor.ConditionBlock;
import RuleEditor.LogicBlock;
import RuleEditor.MetricBlock;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class DetectionWorkerTest {

    private static DetectionWorker detectionWorker;

    @BeforeAll
    static void setUp() {
        RuleNode left_node = new RuleNode(new ConditionBlock(RuleOperator.GREATER, new MetricBlock("WMC_Class"), "50"));
        RuleNode right_node = new RuleNode(new ConditionBlock(RuleOperator.GREATER, new MetricBlock("NOM_Class"), "10", null));
        RuleNode rule = new RuleNode(new LogicBlock(RuleOperator.OR), left_node, right_node);
        CodeSmell god_class = new CodeSmell("isGodClass", rule, true);
        ClassMetrics classMetrics = new ClassMetrics("Class", "package", 0, 0, 0, new ArrayList<>());
        ArrayList<ClassMetrics> metrics = new ArrayList<>();
        metrics.add(classMetrics);
        detectionWorker = new DetectionWorker(god_class, metrics);
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
        assertFalse(detectionWorker.getResults().isEmpty() && detectionWorker.getUndetectedSmells().isEmpty());
    }

    @Test
    void getResults() {
        assertNotNull(detectionWorker.getResults());
    }
}
