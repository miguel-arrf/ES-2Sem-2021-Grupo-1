package RuleEditor;

import g1.ISCTE.AppStyle;
import g1.ISCTE.FontType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class AndBlock implements CustomNodes{

    private Node hBox;


    private Label leftLabel;
    private Label rightLabel;
    public Label andLabel;

    private VBox rightLabelVBox;
    private VBox leftLabelVBox;

    public String label;
    private int depth = 0;

    public String getLabel() {
        return label;
    }

    private DraggingObject oQueEstaASerDragged;

    private String boxColor;

    public AndBlock(DraggingObject oQueEstaASerDragged, String label, String boxColor){
        this.label = label;
        this.oQueEstaASerDragged = oQueEstaASerDragged;
        andLabel = new Label(label);
        this.boxColor = boxColor;
        hBox = getHBox();
    }

    private void setDepth(int depth){
        this.depth += depth;

        java.awt.Color colors = java.awt.Color.decode(boxColor);

        int red = colors.getRed() + depth*8;
        int green = colors.getGreen() + depth*8;
        int blue = colors.getBlue() + depth*8;




        String color = "rgb("+red +","+ green+ ","+ blue +")";

        rightLabelVBox.setStyle("-fx-background-radius: 0 0 7 7;\n" +
                "    -fx-border-radius: 0 0 7 7;\n" +
                "    -fx-background-color:" +color);
        leftLabelVBox.setStyle("-fx-background-radius: 7 7 0 0;\n" +
                "    -fx-border-radius: 7 7 0 0;\n" +
                "    -fx-background-color: " +color);
    }


    private void setDrag(VBox vBox){

        vBox.setOnDragOver(event -> {
            if(event.getDragboard().hasContent(FinalMain.customFormat)){
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        vBox.setPadding(new Insets(30));

        vBox.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if(db.hasContent(FinalMain.customFormat)){
                success = true;


                if(oQueEstaASerDragged.getNodes().getType() == Types.AndBlock){
                    AndBlock andBlock = (AndBlock) oQueEstaASerDragged.getNodes();


                    int depth = 0;
                    Node node = vBox;
                    while(node != null){
                        node = node.getParent();
                        depth++;
                    }

                    System.out.println("DEPTH: " + depth);
                    andBlock.setDepth(depth);

                    vBox.getChildren().clear();
                    vBox.getChildren().add(andBlock.gethBox());

                    VBox.setVgrow(andBlock.gethBox(), Priority.ALWAYS);

                    andBlock.gethBox().setOnDragDetected(newEvent -> {
                        Dragboard new_DB = andBlock.gethBox().startDragAndDrop(TransferMode.ANY);

                        SnapshotParameters snapshotParameters = new SnapshotParameters();
                        snapshotParameters.setFill(Color.TRANSPARENT);
                        new_DB.setDragView(andBlock.gethBox().snapshot(snapshotParameters, null));

                        ClipboardContent content = new ClipboardContent();
                        content.put(FinalMain.customFormat, 1);
                        oQueEstaASerDragged.setNodes(andBlock);

                        System.out.println(oQueEstaASerDragged);
                        new_DB.setContent(content);

                        vBox.getChildren().clear();

                        newEvent.consume();
                    });

                    andBlock.gethBox().setOnDragDone(newEvent -> {
                        if (newEvent.getTransferMode() == TransferMode.MOVE){
                        }

                        newEvent.consume();
                    });

                }

                if(oQueEstaASerDragged.getNodes().getType() == Types.ConditionBlock){
                    ConditionBlock c1 = (ConditionBlock) oQueEstaASerDragged.getNodes();

                    ConditionBlock newBlock = new ConditionBlock(c1.getOperator(), c1.getRule(), c1.getValue(), c1.getoQueEstaASerDragged());

                    System.out.println("estou na caixa do " + andLabel.getText());

                    vBox.getChildren().clear();

                    vBox.getChildren().add(newBlock.gethBox());

                    newBlock.gethBox().setOnDragDetected(newEvent -> {
                        Dragboard new_DB = newBlock.gethBox().startDragAndDrop(TransferMode.ANY);

                        SnapshotParameters snapshotParameters = new SnapshotParameters();
                        snapshotParameters.setFill(Color.TRANSPARENT);
                        new_DB.setDragView(newBlock.gethBox().snapshot(snapshotParameters, null));

                        ClipboardContent content = new ClipboardContent();
                        content.put(FinalMain.customFormat, 1);
                        oQueEstaASerDragged.setNodes(newBlock);

                        System.out.println(oQueEstaASerDragged);
                        new_DB.setContent(content);

                        vBox.getChildren().clear();

                        newEvent.consume();
                    });

                    newBlock.gethBox().setOnDragDone(newEvent -> {
                        if (newEvent.getTransferMode() == TransferMode.MOVE){
                        }

                        newEvent.consume();
                    });


                }



            }

            event.setDropCompleted(success);

            event.consume();
        });
    }


    private VBox getHBox() {
        VBox box = new VBox();

        leftLabel = new Label("Left Condition/Rule");
        rightLabel = new Label("Right Condition/Rule");

        leftLabelVBox = new VBox(leftLabel);
        leftLabelVBox.setAlignment(Pos.CENTER);

        rightLabelVBox = new VBox(rightLabel);
        rightLabelVBox.setAlignment(Pos.CENTER);

        setDrag(leftLabelVBox);
        setDrag(rightLabelVBox);

        String color = "#c7cfb7";

        rightLabelVBox.setStyle("-fx-background-radius: 0 0 7 7;\n" +
                "    -fx-border-radius: 0 0 7 7;\n" +
                "    -fx-background-color:" +color);
        leftLabelVBox.setStyle("-fx-background-radius: 7 7 0 0;\n" +
                "    -fx-border-radius: 7 7 0 0;\n" +
                "    -fx-background-color: " +color);

        VBox.setVgrow(leftLabelVBox, Priority.ALWAYS);
        VBox.setVgrow(rightLabelVBox, Priority.ALWAYS);


        VBox andLabelVBox = new VBox(andLabel);
        andLabelVBox.setAlignment(Pos.CENTER);

        andLabel.setPadding(new Insets(30));
        andLabelVBox.setStyle("-fx-background-color: #f0e4d7;");
        andLabel.setFont(AppStyle.getFont(FontType.ROUNDED_BOLD, 14));
        andLabel.setTextFill(Color.web("#8e7f7f"));

        box.getChildren().addAll(leftLabelVBox,andLabelVBox, rightLabelVBox);

        box.setStyle("-fx-background-radius: 7 7 7 7;\n" +
                "    -fx-border-radius: 7 7 7 7;\n" +
                "    -fx-background-color: transparent;");

        box.setMinWidth(350);
        box.setMinHeight(100);
        box.setAlignment(Pos.CENTER);

        box.setEffect(Others.getDropShadow());

        return box;
    }

    public String getBoxColor() {
        return boxColor;
    }

    @Override
    public Node gethBox() {
        return hBox;
    }

    @Override
    public Types getType() {
        return Types.AndBlock;
    }

    @Override
    public HBox getRuleMakerBox() {
        HBox ruleMakerBox = new HBox(new Label(andLabel.getText()));
        ruleMakerBox.setStyle("-fx-background-radius: 7 7 7 7;\n" +
                "    -fx-border-radius: 7 7 7 7;\n" +
                "    -fx-background-color:" + boxColor + ";");
//ffeebb
        ruleMakerBox.setMinWidth(150);
        ruleMakerBox.setMinHeight(50);

        ruleMakerBox.setAlignment(Pos.CENTER);

        return ruleMakerBox;
    }
}
