package RuleEditor;

import code_smell_detection.RuleOperator;
import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class FinalMain extends Application {

    public static final DataFormat customFormat = new DataFormat("Node");
    public static ArrayList<CustomNode> ruleNodes = new ArrayList<>();

    private final VBox mainPane = new VBox();
    private final ArrayList<CustomNode> rectanglesTypes = new ArrayList<>();
    private final DraggingObject inDragObject = new DraggingObject();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        SplitPane splitPane = new SplitPane();
        configureSceneMainView(splitPane);

        Scene scene = new Scene(splitPane, 1200, 1200);

        configureSceneAndStage(scene, stage);

        stage.show();
    }

    private void configureSceneMainView(SplitPane splitPane) {
        addDefaultBlocks();

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(mainPane);
        scrollPane.setFitToWidth(true);

        splitPane.setStyle("-fx-background-insets: 0; -fx-padding: 0");
        splitPane.setDividerPositions(0.8);
        splitPane.getItems().add(scrollPane);

        VBox rightVBox = rightVBox();

        splitPane.getItems().add(rightVBox);

        configureMainPane();
    }

    private void configureSceneAndStage(Scene scene, Stage stage) {
        scene.getStylesheets().add(getClass().getResource("/style/AppStyle.css").toExternalForm());

        stage.setTitle("Rule Builder");
        stage.setScene(scene);
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

                    inDragObject.setNodes(copyBlock);

                } else if (vBox1.getType() == Types.ConditionBlock) {
                    ConditionBlock block = (ConditionBlock) vBox1;
                    ConditionBlock copyBlock = new ConditionBlock(block.getOperator(), block.getRuleBlock(), block.getValue(), inDragObject);

                    inDragObject.setNodes(copyBlock);

                } else if (vBox1.getType() == Types.RuleBlock) {

                    RuleBlock block = (RuleBlock) vBox1;
                    RuleBlock copyBlock = new RuleBlock(block.getRuleMessage());

                    inDragObject.setNodes(copyBlock);
                }


                //System.out.println(inDragObject);
                db.setContent(content);

                event.consume();
            });


            vBoxItems.getChildren().add(node);


        }

        return vBoxItems;
    }

    private ScrollPane getScrollPane() {
        ScrollPane scrollPane = new ScrollPane();

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(getBlocksVBox());

        scrollPane.setMinWidth(250);
        scrollPane.setPrefWidth(200);
        scrollPane.setMaxWidth(350);

        HBox.setHgrow(scrollPane, Priority.ALWAYS);


        scrollPane.setFitToWidth(true);


        //scrollPane.setFitToHeight(true);

        scrollPane.getStylesheets().add(getClass().getResource("/style/scrollPanel.css").toExternalForm());


        return scrollPane;
    }

    private VBox rightVBox() {
        VBox rightVBox = new VBox();

        rightVBox.setPrefWidth(250);
        rightVBox.setMaxWidth(400);

        rightVBox.setAlignment(Pos.TOP_CENTER);

        rightVBox.setStyle("-fx-background-color: #1c1c1e");


        //ScrollPane where boxes go
        ScrollPane scrollPane = getScrollPane();

        //StackPane due to rounded corners...
        StackPane stackPane = new StackPane();
        VBox emptyPane = new VBox();
        emptyPane.getStyleClass().add("emptyLeftPane");
        VBox.setVgrow(emptyPane, Priority.ALWAYS);

        stackPane.getChildren().add(emptyPane);//Background...
        stackPane.getChildren().add(scrollPane);


        rightVBox.getChildren().addAll(stackPane, getSaveButton(), getLoadButton());


        rightVBox.setPadding(new Insets(15, 15, 15, 15));

        return rightVBox;

    }

    private VBox getSaveButton() {
        VBox saveButtonVBox = new VBox();

        Button saveButton = new Button("Save me :3");
        saveButton.setOnAction(actionEvent -> {

            RuleComplete.createCodeSmell(ruleNodes);
            //RuleComplete.saveToFileSerialize(ruleNodes);
        });


        saveButtonVBox.getChildren().add(saveButton);

        return saveButtonVBox;
    }

    private VBox getLoadButton() {
        VBox saveButtonVBox = new VBox();

        Button saveButton = new Button("Load me papi :c");
        saveButton.setOnAction(actionEvent -> {

            try {
                CustomNode firstCustomNode = RuleComplete.loadJSONFile(inDragObject);
                addCustomNode(firstCustomNode);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });


        saveButtonVBox.getChildren().add(saveButton);

        return saveButtonVBox;
    }

    private void addDefaultBlocks() {
        ConditionBlock conditionBlock = new ConditionBlock(RuleOperator.DEFAULT, "Value", inDragObject);
        RuleBlock classSizeBlock = new RuleBlock("Class Size");
        RuleBlock godBlock = new RuleBlock("God class");

        LogicBlock logicBlock = new LogicBlock(inDragObject, RuleOperator.AND, "#ffeebb");
        LogicBlock orBlock = new LogicBlock(inDragObject, RuleOperator.OR, "#8f4068");

        rectanglesTypes.add(classSizeBlock);
        rectanglesTypes.add(godBlock);

        rectanglesTypes.add(logicBlock);
        rectanglesTypes.add(orBlock);
        rectanglesTypes.add(conditionBlock);
    }

    private void configureMainPane() {
        mainPane.setStyle("-fx-background-color: #3d3c40 ");
        mainPane.setAlignment(Pos.TOP_CENTER);
        mainPane.setPadding(new Insets(20));

        Label firstLabel = new Label("Drag & Drop here");
        firstLabel.setTextFill(Color.WHITE);

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

        mainPane.setOnDragEntered(event -> {
            if (event.getDragboard().hasContent(customFormat)) {
                mainPane.setStyle("-fx-background-color: red ");
                mainPane.getStylesheets().add("style.css");
                mainPane.getStyleClass().add("background");
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
                    if (inDragObject.getNodes().getType() != Types.RuleBlock) {
                        mainPane.getChildren().clear();
                    }
                }


                if (mainPane.getChildren().size() < 1) {
                    CustomNode copy = inDragObject.getNodes().getCopy();
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


}

/**
 * Class to order the Conditions and Rules blocks in the Rule Editor GUI.
 */
class SortBlockArrayList implements Comparator<CustomNode> {

    @Override
    public int compare(CustomNode a, CustomNode b) {
        if (a.getType() == Types.RuleBlock)
            return -1;
        if (a.getType() == b.getType())
            return 0;
        if (a.getType() == Types.LogicBlock && b.getType() == Types.ConditionBlock)
            return 1;
        if (b.getType() == Types.LogicBlock && a.getType() == Types.ConditionBlock)
            return -1;

        return 1;
    }
}
