package RuleEditor;

import code_smell_detection.RuleOperator;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * The type Rule block representing the possible metrics to be analysed.
 */
public class RuleBlock implements CustomNode {

    private final String ruleMessage;

    private final Node hBox;

    /**
     * Instantiates a new Rule block.
     *
     * @param ruleMessage the rule message
     */
    public RuleBlock(String ruleMessage){
        this.ruleMessage = ruleMessage;

        hBox = getHBox();
    }


    /**
     * Get this block graphical representation to be displayed in the main GUI in both draggable panels.
     *
     * @return this block graphical representation
     */
    @Override
    public Node getGraphicalRepresentation() {
        return hBox;
    }

    /**
     * Instantiates the rule block graphical representation.
     *
     * @return the rule block graphical representation.
     */
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

    /**
     * Gets rule message.
     *
     * @return the rule message
     */
    public String getRuleMessage() {
        return ruleMessage;
    }

    /**
     * @return the type of this block.
     */
    @Override
    public Types getType() {
        return Types.RuleBlock;
    }

    /**
     * Get this block graphical representation.
     *
     * @return this block graphical representation.
     */
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

    /**
     * Gets the rule operator, but, since the Rule Block doesn't have operators, returns none.
     *
     * @return the rule operator
     */
    @Override
    public RuleOperator getOperator() {
        return null;
    }

    /**
     * Returns a copy of this block. Since this block doesn't have any external dependencies, the same object can be returned.
     *
     * @return a copy of this block.
     */
    @Override
    public CustomNode getCopy() {
        return this;
    }
}
