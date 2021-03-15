package g1.ISCTE;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class NewGUI extends Application {

    private File selectedFile = null;

    private final VBox centerPane = new VBox();
    private final VBox filePane = new VBox();

    public static void main( String[] args ) {
        launch(args);
    }

    private VBox getLeft(){
        VBox leftVBox = new VBox();

        leftVBox.setPrefWidth(250);
        leftVBox.setMaxWidth(400);

        leftVBox.setStyle("-fx-background-color: #1c1c1e");
        leftVBox.getChildren().addAll(getEmptyLeftPane(), filePane);
        VBox.setVgrow(filePane, Priority.ALWAYS);

        filePane.setSpacing(10);

        leftVBox.setPadding(new Insets(15,15,15,15));

        return leftVBox;
    }

    private void updateFilePane(){
        if (selectedFile != null){

            filePane.getChildren().clear();

            filePane.setPadding(new Insets(15,0,0,0));

             if(selectedFile.isDirectory()){

                MyTree myTree = new MyTree();

                ScrollPane scrollPane = myTree.getScrollPane(selectedFile);
                VBox.setVgrow(scrollPane, Priority.ALWAYS);

                StackPane stackPane = new StackPane();
                VBox emptyLeftPane = new VBox();
                emptyLeftPane.getStyleClass().add("emptyLeftPane");
                VBox.setVgrow(emptyLeftPane, Priority.ALWAYS);

                stackPane.getChildren().add(emptyLeftPane);
                stackPane.getChildren().add(scrollPane);

                VBox.setVgrow(stackPane, Priority.ALWAYS);

                filePane.getChildren().add(stackPane);

            }

        }
    }

    private Pane getSpacer(){
        Pane pane = new Pane();
        pane.setPrefHeight(5);

        return pane;
    }

    private VBox getEmptyLeftPane(){

        VBox emptyLeftPane = new VBox();
        emptyLeftPane.setSpacing(10);

        Label selectFilesLabel = new Label("Select your Java Project!");
        selectFilesLabel.setTextFill(Color.web("#b7b7b8"));
        selectFilesLabel.setFont(AppStyle.getFont(FontType.ROUNDED_BOLD, 14));

        Label filesFormat = new Label("Folder should have a java project");
        filesFormat.setTextFill(Color.web("#76747e"));
        filesFormat.setFont(AppStyle.getFont(FontType.DISPLAY_MEDIUM, 12));

        VBox dragAndDropVBox = new VBox();
        dragAndDropVBox.setPrefHeight(50);
        dragAndDropVBox.setBorder(new Border(new BorderStroke(Color.web("#76747e"), BorderStrokeStyle.DASHED, new CornerRadii(7), new BorderWidths(2))));
        dragAndDropVBox.setAlignment(Pos.CENTER);
        dragAndDropVBox.setBackground(new Background(new BackgroundFill(Color.web("rgba(118,116,126,0.3)"), new CornerRadii(7), Insets.EMPTY)));

        dragAndDropVBox.setOnDragOver(event -> {
            if (event.getGestureSource() != dragAndDropVBox
                    && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        dragAndDropVBox.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                selectedFile = db.getFiles().get(0);
                updateFilePane();
                success = true;
            }

            event.setDropCompleted(success);

            event.consume();
        });

        dragAndDropVBox.getChildren().add(
                AppStyle.getLabelWithColorAndFont(Color.web("#76747e"), FontType.ROUNDED_BOLD, 10, "Drag & drop folder here")
        );


        Button selectFolder = new Button("Select Folder");
        selectFolder.setTextFill(Color.WHITE);
        selectFolder.setMaxWidth(Double.MAX_VALUE);
        selectFolder.getStyleClass().add("selectFolderButton");
        selectFolder.setFont(AppStyle.getFont(FontType.ROUNDED_BOLD, 10));

        selectFolder.setOnMouseClicked(event -> {
            final DirectoryChooser directoryChooser =
                    new DirectoryChooser();
            final File selectedDirectory =
                    directoryChooser.showDialog(selectFolder.getScene().getWindow());
            if (selectedDirectory != null) {
                selectedFile = selectedDirectory;
                updateFilePane();
            }
        });

        HBox buttonsBox = new HBox(selectFolder);
        buttonsBox.setSpacing(10);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(selectFolder, Priority.ALWAYS);

        emptyLeftPane.setPadding(new Insets(10,10,10,10));
        emptyLeftPane.getChildren().addAll(selectFilesLabel, filesFormat,getSpacer(), dragAndDropVBox,getSpacer(), buttonsBox);


        emptyLeftPane.getStyleClass().add("emptyLeftPane");

        emptyLeftPane.setMinHeight(100);

        return emptyLeftPane;
    }

    private void loadCenterPane(){
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        File f = new File(getClass().getResource("/teste.html").getFile());
        webEngine.load(f.toURI().toString());

        VBox.setVgrow(webView, Priority.ALWAYS);

        centerPane.getChildren().add(webView);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("CodeSmells Detector");

        SplitPane splitPane = new SplitPane();
        splitPane.setStyle("-fx-background-insets: 0; -fx-padding: 0;");

        stage.getIcons().add(new Image(getClass().getResourceAsStream("/CodeSmellsIcon.gif")));

        VBox leftPane = getLeft();
        leftPane.setMinWidth(300);

        loadCenterPane();
        centerPane.setMinWidth(600);

        splitPane.setDividerPositions(0.25);
        splitPane.getItems().addAll(leftPane, centerPane);

        Scene scene = new Scene(splitPane,1000,800);
        scene.getStylesheets().add(getClass().getResource("/style/AppStyle.css").toExternalForm());
        stage.setMinWidth(900);
        stage.setMinHeight(300);

        stage.setScene(scene);
        stage.show();
    }
}