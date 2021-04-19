package RuleEditor;

import code_smell_detection.RuleNode;
import code_smell_detection.RuleOperator;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
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

import java.sql.SQLOutput;
import java.util.ArrayList;

public class FinalMain extends Application {

    public static SplitPane splitPane;

    public static final VBox mainPane = new VBox();
    public static ScrollPane mainScrollPane;

    private final ArrayList<CustomNodes> rectanglesTypes = new ArrayList<>();
    private final DraggingObject oQueEstaASerDragged = new DraggingObject();

    public static Scene scene;
    public static final DataFormat customFormat = new DataFormat("Node");

    private RuleNode rootRule;

    public static RuleComplete rule;
    public static ArrayList<RuleComplete> search = new ArrayList<>();

    @Override
    public void start(Stage stage) {

        mainScrollPane = new ScrollPane();

        splitPane = new SplitPane();
        splitPane.setStyle("-fx-background-insets: 0; -fx-padding: 0");
        splitPane.setDividerPositions(0.8);

        configureSplitPane();

        Scene scene = new Scene(splitPane, 1200, 1200);
        scene.getStylesheets().add(getClass().getResource("/style/AppStyle.css").toExternalForm());


        stage.setTitle("Rule Builder");
        stage.setScene(scene);

        FinalMain.scene = scene;

        stage.show();
    }

    private VBox getVBox(){
        VBox vBoxItems = new VBox();

        HBox.setHgrow(vBoxItems, Priority.ALWAYS);


        vBoxItems.setSpacing(30);
        vBoxItems.setAlignment(Pos.CENTER);
        vBoxItems.setPadding(new Insets(30,30,30,30));

        ArrayList<CustomNodes> sortedArrayList = new ArrayList<>(rectanglesTypes);

        sortedArrayList.sort((a,b) -> {
            if(a.getType() == Types.RuleBlock){
                return -1;
            }else if(a.getType() == b.getType()){
                return 0;
            }else if(a.getType() == Types.AndBlock && b.getType() == Types.ConditionBlock){
                return 1;
            }else if(b.getType() == Types.AndBlock && a.getType() == Types.ConditionBlock){
                return -1;
            }
            return 1;

        });

        for(CustomNodes vBox1 : sortedArrayList){

            Node node = vBox1.getRuleMakerBox();

            node.setOnDragDetected(event -> {
                Dragboard db = node.startDragAndDrop(TransferMode.ANY);

                SnapshotParameters snapshotParameters = new SnapshotParameters();
                snapshotParameters.setFill(Color.TRANSPARENT);
                db.setDragView(node.snapshot(snapshotParameters, null));

                ClipboardContent content = new ClipboardContent();
                content.put(customFormat, 1);

                if(vBox1.getType() == Types.AndBlock){
                    AndBlock block = (AndBlock)vBox1;
                    AndBlock copyBlock = new AndBlock(oQueEstaASerDragged, block.getLabel(), block.getBoxColor());

                    oQueEstaASerDragged.setNodes(copyBlock);

                }else if(vBox1.getType() == Types.ConditionBlock){
                    ConditionBlock block = (ConditionBlock)vBox1;
                    ConditionBlock copyBlock = new ConditionBlock(block.getOperator(), block.getRuleBlock(), block.getValue(), oQueEstaASerDragged);

                    oQueEstaASerDragged.setNodes(copyBlock);

                }else if(vBox1.getType() == Types.RuleBlock){

                    RuleBlock block = (RuleBlock)vBox1;
                    RuleBlock copyBlock = new RuleBlock(block.getRuleMessage(), block.getIsNumeric());

                    oQueEstaASerDragged.setNodes(copyBlock);
                }


                System.out.println(oQueEstaASerDragged);
                db.setContent(content);

                event.consume();
            });


            vBoxItems.getChildren().add(node);


        }

        return vBoxItems;
    }

    private ScrollPane getScrollPane(){
        ScrollPane scrollPane = new ScrollPane();

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(getVBox());

        scrollPane.setMinWidth(250);
        scrollPane.setPrefWidth(200);
        scrollPane.setMaxWidth(350);

        HBox.setHgrow(scrollPane, Priority.ALWAYS);


        scrollPane.setFitToWidth(true);


        //scrollPane.setFitToHeight(true);

        scrollPane.getStylesheets().add(getClass().getResource("/style/scrollPanel.css").toExternalForm());


        return scrollPane;
    }

    private VBox rightVBox(){
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


        rightVBox.getChildren().addAll(stackPane, getSaveButton());


        rightVBox.setPadding(new Insets(15,15,15,15));

        return rightVBox;

    }

    private void transverseMainPaneChildren(){
        ObservableList<Node> children = mainPane.getChildren();

        for (Node node : children){
            //System.out.println(node);
        }
    }

