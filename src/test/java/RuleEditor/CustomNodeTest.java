package RuleEditor;

import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomNodeTest {

    private final JFXPanel panel = new JFXPanel();

    @Test
    void getDefaultWidgetGraphicalRepresentation() {
        String label = "testLabel";
        String color = "testColor";

        Assertions.assertNotNull(CustomNode.getDefaultWidgetGraphicalRepresentation(label,color));
    }


}