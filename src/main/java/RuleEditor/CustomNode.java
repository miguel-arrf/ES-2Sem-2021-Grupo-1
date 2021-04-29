package RuleEditor;

import code_smell_detection.RuleOperator;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

enum Types {
    ConditionBlock,
    RuleBlock,
    LogicBlock
}

public interface CustomNode {

    static Node getDefaultWidgetGraphicalRepresentation(String label, String color){
        HBox ruleMakerBox = new HBox(new Label(label));
        ruleMakerBox.setStyle("-fx-background-radius: 7 7 7 7;\n" +
                "    -fx-border-radius: 7 7 7 7;\n" +
                "    -fx-background-color:" + color + ";");

        ruleMakerBox.setMinWidth(150);
        ruleMakerBox.setMinHeight(50);

        ruleMakerBox.setAlignment(Pos.CENTER);

        return ruleMakerBox;
    }

    Types getType();

    Node getGraphicalRepresentation();

    Node getWidgetGraphicalRepresentation();

    RuleOperator getOperator();

    CustomNode getCopy();
}
