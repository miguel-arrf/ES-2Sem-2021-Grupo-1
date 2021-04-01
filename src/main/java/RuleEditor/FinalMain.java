package RuleEditor;

import g1.ISCTE.AppStyle;
import g1.ISCTE.NewGUI;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;

public class FinalMain extends Application {

    public static final BorderPane borderPane = new BorderPane();

    public static final VBox mainPane = new VBox();
    public static final StackPane mainPaneStackPane = new StackPane();

    private final ArrayList<CustomNodes> rectanglesTypes = new ArrayList<>();
    private DraggingObject oQueEstaASerDragged = new DraggingObject();

    public static Scene scene;
    public static final DataFormat customFormat = new DataFormat("Node");

    private double lastMouseX = 0;
    private double lastMouseY = 0;

    @Override
    public void start(Stage stage) throws Exception {

        configureBorderPane();

        Scene scene = new Scene(borderPane, 1200, 1200);
        scene.getStylesheets().add(getClass().getResource("/style/AppStyle.css").toExternalForm());

        stage.setTitle("Rule Builder");
        stage.setScene(scene);

        FinalMain.scene = scene;

        stage.show();
    }

    private VBox getVBox(){
        VBox vBoxItems = new VBox();

        vBoxItems.setSpacing(30);
        vBoxItems.setAlignment(Pos.CENTER);
        vBoxItems.setPadding(new Insets(30,30,30,30));

        for(CustomNodes vBox1 : rectanglesTypes){

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
                    ConditionBlock copyBlock = new ConditionBlock(block.getOperator(), block.getRule(), block.getValue(), oQueEstaASerDragged);

                    oQueEstaASerDragged.setNodes(copyBlock);

                }else if(vBox1.getType() == Types.RuleBlock){

                    RuleBlock block = (RuleBlock)vBox1;
                    RuleBlock copyBlock = new RuleBlock(block.getRuleMessage());

                    oQueEstaASerDragged.setNodes(copyBlock);
                }


                System.out.println(oQueEstaASerDragged);
                db.setContent(content);

                event.consume();
            });

            node.setOnDragDone(event -> {
                if (event.getTransferMode() == TransferMode.MOVE){
                }

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

        return scrollPane;
    }

    private void configureBorderPane(){
        ConditionBlock conditionBlock = new ConditionBlock("Operator", "Rule","Value", oQueEstaASerDragged);
        RuleBlock ruleBlock = new RuleBlock("Rule do Migule");
        AndBlock andBlock = new AndBlock(oQueEstaASerDragged, "AND", "#ffeebb");
        AndBlock orBlock = new AndBlock(oQueEstaASerDragged, "OR", "#8f4068");

        rectanglesTypes.add(ruleBlock);
        rectanglesTypes.add(andBlock);
        rectanglesTypes.add(orBlock);
        rectanglesTypes.add(conditionBlock);

        borderPane.setCenter(mainPaneStackPane);
        mainPaneStackPane.getChildren().add(mainPane);
        borderPane.setRight(getScrollPane());

        configureMainPane();
    }

    private void configureMainPane(){
        mainPane.setStyle("-fx-background-color: #3d3c40 ");
        mainPane.setAlignment(Pos.TOP_CENTER);
        mainPane.setPadding(new Insets(20));

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

                if(mainPane.getChildren().size() <1){
                    if(oQueEstaASerDragged.getNodes().getType() == Types.ConditionBlock){
                        ConditionBlock c1 = (ConditionBlock) oQueEstaASerDragged.getNodes();

                        ConditionBlock customRectangle3 = new ConditionBlock(c1.getOperator(), c1.getRule(), c1.getValue(), oQueEstaASerDragged);

                        VBox.setVgrow(customRectangle3.gethBox(), Priority.ALWAYS);
                        mainPane.getChildren().add(customRectangle3.gethBox());

                        /*customRectangle3.gethBox().setOnMouseClicked(mouseEvent -> {

                            if(mouseEvent.getButton() == MouseButton.SECONDARY){
                                HBox hBox = new HBox();
                                Stage newStage = AppStyle.setUpPopup("Value", "noIcon", hBox,getClass().getResource("/style/AppStyle.css").toExternalForm());

                                newStage.setOnCloseRequest(windowEvent -> {
                                    NewGUI.blurBackground(30, 0, 200, FinalMain.borderPane);
                                });

                                FinalMain.scene.setFill(Color.web("#3d3c40"));
                                NewGUI.blurBackground(0, 30, 500, FinalMain.borderPane);
                            }
                        });*/

                        MenuItem deleteMenu = new MenuItem("delete");
                        ContextMenu menu = new ContextMenu(deleteMenu);

                        deleteMenu.setOnAction(actionEvent -> {
                            mainPane.getChildren().remove(customRectangle3.gethBox());
                        });

                        customRectangle3.gethBox().setOnContextMenuRequested(contextMenuEvent -> {
                            menu.show(customRectangle3.gethBox().getScene().getWindow(), contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
                        });


                    }else if(oQueEstaASerDragged.getNodes().getType() == Types.AndBlock){
                        AndBlock c1 = (AndBlock) oQueEstaASerDragged.getNodes();

                        AndBlock customRectangle3 = new AndBlock(oQueEstaASerDragged, c1.getLabel(), c1.getBoxColor());

                        VBox.setVgrow(customRectangle3.gethBox(), Priority.ALWAYS);

                        mainPane.getChildren().add(customRectangle3.gethBox());

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
