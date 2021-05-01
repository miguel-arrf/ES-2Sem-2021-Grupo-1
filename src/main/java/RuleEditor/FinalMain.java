package RuleEditor;

import code_smell_detection.RuleOperator;
import g1.ISCTE.AppStyle;
import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.Image;
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

public class FinalMain extends Application {

    public static final DataFormat customFormat = new DataFormat("Node");
    public static ArrayList<CustomNode> ruleNodes = new ArrayList<>();

    private final VBox mainPane = new VBox();
    private final ArrayList<CustomNode> rectanglesTypes = new ArrayList<>();
    private final DraggingObject inDragObject = new DraggingObject();

    private boolean isNewRule = true;

    private JSONObject rule;
    private String ruleName;
    private RuleFileManager ruleFileManager;

    private boolean isClassSmell;

    public String getRuleName() {
        return ruleName;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public JSONObject getRule() {
        JSONParser parser = new JSONParser();
        JSONObject json = null;
        try {
            if(rule == null){
                rule = ruleFileManager.guiToJSONObject(ruleNodes, getRuleName(),isClassSmell);
            }
            json = (JSONObject) parser.parse(rule.toJSONString());
        } catch (ParseException | IndexOutOfBoundsException exception) {
            return null;
        }
        return json;
    }

    public FinalMain(boolean isClassSmell){
        this.isClassSmell = isClassSmell;
    }

    @Override
    public void start(Stage stage) {

        SplitPane splitPane = new SplitPane();
        configureSceneMainView(splitPane, stage);

        Scene scene = new Scene(splitPane, 1200, 1200);

        configureSceneAndStage(scene, stage);

        stage.show();
    }

    public SplitPane getRuleEditor(Stage stage, RuleFileManager ruleFileManager){
        this.ruleFileManager = ruleFileManager;
        SplitPane splitPane = new SplitPane();
        configureSceneMainView(splitPane, stage);

        return splitPane;
    }

    public SplitPane getEditRuleEditor(Stage stage, RuleFileManager ruleFileManager, JSONObject jsonObject, String ruleName){
        isNewRule = false;

        this.ruleName = ruleName;
        this.ruleFileManager = ruleFileManager;
        SplitPane splitPane = new SplitPane();
        configureSceneMainView(splitPane, stage);

        mainPane.getChildren().clear();
        CustomNode firstCustomNode = ruleFileManager.jsonToGUI(jsonObject, inDragObject);
        addCustomNodeWithouClear(firstCustomNode);


        return splitPane;
    }

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



    private void configureSceneAndStage(Scene scene, Stage stage) {
        scene.getStylesheets().add(getClass().getResource("/style/AppStyle.css").toExternalForm());

        stage.setTitle("Rule Builder");
        stage.getIcons().add(new Image(AppStyle.class.getResourceAsStream("/RuleBuilderIcon.gif")));

        stage.setScene(scene);
    }


    private VBox getOptionsVBox(Stage stage){
        VBox vBoxOptions = new VBox();

        HBox.setHgrow(vBoxOptions, Priority.ALWAYS);
        vBoxOptions.setSpacing(30);
        vBoxOptions.setAlignment(Pos.CENTER);
        vBoxOptions.setPadding(new Insets(30, 30, 30, 30));

        vBoxOptions.getChildren().addAll(getSaveButton(stage)/*, getLoadButton()*/);


        return vBoxOptions;
    }

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

    private StackPane getStackPane(VBox content){
        //ScrollPane where boxes go
        ScrollPane scrollPane = getScrollPane(content);

        //StackPane due to rounded corners...
        StackPane stackPane = new StackPane();
        VBox emptyPane = new VBox();
        emptyPane.getStyleClass().add("emptyLeftPane");
        VBox.setVgrow(emptyPane, Priority.ALWAYS);

        stackPane.getChildren().add(emptyPane);//Background...
        stackPane.getChildren().add(scrollPane);

        return stackPane;
    }

    private ScrollPane getScrollPane(VBox content) {
        ScrollPane scrollPane = new ScrollPane();

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(content);

        scrollPane.setMinWidth(250);
        scrollPane.setPrefWidth(200);
        scrollPane.setMaxWidth(350);

        HBox.setHgrow(scrollPane, Priority.ALWAYS);

        scrollPane.setFitToWidth(true);

        scrollPane.getStylesheets().add(getClass().getResource("/style/scrollPanel.css").toExternalForm());


        return scrollPane;
    }

    private VBox rightVBox(Stage stage) {
        VBox rightVBox = new VBox();

        rightVBox.setPrefWidth(250);
        rightVBox.setMaxWidth(400);
        rightVBox.setSpacing(20);

        rightVBox.setAlignment(Pos.TOP_CENTER);

        rightVBox.setStyle("-fx-background-color: #1c1c1e");


        //We need this to disable the shrinking of the button while resizing the window... Let the upper panel (with the blocks be the one resizing)
        StackPane saveButtonStackPane = getStackPane(getOptionsVBox(stage));
        saveButtonStackPane.setMinHeight(Region.USE_PREF_SIZE);


        rightVBox.getChildren().addAll(getStackPane(getBlocksVBox()), saveButtonStackPane);


        rightVBox.setPadding(new Insets(15, 15, 15, 15));

        return rightVBox;

    }


    private void textFieldStage(Stage stage){

        Button closeWindow = ConditionBlock.getStyledButton("Save", "#a3ddcb");
        TextField textField = new TextField(ruleName == null ? "Rule Name" : getRuleName());
        textField.setStyle("-fx-text-inner-color: white;");
        textField.setMaxWidth(150);


        HBox hBox = new HBox(textField, closeWindow );
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

        closeWindow.setOnAction(actionEvent -> popupStage.fireEvent(new WindowEvent(popupStage, WindowEvent.WINDOW_CLOSE_REQUEST)));

        popupStage.show();

        popupStage.setOnCloseRequest(windowEvent -> {
            ruleName = textField.getText();
            stage.setTitle(ruleName);



            rule = ruleFileManager.guiToJSONObject(ruleNodes, getRuleName(), isClassSmell);

        });

    }


    private Button getSaveButton(Stage stage) {


        Button saveButton = new Button(isNewRule ? "Save me :3" : "Update me :3");
        saveButton.setOnAction(actionEvent -> {
            textFieldStage(stage);
        });


        saveButton.setStyle("-fx-background-radius: 7 7 7 7;\n" +
                "    -fx-border-radius: 7 7 7 7;\n" +
                "    -fx-background-color: #a29bfe");
        saveButton.setMinWidth(150);
        saveButton.setMinHeight(50);
        saveButton.setAlignment(Pos.CENTER);

        saveButton.setMaxWidth(Double.MAX_VALUE);


        return saveButton;
    }

    private void addDefaultBlocks() {
        ConditionBlock conditionBlock = new ConditionBlock(RuleOperator.DEFAULT, "Value", inDragObject);
        MetricBlock locClassBlock = new MetricBlock("LOC_Class");
        MetricBlock nomClassBlock = new MetricBlock("NOM_Class");
        MetricBlock WMC_Class = new MetricBlock("WMC_Class");
        MetricBlock is_God_Class = new MetricBlock("is_God_Class");
        MetricBlock LOC_method = new MetricBlock("LOC_Method");
        MetricBlock CYCLO_method = new MetricBlock("CYCLO_Method");
        MetricBlock is_Long_Method = new MetricBlock("is_Long_Method");


        LogicBlock logicBlock = new LogicBlock(inDragObject, RuleOperator.AND, "#ffeebb");
        LogicBlock orBlock = new LogicBlock(inDragObject, RuleOperator.OR, "#8f4068");

        if(isClassSmell){
            rectanglesTypes.add(locClassBlock);
            rectanglesTypes.add(nomClassBlock);
            rectanglesTypes.add(WMC_Class);
            rectanglesTypes.add(is_God_Class);
        }else{
            rectanglesTypes.add(LOC_method);
            rectanglesTypes.add(CYCLO_method);
            rectanglesTypes.add(is_Long_Method);
        }

        rectanglesTypes.add(logicBlock);
        rectanglesTypes.add(orBlock);
        rectanglesTypes.add(conditionBlock);
    }

    private void configureMainPane(Stage stage) {
        mainPane.setStyle("-fx-background-color: #3d3c40 ");
        mainPane.setAlignment(Pos.TOP_CENTER);
        mainPane.setPadding(new Insets(20));

        Label firstLabel = new Label("Drag & Drop here");

        stage.heightProperty().addListener((observableValue, number, t1) -> firstLabel.setMinHeight(t1.doubleValue()-80));

        firstLabel.setMaxWidth(Double.MAX_VALUE);
        firstLabel.setTextFill(Color.WHITE);
        firstLabel.setAlignment(Pos.CENTER);
        firstLabel.setPadding(new Insets(20,100,20,100));
        firstLabel.setBorder(new Border(new BorderStroke(Color.web("#76747e"), BorderStrokeStyle.DASHED, new CornerRadii(7), new BorderWidths(2))));
        firstLabel.setBackground(new Background(new BackgroundFill(Color.web("rgba(118,116,126,0.3)"), new CornerRadii(7), Insets.EMPTY)));

        mainPane.setAlignment(Pos.CENTER);

        mainPane.getChildren().add(firstLabel);

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

            }

            event.setDropCompleted(success);

            event.consume();
        });

    }

    private void addCustomNode(CustomNode customNode) {

        VBox.setVgrow(customNode.getGraphicalRepresentation(), Priority.ALWAYS);
        mainPane.getChildren().add(customNode.getGraphicalRepresentation());

        ruleNodes.clear();
        ruleNodes.add(customNode);
    }

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
