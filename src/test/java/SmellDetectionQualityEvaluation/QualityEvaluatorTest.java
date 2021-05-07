package SmellDetectionQualityEvaluation;

import CodeSmellDetection.CodeSmell;
import CodeSmellDetection.RuleNode;
import CodeSmellDetection.RuleOperator;
import RuleEditor.ConditionBlock;
import RuleEditor.LogicBlock;
import RuleEditor.MetricBlock;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class QualityEvaluatorTest {

    private static QualityEvaluator qualityEvaluator;

    @BeforeAll
    static void setUp() {
        qualityEvaluator = new QualityEvaluator();
    }

    @Test
    void run() {
        JFXPanel jfxPanel = new JFXPanel();
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
        qualityEvaluator.setCodeSmells(smells);
        qualityEvaluator.run();
        assertNotNull(qualityEvaluator.getEvaluation());
    }

    @Test
    void getEvaluation() {
        assertNotNull(qualityEvaluator.getEvaluation());
    }

    @Test
    void getDefaultProject() {
        assertNotNull(QualityEvaluator.getDefaultProject());
    }

    @Test
    void setCodeSmells() {
        qualityEvaluator.setCodeSmells(new ArrayList<>());
        assertNotNull(qualityEvaluator.getCodeSmells());
    }

    @Test
    void getCodeSmells() {
        assertNotNull(qualityEvaluator.getCodeSmells());
    }

}