package g1.ISCTE;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Stream;

public class GUI extends Application {

    private Desktop desktop = Desktop.getDesktop();
    private final FileChooser fileChooser = new FileChooser();
    private File selectedFile = null;
    private Font boldButSmaller = Font.loadFont(getClass().getResource("/fonts/SF-Pro-Rounded-Bold.ttf").toExternalForm(), 10);

    private VBox centerPane = new VBox();

    private VBox filePane = new VBox();

    public static void main( String[] args ) {
        launch(args);
    }

    private VBox getMainControllers(){
        VBox mainVBox = new VBox();

        mainVBox.setStyle("-fx-background-radius: 18 18 18 18;\n" +
                "    -fx-border-radius: 18 18 18 18;\n" +
                "    -fx-background-color: white;");

        mainVBox.setPrefSize(400,400);
        mainVBox.setMaxWidth(200);
        mainVBox.setMaxHeight(200);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetY(10);
        dropShadow.setRadius(40);
        dropShadow.setColor(Color.web("rgba(204,246,200,0.5)"));

        mainVBox.setEffect(dropShadow);

        return mainVBox;
    }

    private MenuBar getMenu(){

        MenuBar menuBar = new MenuBar();

        Menu menu = new Menu("File");

        menuBar.getMenus().add(menu);

        return menuBar;
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

            if(selectedFile.isFile()){
                Label fileName = new Label(selectedFile.getName());
                fileName.setStyle("-fx-background-radius: 7 7 7 7;\n" +
                        "    -fx-border-radius: 7 7 7 7;\n" +
                        "    -fx-background-color: #41d1b6;");
                fileName.setFont(boldButSmaller);
                fileName.setTextFill(Color.WHITE);

                fileName.setPadding(new Insets(5,5,5,5));

                fileName.setMaxWidth(Double.MAX_VALUE);


                filePane.getChildren().add(fileName);

            }else if(selectedFile.isDirectory()){

                ArrayList<File> files = new ArrayList<>();
                getAllJavaFiles(files, selectedFile);


                for(File file:files){
                    Label fileName = new Label(file.getName());
                    fileName.setStyle("-fx-background-radius: 7 7 7 7;\n" +
                            "    -fx-border-radius: 7 7 7 7;\n" +
                            "    -fx-background-color: #2cadf6;");
                    fileName.setFont(boldButSmaller);
                    fileName.setTextFill(Color.WHITE);

                    fileName.setPadding(new Insets(5,5,5,5));

                    fileName.setMaxWidth(Double.MAX_VALUE);

                    fileName.setOnMouseClicked(mouseEvent -> {

                        String text = "";

                        try {
                            Scanner myReader = new Scanner(file);

                            while (myReader.hasNextLine()){
                                text = text + myReader.nextLine() + "\n";

                            }

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        CodeEditor editor = new CodeEditor(text);

                        Label textLabel = new Label(text);
                        textLabel.setTextFill(Color.WHITE);

                        VBox.setVgrow(editor, Priority.ALWAYS);
                        VBox.setVgrow(centerPane, Priority.ALWAYS);

                        centerPane.getChildren().add(editor);
                    });

                    filePane.getChildren().add(fileName);
                }

                /*TreeItem<String> rootItem = new TreeItem<String>(selectedFile.getName());
                rootItem.setExpanded(true);

                percolateFolder(rootItem, selectedFile);

                TreeView<String> tree = new TreeView<String>(rootItem);
                VBox.setVgrow(tree, Priority.ALWAYS);


                tree.getStylesheets().add(getClass().getResource("/style/tree.css").toExternalForm());
                tree.getStyleClass().add("myTree");

                tree.setStyle(
                        "  -fx-base: #1c1c1e ;\n" +
                                "  -fx-control-inner-background: derive(-fx-base,20%);\n" +
                                "  -fx-control-inner-background-alt: derive(-fx-control-inner-background,-10%);\n" +
                                "  -fx-accent: #41d1b6;\n" +
                                "  -fx-focus-color: red;\n" +
                                "  -fx-faint-focus-color: yellow;");



                filePane.getChildren().add(tree);*/
            }

        }
    }
    public Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    private void getAllJavaFiles(ArrayList<File> arrayList, File file){
        File[] files = file.listFiles();

        for(File filename: files){
            if(filename.isDirectory()){
                getAllJavaFiles(arrayList, filename);
            }else if(filename.isFile()){
                Optional<String> extension = getExtensionByStringHandling(filename.getName());
                if (extension.isPresent() && extension.get().equals("java")){
                    arrayList.add(filename);
                }

            }
        }

    }

    private void percolateFolder(TreeItem<String> rootItem, File file){

        File[] files = file.listFiles();

        for(File filename: files){
            if(filename.isDirectory()){
                TreeItem<String> newRootItem = new TreeItem<String>(filename.getName());
                rootItem.getChildren().add(newRootItem);
                percolateFolder(newRootItem, filename);
            }else if(filename.isFile()){
                TreeItem<String> item = new TreeItem<String>(filename.getName());
                rootItem.getChildren().add(item);
            }
        }

    }

    private Pane getSpacer(){
        Pane pane = new Pane();

        pane.setPrefHeight(5);
        return pane;
    }

