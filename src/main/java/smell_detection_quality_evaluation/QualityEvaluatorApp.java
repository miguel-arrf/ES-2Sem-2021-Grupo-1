package smell_detection_quality_evaluation;

import g1.ISCTE.AppStyle;
import g1.ISCTE.FontType;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QualityEvaluatorApp extends Application {

    private final Label truePositivesLabel = new Label("0");
    private final Label falsePositivesLabel = new Label("0");
    private final Label trueNegativesLabel = new Label("0");
    private final Label falseNegativesLabel = new Label("0");
    private Button detectionButton;
    private VBox mainBox;
    private VBox addButtonVBox = new VBox();
    private final ProgressBar progressBar = new ProgressBar();

    private final String[] possibleValues = new String[]{"All","True Positive", "False Positive", "False Negative", "True Negative"};
    private final ChoiceBox choiceBox = new ChoiceBox(FXCollections.observableArrayList("All", "True Positive", "False Positive", "False Negative", "True Negative"));
    private final ArrayList<String> consoleOutputs = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene mainScene = initializeGUI();
        stage.setScene(mainScene);
        mainScene.getWindow().sizeToScene();
        stage.show();
    }

    private Scene initializeGUI() {

        mainBox = new VBox(createMatrix());
        mainBox.setSpacing(20);
        mainBox.setPadding(new Insets(10));
        detectionButton = styledButton("Detect", "ORANGE");
        detectionButton.setOnAction(actionEvent -> detectOnClick());

        detectionButton.setMinHeight(25);

        addButtonVBox.setPadding(new Insets(5));
        addButtonVBox.setStyle("-fx-background-radius: 7 7 7 7;\n" +
                "    -fx-border-radius: 7 7 7 7;\n" +
                "    -fx-background-color: orange");

        addButtonVBox.getChildren().add(detectionButton);

        mainBox.getChildren().add(addButtonVBox);
        mainBox.setStyle("-fx-background-color: #3d3c40 ");

        progressBar.setProgress(0);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(progressBar, Priority.ALWAYS);
        progressBar.setMinHeight(15);

        choiceBox.setValue("All");
        choiceBox.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> {
            System.out.println("selected: "  + possibleValues[t1.intValue()]);


            if(possibleValues[t1.intValue()].equalsIgnoreCase("ALL")){
                setupScrollPane(consoleOutputs, false);
            }else{
                List<String> temp = consoleOutputs.stream().filter(a -> a.contains(possibleValues[t1.intValue()])).collect(Collectors.toList());

                setupScrollPane(temp, false);
            }



        });

        Scene scene = new Scene(mainBox);
        scene.getStylesheets().add(getClass().getResource("/style/AppStyle.css").toExternalForm());
        return scene;
    }

    private StackPane getStackPane(VBox content){
        //ScrollPane where boxes go
        ScrollPane scrollPane = getScrollPane(content);

        //scrollPane.vvalueProperty().bind(content.heightProperty());

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

        HBox.setHgrow(scrollPane, Priority.ALWAYS);

        scrollPane.setFitToWidth(true);

        scrollPane.getStylesheets().add(getClass().getResource("/style/scrollPanel.css").toExternalForm());


        return scrollPane;
    }

    private void detectOnClick() {
        //mainBox.getChildren().add(progressBar);
        addButtonVBox.getChildren().add(progressBar);


        resetLabelValues();
        changeButtonState("Pressed");
        QualityEvaluator evaluator = new QualityEvaluator();
        evaluator.run();
        changeButtonState("Normal");

        consoleOutputs.clear() ;
        consoleOutputs.addAll(evaluator.getEvaluation().getConsoleOutputs());

        setupScrollPane(consoleOutputs, true );

        progressBar.progressProperty().addListener((observableValue, number, t1) -> {
            if(t1.doubleValue() == 1){
                addButtonVBox.getChildren().remove(progressBar);
                progressBar.setProgress(0.0);
                updateLabelValues(evaluator.getEvaluation().getConfusionMatrix());
                if(!mainBox.getChildren().contains(choiceBox)){
                    mainBox.getChildren().add(2,choiceBox);
                }

            }
        });

    }

    private void setupScrollPane(List<String> localOutputs,  boolean animation) {
        VBox textBox = new VBox();
        textBox.setSpacing(5);

        ArrayList<Label> labels = new ArrayList<>();

        for(String output : localOutputs) {
            Label label = new Label(output);
            label.setTextFill(Color.WHITE);
            label.setPadding(new Insets(4, 10, 10, 10));
            label.getStyleClass().add("treeLabel");
            label.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(label, Priority.ALWAYS);
            label.setFont(AppStyle.getFont(FontType.ROUNDED_SEMI_BOLD,12));
            labels.add(label);
            if(!animation)
                textBox.getChildren().add(label);
        }

        if(animation){
            AppStyle.addFadingInGroup(150, 20, labels, textBox, progressBar);

        }

        StackPane stackPane = getStackPane(textBox);

        mainBox.getChildren().removeIf(node -> node instanceof StackPane);

        mainBox.getChildren().add(stackPane);
    }

    private void changeButtonState(String pressed) {
        if(pressed.equals("Pressed")) {
            detectionButton.setStyle("-fx-background-radius: 7 7 7 7;\n" +
                    "    -fx-border-radius: 7 7 7 7;\n" +
                    "    -fx-background-color: #cd6133");
            detectionButton.setText("Running...");

        } else {
            detectionButton.setStyle("-fx-background-radius: 7 7 7 7;\n" +
                    "    -fx-border-radius: 7 7 7 7;\n" +
                    "    -fx-background-color: ORANGE");
            detectionButton.setText("Detect");
        }
    }

    private void resetLabelValues() {
        truePositivesLabel.setText("0");
        falsePositivesLabel.setText("0");
        falseNegativesLabel.setText("0");
        trueNegativesLabel.setText("0");
    }

    private void updateLabelValues(ConfusionMatrix matrix) {
        truePositivesLabel.setText(Integer.toString(matrix.getTruePositives()));
        falsePositivesLabel.setText(Integer.toString(matrix.getFalsePositives()));
        falseNegativesLabel.setText(Integer.toString(matrix.getFalseNegatives()));
        trueNegativesLabel.setText(Integer.toString(matrix.getTrueNegatives()));
    }

    private Button styledButton(String text, String color) {
        Button button = new Button(text);
        button.setFont(AppStyle.getFont(FontType.BOLD, 12));


        button.setStyle("-fx-background-radius: 7 7 7 7;\n" +
                "    -fx-border-radius: 7 7 7 7;\n" +
                "    -fx-background-color: " + color);
        button.setMinHeight(50);
        button.setMinWidth(Region.USE_PREF_SIZE);

        button.setAlignment(Pos.CENTER);

        button.setMaxWidth(Double.MAX_VALUE);

        return button;
    }

    private void setColumnConstraints(GridPane gridPane){
        ColumnConstraints columnConstraints1 = new ColumnConstraints();
        columnConstraints1.setPercentWidth(50);

        ColumnConstraints columnConstraints2 = new ColumnConstraints();
        columnConstraints2.setPercentWidth(50);

        gridPane.getColumnConstraints().addAll(columnConstraints1, columnConstraints2);
    }

    private void addToGrid(Pane node, GridPane gridPane, int row, int column){
        GridPane.setRowIndex(node, row);
        GridPane.setColumnIndex(node, column);

        gridPane.getChildren().add(node);
    }

    private Pane createMatrix() {
        Pane truePositivesPane = createMatrixPanel("True Positive", truePositivesLabel);
        Pane falsePositivesPane =  createMatrixPanel("False Positive", falsePositivesLabel);
        Pane falseNegativesPane = createMatrixPanel("False Negative", falseNegativesLabel);
        Pane trueNegativesPane = createMatrixPanel("True Negative", trueNegativesLabel);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));

        setColumnConstraints(grid);

        addToGrid(truePositivesPane,grid, 0, 0);
        addToGrid(falsePositivesPane,grid, 0, 1);

        addToGrid(falseNegativesPane, grid, 1,0);
        addToGrid(trueNegativesPane, grid, 1,1);


        VBox matrix = new VBox(grid);
        matrix.setBorder(new Border(new BorderStroke(Color.web("#76747e"), BorderStrokeStyle.DASHED, new CornerRadii(7), new BorderWidths(2))));
        matrix.setBackground(new Background(new BackgroundFill(Color.web("rgba(118,116,126,0.3)"), new CornerRadii(7), Insets.EMPTY)));
        return matrix;
    }

    private Pane createMatrixPanel(String cellName, Label label) {
        Label cellNameLabel = new Label(cellName);
        cellNameLabel.setTextFill(Color.WHITE);
        label.setTextFill(Color.WHITE);
        cellNameLabel.setFont(AppStyle.getFont(FontType.BOLD,14));
        label.setFont(AppStyle.getFont(FontType.ROUNDED_SEMI_BOLD,12));
        VBox box = new VBox(cellNameLabel, label);
        box.setSpacing(20);
        box.setPadding(new Insets(10));

        return box;
    }

}
