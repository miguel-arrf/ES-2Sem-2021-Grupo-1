package RuleEditor;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ConditionBlock implements CustomNodes{

    private String operator;
    private Node hBox;

    private String rule;
    private String value;

    private Label ruleLabel;
    private Label valueLabel;
    private Label operatorLabel;



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

    private HBox getHBox(){
        HBox box = new HBox();

        ruleLabel = new Label(rule);
        valueLabel = new Label(value);
        operatorLabel = new Label(operator);

        VBox ruleLabelVbox = new VBox(ruleLabel);
        ruleLabelVbox.setAlignment(Pos.CENTER);

        VBox operatorLabelVbox = new VBox(operatorLabel);
        operatorLabelVbox.setAlignment(Pos.CENTER);

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

