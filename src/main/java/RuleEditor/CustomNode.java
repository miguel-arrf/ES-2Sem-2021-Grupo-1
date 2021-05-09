package RuleEditor;

import CodeSmellDetection.RuleOperator;
import g1.ISCTE.AppStyle;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Rule Editor block types.
 */
enum Types {
    /**
     * Block that represents a condition (e.g 5 > X)
     */
    ConditionBlock,
    /**
     * Block that represents a metric (e.g NOM_Class)
     */
    MetricBlock,
    /**
     * Block that represents an AND or an OR.
     */
    LogicBlock
}

/**
 * The interface Custom node.
 */
public interface CustomNode {

    /**
     * Get the default widget graphical representation node (the box that the user drags to create rules).
     *
     * @param label the label to be shown
     * @param color the color of the widget
     * @return the node representing a type.
     */
    static Node getDefaultWidgetGraphicalRepresentation(String label, String color){
        HBox ruleMakerBox = new HBox(new Label(label));
        ruleMakerBox.setStyle(AppStyle.setDefaultBackgroundAndBorderRadiusWithGivenBackgroundColor(color));

        ruleMakerBox.setMinWidth(150);
        ruleMakerBox.setMinHeight(50);

        ruleMakerBox.setAlignment(Pos.CENTER);

        return ruleMakerBox;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    Types getType();

    /**
     * Gets graphical representation.
     *
     * @return the graphical representation
     */
    Node getGraphicalRepresentation();

    /**
     * Gets widget graphical representation.
     *
     * @return the widget graphical representation
     */
    Node getWidgetGraphicalRepresentation();

    /**
     * Gets operator.
     *
     * @return the operator
     */
    RuleOperator getOperator();

    /**
     * Gets copy.
     *
     * @return the copy
     */
    CustomNode getCopy();
}