    private VBox getSaveButton(){
        VBox saveButtonVBox = new VBox();

        Button saveButton = new Button("Save me :3");
        saveButton.setOnAction(actionEvent -> {
            if(rule != null)
                rule.saveRule();

            transverseMainPaneChildren();
            mainPane.setId("teste");
        });


        saveButtonVBox.getChildren().add(saveButton);

        return saveButtonVBox;
    }

    private void configureSplitPane(){
        ConditionBlock conditionBlock = new ConditionBlock("Operator", null,"Value", oQueEstaASerDragged);
        RuleBlock classSizeBlock = new RuleBlock("Class Size", true);
        RuleBlock godBlock = new RuleBlock("God class", false);

        AndBlock andBlock = new AndBlock(oQueEstaASerDragged, "AND", "#ffeebb");
        AndBlock orBlock = new AndBlock(oQueEstaASerDragged, "OR", "#8f4068");

        rectanglesTypes.add(classSizeBlock);
        rectanglesTypes.add(godBlock);

        rectanglesTypes.add(andBlock);
        rectanglesTypes.add(orBlock);
        rectanglesTypes.add(conditionBlock);

        splitPane.getItems().add(mainScrollPane);

        mainScrollPane.setContent(mainPane);
        //mainPaneStackPane.setMinHeight(2000);
        mainScrollPane.setFitToWidth(true);


        VBox rightVBox = rightVBox();

        splitPane.getItems().add(rightVBox);

        configureMainPane();
    }

    private void configureMainPane(){
        mainPane.setStyle("-fx-background-color: #3d3c40 ");
        mainPane.setAlignment(Pos.TOP_CENTER);
        mainPane.setPadding(new Insets(20));

        Label firstLabel = new Label("Drag & Drop here");
        firstLabel.setTextFill(Color.WHITE);

        mainPane.setAlignment(Pos.CENTER);

        mainPane.getChildren().add(firstLabel);


        mainPane.setOnDragOver(event -> {
            if(event.getDragboard().hasContent(customFormat)){
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        mainPane.setOnDragEntered(event -> {
            if(event.getDragboard().hasString()){
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

            if(db.hasContent(customFormat)){
                success = true;


                    if(mainPane.getChildren().contains(firstLabel)){
                        if(oQueEstaASerDragged.getNodes().getType() != Types.RuleBlock) {
                            mainPane.getChildren().clear();
                        }
                    }



                if(mainPane.getChildren().size() <1){


                    if(oQueEstaASerDragged.getNodes().getType() == Types.ConditionBlock){
                        ConditionBlock c1 = (ConditionBlock) oQueEstaASerDragged.getNodes();

                        ConditionBlock customRectangle3 = new ConditionBlock(c1.getOperator(), c1.getRuleBlock(), c1.getValue(), oQueEstaASerDragged);

                        VBox.setVgrow(customRectangle3.getGraphicalRepresentation(), Priority.ALWAYS);
                        mainPane.getChildren().add(customRectangle3.getGraphicalRepresentation());
                        rule = new RuleComplete(customRectangle3);

                        MenuItem deleteMenu = new MenuItem("delete");
                        ContextMenu menu = new ContextMenu(deleteMenu);

                        deleteMenu.setOnAction(actionEvent -> {
                            mainPane.getChildren().remove(customRectangle3.getGraphicalRepresentation());
                            if(mainPane.getChildren().isEmpty()){
                                mainPane.getChildren().add(firstLabel);
                            }
                        });

                        customRectangle3.getGraphicalRepresentation().setOnContextMenuRequested(contextMenuEvent -> menu.show(customRectangle3.getGraphicalRepresentation().getScene().getWindow(), contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY()));


                    }else if(oQueEstaASerDragged.getNodes().getType() == Types.AndBlock){
                        AndBlock c1 = (AndBlock) oQueEstaASerDragged.getNodes();

                        AndBlock customRectangle3 = new AndBlock(oQueEstaASerDragged, c1.getLabel(), c1.getBoxColor());

                        VBox.setVgrow(customRectangle3.getGraphicalRepresentation(), Priority.ALWAYS);

                        mainPane.getChildren().add(customRectangle3.getGraphicalRepresentation());
                        rule = new RuleComplete(customRectangle3);


                        MenuItem deleteMenu = new MenuItem("delete");
                        ContextMenu menu = new ContextMenu(deleteMenu);

                        deleteMenu.setOnAction(actionEvent -> {
                            mainPane.getChildren().remove(customRectangle3.getGraphicalRepresentation());
                            if(mainPane.getChildren().isEmpty()){
                                mainPane.getChildren().add(firstLabel);
                            }
                        });

                        customRectangle3.getCentralBox().setOnContextMenuRequested(contextMenuEvent -> menu.show(customRectangle3.getCentralBox().getScene().getWindow(), contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY()));

                    }
                }

            }

            event.setDropCompleted(success);

            event.consume();
        });

    }


    public static void main(String[] args) {
        launch(args);
    }


}
