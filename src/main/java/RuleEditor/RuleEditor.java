package RuleEditor;

import CodeSmellDetection.RuleOperator;
import g1.ISCTE.AppStyle;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Comparator;

import static g1.ISCTE.AppStyle.lightPurpleColor;

public class RuleEditor  {

    public static final DataFormat customFormat = new DataFormat("Node");
    public static ArrayList<CustomNode> ruleNodes = new ArrayList<>();

    private final VBox mainPane = new VBox();
    private final ArrayList<CustomNode> rectanglesTypes = new ArrayList<>();
    private final DraggingObject inDragObject = new DraggingObject();

    private boolean isNewRule = true;

    private Stage primaryStage;

    private JSONObject rule;
    private String ruleName;
    private RuleFileManager ruleFileManager;

    private SplitPane splitPane;

    private final boolean isClassSmell;


    /**
     * Gets the current rule name.
     *
     * @return the rule name.
     */
    public String getRuleName() {
        return ruleName;
    }


    /**
     * Gets the current rule.
     * If the current rule is null, then we need to convert the GUI representation of it to a JSON one.
     * If there is some problem while converting (incorrect rule, for example), the IncorrectRuleException is internally
     * thrown and the rule is returned as null.
     *
     * @return the rule in the JSON representation.
     */
    public JSONObject getRule() {
        JSONParser parser = new JSONParser();
        JSONObject json;
        try {
            if(rule == null){
                rule = ruleFileManager.guiToJSONObject(ruleNodes, getRuleName(),isClassSmell);
            }
            json = (JSONObject) parser.parse(rule.toJSONString());
        } catch (ParseException | IndexOutOfBoundsException | IncorrectRuleException exception) {
            return null;
        }
        return json;
    }

    /**
     * Rule Editor constructor.
     *
     * @param isClassSmell the boolean representing if the current rule is a class or method one.
     */
    public RuleEditor(boolean isClassSmell){
        this.isClassSmell = isClassSmell;
    }


    /**
     * Auxiliary method to initialize the editor without a rule. Meaning this is a rule editor to create a new one.
     *
     * @param stage the current stage.
     * @param ruleFileManager the current file manager that will be updated with the changes to this rule and to know if a rule name is valid or not.
     * @return the SplitPane representing the rule editor.
     */
    public SplitPane getRuleEditor(Stage stage, RuleFileManager ruleFileManager){
        ruleNodes.clear();

        this.ruleFileManager = ruleFileManager;
        splitPane = new SplitPane();
        configureSceneMainView(splitPane, stage);
        primaryStage = stage;

        return splitPane;
    }

    /**
     * Auxiliary method to initialize the editor with a rule. Meaning this is a rule editor to edit an existing one.
     *
     * @param stage the current stage.
     * @param ruleFileManager the current file manager that will be updated with the changes to this rule and to know if a rule name is valid or not.
     * @param jsonObject the json object that represents the rule that wants to be edited.
     * @param ruleName the name of the current rule that is going to be edited.
     * @return the SplitPane representing the rule editor.
     */
    public SplitPane getEditRuleEditor(Stage stage, RuleFileManager ruleFileManager, JSONObject jsonObject, String ruleName){
        ruleNodes.clear();

        isNewRule = false;

        this.ruleName = ruleName;
        this.ruleFileManager = ruleFileManager;
        splitPane = new SplitPane();
        configureSceneMainView(splitPane, stage);

        mainPane.getChildren().clear();
        CustomNode firstCustomNode = ruleFileManager.jsonToGUI(jsonObject, inDragObject);
        addCustomNodeWithouClear(firstCustomNode);
        primaryStage = stage;

        return splitPane;
    }


    /**
     * Auxiliary method to set up the gui of the rule editor.
     *
     * @param splitPane the SplitPane representing the rule editor.
     * @param stage the current stage.
     */
    private void configureSceneMainView(SplitPane splitPane, Stage stage) {
        addDefaultBlocks();

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(mainPane);
        scrollPane.setFitToWidth(true);

        splitPane.setStyle("-fx-background-insets: 0; -fx-padding: 0");
        splitPane.setDividerPositions(0.8);
        splitPane.getItems().add(scrollPane);

        VBox rightVBox = rightVBox(stage);

        splitPane.getItems().add(rightVBox);

        configureMainPane(stage);
    }


