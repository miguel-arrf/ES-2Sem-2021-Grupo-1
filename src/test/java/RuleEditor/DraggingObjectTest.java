package RuleEditor;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import javafx.embed.swing.JFXPanel;

class DraggingObjectTest {

	@Test
	void testSetNode() {
		JFXPanel panel = new JFXPanel();
		
		DraggingObject dg = new DraggingObject();
		
		dg.setNode(null);
		
		assertNull(dg.getNode());
		
		MetricBlock mb = new MetricBlock("testMetric");
		dg.setNode(mb);
		
		assertEquals(dg.getNode(), mb);
		
	}

	@Test
	void testGetNode() {
JFXPanel panel = new JFXPanel();
		
		DraggingObject dg = new DraggingObject();
		
		dg.setNode(null);
		
		assertNull(dg.getNode());
		
		MetricBlock mb = new MetricBlock("testMetric");
		dg.setNode(mb);
		
		assertEquals(dg.getNode(), mb);
	}

}
