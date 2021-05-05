package g1.ISCTE;

import RuleEditor.RulesManager;
import CodeSmellDetection.RuleApplier;
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
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import MetricExtraction.MetricExtractor;
import SmellDetectionQualityEvaluation.QualityEvaluator;
import SmellDetectionQualityEvaluation.QualityEvaluatorApp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class NewGUI extends Application {

    //Right Side
    private final VBox centerPane = new VBox();
    private final ArrayList<Label> metricBoxes = new ArrayList<>();
    //Left Side
    private final VBox filePane = new VBox();
    public TableView table = new TableView();
    private Stage stage;
    private File selectedFile = null;
    private String docPath = "";
    private VBox centerPaneVBox;
    private StackPane stackPaneLeftVBox;
    private VBox leftUnderVBox;
    private VBox leftPane;
    private VBox buttonsBox;
    private Button processRulesButton = setUpProcessRulesButton();

    //SavedRules
    private RulesManager rulesManager = new RulesManager();
    private MetricExtractor metricExtractor;


    public static void main(String[] args) {
        launch(args);
    }

    public static void blurBackground(double startValue, double endValue, double duration, Node pane) {
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

    private VBox getLeft() {
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

        leftVBox.setPadding(new Insets(15, 15, 15, 15));

        return leftVBox;
    }

    private VBox getButtonsLeft() {
        Button rulesEditor = new Button("Editor De Regras");
        rulesEditor.setTextFill(Color.BLACK);
        rulesEditor.setMaxWidth(Double.MAX_VALUE);
        rulesEditor.getStyleClass().add("selectRuleBuilderButton");
        //rulesEditor.setFont(AppStyle.getFont(FontType.ROUNDED_SEMI_BOLD, 10));

        rulesEditor.setOnMouseClicked(mouseEvent -> {
            Stage stage = AppStyle.setUpPopupStage("Rule Editor", null, true);
            rulesManager.setMetricExtractor(metricExtractor);
            rulesManager.start(stage);

            rulesEditor.getScene().setFill(Color.web("#3d3c40"));
            NewGUI.blurBackground(0, 30, 500, rulesEditor.getScene().getRoot());

            stage.setOnCloseRequest(windowEvent -> {
                NewGUI.blurBackground(30, 0, 200, rulesEditor.getScene().getRoot());
                System.out.println("REGRAS: " + rulesManager.getRules().size());
                if(rulesManager.getRules().size() == 0) {
                    processRulesButton.setDisable(true);
                } else {
                    processRulesButton.setDisable(false);
                }
            });


        });

        Button showMetrics = new Button("Mostrar Métricas");
        showMetrics.setTextFill(Color.BLACK);
        showMetrics.setMaxWidth(Double.MAX_VALUE);
        showMetrics.getStyleClass().add("selectShowMetricsButton");
        // showMetrics.setFont(AppStyle.getFont(FontType.ROUNDED_SEMI_BOLD, 10));

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

        emptyLeftPane.setPadding(new Insets(10, 10, 10, 10));

        emptyLeftPane.getChildren().addAll(buttonsBox, processRulesButton);

        emptyLeftPane.getStyleClass().add("emptyLeftPane");

        return emptyLeftPane;


    }

    private Button setUpProcessRulesButton() {
        Button processRulesButton = new Button("Process Rules");
        processRulesButton.setTextFill(Color.BLACK);
        processRulesButton.setMaxWidth(Double.MAX_VALUE);
        processRulesButton.getStyleClass().add("selectShowMetricsButton");
        // showMetrics.setFont(AppStyle.getFont(FontType.ROUNDED_SEMI_BOLD, 10));
        processRulesButton.setDisable(true);

        processRulesButton.setOnMouseClicked(mouseEvent -> {

            try {
                if (rulesManager.getRulesFile() != null && rulesManager.getRules().size() > 0) {
                    rulesManager.loadFile();
                    rulesManager.createCodeSmells();
                    RuleApplier ra = new RuleApplier(rulesManager.getResults(), docPath);
                    ra.mandar();
                    updateCenterPane();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return processRulesButton;
    }

    private void updateFilePane() {
        if (selectedFile != null) {

            filePane.getChildren().clear();

            filePane.setPadding(new Insets(15, 0, 0, 0));

            filePane.getChildren().addAll(getButtonsLeft());
            filePane.setSpacing(15);

            if (selectedFile.isDirectory()) {


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

    private Pane getSpacer() {
        Pane pane = new Pane();
        pane.setPrefHeight(5);

        return pane;
    }

    private Button getProcessProjectButton() {
        Button processProjectButton = getGrowingButtonWithCSSClass("Processar Projeto", "processProjectButton");

        processProjectButton.setOnMouseClicked(event -> {
            blurBackground(0, 30, 500, leftPane);

            new Thread() {
                @Override
                public void run() {
                    super.run();
                    metricExtractor = new MetricExtractor(selectedFile, "src/main/Created_Excels");

                    try {
                        metricExtractor.executeExtraction();
                        docPath = metricExtractor.getFinalPath();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    Platform.runLater(() -> {
                        updateFilePane();
                        blurBackground(30, 0, 500, leftPane);
                    });
                }
            }.start();

        });

        processProjectButton.setDisable(true);

        return processProjectButton;
    }

    private Button getSelectFolderButton(Button processProjectButton) {
        Button selectFolder = getGrowingButtonWithCSSClass("Select Folder", "selectFolderButton");

        selectFolder.setOnMouseClicked(event -> {
            final DirectoryChooser directoryChooser =
                    new DirectoryChooser();
            final File selectedDirectory =
                    directoryChooser.showDialog(selectFolder.getScene().getWindow());
            if (selectedDirectory != null) {
                selectedFile = selectedDirectory;
                processProjectButton.setDisable(false);
            } else {
                processProjectButton.setDisable(true);
            }
        });

        return selectFolder;
    }

    private Button getGrowingButtonWithCSSClass(String label, String css) {
        Button selectFolder = new Button(label);
        selectFolder.setTextFill(Color.WHITE);
        selectFolder.setMaxWidth(Double.MAX_VALUE);
        selectFolder.getStyleClass().add(css);
        HBox.setHgrow(selectFolder, Priority.ALWAYS);

        return selectFolder;
    }

    private Button getDefaultProjectButton(Button processProjectButton) {
        Button selectFolder = getGrowingButtonWithCSSClass("Load JASML Project", "loadJASMLButton");

        selectFolder.setOnMouseClicked(event -> {



            selectedFile = QualityEvaluator.getDefaultProject();
            processProjectButton.setDisable(false);
            selectFolder.setDisable(true);

            Button showConfusionMatrix = getGrowingButtonWithCSSClass("Show confusion matrix", "showConfusionMatrixButton");
            if (!buttonsBox.getChildren().contains(showConfusionMatrix)) {
                buttonsBox.getChildren().add(showConfusionMatrix);
            }

            showConfusionMatrix.setOnMouseClicked(showConfusionMatrixEvent -> {
                QualityEvaluatorApp qualityEvaluatorApp = new QualityEvaluatorApp();
                VBox mainpane = qualityEvaluatorApp.initializeMainPane(rulesManager);

                mainpane.setStyle(AppStyle.setDefaultBackgroundAndBorderRadiusWithGivenBackgroundColor(AppStyle.darkGrayBoxColor));
                centerPane.getChildren().clear();
                centerPane.getChildren().add(mainpane);
                mainpane.setMaxWidth(Double.MAX_VALUE);
                mainpane.setMaxHeight(Double.MAX_VALUE);
                VBox.setVgrow(mainpane, Priority.ALWAYS);

            });


        });

        return selectFolder;
    }

    private VBox getEmptyLeftPane() {

        VBox emptyLeftPane = new VBox();
        emptyLeftPane.setSpacing(10);

        Button processProjectButton = getProcessProjectButton();

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

                if (file.isDirectory()) {
                    stage.setTitle(file.getName());

                    String name = file.getName();
                    //webEngine.executeScript("changeFirstBox("+"'"+ name +"'"+ ")");

                    selectedFile = file;
                    processProjectButton.setDisable(false);
                    success = true;
                } else {
                    processProjectButton.setDisable(true);
                    blurBackground(0, 30, 500, leftUnderVBox);

                    Label selectFolder = new Label("Drag a folder, not a file!");
                    selectFolder.setTextFill(Color.WHITE);
                    selectFolder.setMaxWidth(Double.MAX_VALUE);
                    selectFolder.getStyleClass().add("errorLabel");
                    selectFolder.setWrapText(true);
                    selectFolder.setAlignment(Pos.CENTER);
                    selectFolder.setPadding(new Insets(10));

                    VBox overlayVBox = new VBox();
                    overlayVBox.getChildren().add(selectFolder);
                    overlayVBox.setAlignment(Pos.CENTER);
                    overlayVBox.setPadding(new Insets(10, 10, 10, 10));

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


        HBox selectFolderANDLoadDefaulProjectHBOX = new HBox();
        Button selectFolder = getSelectFolderButton(processProjectButton);
        selectFolderANDLoadDefaulProjectHBOX.setSpacing(10);

        selectFolderANDLoadDefaulProjectHBOX.getChildren().addAll(selectFolder, getDefaultProjectButton(processProjectButton));

        buttonsBox = new VBox(selectFolderANDLoadDefaulProjectHBOX, processProjectButton);
        buttonsBox.setSpacing(10);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setMaxWidth(Double.MAX_VALUE);

        emptyLeftPane.setPadding(new Insets(10, 10, 10, 10));

        emptyLeftPane.getChildren().addAll(AppStyle.getTitleLabel("Select you Java Project!"), AppStyle.getSubTitleLabel("Folder should have a java project"), getSpacer(), dragAndDropVBox, getSpacer(), buttonsBox);

        emptyLeftPane.getStyleClass().add("emptyLeftPane");

        emptyLeftPane.setMinHeight(190);

        return emptyLeftPane;
    }

    private void updateCenterPane() throws IOException {
        //Passar isto para classe separada e arranjar ordenação

        ProjectInfo projectInfo = new ProjectInfo(ProjectInfo.createWorkbook(docPath));
        String[] metrics = projectInfo.getMainMetricsInfo();
        ArrayList<ArrayList<String>> tableData = projectInfo.getMetricsTable();

        String[][] sdata = new String[tableData.size()][tableData.get(0).size()];
        String[] cols = new String[tableData.size()];
        int j = 0;
        for (int a = 1; a != tableData.size() - 1; a++) {
            ArrayList<String> al = tableData.get(a);
            for (String s : al) {
                sdata[a - 1][j] = s;
                j++;
            }
            j = 0;
        }
        int i = 0;
        for (String s : tableData.get(0))
            cols[i++] = s;
        fillTable(cols, sdata);

        for (int a = 0; a < metrics.length && a < metricBoxes.size(); a++) {
            metricBoxes.get(a).setText(metrics[a]);
        }


        centerPane.getChildren().clear();
        VBox.setVgrow(table, Priority.ALWAYS);


        centerPane.getChildren().addAll(table);

    }

    private VBox centerPane() {
        centerPaneVBox = new VBox();
        centerPaneVBox.setSpacing(15);

        centerPane.setMinWidth(600);
        centerPane.setAlignment(Pos.CENTER);

        VBox.setVgrow(centerPane, Priority.ALWAYS);
        centerPaneVBox.getChildren().addAll(getInfoBoxes() /*, centerPaneWebViewPane*/, centerPane);

        centerPaneVBox.setPadding(new Insets(15));

        return centerPaneVBox;
    }

    @Override
    public void start(Stage stage) throws IOException {
    	this.stage = stage;
        SplitPane splitPane = initializeGUI(stage);

        stage.setTitle("CodeSmells Detector");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/CodeSmellsIcon.gif")));
        Scene scene = new Scene(splitPane, 1000, 800);
        scene.getStylesheets().add(getClass().getResource("/style/AppStyle.css").toExternalForm());
        stage.setMinWidth(1000);
        stage.setMinHeight(400);

        stage.setScene(scene);
        stage.show();
    }
    
    public SplitPane initializeGUI(Stage stage) {
    	System.out.println("im here");
        SplitPane splitPane = new SplitPane();
        splitPane.setStyle("-fx-background-insets: 0; -fx-padding: 0; -fx-background-color: rgb(28,28,30)");


        leftPane = getLeft();
        leftPane.setMinWidth(300);

        splitPane.setDividerPositions(0.20);

        splitPane.getItems().addAll(leftPane, centerPane());
        
        return splitPane;
    }

    private void fillTable(String[] cols, String[][] dataSource) {
        //table.setPadding(new Insets(5,0,0,0));

        table.getColumns().clear();

        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        for (String[] row : dataSource)
            data.add(FXCollections.observableArrayList(row));
        table.setItems(data);

        //  Font.loadFont(getClass().getResourceAsStream("/resources/fonts/SF-Pro-Rounded-Semibold.ttf"), 14);

        for (int i = 0; i < dataSource[0].length; i++) {
            final int currentColumn = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(cols[i]);

            column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(currentColumn)));

            column.setEditable(false);

            //column.setCellFactory(TextFieldTableCell.forTableColumn());

            column.setCellFactory(new Callback() {

                @Override
                public TableCell call(Object param) {
                    return new TableCell<String, String>() {
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);

                            if (isEmpty()) {
                                setText("");
                            } else {
                                setTextFill(Color.web("#d1d1d1"));
                                //                       setFont(AppStyle.getFont(FontType.REGULAR, 14));
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

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


    }

    private HBox getInfoBoxes() {
        VBox infoBox = getSquareInfoBox("Número total de packages", "?");
        VBox infoBox1 = getSquareInfoBox("Número total de classes", "?");
        VBox infoBox2 = getSquareInfoBox("Número total de métodos", "?");
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

    private VBox getSquareInfoBox(String typeOfInfo, String number) {
        VBox emptyLeftPane = new VBox();

        emptyLeftPane.setSpacing(10);
        emptyLeftPane.setPadding(new Insets(10, 10, 10, 10));
        emptyLeftPane.getStyleClass().add("emptyLeftPane");
        emptyLeftPane.setPrefSize(150, 120);
        emptyLeftPane.setMinHeight(120);
        emptyLeftPane.setMinWidth(150);

        Label typeOfInfoLabel = AppStyle.getTitleLabel(typeOfInfo);

        typeOfInfoLabel.setWrapText(true);

        Label numberLabel = new Label(number);
        //  numberLabel.setFont(AppStyle.getFont(FontType.ROUNDED_SEMI_BOLD, 14));
        numberLabel.setTextFill(Color.BLACK);
        numberLabel.setPadding(new Insets(2, 2, 2, 2));
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
