package RuleEditor;

import CodeSmellDetection.RuleOperator;
import g1.ISCTE.AppStyle;
import g1.ISCTE.NewGUI;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.json.simple.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * The type Condition block.
 */
public class ConditionBlock implements CustomNode, Serializable {

    private final Label ruleLabel;
    private final Label valueLabel;
    private final DraggingObject whatIsBeingDragged;
    private RuleOperator operator;
    private Node graphicalRepresentationNode;
    private Label operatorLabel;

    private Stage popupStage;

    private HBox optionsHBox;

    private MetricBlock metricBlock;

    /**
     * Instantiates a new Condition block.
     *
     * @param operator            the operator
     * @param metricBlock         the metric block
     * @param value               the value
     * @param whatIsBeingDragged  what is being dragged
     */
    public ConditionBlock(RuleOperator operator, MetricBlock metricBlock, String value, DraggingObject whatIsBeingDragged) {

        this.whatIsBeingDragged = whatIsBeingDragged;
        this.operator = operator;

        valueLabel = new Label(value);

        this.metricBlock = metricBlock;

        ruleLabel = new Label();
        if (metricBlock == null) {
            ruleLabel.setText("No Rule Block");
        } else {
            ruleLabel.setText(metricBlock.getMetricMessage());
        }


        graphicalRepresentationNode = getHBox();
    }

    /**
     * Instantiates a new Condition block.
     *
     * @param operator            the operator
     * @param value               the value
     * @param whatIsBeingDragged  what is being dragged
     */
    public ConditionBlock(RuleOperator operator, String value, DraggingObject whatIsBeingDragged) {

        this.whatIsBeingDragged = whatIsBeingDragged;
        this.operator = operator;
        valueLabel = new Label(value);

        this.metricBlock = null;

        ruleLabel = new Label();
        ruleLabel.setText("No Rule Block");

        graphicalRepresentationNode = getHBox();
    }

    /**
     * Instantiates a new Condition block.
     *
     * @param operator    the operator
     * @param metricBlock the metric block
     * @param value       the value
     */
    public ConditionBlock(RuleOperator operator, MetricBlock metricBlock, String value) {

        this.whatIsBeingDragged = null;
        this.operator = operator;
        valueLabel = new Label(value);
        this.metricBlock = metricBlock;

        ruleLabel = new Label();
        if (metricBlock == null) {
            ruleLabel.setText("No Rule Block");
        } else {
            ruleLabel.setText(metricBlock.getMetricMessage());
        }


    }

    /**
     * Gets rule label.
     *
     * @return the rule label
     */
    public String getRule() {
        return ruleLabel.getText();
    }

    /**
     * Gets rule block.
     *
     * @return the rule block
     */
    public MetricBlock getRuleBlock() {
        return metricBlock;
    }

    public Node getWidgetGraphicalRepresentation() {
        return CustomNode.getDefaultWidgetGraphicalRepresentation("CONDITION", "lightblue");
    }

