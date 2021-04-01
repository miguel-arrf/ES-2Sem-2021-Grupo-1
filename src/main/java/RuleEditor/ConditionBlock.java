package RuleEditor;

import g1.ISCTE.AppStyle;
import g1.ISCTE.FontType;
import g1.ISCTE.NewGUI;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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

    private DraggingObject oQueEstaASerDragged;

    public DraggingObject getoQueEstaASerDragged() {
        return oQueEstaASerDragged;
    }

    public ConditionBlock(String operator, String rule, String value, DraggingObject oQueEstaASerDragged){

        this.oQueEstaASerDragged = oQueEstaASerDragged;
        this.operator = operator;
        this.rule = rule;
        this.value = value;

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

                    ruleLabel.setText(c1.getRuleMessage());
                    this.rule = c1.getRuleMessage();

                }


            }

            event.setDropCompleted(success);

            event.consume();
        });
    }

    private Button getStyledButton(String label){
        Button button = new Button(label);
        button.getStyleClass().add("roundedAddButton");
        button.setFont(AppStyle.getFont(FontType.ROUNDED_BOLD, 13));
        button.setTextFill(Color.WHITE);

        button.setPadding(new Insets(10, 20, 10, 20));

        button.setOnMouseClicked(mouseEvent -> {
            operator = label;
            operatorLabel.setText(operator);
            Platform.runLater(() -> popupStage.fireEvent(new WindowEvent(popupStage, WindowEvent.WINDOW_CLOSE_REQUEST)));
            NewGUI.blurBackground(30, 0, 200, FinalMain.borderPane);

        });

        return button;
    }

    private HBox optionsHBox(){

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
            optionsHBox = optionsHBox();


            Stage newStage = AppStyle.setUpPopup("Operator", "oi", optionsHBox,getClass().getResource("/style/AppStyle.css").toExternalForm());
            popupStage = newStage;

            FinalMain.scene.setFill(Color.web("#3d3c40"));
            NewGUI.blurBackground(0, 30, 500, FinalMain.borderPane);

            //FinalMain.mainPaneStackPane.setAlignment(Pos.CENTER);
            //FinalMain.mainPaneStackPane.getChildren().add(optionsHBox);

        });

        VBox valueLabelVbox = new VBox(valueLabel);
        valueLabelVbox.setAlignment(Pos.CENTER);

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

