package SmellDetectionQualityEvaluation;

import RuleEditor.RulesManager;
import g1.ISCTE.AppStyle;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Interface displaying the confusion matrix and all the code smells detected with the appropriate tag.
 */
public class QualityEvaluatorApp  {

    private final GUIMatrix GUIMatrix = new GUIMatrix();
	private Button detectionButton;
    private VBox mainBox;
    private final VBox addButtonVBox = new VBox();
    private final ProgressBar progressBar = new ProgressBar();

    private final String[] possibleValues = new String[]{"All","True Positive", "False Positive", "False Negative", "True Negative"};
    private final ChoiceBox<String> choiceBox = new ChoiceBox<>(FXCollections.observableArrayList("All", "True Positive", "False Positive", "False Negative", "True Negative"));
    private final ArrayList<String> consoleOutputs = new ArrayList<>();

    private RulesManager rulesManager;
    private QualityEvaluator qualityEvaluator;
    //Needs to be here, otherwise the labels will not update correctly.
    private File projectFile;

    /**
     * VBox representing the GUI of the code quality evaluation.
     *
     * @param rulesManager the rulesManager containing the code smells to be analysed.
     * @param projectFile the file containing the project.
     * @return the VBox containing the GUI.
     */
    public VBox initializeMainPane(RulesManager rulesManager, File projectFile){
        this.rulesManager = rulesManager;
        this.projectFile = projectFile;

        mainBox = new VBox(GUIMatrix.createMatrix());
        mainBox.setSpacing(20);
        mainBox.setPadding(new Insets(10));
        detectionButton = AppStyle.getButtonWithDropShadow("Detect", AppStyle.lightOrangeColor);
        detectionButton.setOnAction(actionEvent -> detectOnClick());

        detectionButton.setMinHeight(25);

        addButtonVBox.setPadding(new Insets(5));
        addButtonVBox.setStyle(AppStyle.setDefaultBackgroundAndBorderRadiusWithGivenBackgroundColor(AppStyle.lightOrangeColor));

        addButtonVBox.getChildren().add(detectionButton);

        mainBox.getChildren().add(addButtonVBox);
        mainBox.setStyle("-fx-background-color: " + AppStyle.darkGrayBoxColor);

        progressBar.setProgress(0);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(progressBar, Priority.ALWAYS);
        progressBar.setMinHeight(15);

        choiceBox.setValue("All");
        choiceBox.setMaxWidth(Double.MAX_VALUE);

        choiceBox.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> {

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
     * Method in charge of adding (and removing when completed) the progress bar to the detect button.
     * Also creates a listener for the progress property of the progress bar, removing it from the detect button when the progress is complete.
     */
    private void detectOnClick() {


        GUIMatrix.resetLabelValues();
        qualityEvaluator = new QualityEvaluator(projectFile);
        qualityEvaluator.setCodeSmells(rulesManager.createCodeSmells());
        qualityEvaluator.run();


        if(qualityEvaluator.getEvaluation().getConsoleOutputs().size() > 0){
            if(!addButtonVBox.getChildren().contains(progressBar))
                addButtonVBox.getChildren().add(progressBar);

            detectionButton.setText("Running...");

            for(Node node : mainBox.getChildren()){
                if(node instanceof Label){
                    mainBox.getChildren().remove(node);
                    break;
                }
            }
            consoleOutputs.clear() ;
            consoleOutputs.addAll(qualityEvaluator.getEvaluation().getConsoleOutputs());

            setupScrollPane(consoleOutputs, true );

            progressBar.progressProperty().addListener((observableValue, number, t1) -> {
                if(t1.doubleValue() == 1){

                    detectionButton.setText("Re-Calculate");
                    addButtonVBox.getChildren().remove(progressBar);
                    progressBar.setProgress(0.0);

                    GUIMatrix.updateLabelValues(qualityEvaluator.getEvaluation().getConfusionMatrix());

                    if(!mainBox.getChildren().contains(choiceBox)){
                        mainBox.getChildren().add(1,choiceBox);
                    }
                }
            });
        }else{
            if(mainBox.getChildren().stream().noneMatch(p -> p instanceof Label)){
                Label emptyLabel = new Label("You have to creat at least a isGodClass or a isLongMethod rule");
                emptyLabel.setTextFill(Color.WHITE);

                mainBox.setAlignment(Pos.TOP_CENTER);

                mainBox.getChildren().add(emptyLabel);
                mainBox.getChildren().removeIf(node -> node instanceof StackPane || node instanceof ChoiceBox);

            }
        }

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

        StackPane stackPane = AppStyle.getStackPane(textBox, Double.MAX_VALUE);

        mainBox.getChildren().removeIf(node -> node instanceof StackPane);

        mainBox.getChildren().add(stackPane);
    }

}
