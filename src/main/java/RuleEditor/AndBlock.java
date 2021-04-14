package RuleEditor;

import g1.ISCTE.AppStyle;
import g1.ISCTE.FontType;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class AndBlock implements CustomNodes{

    private final Node hBox;

    public Label andLabel;

    private VBox rightLabelVBox;
    private VBox leftLabelVBox;
    private VBox andLabelVBox;

    public String label;

    public String getLabel() {
        return label;
    }

    private final DraggingObject oQueEstaASerDragged;

    private final String boxColor;

    public AndBlock(DraggingObject oQueEstaASerDragged, String label, String boxColor){
        this.label = label;
        this.oQueEstaASerDragged = oQueEstaASerDragged;
        andLabel = new Label(label);
        this.boxColor = boxColor;
        hBox = getHBox();
    }

    private void setDepth(int depth){

        java.awt.Color colors = java.awt.Color.decode(boxColor);

        int red = colors.getRed() + depth*3;
        int green = colors.getGreen() + depth*4;
        int blue = colors.getBlue() + depth*2;


        String color = "rgb("+red +","+ green+ ","+ blue +")";

        rightLabelVBox.setStyle("-fx-background-radius: 0 0 7 7;\n" +
                "    -fx-border-radius: 0 0 7 7;\n" +
                "    -fx-background-color:" +color);
        leftLabelVBox.setStyle("-fx-background-radius: 7 7 0 0;\n" +
                "    -fx-border-radius: 7 7 0 0;\n" +
                "    -fx-background-color: " +color);
    }

    public Node getCentralBox(){
        return andLabelVBox;
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
                    vBox.getChildren().add(andBlock.getGraphicalRepresentation());

                    andBlock.getGraphicalRepresentation().setOnDragDetected(newEvent -> {
                        Dragboard new_DB = andBlock.getGraphicalRepresentation().startDragAndDrop(TransferMode.ANY);

                        SnapshotParameters snapshotParameters = new SnapshotParameters();
                        snapshotParameters.setFill(Color.TRANSPARENT);
                        new_DB.setDragView(andBlock.getGraphicalRepresentation().snapshot(snapshotParameters, null));

                        ClipboardContent content = new ClipboardContent();
                        content.put(FinalMain.customFormat, 1);
                        oQueEstaASerDragged.setNodes(andBlock);

                        System.out.println(oQueEstaASerDragged);
                        new_DB.setContent(content);

                        vBox.getChildren().clear();

                        newEvent.consume();
                    });


                    //DELETE
                        MenuItem deleteMenu = new MenuItem("delete");
                        ContextMenu menu = new ContextMenu(deleteMenu);

                        deleteMenu.setOnAction(actionEvent -> vBox.getChildren().remove(andBlock.getGraphicalRepresentation()));

                    andBlock.getCentralBox().setOnContextMenuRequested(contextMenuEvent -> menu.show(andBlock.getCentralBox().getScene().getWindow(), contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY()));
                    //END DELETE

                }

                if(oQueEstaASerDragged.getNodes().getType() == Types.ConditionBlock){
                    ConditionBlock c1 = (ConditionBlock) oQueEstaASerDragged.getNodes();

                    ConditionBlock newBlock = new ConditionBlock(c1.getOperator(), c1.getRuleBlock(), c1.getValue(), c1.getoQueEstaASerDragged());
                    newBlock.setContextMenuDeletion();

                    System.out.println("estou na caixa do " + andLabel.getText());

                    vBox.getChildren().clear();

                    vBox.getChildren().add(newBlock.getGraphicalRepresentation());

                    newBlock.getGraphicalRepresentation().setOnDragDetected(newEvent -> {
                        Dragboard new_DB = newBlock.getGraphicalRepresentation().startDragAndDrop(TransferMode.ANY);

                        SnapshotParameters snapshotParameters = new SnapshotParameters();
                        snapshotParameters.setFill(Color.TRANSPARENT);
                        new_DB.setDragView(newBlock.getGraphicalRepresentation().snapshot(snapshotParameters, null));

                        ClipboardContent content = new ClipboardContent();
                        content.put(FinalMain.customFormat, 1);
                        oQueEstaASerDragged.setNodes(newBlock);

                        System.out.println(oQueEstaASerDragged);
                        new_DB.setContent(content);

                        vBox.getChildren().clear();

                        newEvent.consume();
                    });


                }



            }

            event.setDropCompleted(success);

            event.consume();
        });
    }

    private void setLabelListener(VBox vBox, Label label){
        final ObservableList<Node> children = vBox.getChildren();

        children.addListener((ListChangeListener<Node>) change -> {
            while(change.next()){
                if(change.wasRemoved()){
                    for(Node item: change.getRemoved()){
                        children.remove(item);
                        if(!item.equals(label)){
                            if(!children.contains(label)){
                                children.add(label);
                            }
                        }
                    }
                }
            }
        });

    }

    private VBox getHBox() {
        VBox box = new VBox();

        Label leftLabel = new Label("Left Condition/Rule");
        Label rightLabel = new Label("Right Condition/Rule");

        leftLabelVBox = new VBox(leftLabel);
        leftLabelVBox.setAlignment(Pos.CENTER);
        setLabelListener(leftLabelVBox, leftLabel);


        rightLabelVBox = new VBox(rightLabel);
        rightLabelVBox.setAlignment(Pos.CENTER);
        setLabelListener(rightLabelVBox, rightLabel);


        setDrag(leftLabelVBox);
        setDrag(rightLabelVBox);

        String color = "#c7cfb7";

        rightLabelVBox.setStyle("-fx-background-radius: 0 0 7 7;\n" +
                "    -fx-border-radius: 0 0 7 7;\n" +
                "    -fx-background-color:" +color);
        leftLabelVBox.setStyle("-fx-background-radius: 7 7 0 0;\n" +
                "    -fx-border-radius: 7 7 0 0;\n" +
                "    -fx-background-color: " +color);

        //VBox.setVgrow(leftLabelVBox, Priority.ALWAYS);
        //VBox.setVgrow(rightLabelVBox, Priority.ALWAYS);

        andLabelVBox = new VBox(andLabel);
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

        box.setEffect(AppStyle.getDropShadow());

        return box;
    }

    public String getBoxColor() {
        return boxColor;
    }

    @Override
    public Node getGraphicalRepresentation() {
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