    /**
     * VBox to add the "Save" or "Update" button, to close and update the rules in the rule editor.
     *
     * @param stage the current stage. Needed to invoke the close request, to close the rule editor window.
     * @return the VBox with the "Save" or "Update" button.
     */
    private VBox getOptionsVBox(Stage stage){
        VBox vBoxOptions = new VBox();

        HBox.setHgrow(vBoxOptions, Priority.ALWAYS);
        vBoxOptions.setSpacing(30);
        vBoxOptions.setAlignment(Pos.CENTER);
        vBoxOptions.setPadding(new Insets(30, 30, 30, 30));

        vBoxOptions.getChildren().addAll(getSaveButton(stage));

        return vBoxOptions;
    }

    /**
     * VBox with the multiple blocks that the user can drag into the main panel.
     *
     * @return the vbox with draggable blocks.
     */
    private VBox getBlocksVBox() {
        VBox vBoxItems = new VBox();

        HBox.setHgrow(vBoxItems, Priority.ALWAYS);
        vBoxItems.setSpacing(30);
        vBoxItems.setAlignment(Pos.CENTER);
        vBoxItems.setPadding(new Insets(30, 30, 30, 30));

        ArrayList<CustomNode> sortedArrayList = new ArrayList<>(rectanglesTypes);
        sortedArrayList.sort(new SortBlockArrayList());

        for (CustomNode vBox1 : sortedArrayList) {

            Node node = vBox1.getWidgetGraphicalRepresentation();

            node.setOnDragDetected(event -> {
                Dragboard db = node.startDragAndDrop(TransferMode.ANY);

                SnapshotParameters snapshotParameters = new SnapshotParameters();
                snapshotParameters.setFill(Color.TRANSPARENT);
                db.setDragView(node.snapshot(snapshotParameters, null));

                ClipboardContent content = new ClipboardContent();
                content.put(customFormat, 1);

                if (vBox1.getType() == Types.LogicBlock) {
                    LogicBlock block = (LogicBlock) vBox1;
                    LogicBlock copyBlock = new LogicBlock(inDragObject, block.getOperator(), block.getBoxColor());

                    inDragObject.setNode(copyBlock);

                } else if (vBox1.getType() == Types.ConditionBlock) {
                    ConditionBlock block = (ConditionBlock) vBox1;
                    ConditionBlock copyBlock = new ConditionBlock(block.getOperator(), block.getRuleBlock(), block.getValue(), inDragObject);

                    inDragObject.setNode(copyBlock);

                } else if (vBox1.getType() == Types.MetricBlock) {

                    MetricBlock block = (MetricBlock) vBox1;
                    MetricBlock copyBlock = new MetricBlock(block.getMetricMessage());

                    inDragObject.setNode(copyBlock);
                }

                db.setContent(content);

                event.consume();
            });

            vBoxItems.getChildren().add(node);

        }

        int alreadyAddedIncrement = 0;
        for (int i = 1; i < sortedArrayList.size(); i++) {

            if(sortedArrayList.get(i-1).getType() != sortedArrayList.get(i).getType()){

                vBoxItems.getChildren().add(i+alreadyAddedIncrement, new Separator());
                alreadyAddedIncrement++;
            }
        }

        return vBoxItems;
    }


    /**
     * VBox representing the right side of the gui, with the save/update button and the draggable blocks.
     *
     * @param stage the current stage.
     * @return the VBox representing the right side of the GUI.
     */
    private VBox rightVBox(Stage stage) {
        VBox rightVBox = new VBox();

        rightVBox.setPrefWidth(250);
        rightVBox.setMaxWidth(400);
        rightVBox.setSpacing(20);

        rightVBox.setAlignment(Pos.TOP_CENTER);

        rightVBox.setStyle("-fx-background-color: #1c1c1e");

        //We need this to disable the shrinking of the button while resizing the window... Let the upper panel (with the blocks be the one resizing)
        StackPane saveButtonStackPane = AppStyle.getStackPane(getOptionsVBox(stage),350);
        saveButtonStackPane.setMinHeight(Region.USE_PREF_SIZE);

        rightVBox.getChildren().addAll(AppStyle.getStackPane(getBlocksVBox(), 350), saveButtonStackPane);

        rightVBox.setPadding(new Insets(15, 15, 15, 15));

        return rightVBox;

    }


