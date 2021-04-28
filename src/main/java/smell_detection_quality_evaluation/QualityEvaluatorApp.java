package smell_detection_quality_evaluation;

import g1.ISCTE.AppStyle;
import g1.ISCTE.FontType;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;

public class QualityEvaluatorApp extends Application {

    private Label truePositivesLabel = new Label("0");
    private Label falsePositivesLabel = new Label("0");
    private Label trueNegativesLabel = new Label("0");
    private Label falseNegativesLabel = new Label("0");
    private Button detectionButton;
    private VBox mainBox;

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
        mainBox.getChildren().add(detectionButton);
        mainBox.setStyle("-fx-background-color: #3d3c40 ");
        Scene scene = new Scene(mainBox);
        scene.getStylesheets().add(getClass().getResource("/style/AppStyle.css").toExternalForm());
        return scene;
    }

    private StackPane getStackPane(VBox content){
        //ScrollPane where boxes go
        ScrollPane scrollPane = getScrollPane(content);

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
        scrollPane.setMaxWidth(350);

        HBox.setHgrow(scrollPane, Priority.ALWAYS);

        scrollPane.setFitToWidth(true);

        scrollPane.getStylesheets().add(getClass().getResource("/style/scrollPanel.css").toExternalForm());


        return scrollPane;
    }

    private void detectOnClick() {
        resetLabelValues();
        changeButtonState("Pressed");
        QualityEvaluator evaluator = new QualityEvaluator();
        evaluator.run();
        changeButtonState("Normal");
        setupScrollPane(evaluator.getEvaluation().getConsoleOutputs());
        updateLabelValues(evaluator.getEvaluation().getConfusionMatrix());
    }

    private void setupScrollPane(ArrayList<String> consoleOutputs) {
        VBox textBox = new VBox();
        textBox.setSpacing(5);
        ArrayList<Label> labels = new ArrayList<>();
        for(String output : consoleOutputs) {
            Label label = new Label(output);
            label.setTextFill(Color.WHITE);
            label.setPadding(new Insets(4, 10, 10, 10));
            label.getStyleClass().add("treeLabel");
            label.setFont(AppStyle.getFont(FontType.ROUNDED_SEMI_BOLD,12));
            labels.add(label);
            //textBox.getChildren().add(label);
        }
        AppStyle.addFadingInGroup(250, 50, labels, textBox);
        mainBox.getChildren().add(getStackPane(textBox));
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

    private Pane createMatrix() {
        HBox firstRow = new HBox(createMatrixPanel("True Positive", truePositivesLabel), createMatrixPanel("False Positive", falsePositivesLabel));
        HBox secondRow = new HBox(createMatrixPanel("False Negative", falseNegativesLabel), createMatrixPanel("True Negative", trueNegativesLabel));
        VBox matrix = new VBox(firstRow, secondRow);
        matrix.setBorder(new Border(new BorderStroke(Color.web("#76747e"), BorderStrokeStyle.DASHED, new CornerRadii(7), new BorderWidths(2))));
        matrix.setBackground(new Background(new BackgroundFill(Color.web("rgba(118,116,126,0.3)"), new CornerRadii(7), Insets.EMPTY)));
        return matrix;
    }

    private Pane createMatrixPanel(String cellName, Label label) {
        Label cell = new Label(cellName);
        cell.setTextFill(Color.WHITE);
        label.setTextFill(Color.WHITE);
        cell.setFont(AppStyle.getFont(FontType.ROUNDED_SEMI_BOLD,12));
        label.setFont(AppStyle.getFont(FontType.ROUNDED_SEMI_BOLD,12));
        VBox box = new VBox(cell, label);
        box.setSpacing(20);
        box.setPadding(new Insets(10));
        return box;
    }

}
