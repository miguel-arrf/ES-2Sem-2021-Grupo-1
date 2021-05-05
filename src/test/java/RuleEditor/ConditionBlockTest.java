package RuleEditor;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import CodeSmellDetection.RuleOperator;
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
    void getRuleBlock() {
    	MetricBlock metricBlock = new MetricBlock("testMetric");
    	
    	ConditionBlock conditionBlock = new ConditionBlock(RuleOperator.AND, metricBlock, "testValue");
		
		assertEquals(metricBlock, conditionBlock.getRuleBlock());
    }

    @Test
    void getWidgetGraphicalRepresentation() {
    	JFXPanel panel = new JFXPanel();
    	
		ConditionBlock conditionBlock = new ConditionBlock(RuleOperator.AND, new MetricBlock("testMetric"), "testValue");
		assertNotNull(conditionBlock.getWidgetGraphicalRepresentation());
    }

    @Test
    void getStyledButton() {
		ConditionBlock conditionBlock = new ConditionBlock(RuleOperator.AND, new MetricBlock("testMetric"), "testValue");
		assertNotNull(conditionBlock.getStyledButton(">", "red"));
    }

    @Test
    void getGraphicalRepresentation() {
    	ConditionBlock conditionBlock = new ConditionBlock(RuleOperator.AND, "NOM_Class", new DraggingObject());
		assertNotNull(conditionBlock.getGraphicalRepresentation());
    }

    @Test
    void getOperator() {
    	ConditionBlock conditionBlock = new ConditionBlock(RuleOperator.AND, new MetricBlock("testMetric"), "testValue");
		assertEquals(conditionBlock.getOperator(), RuleOperator.AND);
    }

    @Test
    void getValue() {
    	ConditionBlock conditionBlock = new ConditionBlock(RuleOperator.AND, new MetricBlock("testMetric"), "testValue");
		assertEquals(conditionBlock.getValue(), "testValue");
    }

    @Test
    void getType() {
    	ConditionBlock conditionBlock = new ConditionBlock(RuleOperator.AND, new MetricBlock("testMetric"), "testValue");
		assertEquals(conditionBlock.getType(), Types.ConditionBlock);
    }

    @Test
    void evaluate() {
    	int negativeValue = -2;
    	int positiveValue = 2;
    	
    	ConditionBlock equal = new ConditionBlock(RuleOperator.EQUAL, new MetricBlock("testMetric"), "1");
    	assertFalse(equal.evaluate(negativeValue));
    	assertTrue(equal.evaluate(1));

    	
    	ConditionBlock greater = new ConditionBlock(RuleOperator.GREATER, new MetricBlock("testMetric"), "1");
    	ConditionBlock greaterEqual = new ConditionBlock(RuleOperator.GREATER_EQUAL, new MetricBlock("testMetric"), "1");
    	ConditionBlock lesser = new ConditionBlock(RuleOperator.LESSER, new MetricBlock("testMetric"), "1");
    	ConditionBlock lesserEqual = new ConditionBlock(RuleOperator.LESSER_EQUAL, new MetricBlock("testMetric"), "1");
    	ConditionBlock different = new ConditionBlock(RuleOperator.DIFFERENT, new MetricBlock("testMetric"), "1");

    	assertTrue(greater.evaluate(positiveValue));
    	assertTrue(greaterEqual.evaluate(positiveValue));
    	
    	assertTrue(lesser.evaluate(negativeValue));
    	assertTrue(lesserEqual.evaluate(negativeValue));
    	assertTrue(different.evaluate(negativeValue));
    	
    	
    	ConditionBlock and = new ConditionBlock(RuleOperator.AND, new MetricBlock("testMetric"), "1");
    	assertFalse(and.evaluate(negativeValue));

    	
    	
    	
    	assertFalse(greater.evaluate(negativeValue));
    	assertFalse(greaterEqual.evaluate(negativeValue));
    	
    	assertFalse(lesser.evaluate(positiveValue));
    	assertFalse(lesserEqual.evaluate(positiveValue));
    	assertTrue(different.evaluate(positiveValue));
    	
    	assertFalse(different.evaluate(1));


    	
    }

    @Test
    void getCopy() {
    	ConditionBlock conditionBlock = new ConditionBlock(RuleOperator.AND, new MetricBlock("testMetric"), "testValue");
		assertNotNull(conditionBlock.getCopy());
    }

    @Test
    void testToString() {
    	ConditionBlock conditionBlock = new ConditionBlock(RuleOperator.AND, new MetricBlock("testMetric"), "testValue");
    	System.out.println("conditionblock string: " + conditionBlock.toString());
    }
}
