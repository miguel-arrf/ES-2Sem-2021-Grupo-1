package RuleEditor;

import CodeSmellDetection.RuleOperator;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * The type Metric block representing the possible metrics to be analysed.
 */
public class MetricBlock implements CustomNode {

    private final String metricMessage;

    private final Node hBox;

    /**
     * Instantiates a new Metric block.
     *
     * @param metricMessage the metric message.
     */
    public MetricBlock(String metricMessage){
        this.metricMessage = metricMessage;

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
     * Instantiates the metric block graphical representation.
     *
     * @return the metric block graphical representation.
     */
    public HBox getHBox(){
        HBox metricMakerBox = new HBox(new Label(metricMessage));
        metricMakerBox.setStyle("-fx-background-radius: 7 7 7 7;\n" +
                "    -fx-border-radius: 7 7 7 7;\n" +
                "    -fx-background-color: red;");

        metricMakerBox.setMinWidth(150);
        metricMakerBox.setMinHeight(50);

        metricMakerBox.setAlignment(Pos.CENTER);

        return metricMakerBox;
    }

    /**
     * Gets metric message.
     *
     * @return the metric message
     */
    public String getMetricMessage() {
        return metricMessage;
    }

    /**
     * @return the type of this block.
     */
    @Override
    public Types getType() {
        return Types.MetricBlock;
    }

    /**
     * Get this block graphical representation.
     *
     * @return this block graphical representation.
     */
    @Override
    public Node getWidgetGraphicalRepresentation() {
        return CustomNode.getDefaultWidgetGraphicalRepresentation(metricMessage, "lightpink");
    }

    /**
     * Gets the metric operator, but, since the Metric Block doesn't have operators, returns none.
     *
     * @return the metric operator
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
