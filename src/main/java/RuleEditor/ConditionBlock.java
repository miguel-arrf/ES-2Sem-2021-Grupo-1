package RuleEditor;

import code_smell_detection.RuleOperator;
import g1.ISCTE.AppStyle;
import g1.ISCTE.FontType;
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
import java.text.ParsePosition;

public class ConditionBlock implements CustomNode , Serializable {

    private RuleOperator operator;

    private Node graphicalRepresentationNode;

    public String getRule() {
        return ruleLabel.getText();
    }

    private final Label ruleLabel;
    private final Label valueLabel;
    private Label operatorLabel;


    private Stage popupStage;

    private HBox optionsHBox;

    private RuleBlock ruleBlock;

    private final DraggingObject oQueEstaASerDragged;

    public RuleBlock getRuleBlock() {
        return ruleBlock;
    }

    public ConditionBlock(RuleOperator operator, RuleBlock ruleBlock, String value, DraggingObject oQueEstaASerDragged){

        this.oQueEstaASerDragged = oQueEstaASerDragged;
        this.operator = operator;

        valueLabel = new Label(value);

        this.ruleBlock = ruleBlock;

        ruleLabel = new Label();
        if(ruleBlock == null){
            ruleLabel.setText("No Rule Block");
        }else{
            ruleLabel.setText(ruleBlock.getRuleMessage());
        }


        graphicalRepresentationNode = getHBox();
    }

    public ConditionBlock(RuleOperator operator, String value, DraggingObject oQueEstaASerDragged){

        this.oQueEstaASerDragged = oQueEstaASerDragged;
        this.operator = operator;
        valueLabel = new Label(value);

        this.ruleBlock = null;

        ruleLabel = new Label();
        ruleLabel.setText("No Rule Block");

        graphicalRepresentationNode = getHBox();
    }

    public ConditionBlock(RuleOperator operator, RuleBlock ruleBlock, String value){

        this.oQueEstaASerDragged = null;
        this.operator = operator;
        valueLabel = new Label(value);
        this.ruleBlock = ruleBlock;

        ruleLabel = new Label();
        if(ruleBlock == null){
            ruleLabel.setText("No Rule Block");
        }else{
            ruleLabel.setText(ruleBlock.getRuleMessage());
        }


    }

    public Node getWidgetGraphicalRepresentation(){
        return CustomNode.getDefaultWidgetGraphicalRepresentation("CONDITION", "lightblue");
    }


