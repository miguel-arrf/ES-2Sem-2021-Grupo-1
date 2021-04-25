package RuleEditor;

import code_smell_detection.RuleOperator;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.json.simple.JSONObject;

public class LogicBlock implements CustomNode  {

    private final Node graphicalRepresentationNode;

    public Label andLabel;
    public RuleOperator label;
    private VBox rightLabelVBox;
    private VBox leftLabelVBox;
    private VBox andLabelVBox;
    private DraggingObject oQueEstaASerDragged = null;
    private String boxColor = null;

    public LogicBlock(DraggingObject oQueEstaASerDragged, RuleOperator label, String boxColor) {
        this.label = label;
        this.oQueEstaASerDragged = oQueEstaASerDragged;
        this.boxColor = boxColor;

        andLabel = new Label(label.label);

        graphicalRepresentationNode = getHBox();
    }

    public LogicBlock(LogicBlock logicBlock){
        this.label = logicBlock.label;
        this.oQueEstaASerDragged = logicBlock.oQueEstaASerDragged;
        this.boxColor = logicBlock.boxColor;
        andLabel = logicBlock.andLabel;
        this.graphicalRepresentationNode = logicBlock.graphicalRepresentationNode;
        this.leftLabelVBox = logicBlock.leftLabelVBox;
        this.rightLabelVBox = logicBlock.rightLabelVBox;
        this.andLabelVBox = logicBlock.andLabelVBox;
    }

    public LogicBlock(RuleOperator label) {
        this.label = label;

        andLabel = new Label(label.label);

        graphicalRepresentationNode = getHBox();
    }

    public VBox getLeftLabelVBox() {
        return leftLabelVBox;
    }

    public VBox getRightLabelVBox() {
        return rightLabelVBox;
    }

    private void setDrag(VBox vBox) {

        vBox.setOnDragOver(event -> {
            if (event.getDragboard().hasContent(FinalMain.customFormat)) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        vBox.setPadding(new Insets(30));

        vBox.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasContent(FinalMain.customFormat)) {
                success = true;

                addCustomNodeOnDrag(vBox, oQueEstaASerDragged.getNodes().getCopy());

            }

            event.setDropCompleted(success);

            event.consume();
        });
    }

    private void setLabelListener(VBox vBox, Label label) {
        final ObservableList<Node> children = vBox.getChildren();

        children.addListener((ListChangeListener<Node>) change -> {
            while (change.next()) {
                if (change.wasRemoved()) {
                    for (Node item : change.getRemoved()) {
                        children.remove(item);
                        if (!item.equals(label)) {
                            if (!children.contains(label)) {
                                children.add(label);
                            }
                        }
                    }
                }
            }
        });

    }

    private void setAndLabelVBoxDelete() {
        ContextMenu menu = new ContextMenu();
        andLabelVBox.setOnContextMenuRequested(contextMenuEvent -> menu.show(andLabelVBox.getScene().getWindow(), contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY()));

        MenuItem deleteMenu = new MenuItem("delete");
        menu.getItems().add(deleteMenu);

        deleteMenu.setOnAction(actionEvent -> {
            Pane parent = (Pane) andLabelVBox.getParent().getParent();
            parent.getChildren().remove(this.getGraphicalRepresentation());
            FinalMain.ruleNodes.remove(this);
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
                "    -fx-background-color:" + color);
        leftLabelVBox.setStyle("-fx-background-radius: 7 7 0 0;\n" +
                "    -fx-border-radius: 7 7 0 0;\n" +
                "    -fx-background-color: " + color);


        andLabelVBox = new VBox(andLabel);

        setAndLabelVBoxDelete();


        andLabelVBox.setAlignment(Pos.CENTER);

        andLabel.setPadding(new Insets(30));
        andLabelVBox.setStyle("-fx-background-color: #f0e4d7;");
        andLabel.setFont(AppStyle.getFont(FontType.ROUNDED_BOLD, 14));
        andLabel.setTextFill(Color.web("#8e7f7f"));

        box.getChildren().addAll(leftLabelVBox, andLabelVBox, rightLabelVBox);

        box.setStyle("-fx-background-radius: 7 7 7 7;\n" +
                "    -fx-border-radius: 7 7 7 7;\n" +
                "    -fx-background-color: transparent;");

        box.setMinWidth(350);
        box.setMinHeight(100);
        box.setAlignment(Pos.CENTER);

        box.setEffect(AppStyle.getDropShadow());

        return box;
    }

    public void addCustomNodeOnDrag(VBox vBox, CustomNode customNode ){
        vBox.getChildren().clear();
        vBox.getChildren().add(customNode.getGraphicalRepresentation());

        FinalMain.ruleNodes.add(customNode);


        customNode.getGraphicalRepresentation().setOnDragDetected(newEvent -> {
            Dragboard new_DB = customNode.getGraphicalRepresentation().startDragAndDrop(TransferMode.ANY);

            SnapshotParameters snapshotParameters = new SnapshotParameters();
            snapshotParameters.setFill(Color.TRANSPARENT);
            new_DB.setDragView(customNode.getGraphicalRepresentation().snapshot(snapshotParameters, null));

            ClipboardContent content = new ClipboardContent();
            content.put(FinalMain.customFormat, 1);
            oQueEstaASerDragged.setNodes(customNode);

            //System.out.println(oQueEstaASerDragged);
            new_DB.setContent(content);

            vBox.getChildren().clear();

            newEvent.consume();
        });

    }


    public void addToRight(CustomNode customNode){
        addCustomNodeOnDrag(rightLabelVBox, customNode);
    }

    public void addToLeft(CustomNode customNode){
            addCustomNodeOnDrag(leftLabelVBox, customNode);
    }



    public String getBoxColor() {
        return boxColor;
    }

    @Override
    public Node getGraphicalRepresentation() {
        return graphicalRepresentationNode;
    }

    @Override
    public Types getType() {
        return Types.LogicBlock;
    }

    @Override
    public Node getWidgetGraphicalRepresentation() {
        return CustomNode.getDefaultWidgetGraphicalRepresentation(andLabel.getText(), boxColor);
    }

    @Override
    public RuleOperator getOperator() {
        return label;
    }

    public LogicBlock getCopy() {
        return new LogicBlock(this);
    }

    @Override
    public String toString() {
        JSONObject object = new JSONObject();
        object.put("operator", getOperator().label);
        return object.toJSONString();
        //return "LogicBlock [ operator: " + getOperator().label + " ]";

    }


}
