package RuleEditor;

import code_smell_detection.RuleOperator;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class RuleBlock implements CustomNode {

    private final String ruleMessage;

    private final Node hBox;

    public RuleBlock(String ruleMessage){
        this.ruleMessage = ruleMessage;

        hBox = getHBox();
    }


    @Override
    public Node getGraphicalRepresentation() {
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
    public Node getWidgetGraphicalRepresentation() {

        String color = "lightpink";

        HBox ruleMakerBox = new HBox(new Label(ruleMessage));
        ruleMakerBox.setStyle("-fx-background-radius: 7 7 7 7;\n" +
                "    -fx-border-radius: 7 7 7 7;\n" +
                "    -fx-background-color: " + color + ";");

        ruleMakerBox.setMinWidth(150);
        ruleMakerBox.setMinHeight(50);

        ruleMakerBox.setAlignment(Pos.CENTER);

        return ruleMakerBox;
    }

    @Override
    public RuleOperator getOperator() {
        return null;
    }

    @Override
    public CustomNode getCopy() {
        return null;
    }
}
