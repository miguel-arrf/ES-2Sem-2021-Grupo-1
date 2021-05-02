package RuleEditor;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import code_smell_detection.RuleOperator;
import javafx.embed.swing.JFXPanel;

class ConditionBlockTest {

    private final JFXPanel panel = new JFXPanel();


	@Test
	void testGetRule() {
		ConditionBlock conditionBlock = new ConditionBlock(RuleOperator.AND, new MetricBlock("testMetric"), "testValue");
		
		org.junit.jupiter.api.Assertions.assertNotNull(conditionBlock.getRule());
		assertTrue(conditionBlock.getRule() instanceof String);
		
	}


}
