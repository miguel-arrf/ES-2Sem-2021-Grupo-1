package CodeSmellDetection;

import RuleEditor.ConditionBlock;
import RuleEditor.MetricBlock;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class RuleNodeTest {

    private static RuleNode ruleNode;

    @BeforeAll
    static void setUp() {
        ruleNode = new RuleNode(new ConditionBlock(RuleOperator.GREATER, new MetricBlock("WMC_Class"), "50"));
    }

    @Test
    void getElement() {
        assertNotNull(ruleNode.getElement());
    }

    @Test
    void getLeft_node() {
        assertNull(ruleNode.getLeft_node());
    }

    @Test
    void getRight_node() {
        assertNull(ruleNode.getRight_node());
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