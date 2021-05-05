package CodeSmellDetection;

import RuleEditor.ConditionBlock;
import RuleEditor.MetricBlock;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class RuleNodeTest {

    private static RuleNode ruleNode;

    @BeforeAll
    static void setUp() {
        JFXPanel jfxPanel = new JFXPanel();

        ruleNode = new RuleNode(new ConditionBlock(RuleOperator.GREATER, new MetricBlock("WMC_Class"), "50"));
    }

    @Test
    void getElement() {
        assertNotNull(ruleNode.getElement());
    }

    @Test
    void getLeft_node() {
        assertNotNull(ruleNode.getLeft_node());
    }

    @Test
    void getRight_node() {
        assertNotNull(ruleNode.getRight_node());
    }

    @Test
    void setElement() {
        ruleNode.setElement(null);
        assertNull(ruleNode.getElement());
    }

    @Test
    void setLeft_node() {
        ruleNode.setLeft_node(null);
        assertNull(ruleNode.getLeft_node());
    }

    @Test
    void setRight_node() {
        ruleNode.setRight_node(null);
        assertNull(ruleNode.getRight_node());
    }

    @Test
    void isLeafNode() {
        ruleNode.setLeft_node(null);
        ruleNode.setRight_node(null);
        assertTrue(ruleNode.isLeafNode());
    }

    @Test
    void saveRule() {
    }
}