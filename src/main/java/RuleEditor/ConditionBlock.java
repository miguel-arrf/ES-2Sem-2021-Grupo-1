package RuleEditor;

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

import java.text.DecimalFormat;
import java.text.ParsePosition;

public class ConditionBlock implements CustomNodes{

    private String operator;
    private Node hBox;

    private String rule;
    private String value;

    private Label ruleLabel;
    private Label valueLabel;
    private Label operatorLabel;

    private Stage popupStage;

    private HBox optionsHBox;

    private RuleBlock ruleBlock;

    private DraggingObject oQueEstaASerDragged;

    public DraggingObject getoQueEstaASerDragged() {
        return oQueEstaASerDragged;
    }

    public RuleBlock getRuleBlock() {
        return ruleBlock;
    }

    public ConditionBlock(String operator, RuleBlock ruleBlock, String value, DraggingObject oQueEstaASerDragged){

        this.oQueEstaASerDragged = oQueEstaASerDragged;
        this.operator = operator;
        this.value = value;
        this.ruleBlock = ruleBlock;
        if(ruleBlock == null){
            this.rule = "Rule";
        }else{
            this.rule = ruleBlock.getRuleMessage();
        }


        hBox = getHBox();
    }

    public HBox getRuleMakerBox(){
        HBox ruleMakerBox = new HBox(new Label("CONDITION"));
        ruleMakerBox.setStyle("-fx-background-radius: 7 7 7 7;\n" +
                "    -fx-border-radius: 7 7 7 7;\n" +
                "    -fx-background-color: lightblue;");

        ruleMakerBox.setMinWidth(150);
        ruleMakerBox.setMinHeight(50);

        ruleMakerBox.setAlignment(Pos.CENTER);

        return ruleMakerBox;
    }



    private void setDrag(VBox vBox, Label label){

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

                System.out.println("conditionblock: " + oQueEstaASerDragged);
                if(oQueEstaASerDragged.getNodes().getType() == Types.RuleBlock){
                    RuleBlock c1 = (RuleBlock) oQueEstaASerDragged.getNodes();

                    ruleBlock = c1;

                    ruleLabel.setText(c1.getRuleMessage());
                    this.rule = c1.getRuleMessage();

                    value = "Value";
                    operator = "Operator";

                    operatorLabel.setText(operator);
                    valueLabel.setText(value);
                }


            }

            event.setDropCompleted(success);

