package RuleEditor;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class RuleBlock implements CustomNodes{

    private String ruleMessage;

    private Node hBox;

    public RuleBlock(String ruleMessage ){
        this.ruleMessage = ruleMessage;

        hBox = getHBox();
    }



    @Override
    public Node gethBox() {
        return hBox;
    }

    public HBox getHBox(){
        HBox ruleMakerBox = new HBox(new Label(ruleMessage));
        ruleMakerBox.setStyle("-fx-background-radius: 7 7 7 7;\n" +
                "    -fx-border-radius: 7 7 7 7;\n" +
                "    -fx-background-color: red;");

        ruleMakerBox.setMinWidth(150);
        ruleMakerBox.setMinHeight(50);

        ruleMakerBox.setAlignment(Pos.CENTER);

        return ruleMakerBox;
    }

    public String getRuleMessage() {
        return ruleMessage;
    }

    @Override
    public Types getType() {
        return Types.RuleBlock;
    }

    @Override
    public HBox getRuleMakerBox() {
        HBox ruleMakerBox = new HBox(new Label(ruleMessage));
        ruleMakerBox.setStyle("-fx-background-radius: 7 7 7 7;\n" +
                "    -fx-border-radius: 7 7 7 7;\n" +
                "    -fx-background-color: lightpink;");

        ruleMakerBox.setMinWidth(150);
        ruleMakerBox.setMinHeight(50);

        ruleMakerBox.setAlignment(Pos.CENTER);

        return ruleMakerBox;
    }
}
