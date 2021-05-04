package RuleEditor;

import CodeSmellDetection.RuleOperator;
import g1.ISCTE.AppStyle;
import javafx.application.Platform;
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

/**
 * The type Logic block.
 */
public class LogicBlock implements CustomNode  {

    private final Node graphicalRepresentationNode;
    public Label andLabel;
    public RuleOperator label;
    private VBox rightLabelVBox;
    private VBox leftLabelVBox;
    private VBox andLabelVBox;
    private DraggingObject whatIsBeingDragged = null;
    private String boxColor = null;

    /**
     * Instantiates a new Logic block.
     *
     * @param whatIsBeingDragged o que esta a ser dragged
     * @param label               the label
     * @param boxColor            the box color
     */
    public LogicBlock(DraggingObject whatIsBeingDragged, RuleOperator label, String boxColor) {
        this.label = label;
        this.whatIsBeingDragged = whatIsBeingDragged;
        this.boxColor = boxColor;

        andLabel = new Label(label.label);

        graphicalRepresentationNode = getHBox();
    }

    /**
     * Instantiates a new Logic block.
     *
     * @param logicBlock the logic block
     */
    public LogicBlock(LogicBlock logicBlock){
        this.label = logicBlock.label;
        this.whatIsBeingDragged = logicBlock.whatIsBeingDragged;
        this.boxColor = logicBlock.boxColor;
        andLabel = logicBlock.andLabel;
        this.graphicalRepresentationNode = logicBlock.graphicalRepresentationNode;
        this.leftLabelVBox = logicBlock.leftLabelVBox;
        this.rightLabelVBox = logicBlock.rightLabelVBox;
        this.andLabelVBox = logicBlock.andLabelVBox;
    }

    /**
     * Instantiates a new Logic block.
     *
     * @param label the label
     */
    public LogicBlock(RuleOperator label) {
        this.label = label;
        andLabel = new Label(label.label);
        graphicalRepresentationNode = getHBox();
    }

    /**
     * Gets left label v box.
     *
     * @return the left label v box
     */
    public VBox getLeftLabelVBox() {
        return leftLabelVBox;
    }

    /**
     * Gets right label v box.
     *
     * @return the right label v box
     */
    public VBox getRightLabelVBox() {
        return rightLabelVBox;
    }

    /**
     * Define what happens when something is dragged.
     */
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

                if(whatIsBeingDragged.getNode().getType() != Types.MetricBlock){
                    addCustomNodeOnDrag(vBox, whatIsBeingDragged.getNode().getCopy());
                }
            }

            event.setDropCompleted(success);

            event.consume();
        });
    }

    /**
     * Updates the right/left label of the LogicBlock.
     */
    private void setLabelListener(VBox vBox, Label label) {
        ObservableList<Node> children = vBox.getChildren();

        children.addListener((ListChangeListener<Node>) change -> {

            while (change.next()) {
                if (change.wasRemoved()) {
                    if(!change.getRemoved().contains(label)){
                        if (!vBox.getChildren().contains(label)) {
                            vBox.getChildren().add(label);
                        }
                    }
                }
            }

            if(vBox.getChildren().size() == 2){
                Platform.runLater(() -> vBox.getChildren().remove(label));
            }
        });
    }

    /**
     * Set the LogicBlock delete menu, that allows the user to delete a LogicBlock when creating/editing a rule.
     */
    private void setLogicBlockVBoxDelete() {
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

    /**
     * Set up the HBox and its components.
     *
     * @return HBox
     */
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

        setLogicBlockVBoxDelete();

        andLabelVBox.setAlignment(Pos.CENTER);

        andLabel.setPadding(new Insets(30));
        andLabelVBox.setStyle("-fx-background-color: #f0e4d7;");
       // andLabel.setFont(AppStyle.getFont(FontType.ROUNDED_SEMI_BOLD, 14));
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

    /**
     * Add custom node on drag.
     *
     * @param vBox       the v box
     * @param customNode the custom node
     */
    public void addCustomNodeOnDrag(VBox vBox, CustomNode customNode ){
        Platform.runLater(() -> vBox.getChildren().clear());
        Platform.runLater(() -> vBox.getChildren().add(customNode.getGraphicalRepresentation()));

        FinalMain.ruleNodes.add(customNode);

        customNode.getGraphicalRepresentation().setOnDragDetected(newEvent -> {
            Dragboard new_DB = customNode.getGraphicalRepresentation().startDragAndDrop(TransferMode.ANY);

            SnapshotParameters snapshotParameters = new SnapshotParameters();
            snapshotParameters.setFill(Color.TRANSPARENT);
            new_DB.setDragView(customNode.getGraphicalRepresentation().snapshot(snapshotParameters, null));

            ClipboardContent content = new ClipboardContent();
            content.put(FinalMain.customFormat, 1);
            whatIsBeingDragged.setNode(customNode);

            new_DB.setContent(content);

            vBox.getChildren().clear();

            newEvent.consume();
        });
    }

    /**
     * Add to the right side of CustomNode.
     *
     * @param customNode the custom node
     */
    public void addToRight(CustomNode customNode){
        addCustomNodeOnDrag(rightLabelVBox, customNode);
    }

    /**
     * Add to the left side of CustomNode.
     *
     * @param customNode the custom node
     */
    public void addToLeft(CustomNode customNode){
            addCustomNodeOnDrag(leftLabelVBox, customNode);
    }


    /**
     * Gets box color.
     *
     * @return the box color
     */
    public String getBoxColor() {
        return boxColor;
    }

    /**
     * Gets graphical representation of Node.
     *
     * @return graphicalRepresentationNode
     */
    @Override
    public Node getGraphicalRepresentation() {
        return graphicalRepresentationNode;
    }

    /**
     * Gets the block type (LogicBlock).
     *
     * @return Type.LogicBlock
     */
    @Override
    public Types getType() {
        return Types.LogicBlock;
    }

    /**
     * Gets the widgets graphical representation.
     *
     * @return Node
     */
    @Override
    public Node getWidgetGraphicalRepresentation() {
        return CustomNode.getDefaultWidgetGraphicalRepresentation(andLabel.getText(), boxColor);
    }

    /**
     * Gets the operator label.
     *
     * @return operator label
     */
    @Override
    public RuleOperator getOperator() {
        return label;
    }

    /**
     * Gets a copy of this block.
     *
     * @return copy of block
     */
    public LogicBlock getCopy() {
        return new LogicBlock(this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public String toString() {
        JSONObject object = new JSONObject();
        object.put("operator", getOperator().label);
        return object.toJSONString();
        //return "LogicBlock [ operator: " + getOperator().label + " ]";
    }


}