            event.consume();
        });
    }

    private Button getStyledButton(String label){
        return getStyledButton(label, null, true);
    }

    private Button getStyledButton(String label, String customColor, boolean isOperator){
        Button button = new Button(label);
        button.getStyleClass().add(ruleBlock.getIsNumeric() ? "roundedAddButton" : "textualRuleButton");
        button.setFont(AppStyle.getFont(FontType.ROUNDED_BOLD, 13));
        button.setTextFill(Color.WHITE);

        button.setPadding(new Insets(10, 20, 10, 20));

        if(isOperator){
            button.setOnMouseClicked(mouseEvent -> {
                operator = label;
                operatorLabel.setText(operator);
                Platform.runLater(() -> popupStage.fireEvent(new WindowEvent(popupStage, WindowEvent.WINDOW_CLOSE_REQUEST)));

            });
        }


        if(customColor!=null){
            button.setStyle("-fx-background-color: " + customColor);
        }

        return button;
    }


    private HBox optionsHBox(){
        if(ruleBlock.getIsNumeric()){
            Button lessButton = getStyledButton("<");
            Button lessOrEqualButton = getStyledButton("<=");
            Button greaterButton = getStyledButton(">");
            Button greatOrEqualButton = getStyledButton(">=");
            Button equalButton = getStyledButton("=");
            Button differentButton = getStyledButton("!=");

            HBox hBox = new HBox(lessButton, lessOrEqualButton, greaterButton, greatOrEqualButton, equalButton, differentButton);
            hBox.getStyleClass().add("ruleBuilderMenu");

            hBox.setPadding(new Insets(10));
            hBox.setSpacing(10);

            hBox.setMaxHeight(100);
            hBox.setMaxWidth(400);
            hBox.setEffect(Others.getDropShadow());

            hBox.setAlignment(Pos.CENTER);

            return hBox;
        }else{
            Button equalButton = getStyledButton("==");
            Button differentButton = getStyledButton("!=");

            HBox hBox = new HBox(equalButton,differentButton);
            hBox.getStyleClass().add("ruleBuilderMenu");

            hBox.setPadding(new Insets(10));
            hBox.setSpacing(10);

            hBox.setMaxHeight(100);
            hBox.setMaxWidth(400);
            hBox.setEffect(Others.getDropShadow());

            hBox.setAlignment(Pos.CENTER);

            return hBox;
        }

    }

    private HBox valueHBox(){

        Button updateButton = getStyledButton("Update", "#a3ddcb", false);
        Button cancelButton = getStyledButton("Cancel", "#d8345f", false);

        cancelButton.setOnAction(actionEvent -> {
            Platform.runLater(() -> popupStage.fireEvent(new WindowEvent(popupStage, WindowEvent.WINDOW_CLOSE_REQUEST)));
        });

        HBox hBox = new HBox();

        if(ruleBlock.getIsNumeric()){
            TextField textField = new TextField("0.0");

            updateButton.setOnAction(actionEvent -> {
                value = textField.getText();
                valueLabel.setText(value);

                Platform.runLater(() -> popupStage.fireEvent(new WindowEvent(popupStage, WindowEvent.WINDOW_CLOSE_REQUEST)));
            });

            DecimalFormat format = new DecimalFormat( "#.0" );

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

        }else{
            Button trueButton = getStyledButton("TRUE", "#a3ddcb", false);
            Button falseButton = getStyledButton("FALSE", "#d8345f", false);

            trueButton.setOnMouseClicked(mouseEvent -> {
                valueLabel.setText("TRUE");
                value = "TRUE";
                Platform.runLater(() -> popupStage.fireEvent(new WindowEvent(popupStage, WindowEvent.WINDOW_CLOSE_REQUEST)));
            });

            falseButton.setOnMouseClicked(mouseEvent -> {
                value = "FALSE";
                valueLabel.setText("FALSE");
                Platform.runLater(() -> popupStage.fireEvent(new WindowEvent(popupStage, WindowEvent.WINDOW_CLOSE_REQUEST)));
            });

            hBox.getChildren().addAll(trueButton, falseButton);
        }





        hBox.getStyleClass().add("ruleBuilderMenu");

        hBox.setPadding(new Insets(10));
        hBox.setSpacing(10);


        hBox.setMaxHeight(100);
        hBox.setMaxWidth(500);
        hBox.setEffect(Others.getDropShadow());

        hBox.setAlignment(Pos.CENTER);

        return hBox;
    }

    private HBox getHBox(){
        HBox box = new HBox();

        ruleLabel = new Label(rule);
        valueLabel = new Label(value);
        operatorLabel = new Label(operator);

        VBox ruleLabelVbox = new VBox(ruleLabel);
        ruleLabelVbox.setAlignment(Pos.CENTER);

        VBox operatorLabelVbox = new VBox(operatorLabel);
        operatorLabelVbox.setAlignment(Pos.CENTER);

        operatorLabelVbox.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton() == MouseButton.PRIMARY){
                if(ruleBlock != null){
                    optionsHBox = optionsHBox();

                    Stage newStage = AppStyle.setUpPopup("Operator", "oi", optionsHBox,getClass().getResource("/style/AppStyle.css").toExternalForm());
                    popupStage = newStage;

                    newStage.setOnCloseRequest(windowEvent -> {
                        NewGUI.blurBackground(30, 0, 200, FinalMain.splitPane);
                    });

                    FinalMain.scene.setFill(Color.web("#3d3c40"));
                    NewGUI.blurBackground(0, 30, 500, FinalMain.splitPane);
                }

            }


        });

        VBox valueLabelVbox = new VBox(valueLabel);
        valueLabelVbox.setAlignment(Pos.CENTER);

        valueLabelVbox.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton() == MouseButton.PRIMARY){
                if(ruleBlock != null){
                    optionsHBox = valueHBox();

                    Stage newStage = AppStyle.setUpPopup("Value", "noIcon", optionsHBox,getClass().getResource("/style/AppStyle.css").toExternalForm());
                    popupStage = newStage;

                    newStage.setOnCloseRequest(windowEvent -> NewGUI.blurBackground(30, 0, 200, FinalMain.splitPane));

                    FinalMain.scene.setFill(Color.web("#3d3c40"));
                    NewGUI.blurBackground(0, 30, 500, FinalMain.splitPane);
                }

            }

        });

        setDrag(ruleLabelVbox, ruleLabel);

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

        box.setEffect(Others.getDropShadow());

        return box;
    }

    public void setContextMenuDeletion(){

        //DELETE
        MenuItem deleteMenu = new MenuItem("delete");
        ContextMenu menu = new ContextMenu(deleteMenu);

        deleteMenu.setOnAction(actionEvent -> {
            Pane parent = (Pane) hBox.getParent();
            parent.getChildren().remove(hBox);
            //hBox.getParent().getChi.remove(hBox);
            //vBox.getChildren().remove(andBlock.gethBox());
        });

        hBox.setOnContextMenuRequested(contextMenuEvent -> menu.show(hBox.getScene().getWindow(), contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY()));
        //END DELETE
    }

    @Override
    public Node gethBox() {
        return hBox;
    }

    public String getOperator() {
        return operator;
    }

    public String getRule() {
        return rule;
    }

    public String getValue() {
        return value;
    }

    @Override
    public Types getType() {
        return Types.ConditionBlock;
    }
}

