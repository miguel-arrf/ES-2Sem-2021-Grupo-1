package RuleEditor;

import javafx.scene.Node;
import javafx.scene.layout.HBox;

enum Types {
    ConditionBlock,
    RuleBlock,
    AndBlock
};

public interface CustomNodes {

    Node gethBox();
    Types getType();
    HBox getRuleMakerBox();

}
