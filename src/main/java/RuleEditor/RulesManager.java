package RuleEditor;

import CodeSmellDetection.CodeSmell;
import CodeSmellDetection.CodeSmellDetector;
import g1.ISCTE.AppStyle;
import g1.ISCTE.FontType;
import g1.ISCTE.NewGUI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import MetricExtraction.MetricExtractor;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static g1.ISCTE.AppStyle.*;


/**
 * Rules editor: Allows for creation, deletion, and edition of rules.
 */
public class RulesManager extends Application {

    private static final String FILE_EXTENSION = "rule";

    private  VBox mainPane;
    private  VBox rulesPanel;
    private Label numberOfRules;
    private Button setRulesDirectoryButton;
    private Button loadRulesButton;
    private Button addNewRuleButton;
    private File rulesFile = null;
    private HashMap<String, ArrayList<String>> results = new HashMap<>();

    private ObservableList<JSONObject> rules = FXCollections.observableArrayList();
    private RuleFileManager ruleFileManager;
    private MetricExtractor metricExtractor;

    /**
     * Sets metric extractor.
     *
     * @param metricExtractor the metric extractor
     */
    public void setMetricExtractor(MetricExtractor metricExtractor) {
        this.metricExtractor = metricExtractor;
    }

    /**
     * Gets rules.
     *
     * @return the rules
     */
    public ObservableList<JSONObject> getRules() {
        return rules;
    }


    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Gets a File Chooser for rules files.
     *
     * @param message the type of file chooser (to save or to load).
     * @return the FileChooser
     */
    private FileChooser getFileChooser(String message){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Rule file(*." + FILE_EXTENSION + ")", "*." + FILE_EXTENSION));
        fileChooser.setInitialFileName("*." + FILE_EXTENSION);
        fileChooser.setTitle(message + " Rules File");

        return fileChooser;
    }

    /**
     * Creates the fileChooser for the .rule files and the button that displays it (and how it will react when pressed).
     *
     * @return the button that loads the .rule files.
     */
    private Button setUpLoadRulesFileButton() {
        FileChooser fileChooser = getFileChooser("Select");

        Button setDirectoryButton = styledButton("Load Rules", "#a3ddcb");
        setButtonIcon(setDirectoryButton, "icons8-download-96.png");

        setDirectoryButton.setOnAction(actionEvent -> {
            File tempFile;
            tempFile = fileChooser.showOpenDialog(this.mainPane.getScene().getWindow());

            if (tempFile != null) {
                if (tempFile.getName().endsWith("." + FILE_EXTENSION)) {
                    rulesFile = tempFile;
                    loadFile();

                }
            }
        });

        HBox.setHgrow(setDirectoryButton, Priority.ALWAYS);

        return setDirectoryButton;
    }

