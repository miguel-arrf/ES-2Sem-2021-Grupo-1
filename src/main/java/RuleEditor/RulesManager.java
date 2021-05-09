package RuleEditor;

import CodeSmellDetection.CodeSmell;
import CodeSmellDetection.CodeSmellDetector;
import CodeSmellDetection.RuleOperator;
import MetricExtraction.MetricExtractor;
import g1.ISCTE.AppStyle;
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
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

import static g1.ISCTE.AppStyle.*;


/**
 * Rules editor: Allows for creation, deletion, and edition of rules.
 */
public class RulesManager extends Application {

    private static final String FILE_EXTENSION = "rule";

    private VBox mainPane = new VBox();
    private VBox rulesPanel = new VBox();
    private Label numberOfRules = new Label("No Rules");
    private Button setRulesDirectoryButton;
    private Button loadRulesButton;
    private Button addNewRuleButton;
    private File rulesFile = null;
    private HashMap<String, ArrayList<String>> results = new HashMap<>();

    private final ObservableList<JSONObject> rules = FXCollections.observableArrayList();
    private RuleFileManager ruleFileManager;
    private MetricExtractor metricExtractor;


    private static ConditionBlock getDefaultIsGodClassBlock(){
        return new ConditionBlock(RuleOperator.GREATER, new MetricBlock("LOC_Class"), "30");
    }

    private static ConditionBlock getDefaultIsLongMethodBlock(){
        return new ConditionBlock(RuleOperator.GREATER, new MetricBlock("LOC_Method"), "30");
    }


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

    public void setFile(File file) {
        this.rulesFile = file;
    }