    /**
     * Auxiliary method that displays a textField to set/update the rule name.
     * In case the the name is valid the window is closed. Otherwise the name is not updated or the rule is not created.
     *
     * @param stage the current stage. Needed to fire a close event if the name is valid and close the window.
     */
    private void saveOrUpdateMenu(Stage stage){
        Button saveButton = AppStyle.getBolderButton("Save", "#a3ddcb");
        TextField textField = new TextField(ruleName == null ? "Rule Name" : getRuleName());
        textField.setStyle("-fx-text-inner-color: white; -fx-background-color: #606060");
        textField.setMaxWidth(150);

        HBox hBox = new HBox(textField, saveButton );
        hBox.setStyle("-fx-background-color: #3d3c40");
        hBox.getStyleClass().add("ruleBuilderMenu");
        hBox.setPadding(new Insets(10));
        hBox.setSpacing(10);
        hBox.setMaxHeight(100);
        hBox.setMaxWidth(500);
        hBox.setEffect(AppStyle.getDropShadow());
        hBox.setAlignment(Pos.CENTER);

        Stage popupStage = AppStyle.setUpPopupStage("Rule name", "/RuleBuilderIcon.gif", true);

        VBox.setMargin(hBox, new Insets(20));
        hBox.setPadding(new Insets(10));

        Scene scene = new Scene(hBox);
        scene.getStylesheets().add(getClass().getResource("/style/AppStyle.css").toExternalForm());
        scene.setFill(Color.web("#3d3c40"));

        popupStage.setScene(scene);

        saveButton.setOnAction(actionEvent -> {
            if(ruleFileManager.isValidName(textField.getText()) || (ruleName != null && ruleName.equals(textField.getText()))) {
                ruleName = textField.getText();
                popupStage.fireEvent(new WindowEvent(popupStage, WindowEvent.WINDOW_CLOSE_REQUEST));
            } else {
                textField.setStyle("-fx-border-radius: 10; -fx-text-inner-color: white; -fx-background-color: #606060; -fx-border-color: red");
            }
        });

        popupStage.show();

        popupStage.setOnCloseRequest(windowEvent -> {
            try {
                rule = ruleFileManager.guiToJSONObject(ruleNodes, getRuleName(), isClassSmell);
                stage.setTitle(ruleName);
                primaryStage.fireEvent(new WindowEvent(primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST));
            } catch (IncorrectRuleException e) {
                System.err.println("Incorrect rule");
                mainPane.setStyle("-fx-background-color: " + AppStyle.redRowBackgroundColor + "; -fx-background-radius: 0 0 7 7 ");
            }
        });

    }

    /**
     * Gets and sets the save or update button.
     *
     * @param stage the current stage.
     * @return the button.
     */
    private Button getSaveButton(Stage stage) {
        Button saveButton = AppStyle.getButtonWithDropShadow(isNewRule ? "Save me :3" : "Update me :3", lightPurpleColor);

        saveButton.setOnAction(actionEvent -> saveOrUpdateMenu(stage));

        return saveButton;
    }

    /**
     * Sets the predefined blocks to be dragged.
     */
    private void addDefaultBlocks() {
        ConditionBlock conditionBlock = new ConditionBlock(RuleOperator.DEFAULT, "Value", inDragObject);
        MetricBlock locClassBlock = new MetricBlock("LOC_Class");
        MetricBlock nomClassBlock = new MetricBlock("NOM_Class");
        MetricBlock WMC_Class = new MetricBlock("WMC_Class");
        MetricBlock LOC_method = new MetricBlock("LOC_Method");
        MetricBlock CYCLO_method = new MetricBlock("CYCLO_Method");


        LogicBlock logicBlock = new LogicBlock(inDragObject, RuleOperator.AND, AppStyle.lightOrangeColor);
        LogicBlock orBlock = new LogicBlock(inDragObject, RuleOperator.OR, AppStyle.lightPinkColor);

        if(isClassSmell){
            rectanglesTypes.add(locClassBlock);
            rectanglesTypes.add(nomClassBlock);
            rectanglesTypes.add(WMC_Class);
        }else{
            rectanglesTypes.add(LOC_method);
            rectanglesTypes.add(CYCLO_method);
        }

        rectanglesTypes.add(logicBlock);
        rectanglesTypes.add(orBlock);
        rectanglesTypes.add(conditionBlock);
    }