    /**
     * Load JSON files from the chosen path.
     */
    public void loadFile(){
        ruleFileManager.setFile(rulesFile);

        try {
            ArrayList<JSONObject> arrayList = ruleFileManager.loadJSONRuleFile();
            rules.clear();

            //If we don't do it this way, the listener for the label, wouldn't work.
            rules.addAll(arrayList);

            updateRulesEditorPanel();
            addNewRuleButton.setDisable(false);

        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Creates the filechooser for the .rule files creation and the button that displays it (and how it will react when pressed).
     *
     * @return the button that creates the .rule file in the specified path.
     */
    private Button setUpSetRulesFileButton() {
        FileChooser fileChooser = getFileChooser("Save");

        Button setDirectoryButton = styledButton("Create Rules file", "#d5ecc2");
        setButtonIcon(setDirectoryButton, "add-file.png");

        setDirectoryButton.setOnAction(actionEvent -> {
            File tempFile;
            tempFile = fileChooser.showSaveDialog(this.mainPane.getScene().getWindow());

            if (tempFile != null) {
                if (tempFile.getName().endsWith("." + FILE_EXTENSION)) {
                    rulesFile = tempFile;
                    ruleFileManager.setFile(rulesFile);

                    rules.clear();

                    addNewRuleButton.setDisable(false);
                }
            }
        });

        HBox.setHgrow(setDirectoryButton, Priority.ALWAYS);


        return setDirectoryButton;
    }

    /**
     * HBox with the respective content (rule rename text field) to be displayed into a popup.
     *
     * @param label the label that displays the rule name and that shall be updated or not by the user.
     * @param jsonObject the rule in JSON format.
     * @return the content needed to create a popup to rename a given rule.
     */
    @SuppressWarnings("unchecked")
    private HBox getRenameTextField(Label label, JSONObject jsonObject) {
        Button updateButton = styledButton("Update", lightGreenColor);
        Button cancelButton = styledButton("Cancel", lightPinkColor);

        cancelButton.setOnAction(actionEvent -> Platform.runLater(() -> cancelButton.getScene().getWindow().fireEvent(new WindowEvent(cancelButton.getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST))));

        HBox hBox = new HBox();
        hBox.setStyle("-fx-background-color: " + darkGrayBoxColor);
        TextField textField = new TextField((String) ((JSONObject)jsonObject.get("outerName")).get("innerName"));
        textField.setStyle("-fx-text-inner-color: white;");

        updateButton.setOnAction(actionEvent -> {

                if(ruleFileManager.isNameValid(textField.getText())) {
                    label.setText(textField.getText());

                    JSONObject outerName = (JSONObject) jsonObject.get("outerName");
                    outerName.replace("innerName", textField.getText());

                    jsonObject.replace("outerName", outerName);

                    ruleFileManager.saveJSONListToFile(rules);
                    Platform.runLater(() -> cancelButton.getScene().getWindow().fireEvent(new WindowEvent(cancelButton.getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST)));
                } else {
                    System.out.println("Nome inválido");
                }

        });

        textField.setMaxWidth(150);

        hBox.getChildren().addAll(textField, updateButton, cancelButton);

        hBox.getStyleClass().add("ruleBuilderMenu");

        hBox.setPadding(new Insets(10));
        hBox.setSpacing(10);

        hBox.setMaxHeight(100);
        hBox.setMaxWidth(500);
        hBox.setEffect(AppStyle.getDropShadow());

        hBox.setAlignment(Pos.CENTER);

        return hBox;
    }

    /**
     * Helper method to create the cell that represents a rule (with the edit, rename and delete buttons)
     *
     * @param nodeJSON the rule in JSON format.
     * @return the cell graphical representation.
     */
    private Node getRulePane(JSONObject nodeJSON) {

        HBox pane = new HBox();
        pane.setSpacing(20);

        Label label = new Label((String) ((JSONObject) nodeJSON.get("outerName")).get("innerName"));
        label.setFont(AppStyle.getFont(FontType.BOLD, 12));

        label.setAlignment(Pos.CENTER);

        HBox.setHgrow(label, Priority.ALWAYS);
        label.setMaxWidth(Double.MAX_VALUE);

        label.setMinWidth(Region.USE_PREF_SIZE);
        label.setTextFill(Color.BLACK);
        label.setPadding(new Insets(5));
        label.setStyle(setDefaultBackgroundAndBorderRadiusWithGivenBackgroundColor(lightGreenColor));


        Button delete = styledButton("Delete", lightRedColor);
        setButtonIcon(delete,"icons8-full-trash-96.png" );

        delete.setOnAction(actionEvent -> {
            rules.remove(nodeJSON);
            ruleFileManager.saveJSONListToFile(rules);

            updateRulesEditorPanel();
        });
        delete.setMinHeight(0);

        Button edit = styledButton("Edit", lightGrayColor);
        setButtonIcon(edit, "icons8-edit-384.png");

        edit.setOnAction(actionEvent -> {

            Stage popupStage = AppStyle.setUpPopupStage("Edit Rule", "/RuleBuilderIcon.gif", true);

            boolean isClassSmell = (Boolean) ((JSONObject) nodeJSON.get("outerName")).get("isClassSmell");


            FinalMain finalMain = new FinalMain(isClassSmell);
            String ruleName = (String) ((JSONObject) nodeJSON.get("outerName")).get("innerName");

            SplitPane content = finalMain.getEditRuleEditor(popupStage, ruleFileManager, nodeJSON, ruleName);

            VBox.setMargin(content, new Insets(20));
            content.setPadding(new Insets(10));

            Scene scene = new Scene(content, 1000, 700);
            scene.getStylesheets().add(getClass().getResource("/style/AppStyle.css").toExternalForm());

            popupStage.setScene(scene);

            popupStage.setMinHeight(300);
            popupStage.setMinWidth(700);

            popupStage.setOnCloseRequest(windowEvent -> {
                rules.remove(nodeJSON);

                JSONObject ruleToADD = finalMain.getRule();

                if (ruleToADD != null) {

                    rules.add(ruleToADD);
                    ruleFileManager.saveJSONListToFile(rules);
                    updateRulesEditorPanel();
                }

            });

            popupStage.show();
        });

        edit.setMinHeight(0);

        Button rename = styledButton("Rename", lightYellowColor);
        setButtonIcon(rename, "icons8-rename-96.png");

        rename.setOnAction(actionEvent -> {


            HBox optionsHBox = getRenameTextField(label, nodeJSON);
            Stage popupStage = AppStyle.setUpPopup("Value", "/PreferencesPanelIcon.gif", optionsHBox, getClass().getResource("/style/AppStyle.css").toExternalForm());

            popupStage.getScene().setFill(Color.web(darkGrayBoxColor));

            popupStage.setOnCloseRequest(windowEvent -> NewGUI.blurBackground(30, 0, 200, rename.getScene().getRoot()));

            rename.getScene().setFill(Color.web(darkGrayBoxColor));
            NewGUI.blurBackground(0, 30, 500, rename.getScene().getRoot());


        });
        rename.setMinHeight(0);


        pane.getChildren().addAll(label, delete, edit, rename);

        return pane;
    }

    /**
     * Updates the rules panel, where each rule is represented.
     */
    private void updateRulesEditorPanel() {
        rulesPanel.getChildren().clear();
        
        if(rules.size() == 0)
            rulesPanel.getChildren().add(numberOfRules);

        rulesPanel.getChildren().add(numberOfRules);

        for (JSONObject entry : rules) {
            Node rulePane = getRulePane(entry);
            rulesPanel.getChildren().add(rulePane);
        }

        rulesPanel.getScene();
        rulesPanel.getScene().getWindow().sizeToScene();

    }

    /**
     * Creates the button to add a new rule of type ClassSmell or MethodSmell.
     *
     * @return the button to add a new rule.
     */
    private Button setUpAddNewRuleButton() {
        Button addNewRule = styledButton("Add rule", lightPurpleColor);
        setButtonIcon(addNewRule, "add.png");

        addNewRule.setMaxHeight(30);

        ContextMenu contextMenu = new ContextMenu();

        MenuItem classSmellMenuItem = new MenuItem("Class Smell");
        classSmellMenuItem.setOnAction(actionEvent -> openAddRuleEditor(true));

        MenuItem methodSmellMenuItem = new MenuItem("Method Smell");
        methodSmellMenuItem.setOnAction(actionEvent -> openAddRuleEditor(false));

        contextMenu.getItems().addAll(classSmellMenuItem, methodSmellMenuItem);

        addNewRule.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton() == MouseButton.PRIMARY){
                contextMenu.show(addNewRule, mouseEvent.getScreenX(), mouseEvent.getScreenY());
            }
        });



