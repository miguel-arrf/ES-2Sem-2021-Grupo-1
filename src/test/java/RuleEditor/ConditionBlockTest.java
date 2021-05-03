package RuleEditor;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import code_smell_detection.RuleOperator;
import javafx.embed.swing.JFXPanel;

class ConditionBlockTest {

    private final JFXPanel panel = new JFXPanel();


    @Test
    void testConstructors(){
     ConditionBlock firstBuilder = new ConditionBlock(RuleOperator.OR, new MetricBlock("testMetric"), "20", new DraggingObject());
     ConditionBlock secondBuilder = new ConditionBlock(RuleOperator.OR, "2", new DraggingObject());
     ConditionBlock thirdBuilder = new ConditionBlock(RuleOperator.OR, new MetricBlock("testMetric"), "testValue");
    }

	@Test
	void testGetRule() {
		ConditionBlock conditionBlock = new ConditionBlock(RuleOperator.AND, new MetricBlock("testMetric"), "testValue");
		
		org.junit.jupiter.api.Assertions.assertNotNull(conditionBlock.getRule());
		assertNotNull(conditionBlock.getRule());
	}


    @Test
    void getRule() {
    }

    @Test
    void getRuleBlock() {
    }

    @Test
    void getWidgetGraphicalRepresentation() {
    }

    @Test
    void getStyledButton() {
    }

    @Test
    void getGraphicalRepresentation() {
    }

    @Test
    void getOperator() {
    }

    @Test
    void getValue() {
    }

    @Test
    void getType() {
    }

    @Test
    void evaluate() {
    }

    @Test
    void getCopy() {
    }

    @Test
    void testToString() {
    }
}