    /**
     * Configures the mainPane graphics and sets the behaviour on drag.
     *
     * @param stage the current stage.
     */
    private void configureMainPane(Stage stage) {
        mainPane.setStyle("-fx-background-color: " + AppStyle.darkGrayBoxColor);
        mainPane.setAlignment(Pos.TOP_CENTER);
        mainPane.setPadding(new Insets(20));

        Label firstLabel = new Label("Drag & Drop here");

        stage.heightProperty().addListener((observableValue, number, t1) -> firstLabel.setMinHeight(t1.doubleValue()-80));

        firstLabel.setMaxWidth(Double.MAX_VALUE);
        firstLabel.setTextFill(Color.WHITE);
        firstLabel.setAlignment(Pos.CENTER);
        firstLabel.setPadding(new Insets(20,100,20,100));
        firstLabel.setBorder(new Border(new BorderStroke(Color.web(AppStyle.grayTextColor), BorderStrokeStyle.DASHED, new CornerRadii(7), new BorderWidths(2))));
        firstLabel.setBackground(new Background(new BackgroundFill(Color.web("rgba(118,116,126,0.3)"), new CornerRadii(7), Insets.EMPTY)));

        mainPane.setAlignment(Pos.CENTER);
        mainPane.getChildren().add(firstLabel);

       setMainPaneBehaviourOnDrag(firstLabel);
    }

    /**
     * Sets the behaviour on drag in the mainPane.
     *
     * @param firstLabel the label to be added if there are no blocks dragged, or to be removed, if there are.
     */
    private void setMainPaneBehaviourOnDrag(Label firstLabel){
        mainPane.getChildren().addListener((ListChangeListener<Node>) change -> {
            while (change.next()) {
                if (change.wasRemoved() && mainPane.getChildren().size() == 0) {
                    if (!change.getRemoved().get(0).equals(firstLabel)) {
                        mainPane.getChildren().add(firstLabel);
                    }
                }
            }
        });

        mainPane.setOnDragOver(event -> {
            if (event.getDragboard().hasContent(customFormat)) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        mainPane.setOnDragExited(event -> {
            mainPane.getStyleClass().remove("background");

            event.consume();
        });

        mainPane.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasContent(customFormat)) {
                success = true;


                if (mainPane.getChildren().contains(firstLabel)) {
                    if (inDragObject.getNode().getType() != Types.MetricBlock) {
                        mainPane.getChildren().clear();
                    }
                }


                if (mainPane.getChildren().size() < 1) {
                    CustomNode copy = inDragObject.getNode().getCopy();
                    addCustomNode(copy);
                }

                mainPane.setStyle("-fx-background-color: " + AppStyle.darkGrayBoxColor);

            }

            event.setDropCompleted(success);

            event.consume();
        });
    }

    /**
     * Adds a copy of the dragged node into the main pane. Removes the previously added into the ruleNodes,
     * since this is the root one.
     *
     * @param customNode the node to be added.
     */
    private void addCustomNode(CustomNode customNode) {
        VBox.setVgrow(customNode.getGraphicalRepresentation(), Priority.ALWAYS);
        mainPane.getChildren().add(customNode.getGraphicalRepresentation());

        ruleNodes.clear();
        ruleNodes.add(customNode);
    }

    /**
     * Adds a copy of the dragged node into the main pane. Doesn't remove the previously added since this is to be used
     * when we are in edit mode in the rule editor. As since, then the RuleEditor was opened, when the JSON rule was parsed,
     * the children node (if there were any), were added into the rulesNode, as such, it can't be emptied.
     *
     * The node is added into the first position, since it is the root one.
     *
     * @param customNode the node to be added.
     */
    private void addCustomNodeWithouClear(CustomNode customNode){
        VBox.setVgrow(customNode.getGraphicalRepresentation(), Priority.ALWAYS);
        mainPane.getChildren().add(customNode.getGraphicalRepresentation());

        ruleNodes.add(0,customNode);
    }


}

/**
 * Class to order the Conditions and Rules blocks in the Rule Editor GUI.
 */
class SortBlockArrayList implements Comparator<CustomNode> {

    @Override
    public int compare(CustomNode a, CustomNode b) {
        if (a.getType() == Types.MetricBlock){
            return -1;
        }if (a.getType() == b.getType()){
            return 0;
        }
        if (a.getType() == Types.LogicBlock && b.getType() == Types.ConditionBlock){
            return 1;
        }
        if (b.getType() == Types.LogicBlock && a.getType() == Types.ConditionBlock){
            return -1;
        }

        return 1;
    }
}