    /**
     * Gets a File Chooser for rules files.
     *
     * @param message the type of file chooser (to save or to load).
     * @return the FileChooser
     */
    private FileChooser getFileChooser(String message) {
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

        Button setDirectoryButton = AppStyle.getButtonWithDropShadow("Load Rules", "#a3ddcb");
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
    public void loadFile() {
        ruleFileManager.setFile(rulesFile);

        try {
            ArrayList<JSONObject> arrayList = ruleFileManager.loadJSONRuleFile();
            rules.clear();

            //If we don't do it this way, the listener for the label, wouldn't work.
            rules.addAll(arrayList);

            updateRulesEditorPanel();
            addNewRuleButton.setDisable(false);
            setButtonIcon(addNewRuleButton, "add.png");

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

        Button setDirectoryButton = AppStyle.getButtonWithDropShadow("Create Rules file", "#d5ecc2");
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
                    setButtonIcon(addNewRuleButton, "add.png");

                }
            }
        });

        HBox.setHgrow(setDirectoryButton, Priority.ALWAYS);


        return setDirectoryButton;
    }

    /**
     * HBox with the respective content (rule rename text field) to be displayed into a popup.
     *
     * @param label      the label that displays the rule name and that shall be updated or not by the user.
     * @param jsonObject the rule in JSON format.
     * @return the content needed to create a popup to rename a given rule.
     */
    @SuppressWarnings("unchecked")
    private HBox getRenameTextField(Label label, JSONObject jsonObject) {
        Button updateButton = AppStyle.getButtonWithDropShadow("Update", lightGreenColor);

        String ruleName = (String) ((JSONObject) jsonObject.get("outerName")).get("innerName");

        HBox hBox = new HBox();
        hBox.setStyle("-fx-background-color: " + darkGrayBoxColor);
        TextField textField = new TextField(ruleName);
        textField.setStyle("-fx-text-inner-color: white; -fx-background-color: #606060");

        boolean isClassRule = (Boolean) ((JSONObject) jsonObject.get("outerName")).get("isClassSmell");

        updateButton.setOnAction(actionEvent -> {

            if (ruleFileManager.isValidName(textField.getText()) || textField.getText().equals(ruleName)) {
                label.setText(textField.getText() + " | " + (isClassRule ? "Class rule" : "Method rule"));


                JSONObject outerName = (JSONObject) jsonObject.get("outerName");
                outerName.replace("innerName", textField.getText());

                jsonObject.replace("outerName", outerName);

                ruleFileManager.saveJSONListToFile(rules);

                Platform.runLater(() -> updateButton.getScene().getWindow().fireEvent(new WindowEvent(updateButton.getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST)));
            } else {
                textField.setStyle("-fx-border-radius: 10; -fx-text-inner-color: white; -fx-background-color: #606060; -fx-border-color: red");

            }

        });

        textField.setMaxWidth(150);

        hBox.getChildren().addAll(textField, updateButton);

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
        boolean isClassRule = (Boolean) ((JSONObject) nodeJSON.get("outerName")).get("isClassSmell");

        HBox pane = new HBox();
        pane.setSpacing(20);

        Label label = AppStyle.getStyledLabel((String) ((JSONObject) nodeJSON.get("outerName")).get("innerName"), lightGreenColor,5);
        label.setText(label.getText() + " | " + (isClassRule ? "Class rule" : "Method rule"));

        Button deleteButton = getDeleteButton(nodeJSON);
        Button editButton = getEditButton(isClassRule, nodeJSON);
        Button renameButton = getRenameButton(label, nodeJSON);

        pane.getChildren().addAll(label, deleteButton, editButton, renameButton);

        return pane;
    }


    /**
     * Gets the delete button while also setting up the behaviour of it.
     *
     * @param nodeJSON the json node representing the rule.
     * @return the delete button.
     */
    private Button getDeleteButton(JSONObject nodeJSON) {
        Button delete = AppStyle.getButtonWithDropShadow("Delete", lightRedColor);
        setButtonIcon(delete, "icons8-full-trash-96.png");

        delete.setOnAction(actionEvent -> {
            rules.remove(nodeJSON);
            ruleFileManager.saveJSONListToFile(rules);

            updateRulesEditorPanel();
        });
        delete.setMinHeight(0);

        return delete;
    }


    /**
     * Gets the edit button while also setting up the behaviour of it.
     *
     * @param isClassRule if the rule is a class or method one.
     * @param nodeJSON    the json node representing the rule.
     * @return the edit button.
     */
    private Button getEditButton(boolean isClassRule, JSONObject nodeJSON) {
        Button edit = AppStyle.getButtonWithDropShadow("Edit", lightGrayColor);
        setButtonIcon(edit, "icons8-edit-384.png");

        edit.setOnAction(actionEvent -> {

            Stage popupStage = AppStyle.setUpPopupStage("Edit Rule", "/RuleBuilderIcon.gif", true);

            RuleEditor ruleEditor = new RuleEditor(isClassRule);
            String ruleName = (String) ((JSONObject) nodeJSON.get("outerName")).get("innerName");

            SplitPane content = ruleEditor.getEditRuleEditor(popupStage, ruleFileManager, nodeJSON, ruleName);

            VBox.setMargin(content, new Insets(20));
            content.setPadding(new Insets(10));

            Scene scene = new Scene(content, 1000, 700);
            scene.getStylesheets().add(getClass().getResource("/style/AppStyle.css").toExternalForm());

            popupStage.setScene(scene);

            popupStage.setMinHeight(300);
            popupStage.setMinWidth(700);
            popupStage.setMinWidth(700);

            popupStage.setOnCloseRequest(windowEvent -> {

                JSONObject ruleToADD = ruleEditor.getRule();

                if (ruleToADD != null) {
                    rules.remove(nodeJSON);
                    rules.add(ruleToADD);
                    ruleFileManager.saveJSONListToFile(rules);
                }
                updateRulesEditorPanel();


            });

            popupStage.show();
        });

        edit.setMinHeight(0);

        return edit;
    }


    /**
     * Gets the rename button while also setting up the behaviour of it.
     *
     * @param label    the label to be updated on the rename of the rule.
     * @param nodeJSON the json node representing the rule.
     * @return the rename button.
     */
    private Button getRenameButton(Label label, JSONObject nodeJSON) {
        Button rename = AppStyle.getButtonWithDropShadow("Rename", lightYellowColor);
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

        return rename;
    }

    /**
     * Updates the rules panel, where each rule is represented.
     */
    private void updateRulesEditorPanel() {
        rulesPanel.getChildren().clear();

        if (rules.size() == 0)
            rulesPanel.getChildren().add(numberOfRules);


        for (JSONObject entry : rules) {
            Node rulePane = getRulePane(entry);
            rulesPanel.getChildren().add(rulePane);
        }

        if (rulesPanel.getScene() != null)
            rulesPanel.getScene().getWindow().sizeToScene();


    }

    /**
     * Creates the button to add a new rule of type ClassSmell or MethodSmell.
     *
     * @return the button to add a new rule.
     */
    private Button setUpAddNewRuleButton() {
        Button addNewRule = AppStyle.getButtonWithDropShadow("Add rule", lightPurpleColor);

        addNewRule.setMaxHeight(30);

        ContextMenu contextMenu = new ContextMenu();

        MenuItem longMethodItem = new MenuItem("isLongMethod");
        longMethodItem.setOnAction(actionEvent -> {
            try {
                addIsLongMethod();
            } catch (IncorrectRuleException | ParseException e) {
                e.printStackTrace();
            }
        });
        MenuItem godClassItem = new MenuItem("isGodClass");
        godClassItem.setOnAction(actionEvent -> {
            try {
                addIsGodClass();
            } catch (IncorrectRuleException | ParseException e) {
                e.printStackTrace();
            }
        });

        MenuItem classSmellMenuItem = new MenuItem("Class Rule");
        classSmellMenuItem.setOnAction(actionEvent -> openAddRuleEditor(true));

        MenuItem methodSmellMenuItem = new MenuItem("Method Rule");
        methodSmellMenuItem.setOnAction(actionEvent -> openAddRuleEditor(false));

        contextMenu.getItems().addAll(classSmellMenuItem, methodSmellMenuItem, longMethodItem, godClassItem);

        addNewRule.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                contextMenu.show(addNewRule, mouseEvent.getScreenX(), mouseEvent.getScreenY());
            }
        });


        return addNewRule;
    }

    private void addIsLongMethod() throws IncorrectRuleException, ParseException {
        addDefaultRule(getDefaultIsLongMethodBlock(), "isLongMethod", false);
    }

    private void addDefaultRule(ConditionBlock defaultBlock, String nameToSearch, boolean isClassSmell) throws IncorrectRuleException, ParseException {
        ConditionBlock isGodClass = defaultBlock;
        ArrayList<CustomNode> nodes = new ArrayList<>(Arrays.asList(isGodClass));

        JSONObject ruleToAdd =  ruleFileManager.guiToJSONObject(nodes, nameToSearch, isClassSmell);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonArray = (JSONObject) jsonParser.parse(ruleToAdd.toJSONString());


        int count = (int) rules.stream().filter(p ->((String) ((JSONObject) p.get("outerName")).get("innerName")).contains(nameToSearch)).count();
        if(count != 0){
            Optional<JSONObject> rule = rules.stream().filter(p -> ((JSONObject) p.get("outerName")).get("innerName").equals(nameToSearch)).findFirst();
            if(rule.isPresent()){
                rules.remove(rule.get());

                JSONObject ruleExists = rule.get();

                Optional<Integer> newValue = rules.stream().filter(p ->((String) ((JSONObject) p.get("outerName")).get("innerName")).contains(nameToSearch + "_old")).map(p -> {
                    String name = (String) ((JSONObject) p.get("outerName")).get("innerName");
                    name = name.replace(nameToSearch + "_old", "");
                    return Integer.parseInt(name);
                }).sorted().findFirst();

                if(newValue.isPresent()){
                    ruleFileManager.renameJSONRule(ruleExists,  nameToSearch + "_old" + (newValue.get() + 1));
                }else{
                    ruleFileManager.renameJSONRule(ruleExists, nameToSearch + "_old" + 1);
                }

                rules.add(ruleExists);

            }

        }

        rules.add(jsonArray);
        ruleFileManager.saveJSONListToFile(rules);
        updateRulesEditorPanel();

    }

    private void addIsGodClass() throws IncorrectRuleException, ParseException {
        addDefaultRule(getDefaultIsGodClassBlock(), "isGodClass", true);
    }


    /**
     * Helper method to open the rule editor with the given type of the rule (Class Rule or Method Rule)
     *
     * @param isClassRule the type of the rule.
     */
    private void openAddRuleEditor(boolean isClassRule) {
        Stage popupStage = AppStyle.setUpPopupStage("New Rule", "/RuleBuilderIcon.gif", true);

        RuleEditor ruleEditor = new RuleEditor(isClassRule);
        SplitPane content = ruleEditor.getRuleEditor(popupStage, ruleFileManager);

        VBox.setMargin(content, new Insets(20));
        content.setPadding(new Insets(10));

        Scene scene = new Scene(content, 1000, 700);
        scene.getStylesheets().add(getClass().getResource("/style/AppStyle.css").toExternalForm());

        popupStage.setScene(scene);

        popupStage.setMinHeight(300);
        popupStage.setMinWidth(700);

        popupStage.setOnCloseRequest(windowEvent -> {
            JSONObject ruleToADD = ruleEditor.getRule();

            if (ruleToADD != null && (ruleEditor.getRuleName() != null)) {
                rules.add(ruleEditor.getRule());
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
            System.out.println("ENTRÁAAAAMOS AQUI: " + rules.size());
            if (rules.size() == 0) {
                if (!rulesPanel.getChildren().contains(numberOfRules))
                    rulesPanel.getChildren().add(numberOfRules);
                numberOfRules.setText("No Rules");
                numberOfRules.setGraphic(getIcon("bird.png"));
            } else {
                rulesPanel.getChildren().remove(numberOfRules);
            }
        });

        numberOfRules.setTextFill(Color.WHITE);
        numberOfRules.setGraphic(getIcon("bird.png"));
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
     * Helper method to deal with the multiple times this GUI can be presented.
     */
    private void resetEverything() {
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
    public ArrayList<CodeSmell> createCodeSmells() {
        ArrayList<CodeSmell> smells = new ArrayList<>();

        for (JSONObject entry : rules) {
            String ruleName = (String) ((JSONObject) entry.get("outerName")).get("innerName");
            RuleFileManager tempRuleFileManager = new RuleFileManager();
            boolean isClassSmell = (Boolean) ((JSONObject) entry.get("outerName")).get("isClassSmell");


            smells.add(tempRuleFileManager.jsonObjectToCodeSmell(entry, new DraggingObject(), ruleName, isClassSmell));

        }

        if (metricExtractor != null && metricExtractor.getResults() != null) {
            CodeSmellDetector detector = new CodeSmellDetector(metricExtractor.getResults(), smells);
            try {
                detector.runDetection();
                results = detector.getResults();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return smells;
        } else {
            return new ArrayList<>();
        }


    }


    /**
     * Start of the GUI.
     *
     * @param stage stage to be displayed.
     */
    @Override
    public void start(Stage stage) {
        setUpGUI();

        Scene scene = new Scene(mainPane);
        mainPane.setPrefSize(500, 500);

        scene.getStylesheets().add(getClass().getResource("/style/AppStyle.css").toExternalForm());

        stage.setTitle("Rule Builder");
        stage.getIcons().add(new Image(AppStyle.class.getResourceAsStream("/RuleBuilderIcon.gif")));

        stage.setScene(scene);
        stage.show();

        if (rulesFile != null) {
            loadFile();
            ruleFileManager.setRules(rules);
        }

    }

    private void setUpGUI() {
        resetEverything();
        setUpMainPane();

    }


    public File getRulesFile() {
        return rulesFile;
    }
}