    private void setDrag(VBox vBox){

        vBox.setOnDragOver(event -> {
            if(event.getDragboard().hasContent(FinalMain.customFormat)){
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        vBox.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if(db.hasContent(FinalMain.customFormat)){
                success = true;

                //System.out.println("conditionblock: " + oQueEstaASerDragged);
                if(oQueEstaASerDragged.getNodes().getType() == Types.RuleBlock){
                    RuleBlock c1 = (RuleBlock) oQueEstaASerDragged.getNodes();

                    ruleBlock = c1;

                    ruleLabel.setText(c1.getRuleMessage());

                    operator = RuleOperator.DEFAULT;

                    operatorLabel.setText(operator.label);
                    valueLabel.setText("Value");
                }


            }

            event.setDropCompleted(success);

            event.consume();
        });
    }

    private Button getStyledButton(RuleOperator label){
        return getStyledButton(label, null);
    }

    private Button getStyledButton(RuleOperator operatorToPut, String customColor){
        Button button = new Button(operatorToPut.label);
        button.getStyleClass().add("roundedAddButton");
        button.setFont(AppStyle.getFont(FontType.ROUNDED_BOLD, 13));
        button.setTextFill(Color.WHITE);

        button.setPadding(new Insets(10, 20, 10, 20));

        button.setOnMouseClicked(mouseEvent -> {
            operator = operatorToPut;
            operatorLabel.setText(operator.label);
            Platform.runLater(() -> popupStage.fireEvent(new WindowEvent(popupStage, WindowEvent.WINDOW_CLOSE_REQUEST)));

        });

        if(customColor!=null){
            button.setStyle("-fx-background-color: " + customColor);
        }

        return button;
    }

    private Button getStyledButton(String operatorToPut, String customColor){
        Button button = new Button(operatorToPut);
        button.getStyleClass().add( "roundedAddButton");
        button.setFont(AppStyle.getFont(FontType.ROUNDED_BOLD, 13));
        button.setTextFill(Color.WHITE);

        button.setPadding(new Insets(10, 20, 10, 20));


        if(customColor!=null){
            button.setStyle("-fx-background-color: " + customColor);
        }

        return button;
    }

    private HBox optionsHBox(){
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

    private HBox valueHBox(){

        Button updateButton = getStyledButton("Update", "#a3ddcb");
        Button cancelButton = getStyledButton("Cancel", "#d8345f");

        cancelButton.setOnAction(actionEvent -> Platform.runLater(() -> popupStage.fireEvent(new WindowEvent(popupStage, WindowEvent.WINDOW_CLOSE_REQUEST))));

        HBox hBox = new HBox();

            TextField textField = new TextField("0.0");

            updateButton.setOnAction(actionEvent -> {
                valueLabel.setText(textField.getText());

                Platform.runLater(() -> popupStage.fireEvent(new WindowEvent(popupStage, WindowEvent.WINDOW_CLOSE_REQUEST)));
            });

            DecimalFormat format = new DecimalFormat( "-#.0;#.0" );

            textField.setTextFormatter( new TextFormatter<>(c ->
            {
                if ( c.getControlNewText().isEmpty() )
                {
                    return c;
                }

                ParsePosition parsePosition = new ParsePosition( 0 );
                Object object = format.parse( c.getControlNewText(), parsePosition );

                if ( object == null || parsePosition.getIndex() < c.getControlNewText().length() )
                {
                    return null;
                }
                else
                {
                    return c;
                }
            }));
            textField.setMaxWidth(150);

            hBox.getChildren().addAll( textField,updateButton, cancelButton);


        hBox.getStyleClass().add("ruleBuilderMenu");

        hBox.setPadding(new Insets(10));
        hBox.setSpacing(10);


        hBox.setMaxHeight(100);
        hBox.setMaxWidth(500);
        hBox.setEffect(AppStyle.getDropShadow());

        hBox.setAlignment(Pos.CENTER);

        return hBox;
    }

    private void setHBoxDelete(Node box){
        ContextMenu menu = new ContextMenu();
        box.setOnContextMenuRequested(contextMenuEvent -> menu.show(box.getScene().getWindow(), contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY()));

        MenuItem deleteMenu = new MenuItem("delete");
        menu.getItems().add(deleteMenu);

        deleteMenu.setOnAction(actionEvent -> {
            Pane parent = (Pane) box.getParent();
            parent.getChildren().remove(this.getGraphicalRepresentation());
            FinalMain.ruleNodes.remove(this);
        });

    }

    private HBox getHBox(){
        HBox box = new HBox();


        operatorLabel = new Label(operator.label);

        VBox ruleLabelVbox = new VBox(ruleLabel);
        ruleLabelVbox.setAlignment(Pos.CENTER);

        VBox operatorLabelVbox = new VBox(operatorLabel);
        operatorLabelVbox.setAlignment(Pos.CENTER);

        operatorLabelVbox.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton() == MouseButton.PRIMARY){
                if(ruleBlock != null){
                    optionsHBox = optionsHBox();

                    Stage newStage = AppStyle.setUpPopup("Operator", "/PreferencesPanelIcon.gif", optionsHBox,getClass().getResource("/style/AppStyle.css").toExternalForm(), mouseEvent.getScreenX(), mouseEvent.getScreenY());
                    popupStage = newStage;

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
            if(mouseEvent.getButton() == MouseButton.PRIMARY){
                if(ruleBlock != null){
                    optionsHBox = valueHBox();

                    Stage newStage = AppStyle.setUpPopup("Value", "/PreferencesPanelIcon.gif", optionsHBox,getClass().getResource("/style/AppStyle.css").toExternalForm(), mouseEvent.getScreenX(), mouseEvent.getScreenY());
                    popupStage = newStage;

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

    public RuleOperator getOperator() {
        return operator;
    }

    public String getValue() {
        return valueLabel.getText();
    }

    @Override
    public Types getType() {
        return Types.ConditionBlock;
    }

    public boolean evaluate(int value) {
        switch (operator) {
            case EQUAL: return value == Integer.parseInt(getValue());
            case GREATER: return value > Integer.parseInt(getValue());
            case GREATER_EQUAL: return value >= Integer.parseInt(getValue());
            case LESSER: return value < Integer.parseInt(getValue());
            case LESSER_EQUAL: return value <= Integer.parseInt(getValue());
            case DIFFERENT: return value != Integer.parseInt(getValue());
        }
        return false;
    }

    public ConditionBlock getCopy(){
        return new ConditionBlock(getOperator(), getRuleBlock(), getValue(), oQueEstaASerDragged);
    }

    @Override
    public String toString() {
        JSONObject object = new JSONObject();
        object.put("operator", getOperator().label);
        object.put("ruleLabel", ruleLabel.getText());
        object.put("valueLabel", valueLabel.getText());
        return object.toJSONString();
//        return "ConditionBlock [ " +
//                "operator: " + operator.label +
//                "; ruleLabel: " + ruleLabel.getText() +
//                "; valueLabel: " + valueLabel.getText() +
//                " ]";

    }


}

