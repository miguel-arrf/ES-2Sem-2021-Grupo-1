package SmellDetectionQualityEvaluation;

import g1.ISCTE.AppStyle;
import g1.ISCTE.FontType;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Interface displaying the confusion matrix and all the code smells detected with the appropriate tag.
 */
public class QualityEvaluatorApp extends Application {

    private final Label truePositivesLabel = new Label("0");
    private final Label falsePositivesLabel = new Label("0");
    private final Label trueNegativesLabel = new Label("0");
    private final Label falseNegativesLabel = new Label("0");
    private Button detectionButton;
    private VBox mainBox;
    private final VBox addButtonVBox = new VBox();
    private final ProgressBar progressBar = new ProgressBar();

    private final String[] possibleValues = new String[]{"All","True Positive", "False Positive", "False Negative", "True Negative"};
    private final ChoiceBox<String> choiceBox = new ChoiceBox<>(FXCollections.observableArrayList("All", "True Positive", "False Positive", "False Negative", "True Negative"));
    private final ArrayList<String> consoleOutputs = new ArrayList<>();

    /**
     * The entry point of application.
     *
     * @param args the input arguments (shall be none).
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Scene mainScene = initializeGUI();
        stage.setScene(mainScene);
        mainScene.getWindow().sizeToScene();
        stage.show();
    }

    public VBox initializeMainPane(){

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
        choiceBox.setMaxWidth(Double.MAX_VALUE);

        choiceBox.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> {
            System.out.println("selected: "  + possibleValues[t1.intValue()]);


            if(possibleValues[t1.intValue()].equalsIgnoreCase("ALL")){
                setupScrollPane(consoleOutputs, false);
            }else{
                List<String> temp = consoleOutputs.stream().filter(a -> a.contains(possibleValues[t1.intValue()])).collect(Collectors.toList());

                setupScrollPane(temp, false);
            }



        });

        return mainBox;
    }

    /**
     * Method in charge of initializing the GUI.
     *
     * @return the scene containing the interface
     */
    private Scene initializeGUI() {
        initializeMainPane();

        Scene scene = new Scene(mainBox);
        scene.getStylesheets().add(getClass().getResource("/style/AppStyle.css").toExternalForm());
        return scene;
    }

    /**
     * Method that adds a content to the top of a stackPane allowing us to mimic a content with rounded corners. Purely for style purposes.
     *
     * @param content the content (vbox) to be added to the stack pane.
     * @return the stackPane with the given content on top.
     */
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

    /**
     * Helper method that stylizes and creates a scrollpane with the appropriate design for this application.
     *
     * @param content to be added in the scrollpane
     * @return the scrollpane with the respective content
     */
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

    /**
     * Method in charge of adding (and removing when completed) the progress bar to the detect button.
     * Also creates a listener for the progress property of the progress bar, removing it from the detect button when the progress is complete.
     */
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
                //addButtonVBox.getChildren().remove(progressBar);
                mainBox.getChildren().remove(addButtonVBox);
                progressBar.setProgress(0.0);
                updateLabelValues(evaluator.getEvaluation().getConfusionMatrix());
                if(!mainBox.getChildren().contains(choiceBox)){
                    mainBox.getChildren().add(1,choiceBox);
                }

            }
        });

    }

    /**
     * Method that adds each string to a row in the scrollpane, and animates them in a waterfall way.
     *
     * @param localOutputs list of strings to be displayed.
     * @param animation if the scrollpane should add the items with animation or not.
     */
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

    /**
     * Helper method that changes the text on a button and the respective color.
     *
     * @param pressed text to be displayed in the button.
     */
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

    /**
     * Every label in the confusion matrix is setted to 0.
     */
    private void resetLabelValues() {
        truePositivesLabel.setText("0");
        falsePositivesLabel.setText("0");
        falseNegativesLabel.setText("0");
        trueNegativesLabel.setText("0");
    }

    /**
     * Updates the confusion matrix graphical representation with the correct values from the analysis.
     *
     * @param matrix the confusion matrix
     */
    private void updateLabelValues(ConfusionMatrix matrix) {
        truePositivesLabel.setText(Integer.toString(matrix.getTruePositives()));
        falsePositivesLabel.setText(Integer.toString(matrix.getFalsePositives()));
        falseNegativesLabel.setText(Integer.toString(matrix.getFalseNegatives()));
        trueNegativesLabel.setText(Integer.toString(matrix.getTrueNegatives()));
    }

    /**
     * Creates a button with the Application general style.
     *
     * @param text the text to be displayed in the button.
     * @param color the background color of the button
     * @return the stylized button with the given color and text.
     */
    private Button styledButton(String text, String color) {
        Button button = new Button(text);


        button.setStyle("-fx-background-radius: 7 7 7 7;\n" +
                "    -fx-border-radius: 7 7 7 7;\n" +
                "    -fx-background-color: " + color);
        button.setMinHeight(50);
        button.setMinWidth(Region.USE_PREF_SIZE);

        button.setAlignment(Pos.CENTER);

        button.setMaxWidth(Double.MAX_VALUE);

        return button;
    }

    /**
     * Sets the first two columns of a given gridpane as occupying 50% each of the grid size.
     *
     * @param gridPane the gridpane to which we want to apply the column constraints
     */
    private void setColumnConstraints(GridPane gridPane){
        ColumnConstraints columnConstraints1 = new ColumnConstraints();
        columnConstraints1.setPercentWidth(50);

        ColumnConstraints columnConstraints2 = new ColumnConstraints();
        columnConstraints2.setPercentWidth(50);

        gridPane.getColumnConstraints().addAll(columnConstraints1, columnConstraints2);
    }

    /**
     * Adds a given node in a given gridPane in a given position defined by a row and a column.
     *
     * @param node the node to be added to the gridPane.
     * @param gridPane the gridPane to where the items shall be added.
     * @param row the row to put the node.
     * @param column the column to put the node.
     */
    private void addToGrid(Pane node, GridPane gridPane, int row, int column){
        GridPane.setRowIndex(node, row);
        GridPane.setColumnIndex(node, column);

        gridPane.getChildren().add(node);
    }

    /**
     * Method in charge of creating the graphical representation of the confusion matrix using a gridPane.
     *
     * @return a Pane that represents a confusion matrix.
     */
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

    /**
     * Creates a confusion matrix label with the correct design. Associating to each row/column in the confusion matrix a description (cellName) and the value (result of the analysis - label).
     *
     * @param cellName the cell description.
     * @param label the label containing the value of the analysis.
     * @return a Pane representing the description and value of each of the rows and columns of the confusion matrix.
     */
    private Pane createMatrixPanel(String cellName, Label label) {
        Label cellNameLabel = new Label(cellName);
        cellNameLabel.setTextFill(Color.WHITE);

        label.setTextFill(Color.WHITE);

        VBox box = new VBox(cellNameLabel, label);
        box.setSpacing(20);
        box.setPadding(new Insets(10));

        return box;
    }

}
