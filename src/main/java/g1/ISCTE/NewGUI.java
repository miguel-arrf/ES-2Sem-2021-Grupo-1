package g1.ISCTE;

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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class NewGUI extends Application {

    private File selectedFile = null;

    private final VBox centerPane = new VBox();
    private final VBox filePane = new VBox();

    private final ArrayList<Label> metricBoxes = new ArrayList<>();

    public TableView table = new TableView();

    private StackPane stackPaneLeftVBox;
    private VBox leftUnderVBox;
    private Stage stage;
    private VBox centerPaneWebViewPane = new VBox();

    private WebEngine webEngine;

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

        filePane.setSpacing(10);

        leftVBox.setPadding(new Insets(15,15,15,15));

        return leftVBox;
    }

    private VBox getButtonsLeft(){
        Button rulesEditor = new Button("Editor De Regras");
        rulesEditor.setTextFill(Color.BLACK);
        rulesEditor.setMaxWidth(Double.MAX_VALUE);
        rulesEditor.getStyleClass().add("selectRuleBuilderButton");
        rulesEditor.setFont(AppStyle.getFont(FontType.ROUNDED_BOLD, 10));

        Button showMetrics = new Button("Mostrar Métricas");
        showMetrics.setTextFill(Color.BLACK);
        showMetrics.setMaxWidth(Double.MAX_VALUE);
        showMetrics.getStyleClass().add("selectShowMetricsButton");
        showMetrics.setFont(AppStyle.getFont(FontType.ROUNDED_BOLD, 10));

        showMetrics.setOnMouseClicked(mouseEvent -> {
            addTableToCenterPane();
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

    private Pane getSpacer(int height){
        Pane pane = new Pane();
        pane.setPrefHeight(height);

        return pane;
    }

    private VBox getEmptyLeftPane(){

        VBox emptyLeftPane = new VBox();
        emptyLeftPane.setSpacing(10);

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
                    webEngine.executeScript("changeFirstBox("+"'"+ name +"'"+ ")");

                    selectedFile = file;
                    updateFilePane();
                    updateCenterPane();
                    success = true;
                }else{
                    blurBackground(0, 30, 500, leftUnderVBox);

                    Label selectFolder = new Label("Drag a folder, not a file!");
                    selectFolder.setTextFill(Color.WHITE);
                    selectFolder.setMaxWidth(Double.MAX_VALUE);
                    selectFolder.getStyleClass().add("errorLabel");
                    selectFolder.setFont(AppStyle.getFont(FontType.ROUNDED_BOLD, 15));
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
                updateCenterPane();
            }
        });

        HBox buttonsBox = new HBox(selectFolder);
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

    private void loadCenterPaneWebView(){
        WebView webView = new WebView();
        webEngine = webView.getEngine();

        File f = new File(getClass().getResource("/testeScript.html").getFile());
        webEngine.load(f.toURI().toString());

        //VBox.setVgrow(webView, Priority.ALWAYS);

        webView.setStyle("-fx-background-radius: 7 7 7 7;\n" +
                "    -fx-border-radius: 7 7 7 7;");

        centerPaneWebViewPane.setMaxHeight(132);
        centerPaneWebViewPane.setMinHeight(132);

        centerPaneWebViewPane.getChildren().add(webView);
    }


    private void updateCenterPane(){
        String[] metrics = ProjectInfo.getMainMetricsInfo(selectedFile);

        for(int i = 0; i < metrics.length && i <metricBoxes.size(); i++){
            metricBoxes.get(i).setText(metrics[i]);
        }

    }

    private VBox centerPane(){
        VBox centerPaneVBox = new VBox();
        centerPaneVBox.setSpacing(10);

        loadCenterPaneWebView();
        centerPane.setMinWidth(600);
        centerPane.setAlignment(Pos.CENTER);

        centerPaneVBox.getChildren().addAll(centerPaneWebViewPane,centerPane);

        VBox.setVgrow(centerPane, Priority.ALWAYS);


        centerPaneVBox.setPadding(new Insets(10,10,10,10));

        return centerPaneVBox;
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;

        stage.setTitle("CodeSmells Detector");

        SplitPane splitPane = new SplitPane();
        splitPane.setStyle("-fx-background-insets: 0; -fx-padding: 0; -fx-background-color: rgb(28,28,30)");

        stage.getIcons().add(new Image(getClass().getResourceAsStream("/CodeSmellsIcon.gif")));

        VBox leftPane = getLeft();
        leftPane.setMinWidth(300);


        splitPane.setDividerPositions(0.20);
        splitPane.getItems().addAll(leftPane, centerPane());

        Scene scene = new Scene(splitPane,1000,800);
        scene.getStylesheets().add(getClass().getResource("/style/AppStyle.css").toExternalForm());
        stage.setMinWidth(1000);
        stage.setMinHeight(400);

        configureTableData();

        stage.setScene(scene);
        stage.show();
    }

    private void configureTableData() throws IOException  {

        File file = new File("C:\\Users\\Antonio Martins\\Downloads\\Code_Smells.xlsx");
        FileInputStream fip = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fip);
        ProjectInfo metricsinfo = new ProjectInfo(workbook);
        ArrayList<ArrayList<String>> table = metricsinfo.getMetricsTable();

        String[][] sdata = new String[table.size()][table.get(0).size()];
        String[] cols = new String[table.size()];
        int i = 0;
        int j = 0;
        for(ArrayList<String> al : table) {
            for(String s : al) {
                sdata[i][j] = s;
                j++;
            }
            j = 0;
            cols[i] = al.get(0);
            i++;
        }
        fillTable(cols,sdata);

    }

    private void addTableToCenterPane(){
        centerPane.getChildren().clear();

        BorderPane center = new BorderPane();
        HBox topBox = new HBox();

        HBox centerBox = new HBox();

        centerBox.getChildren().add(table);

        center.setTop(topBox);
        center.setCenter(centerBox);


        centerPane.getChildren().addAll(center);
    }

    private void fillTable(String[] cols,String[][] dataSource) {
        table.getColumns().clear();

        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        for (String[] row : dataSource)
            data.add(FXCollections.observableArrayList(row));
        table.setItems(data);

        for (int i = 0; i < dataSource[0].length; i++) {
            final int currentColumn = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(cols[i]);
            column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(currentColumn)));
            column.setEditable(false);
            column.setCellFactory(TextFieldTableCell.forTableColumn());
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





}
