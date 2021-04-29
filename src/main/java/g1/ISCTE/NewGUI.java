package g1.ISCTE;

import RuleEditor.RuleEditor;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import metric_extraction.MetricExtractor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class NewGUI extends Application {

    private Stage stage;
    private File selectedFile = null;
    private String docPath = "";

    //Right Side
    private final VBox centerPane = new VBox();
    private final ArrayList<Label> metricBoxes = new ArrayList<>();
    public TableView table = new TableView();
    private VBox centerPaneVBox;

    //Left Side
    private final VBox filePane = new VBox();
    private StackPane stackPaneLeftVBox;
    private VBox leftUnderVBox;
    private VBox leftPane;

    //SavedRules
    RuleEditor ruleEditor = new RuleEditor();


    public static void main( String[] args ) {
        launch(args);
    }

    private VBox getLeft(){
        VBox leftVBox = new VBox();

        leftVBox.setPrefWidth(250);
        leftVBox.setMaxWidth(400);

        leftVBox.setStyle("-fx-background-color: #1c1c1e");

        stackPaneLeftVBox = new StackPane();
        leftUnderVBox = getEmptyLeftPane();
        stackPaneLeftVBox.getChildren().add(leftUnderVBox);

        leftVBox.getChildren().addAll(stackPaneLeftVBox, filePane);
        VBox.setVgrow(filePane, Priority.ALWAYS);

        filePane.setSpacing(15);

        leftVBox.setPadding(new Insets(15,15,15,15));

        return leftVBox;
    }

    private VBox getButtonsLeft(){
        Button rulesEditor = new Button("Editor De Regras");
        rulesEditor.setTextFill(Color.BLACK);
        rulesEditor.setMaxWidth(Double.MAX_VALUE);
        rulesEditor.getStyleClass().add("selectRuleBuilderButton");
        rulesEditor.setFont(AppStyle.getFont(FontType.ROUNDED_SEMI_BOLD, 10));

        rulesEditor.setOnMouseClicked(mouseEvent -> {
            Stage stage = AppStyle.setUpPopupStage("Rule Editor", null, true);
            ruleEditor.start(stage);

            rulesEditor.getScene().setFill(Color.web("#3d3c40"));
            NewGUI.blurBackground(0, 30, 500, rulesEditor.getScene().getRoot());

            stage.setOnCloseRequest(windowEvent -> {
                NewGUI.blurBackground(30, 0, 200, rulesEditor.getScene().getRoot());
                System.out.println("REGRAS: " + ruleEditor.getRules().size());
            });


        });

        Button showMetrics = new Button("Mostrar Métricas");
        showMetrics.setTextFill(Color.BLACK);
        showMetrics.setMaxWidth(Double.MAX_VALUE);
        showMetrics.getStyleClass().add("selectShowMetricsButton");
        showMetrics.setFont(AppStyle.getFont(FontType.ROUNDED_SEMI_BOLD, 10));

        showMetrics.setOnMouseClicked(mouseEvent -> {

            try {
                updateCenterPane();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        VBox emptyLeftPane = new VBox();
        emptyLeftPane.setSpacing(10);

        HBox buttonsBox = new HBox(rulesEditor, showMetrics);
        buttonsBox.setSpacing(10);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(rulesEditor, Priority.ALWAYS);
        HBox.setHgrow(showMetrics, Priority.ALWAYS);

        emptyLeftPane.setPadding(new Insets(10,10,10,10));

        emptyLeftPane.getChildren().addAll(buttonsBox);

        emptyLeftPane.getStyleClass().add("emptyLeftPane");

        return emptyLeftPane;


    }

    private void updateFilePane(){
        if (selectedFile != null){

            filePane.getChildren().clear();

            filePane.setPadding(new Insets(15,0,0,0));

            filePane.getChildren().addAll(getButtonsLeft());
            filePane.setSpacing(15);

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

                filePane.getChildren().addAll(stackPane);

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

        Button calcMetrics = new Button("Processar Projeto");
        calcMetrics.setTextFill(Color.WHITE);
        calcMetrics.setMaxWidth(Double.MAX_VALUE);
        calcMetrics.getStyleClass().add("selectFolderButton");
        calcMetrics.setFont(AppStyle.getFont(FontType.ROUNDED_SEMI_BOLD, 10));

        calcMetrics.setOnMouseClicked(event -> {
            blurBackground(0, 30, 500, leftPane);

            new Thread(){
                @Override
                public void run() {
                    super.run();
                    MetricExtractor me = new MetricExtractor(selectedFile, "src/main/Created_Excels");

                    try {
                        me.executeExtraction();
                        docPath = me.getFinalPath();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    Platform.runLater(() ->{
                        updateFilePane();
                        blurBackground(30, 0, 500, leftPane);
                    } );
                }
            }.start();

        });

        calcMetrics.setDisable(true);

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
                File file = db.getFiles().get(0);

                if(file.isDirectory()){
                    stage.setTitle(file.getName());

                    String name = file.getName();
                    //webEngine.executeScript("changeFirstBox("+"'"+ name +"'"+ ")");

                    selectedFile = file;
                    calcMetrics.setDisable(false);
                    success = true;
                }else{
                    calcMetrics.setDisable(true);
                    blurBackground(0, 30, 500, leftUnderVBox);

                    Label selectFolder = new Label("Drag a folder, not a file!");
                    selectFolder.setTextFill(Color.WHITE);
                    selectFolder.setMaxWidth(Double.MAX_VALUE);
                    selectFolder.getStyleClass().add("errorLabel");
                    selectFolder.setFont(AppStyle.getFont(FontType.ROUNDED_SEMI_BOLD, 15));
                    selectFolder.setWrapText(true);
                    selectFolder.setAlignment(Pos.CENTER);
                    selectFolder.setPadding(new Insets(10));

                    VBox overlayVBox = new VBox();
                    overlayVBox.getChildren().add(selectFolder);
                    overlayVBox.setAlignment(Pos.CENTER);
                    overlayVBox.setPadding(new Insets(10,10,10,10));

                    overlayVBox.setMinHeight(190);

                    AppStyle.addFadingIn(overlayVBox, stackPaneLeftVBox);

                    Timer timer = new Timer();
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            Platform.runLater(() -> {
                                AppStyle.removeFadingOut(overlayVBox, stackPaneLeftVBox);
                                blurBackground(30, 0, 500, leftUnderVBox);
                            });

                        }
                    };
                    timer.schedule(task, 3000l);

                }


            }

            event.setDropCompleted(success);

            event.consume();
        });

        dragAndDropVBox.getChildren().add(
                AppStyle.getLabelWithColorAndFont(Color.web("#76747e"), FontType.ROUNDED_SEMI_BOLD, 10, "Drag & drop folder here")
        );


        Button selectFolder = new Button("Select Folder");
        selectFolder.setTextFill(Color.WHITE);
        selectFolder.setMaxWidth(Double.MAX_VALUE);
        selectFolder.getStyleClass().add("selectFolderButton");
        selectFolder.setFont(AppStyle.getFont(FontType.ROUNDED_SEMI_BOLD, 10));

        selectFolder.setOnMouseClicked(event -> {
            final DirectoryChooser directoryChooser =
                    new DirectoryChooser();
            final File selectedDirectory =
                    directoryChooser.showDialog(selectFolder.getScene().getWindow());
            if (selectedDirectory != null) {
                selectedFile = selectedDirectory;
                calcMetrics.setDisable(false);
            } else {  calcMetrics.setDisable(true); }
        });


        VBox buttonsBox = new VBox(selectFolder, calcMetrics);
        buttonsBox.setSpacing(10);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(selectFolder, Priority.ALWAYS);

        emptyLeftPane.setPadding(new Insets(10,10,10,10));

        emptyLeftPane.getChildren().addAll(AppStyle.getTitleLabel("Select you Java Project!"), AppStyle.getSubTitleLabel("Folder should have a java project"),getSpacer(), dragAndDropVBox,getSpacer(), buttonsBox);

        emptyLeftPane.getStyleClass().add("emptyLeftPane");

        emptyLeftPane.setMinHeight(190);

        return emptyLeftPane;
    }

    private void updateCenterPane() throws IOException {
        //Passar isto para classe separada e arranjar ordenação

        ProjectInfo pi = new ProjectInfo(ProjectInfo.createWorkbook(docPath));
        String[] metrics = pi.getMainMetricsInfo();
        ArrayList<ArrayList<String>> tableData = pi.getMetricsTable();

        String[][] sdata = new String[tableData.size()][tableData.get(0).size()];
        String[] cols = new String[tableData.size()];
        int j = 0;
        for(int a = 1; a!=tableData.size()-1; a++) {
            ArrayList<String> al = tableData.get(a);
            for(String s : al) {
                sdata[a-1][j] = s;
                j++;
            }
            j = 0;
        }
        int i = 0;
        for(String s : tableData.get(0))
            cols[i++] = s;
        fillTable(cols,sdata);

        for(int a = 0; a < metrics.length && a <metricBoxes.size(); a++){
            metricBoxes.get(a).setText(metrics[a]);
        }


        centerPane.getChildren().clear();
        VBox.setVgrow(table, Priority.ALWAYS);


        centerPane.getChildren().addAll(table);

    }



    private VBox centerPane(){
        centerPaneVBox = new VBox();
        centerPaneVBox.setSpacing(15);

        centerPane.setMinWidth(600);
        centerPane.setAlignment(Pos.CENTER);

        VBox.setVgrow(centerPane, Priority.ALWAYS);
        centerPaneVBox.getChildren().addAll(getInfoBoxes() /*, centerPaneWebViewPane*/,centerPane);

        centerPaneVBox.setPadding(new Insets(15));

        return centerPaneVBox;
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        stage.setTitle("CodeSmells Detector");

        SplitPane splitPane = new SplitPane();
        splitPane.setStyle("-fx-background-insets: 0; -fx-padding: 0; -fx-background-color: rgb(28,28,30)");

        stage.getIcons().add(new Image(getClass().getResourceAsStream("/CodeSmellsIcon.gif")));

        leftPane = getLeft();
        leftPane.setMinWidth(300);

        splitPane.setDividerPositions(0.20);

        splitPane.getItems().addAll(leftPane, centerPane());

        Scene scene = new Scene(splitPane,1000,800);
        scene.getStylesheets().add(getClass().getResource("/style/AppStyle.css").toExternalForm());
        stage.setMinWidth(1000);
        stage.setMinHeight(400);

        stage.setScene(scene);
        stage.show();
    }

    private void fillTable(String[] cols,String[][] dataSource) {
        table.setPadding(new Insets(5,0,0,0));

        table.getColumns().clear();

        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        for (String[] row : dataSource)
            data.add(FXCollections.observableArrayList(row));
        table.setItems(data);

        Font.loadFont(getClass().getResourceAsStream("/resources/fonts/SF-Pro-Rounded-Semibold.ttf"), 14);

        for (int i = 0; i < dataSource[0].length; i++) {
            final int currentColumn = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(cols[i]);

            column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(currentColumn)));

            column.setEditable(false);

            //column.setCellFactory(TextFieldTableCell.forTableColumn());

            column.setCellFactory(new Callback() {

                @Override
                public TableCell call(Object param) {
                    return new TableCell<String, String>(){
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);

                            if(isEmpty())
                            {
                                setText("");
                            }
                            else
                            {
                                setTextFill(Color.web("#d1d1d1"));
                                setFont(AppStyle.getFont(FontType.REGULAR, 14));
                                setText(item);
                            }
                        }
                    };
                }

            });


            column.setOnEditCommit(
                    (TableColumn.CellEditEvent<ObservableList<String>, String> t) -> {
                        t.getTableView().getItems().get(t.getTablePosition().getRow()).set(t.getTablePosition().getColumn(), t.getNewValue());
                    });
            table.getColumns().add(column);
        }
    }

    public static void blurBackground(double startValue, double endValue, double duration, Node pane){
        GaussianBlur gaussianBlur = new GaussianBlur(startValue);
        SimpleDoubleProperty value = new SimpleDoubleProperty(startValue);

        pane.setEffect(gaussianBlur);

        value.addListener((observableValue, number, t1) -> {
            gaussianBlur.setRadius(t1.doubleValue());
        });

        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(value, endValue);
        KeyFrame kf = new KeyFrame(Duration.millis(duration), kv);

        timeline.getKeyFrames().add(kf);
        timeline.play();
    }

    private HBox getInfoBoxes(){
        VBox infoBox = getSquareInfoBox("Número total de packages", "?");
        VBox infoBox1 = getSquareInfoBox("Número total de classes", "?");
        VBox infoBox2  = getSquareInfoBox("Número total de métodos", "?");
        VBox infoBox3 = getSquareInfoBox("Número total de linhas de código do projeto", "?");

        AppStyle.addFadingInGroup(1000, 500, infoBox, infoBox1, infoBox2, infoBox3);

        HBox.setHgrow(infoBox, Priority.ALWAYS);
        HBox.setHgrow(infoBox1, Priority.ALWAYS);
        HBox.setHgrow(infoBox2, Priority.ALWAYS);
        HBox.setHgrow(infoBox3, Priority.ALWAYS);


        HBox infoBoxes = new HBox();
        infoBoxes.setSpacing(15);

        infoBoxes.getChildren().addAll(infoBox, infoBox1, infoBox2, infoBox3);

        return infoBoxes;
    }

    private VBox getSquareInfoBox(String typeOfInfo, String number){
        VBox emptyLeftPane = new VBox();

        emptyLeftPane.setSpacing(10);
        emptyLeftPane.setPadding(new Insets(10,10,10,10));
        emptyLeftPane.getStyleClass().add("emptyLeftPane");
        emptyLeftPane.setPrefSize(150,120);
        emptyLeftPane.setMinHeight(120);
        emptyLeftPane.setMinWidth(150);

        Label typeOfInfoLabel = AppStyle.getTitleLabel(typeOfInfo);

        typeOfInfoLabel.setWrapText(true);

        Label numberLabel = new Label(number);
        numberLabel.setFont(AppStyle.getFont(FontType.ROUNDED_SEMI_BOLD, 14));
        numberLabel.setTextFill(Color.BLACK);
        numberLabel.setPadding(new Insets(2,2,2,2));
        numberLabel.setMinWidth(20);
        numberLabel.setAlignment(Pos.CENTER);

        numberLabel.setStyle("-fx-background-color: #a3ddcb;" +
                " -fx-background-radius: 7 7 7 7;\n" +
                "    -fx-border-radius: 7 7 7 7;");

        Pane spacer = new Pane();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        emptyLeftPane.getChildren().addAll(typeOfInfoLabel, spacer, numberLabel);

        metricBoxes.add(numberLabel);

        return emptyLeftPane;
    }



}
