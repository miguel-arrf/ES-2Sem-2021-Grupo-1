package g1.ISCTE;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
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
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;

public class NewGUI extends Application {

    private File selectedFile = null;

    private final VBox centerPane = new VBox();
    private final VBox filePane = new VBox();

    private final ArrayList<Label> metricBoxes = new ArrayList<>();

    private StackPane stackPaneLeftVBox;
    private VBox leftUnderVBox;
    private Stage stage;

    private WebEngine webEngine;
    public TableView table = new TableView();

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

            Button rulesEditor = new Button("Editor De Regras");
            rulesEditor.setTextFill(Color.WHITE);
            rulesEditor.setMaxWidth(Double.MAX_VALUE);
            rulesEditor.getStyleClass().add("selectFolderButton");

            Button showMetrics = new Button("Mostrar Métricas");
            showMetrics.setTextFill(Color.WHITE);
            showMetrics.setMaxWidth(Double.MAX_VALUE);
            showMetrics.getStyleClass().add("selectFolderButton");
            // selectFolder.setFont(AppStyle.getFont(FontType.ROUNDED_BOLD, 10));

            showMetrics.setOnMouseClicked(event -> {

                setCenterPane();

            });

            filePane.getChildren().addAll(showMetrics,rulesEditor);

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
      //  selectFilesLabel.setFont(AppStyle.getFont(FontType.ROUNDED_BOLD, 14));

        Label filesFormat = new Label("Folder should have a java project");
        filesFormat.setTextFill(Color.web("#76747e"));
       // filesFormat.setFont(AppStyle.getFont(FontType.DISPLAY_MEDIUM, 12));

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

       /* dragAndDropVBox.getChildren().add(
                AppStyle.getLabelWithColorAndFont(Color.web("#76747e"), FontType.ROUNDED_BOLD, 10, "Drag & drop folder here")
        );*/


        Button selectFolder = new Button("Select Folder");
        selectFolder.setTextFill(Color.WHITE);
        selectFolder.setMaxWidth(Double.MAX_VALUE);
        selectFolder.getStyleClass().add("selectFolderButton");
       // selectFolder.setFont(AppStyle.getFont(FontType.ROUNDED_BOLD, 10));

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

        emptyLeftPane.setMinHeight(190);

        return emptyLeftPane;
    }

    private void setCenterPane() {

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



    @Override
    public void start(Stage stage) {
        stage.setTitle("CodeSmells Detector");

        SplitPane splitPane = new SplitPane();
        splitPane.setStyle("-fx-background-insets: 0; -fx-padding: 0;");

        stage.getIcons().add(new Image(getClass().getResourceAsStream("/CodeSmellsIcon.gif")));

        VBox leftPane = getLeft();
        leftPane.setMinWidth(300);

        centerPane.setStyle("-fx-background-color: #1c1c1e");
        centerPane.setMinWidth(600);

        splitPane.setDividerPositions(0.25);
        splitPane.getItems().addAll(leftPane, centerPane);

        Scene scene = new Scene(splitPane,1000,800);
        scene.getStylesheets().add(getClass().getResource("/style/AppStyle.css").toExternalForm());
        stage.setMinWidth(900);
        stage.setMinHeight(400);

        String[][] sdata = new String[1][2];
        sdata[0][1] = "True";
        sdata[0][0] = "False";

        String[] cols = new String[2];
        cols[0] = "isLongMethod";
        cols[1] = "isGodClass";
        fillTable(cols,sdata);


        stage.setScene(scene);
        stage.show();
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