    private VBox getEmptyLeftPane(){
        Font font = Font.loadFont(getClass().getResource("/fonts/SF-Pro-Rounded-Bold.ttf").toExternalForm(), 14);
        Font regular = Font.loadFont(getClass().getResource("/fonts/SFProDisplay-Medium.ttf").toExternalForm(), 12);


        VBox emptyLeftPane = new VBox();
        emptyLeftPane.setSpacing(10);

        Label selectFilesLabel = new Label("Select your Java Project!");
        selectFilesLabel.setTextFill(Color.web("#b7b7b8"));
        selectFilesLabel.setFont(font);

        Label filesFormat = new Label("Folder should have a java project");
        filesFormat.setTextFill(Color.web("#76747e"));
        filesFormat.setFont(regular);


        VBox dragAndDropVBox = new VBox();
        dragAndDropVBox.setPrefHeight(50);
        dragAndDropVBox.setBorder(new Border(new BorderStroke(Color.web("#76747e"), BorderStrokeStyle.DASHED, new CornerRadii(7), new BorderWidths(2))));
        dragAndDropVBox.setAlignment(Pos.CENTER);
        dragAndDropVBox.setBackground(new Background(new BackgroundFill(Color.web("rgba(118,116,126,0.3)"), new CornerRadii(7), Insets.EMPTY)));

        dragAndDropVBox.setOnDragOver(event -> {
            if (event.getGestureSource() != dragAndDropVBox
                    && event.getDragboard().hasFiles()) {
                /* allow for both copying and moving, whatever user chooses */
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        dragAndDropVBox.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                //System.out.println(db.getFiles().toString());
                selectedFile = db.getFiles().get(0);
                System.out.println(selectedFile);
                updateFilePane();
                success = true;
            }
            /* let the source know whether the string was successfully
             * transferred and used */
            event.setDropCompleted(success);

            event.consume();
        });

        Label dragAndDropLabel = new Label("Drag & drop folder here");
        dragAndDropLabel.setTextFill(Color.web("#76747e"));
        dragAndDropLabel.setFont(boldButSmaller);
        dragAndDropVBox.getChildren().add(dragAndDropLabel);

//        Button selectFiles = new Button("Select Files");
//        selectFiles.setTextFill(Color.WHITE);
//        selectFiles.setMaxWidth(Double.MAX_VALUE);
//        selectFiles.setStyle("-fx-background-radius: 7 7 7 7;\n" +
//                "    -fx-border-radius: 7 7 7 7;\n" +
//                "    -fx-background-color: #6562fc;");
//        selectFiles.setFont(boldButSmaller);
//
//        selectFiles.setOnMouseClicked(event -> {
//            File file = fileChooser.showOpenDialog(selectFiles.getScene().getWindow());
//            if(file != null){
//                if(file.isDirectory()){
//                    System.out.println(file);
//                }
//                if(file.isFile()){
//                    System.out.println(file);
//                }
//            }
//        });

        Button selectFolder = new Button("Select Folder");
        selectFolder.setTextFill(Color.WHITE);
        selectFolder.setMaxWidth(Double.MAX_VALUE);
        selectFolder.setStyle("-fx-background-radius: 7 7 7 7;\n" +
                "    -fx-border-radius: 7 7 7 7;\n" +
                "    -fx-background-color: #6562fc;");
        selectFolder.setFont(boldButSmaller);

        selectFolder.setOnMouseClicked(event -> {
            final DirectoryChooser directoryChooser =
                    new DirectoryChooser();
            final File selectedDirectory =
                    directoryChooser.showDialog(selectFolder.getScene().getWindow());
            if (selectedDirectory != null) {
                selectedDirectory.getAbsolutePath();
            }
        });

        HBox buttonsBox = new HBox(/*selectFiles, */selectFolder);
        buttonsBox.setSpacing(10);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setMaxWidth(Double.MAX_VALUE);
        //HBox.setHgrow(selectFiles, Priority.ALWAYS);
        HBox.setHgrow(selectFolder, Priority.ALWAYS);



        emptyLeftPane.setPadding(new Insets(10,10,10,10));
        emptyLeftPane.getChildren().addAll(selectFilesLabel, filesFormat,getSpacer(), dragAndDropVBox,getSpacer(), buttonsBox);


        emptyLeftPane.setStyle("-fx-background-radius: 10 10 10 10;\n" +
                "    -fx-border-radius: 10 10 10 10;\n" +
                "    -fx-background-color: #3d3c40;");



        emptyLeftPane.setMinHeight(100);


        return emptyLeftPane;
    }

    private void loadCenterPane(){
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        File f = new File("C:\\Users\\mferr\\IdeaProjects\\ES-2Sem-2021-Grupo-1\\src\\teste.html");
        webEngine.load(f.toURI().toString());

        VBox.setVgrow(webView, Priority.ALWAYS);

        centerPane.getChildren().add(webView);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("CodeSmells Detector");

        BorderPane initialView = new BorderPane();
        initialView.setStyle("-fx-background-color: #3d3c40");

        stage.getIcons().add(new Image(getClass().getResourceAsStream("/CodeSmellsIcon.gif")));
        //initialView.setTop(getMenu());
        initialView.setCenter(centerPane);
        initialView.setLeft(getLeft());

        loadCenterPane();


        Scene scene = new Scene(initialView,1000,800);


        initialView.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));

        stage.setScene(scene);
        stage.show();
    }
}
