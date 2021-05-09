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

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class QualityEvaluatorTest {

    private static QualityEvaluator qualityEvaluator;

    @BeforeAll
    static void setUp() {
        File projectFile = new File("C:\\Users\\mferr\\Downloads\\ES-2Sem-2021-Grupo-1-e1e0d12b5bc8ee1df4610651d3a5af1c2d356e95\\jasml_0.10");

        qualityEvaluator = new QualityEvaluator(projectFile);
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
    void setCodeSmells() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
    	Field f = QualityEvaluator.class.getDeclaredField("codeSmells"); //NoSuchFieldException
    	f.setAccessible(true);
    	ArrayList<CodeSmell> iWantThis = (ArrayList<CodeSmell>) f.get(qualityEvaluator); //IllegalAccessException
    	
        qualityEvaluator.setCodeSmells(new ArrayList<>());
    }


}