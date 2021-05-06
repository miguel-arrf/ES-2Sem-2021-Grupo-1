package RuleEditor;

import CodeSmellDetection.RuleOperator;
import javafx.embed.swing.JFXPanel;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LogicBlockTest {

    private static final JFXPanel panel = new JFXPanel();

    @Test
    void getLeftLabelVBox() {
        LogicBlock logicBlock = new LogicBlock(new DraggingObject(), RuleOperator.AND, "test");
        assertNotNull(logicBlock.getLeftLabelVBox());
    }

    @Test
    void getRightLabelVBox() {
        LogicBlock logicBlock = new LogicBlock(new DraggingObject(), RuleOperator.AND, "test");
        assertNotNull(logicBlock.getRightLabelVBox());
    }

    @Test
    void addToRight() {
        LogicBlock logicBlock = new LogicBlock(new DraggingObject(), RuleOperator.AND, "test");
        LogicBlock rightBlock = new LogicBlock(new DraggingObject(), RuleOperator.OR, "rightTest");
        logicBlock.addToRight(rightBlock);
    }

    @Test
    void addToLeft() {
        LogicBlock logicBlock = new LogicBlock(new DraggingObject(), RuleOperator.AND, "test");
        LogicBlock leftBlock = new LogicBlock(new DraggingObject(), RuleOperator.OR, "leftTest");
        logicBlock.addToLeft(leftBlock);
    }

    @Test
    void getBoxColor() {
        LogicBlock logicBlock = new LogicBlock(new DraggingObject(), RuleOperator.AND, "test");
        assertEquals("test", logicBlock.getBoxColor());
    }

    @Test
    void getGraphicalRepresentation() {
        LogicBlock logicBlock = new LogicBlock(new DraggingObject(), RuleOperator.AND, "test");
        assertNotNull(logicBlock.getGraphicalRepresentation());
    }

    @Test
    void getType() {
        LogicBlock logicBlock = new LogicBlock(new DraggingObject(), RuleOperator.AND, "test");
        assertEquals(logicBlock.getType(), Types.LogicBlock);
    }

    @Test
    void getWidgetGraphicalRepresentation() {
        LogicBlock logicBlock = new LogicBlock(new DraggingObject(), RuleOperator.AND, "test");
        assertNotNull(logicBlock.getWidgetGraphicalRepresentation());
    }

    @Test
    void getOperator() {
        LogicBlock logicBlock = new LogicBlock(new DraggingObject(), RuleOperator.AND, "test");
        assertEquals(logicBlock.getOperator(), RuleOperator.AND);
    }

    @Test
    void getCopy() {
        LogicBlock logicBlock = new LogicBlock(new DraggingObject(), RuleOperator.AND, "test");
        assertNotNull(logicBlock.getCopy());
    }

    @Test
    void testToString() {
        LogicBlock logicBlock = new LogicBlock(new DraggingObject(), RuleOperator.AND, "test");
        String string = logicBlock.toString();
        assertNotNull(string);
    }
}