        return addNewRule;
    }

    /**
     * Helper method to open the rule editor with the given type of the rule (Class Smell or Method Smell).
     *
     * @param isClassSmell the type of the rule.
     */
    private void openAddRuleEditor(boolean isClassSmell){
        Stage popupStage = AppStyle.setUpPopupStage("New Rule", "/RuleBuilderIcon.gif", true);

        FinalMain finalMain = new FinalMain(isClassSmell);
        SplitPane content = finalMain.getRuleEditor(popupStage, ruleFileManager);

        VBox.setMargin(content, new Insets(20));
        content.setPadding(new Insets(10));

        Scene scene = new Scene(content, 1000, 700);
        scene.getStylesheets().add(getClass().getResource("/style/AppStyle.css").toExternalForm());

        popupStage.setScene(scene);

        popupStage.setMinHeight(300);
        popupStage.setMinWidth(700);

        popupStage.setOnCloseRequest(windowEvent -> {
            JSONObject ruleToADD = finalMain.getRule();

            if (ruleToADD != null && (finalMain.getRuleName() != null)) {
                rules.add(finalMain.getRule());
                updateRulesEditorPanel();

                ruleFileManager.saveJSONListToFile(rules);
            }


        });

        popupStage.show();

    }

    /**
     * Sets up the main pane and deals with the initiation of the GUI.
     */
    private void setUpMainPane() {
        mainPane.setSpacing(20);
        mainPane.setStyle("-fx-background-color: " + darkGrayBoxColor);
        mainPane.setAlignment(Pos.TOP_CENTER);
        mainPane.setPadding(new Insets(20));

        rules.addListener((ListChangeListener<JSONObject>) change -> {
            System.out.println("rules size: "  + rules.size());
            if (rules.size() == 0) {
                if(!rulesPanel.getChildren().contains(numberOfRules))
                    rulesPanel.getChildren().add(numberOfRules);
                numberOfRules.setText("No Rules");
                numberOfRules.setGraphic(getIcon("bird.png"));
            } else {
                rulesPanel.getChildren().remove(numberOfRules);
            }
        });

        numberOfRules.setTextFill(Color.WHITE);
        numberOfRules.setFont(AppStyle.getFont(FontType.BOLD, 12));
        numberOfRules.setGraphic(getIcon("bird.png"));

        rulesPanel.getChildren().add(numberOfRules);

        rulesPanel.setSpacing(20);
        rulesPanel.setMaxWidth(Double.MAX_VALUE);
        rulesPanel.setAlignment(Pos.CENTER);
        rulesPanel.setPadding(new Insets(20));
        rulesPanel.setBorder(new Border(new BorderStroke(Color.web("#76747e"), BorderStrokeStyle.DASHED, new CornerRadii(7), new BorderWidths(2))));
        rulesPanel.setBackground(new Background(new BackgroundFill(Color.web("rgba(118,116,126,0.3)"), new CornerRadii(7), Insets.EMPTY)));

        VBox.setVgrow(rulesPanel, Priority.ALWAYS);

        mainPane.getChildren().add(rulesPanel);


        addNewRuleButton = setUpAddNewRuleButton();
        mainPane.getChildren().add(addNewRuleButton);
        addNewRuleButton.setDisable(true);

        HBox saveAndLoadButtons = new HBox();
        saveAndLoadButtons.setSpacing(20);

        setRulesDirectoryButton = setUpSetRulesFileButton();
        loadRulesButton = setUpLoadRulesFileButton();



        saveAndLoadButtons.getChildren().addAll(setRulesDirectoryButton, loadRulesButton);
        saveAndLoadButtons.setMaxHeight(30);
        saveAndLoadButtons.setMaxWidth(Double.MAX_VALUE);

        mainPane.getChildren().add(saveAndLoadButtons);
    }


    /**
     * Helper method to have buttons with a consistent design all around.
     *
     * @param text the text to be displayed.
     * @param color the color of the button background.
     * @return the button.
     */
    private Button styledButton(String text, String color) {
        Button button = new Button(text);
        button.setFont(AppStyle.getFont(FontType.BOLD, 12));

        button.setStyle(setDefaultBackgroundAndBorderRadiusWithGivenBackgroundColor(color));

        button.setMinHeight(50);
        button.setMinWidth(Region.USE_PREF_SIZE);

        button.setAlignment(Pos.CENTER);

        button.setMaxWidth(Double.MAX_VALUE);

        return button;
    }

    /**
     * Helper method to deal with the multiple times this GUI can be presented.
     */
    private void resetEverything(){
        mainPane = new VBox();
        rulesPanel = new VBox();
        numberOfRules = new Label("No Rules");
        ruleFileManager = new RuleFileManager();
        ruleFileManager.setRules(rules);
        setRulesDirectoryButton = null;
        loadRulesButton = null;
        addNewRuleButton = null;
    }


    public HashMap<String, ArrayList<String>> getResults() {
        return results;
    }
    /**
     * Creates the Code Smells for each rule and detects them.
     */
    public void createCodeSmells( )   {
        ArrayList<CodeSmell> smells = new ArrayList<>();

        for(JSONObject entry: rules){
            String ruleName = (String) ((JSONObject) entry.get("outerName")).get("innerName");
            RuleFileManager tempRuleFileManager = new RuleFileManager();
            boolean isClassSmell = (Boolean) ((JSONObject) entry.get("outerName")).get("isClassSmell");


            smells.add(tempRuleFileManager.jsonObjectToCodeSmell(entry, new DraggingObject(), ruleName, isClassSmell));

        }

        CodeSmellDetector detector = new CodeSmellDetector(metricExtractor.getMetrics(), smells);
        try {
            detector.runDetection();
            results = detector.getResults();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    /**
     * Start of the GUI.
     *
     * @param stage stage to be displayed.
     */
    @Override
    public void start(Stage stage) {
        resetEverything();

        setUpMainPane();

        Scene scene = new Scene(mainPane);
        mainPane.setPrefSize(500, 500);

        scene.getStylesheets().add(getClass().getResource("/style/AppStyle.css").toExternalForm());

        stage.setTitle("Rule Builder");
        stage.getIcons().add(new Image(AppStyle.class.getResourceAsStream("/RuleBuilderIcon.gif")));

        stage.setScene(scene);
        stage.show();

      if(rulesFile != null){
          loadFile();
          ruleFileManager.setRules(rules);
      }

    }

    public File getRulesFile() {
        return rulesFile;
    }
}
