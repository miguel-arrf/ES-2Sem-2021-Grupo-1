package RuleEditor;

import code_smell_detection.RuleOperator;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

enum Types {
    ConditionBlock,
    RuleBlock,
    AndBlock
}

public interface CustomNode {

    Node getGraphicalRepresentation();
    Types getType();
    HBox getRuleMakerBox();
    RuleOperator getOperator();
    CustomNode getCopy();
}