    /**
     * Set what happens when a Condition block is dragged.
     */
    private void setDrag(VBox vBox) {

        vBox.setOnDragOver(event -> {
            if (event.getDragboard().hasContent(RuleEditor.customFormat)) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        vBox.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasContent(RuleEditor.customFormat)) {
                success = true;


                if (whatIsBeingDragged.getNode().getType() == Types.MetricBlock) {
                    MetricBlock c1 = (MetricBlock) whatIsBeingDragged.getNode();

                    metricBlock = c1;

                    ruleLabel.setText(c1.getMetricMessage());

                    operator = RuleOperator.DEFAULT;

                    operatorLabel.setText(operator.label);
                    valueLabel.setText("Value");
                }


            }

            event.setDropCompleted(success);

            event.consume();
        });
    }

    /**
     * Returns a button with the default design
     *
     * @param operatorToPut the operator to be displayed in the button.
     * @return button with default design and desired label
     */
    private Button getStyledButton(RuleOperator operatorToPut ) {
        Button button = new Button(operatorToPut.label);
        button.getStyleClass().add("roundedAddButton");
        button.setTextFill(Color.WHITE);

        button.setPadding(new Insets(10, 20, 10, 20));

        button.setOnMouseClicked(mouseEvent -> {
            operator = operatorToPut;
            operatorLabel.setText(operator.label);
            Platform.runLater(() -> popupStage.fireEvent(new WindowEvent(popupStage, WindowEvent.WINDOW_CLOSE_REQUEST)));

        });

        return button;
    }

    /**
     * Returns HBox with operator options.
     *
     * @return hBox
     */
    private HBox optionsHBox() {
        HBox hBox = new HBox();

        Button lessButton = getStyledButton(RuleOperator.LESSER);
        Button lessOrEqualButton = getStyledButton(RuleOperator.LESSER_EQUAL);
        Button greaterButton = getStyledButton(RuleOperator.GREATER);
        Button greatOrEqualButton = getStyledButton(RuleOperator.GREATER_EQUAL);
        Button equalButton = getStyledButton(RuleOperator.EQUAL);
        Button differentButton = getStyledButton(RuleOperator.DIFFERENT);

        hBox.getChildren().addAll(lessButton, lessOrEqualButton, greaterButton, greatOrEqualButton, equalButton, differentButton);


        hBox.getStyleClass().add("ruleBuilderMenu");

        hBox.setPadding(new Insets(10));
        hBox.setSpacing(10);

        hBox.setMaxHeight(100);
        hBox.setMaxWidth(400);
        hBox.setEffect(AppStyle.getDropShadow());

        hBox.setAlignment(Pos.CENTER);

        return hBox;

    }

    /**
     * Returns an HBox where the value is introduced by user.
     *
     * @return hBox
     */
    private HBox valueHBox() {

        Button updateButton = AppStyle.getBolderButton("Update", "#a3ddcb");
        Button cancelButton = AppStyle.getBolderButton("Cancel", "#d8345f");

        cancelButton.setOnAction(actionEvent -> Platform.runLater(() -> popupStage.fireEvent(new WindowEvent(popupStage, WindowEvent.WINDOW_CLOSE_REQUEST))));

        HBox hBox = new HBox();
        hBox.setStyle("-fx-background-color: #3d3c40");

        TextField textField = new TextField();
        if(valueLabel.getText() != null){
            textField.setText(valueLabel.getText());
        }else{
            textField.setText("0");
        }
        textField.setStyle("-fx-text-inner-color: white;");

        updateButton.setOnAction(actionEvent -> {
            try{
                Double value = Double.valueOf(textField.getText());
                valueLabel.setText(textField.getText());

                Platform.runLater(() -> popupStage.fireEvent(new WindowEvent(popupStage, WindowEvent.WINDOW_CLOSE_REQUEST)));
            }catch(NumberFormatException e){
                textField.setStyle("-fx-border-radius: 10; -fx-text-inner-color: white; -fx-background-color: #606060; -fx-border-color: red");
            }

        });

        DecimalFormat format = new DecimalFormat("-#.0;#.0");

        textField.setMaxWidth(150);

        hBox.getChildren().addAll(textField, updateButton, cancelButton);


        hBox.getStyleClass().add("ruleBuilderMenu");

        hBox.setPadding(new Insets(10));
        hBox.setSpacing(10);


        hBox.setMaxHeight(100);
        hBox.setMaxWidth(500);
        hBox.setEffect(AppStyle.getDropShadow());

        hBox.setAlignment(Pos.CENTER);

        return hBox;
	}

    /**
     * Setup the ConditionBlock delete menu
     *
     * @param box the node to add which this delete context menu shall be added.
     */
    private void setHBoxDelete(Node box) {
        ContextMenu menu = new ContextMenu();
        box.setOnContextMenuRequested(contextMenuEvent -> menu.show(box.getScene().getWindow(), contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY()));

        MenuItem deleteMenu = new MenuItem("delete");
        menu.getItems().add(deleteMenu);

        deleteMenu.setOnAction(actionEvent -> {
            Pane parent = (Pane) box.getParent();
            parent.getChildren().remove(this.getGraphicalRepresentation());
            RuleEditor.ruleNodes.remove(this);

        });

    }

    /**
     * Setup the ConditionBlock main HBox
     *
     * @return box
     */
    private HBox getHBox() {
        HBox box = new HBox();

        operatorLabel = new Label(operator.label);

        VBox ruleLabelVbox = new VBox(ruleLabel);
        ruleLabelVbox.setAlignment(Pos.CENTER);

        VBox operatorLabelVbox = new VBox(operatorLabel);
        operatorLabelVbox.setAlignment(Pos.CENTER);


        operatorLabelVbox.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                if (metricBlock != null) {
                    optionsHBox = optionsHBox();
                    optionsHBox.setStyle("-fx-background-color: #3d3c40");

                    Stage newStage = AppStyle.setUpPopup("Operator", "/PreferencesPanelIcon.gif", optionsHBox, getClass().getResource("/style/AppStyle.css").toExternalForm(), mouseEvent.getScreenX(), mouseEvent.getScreenY());
                    popupStage = newStage;
                    newStage.getScene().setFill(Color.web("#3d3c40"));


                    //newStage.setOnCloseRequest(windowEvent -> NewGUI.blurBackground(30, 0, 200, FinalMain.splitPane));
                    newStage.setOnCloseRequest(windowEvent -> NewGUI.blurBackground(30, 0, 200, graphicalRepresentationNode.getScene().getRoot()));

                    graphicalRepresentationNode.getScene().setFill(Color.web("#3d3c40"));
                    NewGUI.blurBackground(0, 30, 500, graphicalRepresentationNode.getScene().getRoot());
                }

            }


        });

        VBox valueLabelVbox = new VBox(valueLabel);
        valueLabelVbox.setAlignment(Pos.CENTER);

        valueLabelVbox.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                if (metricBlock != null) {
                    optionsHBox = valueHBox();

                    Stage newStage = AppStyle.setUpPopup("Value", "/PreferencesPanelIcon.gif", optionsHBox, getClass().getResource("/style/AppStyle.css").toExternalForm(), mouseEvent.getScreenX(), mouseEvent.getScreenY());
                    popupStage = newStage;
                    popupStage.getScene().setFill(Color.web("#3d3c40"));

                    newStage.setOnCloseRequest(windowEvent -> NewGUI.blurBackground(30, 0, 200, graphicalRepresentationNode.getScene().getRoot()));

                    graphicalRepresentationNode.getScene().setFill(Color.web("#3d3c40"));
                    NewGUI.blurBackground(0, 30, 500, graphicalRepresentationNode.getScene().getRoot());
                }

            }

        });

        setDrag(ruleLabelVbox);

        operatorLabelVbox.setStyle("-fx-background-color: #f4eeed;");

        HBox.setHgrow(ruleLabelVbox, Priority.ALWAYS);
        HBox.setHgrow(operatorLabelVbox, Priority.ALWAYS);
        HBox.setHgrow(valueLabelVbox, Priority.ALWAYS);

        box.getChildren().addAll(ruleLabelVbox, operatorLabelVbox, valueLabelVbox);

        box.setStyle("-fx-background-radius: 7 7 7 7;\n" +
                "    -fx-border-radius: 7 7 7 7;\n" +
                "    -fx-background-color: white;");

        box.setMinWidth(200);
        box.setMinHeight(50);
        box.setAlignment(Pos.CENTER);

        box.setEffect(AppStyle.getDropShadow());

        return box;
    }

    @Override
    public Node getGraphicalRepresentation() {
        setHBoxDelete(graphicalRepresentationNode);
        return graphicalRepresentationNode;
    }

    /**
     * Gets operator.
     *
     * @return the operator
     */
    public RuleOperator getOperator() {
        return operator;
    }

    /**
     * Gets text in the valueLabel.
     *
     * @return the value
     */
    public String getValue() {
        return valueLabel.getText();
    }

    /**
     * Gets block type (ConditionBlock).
     *
     * @return Types.ConditionBlock
     */
    @Override
    public Types getType() {
        return Types.ConditionBlock;
    }

    /**
     * Evaluate the condition defined by user, comparing the rule value with the value in the param.
     *
     * @param value the value
     * @return the boolean
     */
    public boolean evaluate(int value) {
        switch (operator) {
            case EQUAL:
                return value == Integer.parseInt(getValue());
            case GREATER:
                return value > Integer.parseInt(getValue());
            case GREATER_EQUAL:
                return value >= Integer.parseInt(getValue());
            case LESSER:
                return value < Integer.parseInt(getValue());
            case LESSER_EQUAL:
                return value <= Integer.parseInt(getValue());
            case DIFFERENT:
                return value != Integer.parseInt(getValue());
        }
        return false;
    }

    /**
     * Gets a copy of this condition block.
     */
    @Override
    public ConditionBlock getCopy() {
        return new ConditionBlock(getOperator(), getRuleBlock(), getValue(), whatIsBeingDragged);
    }

    @Override
    @SuppressWarnings("unchecked")
    public String toString() {
        JSONObject object = new JSONObject();
        object.put("operator", getOperator().label);
        object.put("ruleLabel", ruleLabel.getText());
        object.put("valueLabel", valueLabel.getText());
        return object.toJSONString();
    }

	/**
	 * Evaluates the boolean value of a condition, given the required metrics numeric value as a parameter to compare with the other numeric value defined in the rule's condition
	 * @param metrics  A HashMap mapping the metrics' names to their numeric values
	 * @return  The boolean value of the condition's evaluation
	 */
	public boolean evaluateCondition(HashMap<String, Integer> metrics) {
		int value = metrics.get(getRule());
		return evaluate(value);
	}


}

