package RuleEditor;

import g1.ISCTE.AppStyle;
import g1.ISCTE.FontType;
import g1.ISCTE.MyTree;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class RuleEditor extends Application {

    private final VBox mainPane = new VBox();
    private final VBox rulesPanel = new VBox();
    private final Label numberOfRules = new Label("No Rules");
    private final RuleComplete ruleComplete = new RuleComplete();
    private Button setRulesDirectoryButton;
    private Button loadRulesButton;
    private Button addNewRuleButton;
    private File rulesFile = null;

    private ObservableList<JSONObject> rules = FXCollections.observableArrayList();

    public static void main(String[] args) {
        launch(args);
    }

    private Button setUpLoadRulesFileButton() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Rule file(*.rule)", "*.rule"));
        fileChooser.setInitialFileName("*.rule");
        fileChooser.setTitle("Select Rules File");

        Button setDirectoryButton = styledButton("Load Rules", "#a3ddcb");
        setButtonIcon(setDirectoryButton, "icons8-download-96.png");

        setDirectoryButton.setOnAction(actionEvent -> {
            File tempFile;
            tempFile = fileChooser.showOpenDialog(this.mainPane.getScene().getWindow());

            if (tempFile != null) {
                if (tempFile.getName().endsWith(".rule")) {
                    rulesFile = tempFile;
                    ruleComplete.setFile(rulesFile);

                    try {
                        ArrayList<JSONObject> arrayList = ruleComplete.loadJSONRuleFile();
                        rules.clear();

                        //If we don't do it this way, the listener for the label, wouldn't work.
                        rules.addAll(arrayList);

                        updateRulesEditorPanel();
                        addNewRuleButton.setDisable(false);

                    } catch (ParseException | IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        HBox.setHgrow(setDirectoryButton, Priority.ALWAYS);

        return setDirectoryButton;
    }

    private Button setUpSetRulesFileButton() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Rule file(*.rule)", "*.rule"));
        fileChooser.setInitialFileName("*.rule");
        fileChooser.setTitle("Save Rules File");

        Button setDirectoryButton = styledButton("Create Rules file", "#d5ecc2");
        setButtonIcon(setDirectoryButton, "add-file.png");

        setDirectoryButton.setOnAction(actionEvent -> {
            File tempFile;
            tempFile = fileChooser.showSaveDialog(this.mainPane.getScene().getWindow());

            if (tempFile != null) {
                if (tempFile.getName().endsWith(".rule")) {
                    rulesFile = tempFile;
                    ruleComplete.setFile(rulesFile);

                    rules.clear();

                    addNewRuleButton.setDisable(false);
                }
            }
        });

        HBox.setHgrow(setDirectoryButton, Priority.ALWAYS);


        return setDirectoryButton;
    }

    private HBox getRenameTextField(Label label, JSONObject jsonObject) {

        Button updateButton = styledButton("Update", "#a3ddcb");
        Button cancelButton = styledButton("Cancel", "#d8345f");

        cancelButton.setOnAction(actionEvent -> Platform.runLater(() -> cancelButton.getScene().getWindow().fireEvent(new WindowEvent(cancelButton.getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST))));


        HBox hBox = new HBox();
        hBox.setStyle("-fx-background-color: #3d3c40");
        TextField textField = new TextField("0.0");
        textField.setStyle("-fx-text-inner-color: white;");


        updateButton.setOnAction(actionEvent -> {
            label.setText(textField.getText());

            jsonObject.replace("name", textField.getText());

            ruleComplete.arrayListToJSON(rules);
            Platform.runLater(() -> cancelButton.getScene().getWindow().fireEvent(new WindowEvent(cancelButton.getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST)));
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

    private ImageView getIcon(String imageLocation){
        Image image = new Image(MyTree.class.getResource("/icons/" + imageLocation).toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(15);
        imageView.setPreserveRatio(true);
        imageView.setBlendMode(BlendMode.DIFFERENCE);

        return imageView;
    }

    private void setButtonIcon(Button button, String imageLocation){
        button.setGraphic(getIcon(imageLocation));

    }

    private Node getRulePane(JSONObject nodeJSON) {

        HBox pane = new HBox();
        pane.setSpacing(20);

        Label label = new Label((String) nodeJSON.get("name"));
        label.setFont(AppStyle.getFont(FontType.BOLD, 12));

        label.setAlignment(Pos.CENTER);

        HBox.setHgrow(label, Priority.ALWAYS);
        label.setMaxWidth(Double.MAX_VALUE);

        label.setMinWidth(Region.USE_PREF_SIZE);
        label.setTextFill(Color.BLACK);
        label.setPadding(new Insets(5));
        label.setStyle("-fx-background-radius: 7 7 7 7;\n"
                + "    -fx-border-radius: 7 7 7 7;\n" +
                "    -fx-background-color: #a3ddcb");


        Button delete = styledButton("Delete", "#f39189");
        setButtonIcon(delete,"icons8-full-trash-96.png" );


        delete.setOnAction(actionEvent -> {
            rules.remove(nodeJSON);
            ruleComplete.arrayListToJSON(rules);

            updateRulesEditorPanel();
        });
        delete.setMinHeight(0);

        Button edit = styledButton("Edit", "#ece2e1");
        setButtonIcon(edit, "icons8-edit-384.png");

        edit.setOnAction(actionEvent -> {

            Stage popupStage = AppStyle.setUpPopupStage("Edit Rule", "/RuleBuilderIcon.gif", true);

            FinalMain finalMain = new FinalMain();
            String ruleName = (String) nodeJSON.get("name");

            SplitPane content = finalMain.getEditRuleEditor(popupStage, ruleComplete, nodeJSON, ruleName);

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
                    ruleComplete.arrayListToJSON(rules);
                    updateRulesEditorPanel();
                }

            });

            popupStage.show();
        });

        edit.setMinHeight(0);

        Button rename = styledButton("Rename", "#ded7b1");
        setButtonIcon(rename, "icons8-rename-96.png");

        rename.setOnAction(actionEvent -> {


            HBox optionsHBox = getRenameTextField(label, nodeJSON);
            Stage popupStage = AppStyle.setUpPopup("Value", "/PreferencesPanelIcon.gif", optionsHBox, getClass().getResource("/style/AppStyle.css").toExternalForm());

            popupStage.getScene().setFill(Color.web("#3d3c40"));

            popupStage.setOnCloseRequest(windowEvent -> NewGUI.blurBackground(30, 0, 200, rename.getScene().getRoot()));

            rename.getScene().setFill(Color.web("#3d3c40"));
            NewGUI.blurBackground(0, 30, 500, rename.getScene().getRoot());


        });
        rename.setMinHeight(0);


        pane.getChildren().addAll(label, delete, edit, rename);

        return pane;
    }

    private void updateRulesEditorPanel() {
        rulesPanel.getChildren().clear();

        for (JSONObject entry : rules) {
            Node rulePane = getRulePane(entry);
            rulesPanel.getChildren().add(rulePane);
        }

        rulesPanel.getScene().getWindow().sizeToScene();

    }

    private Button setUpAddNewRuleButton() {
        Button addNewRule = styledButton("Add rule", "#a29bfe");
        setButtonIcon(addNewRule, "add.png");

        addNewRule.setMaxHeight(30);


        addNewRule.setOnAction(actionEvent -> {
            Stage popupStage = AppStyle.setUpPopupStage("New Rule", "/RuleBuilderIcon.gif", true);

            FinalMain finalMain = new FinalMain();
            SplitPane content = finalMain.getRuleEditor(popupStage, ruleComplete);

            VBox.setMargin(content, new Insets(20));
            content.setPadding(new Insets(10));

            Scene scene = new Scene(content, 1000, 700);
            scene.getStylesheets().add(getClass().getResource("/style/AppStyle.css").toExternalForm());

            popupStage.setScene(scene);

            popupStage.setMinHeight(300);
            popupStage.setMinWidth(700);

            popupStage.setOnCloseRequest(windowEvent -> {
                JSONObject ruleToADD = finalMain.getRule();
                if (ruleToADD != null) {
                    rules.add(finalMain.getRule());
                    updateRulesEditorPanel();

                    ruleComplete.arrayListToJSON(rules);
                }


            });

            popupStage.show();

        });

        return addNewRule;
    }

    private void setUpMainPane() {

        mainPane.setSpacing(20);
        mainPane.setStyle("-fx-background-color: #3d3c40 ");
        mainPane.setAlignment(Pos.TOP_CENTER);
        mainPane.setPadding(new Insets(20));

        rules.addListener((ListChangeListener<JSONObject>) change -> {
            if (rules.size() == 0) {
                numberOfRules.setText("No Rules");
                numberOfRules.setGraphic(getIcon("bird.png"));
            } else {
                numberOfRules.setText(rules.size() + " rules");
                numberOfRules.setGraphic(null);
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

    @Override
    public void start(Stage stage) {

        setUpMainPane();

        Scene scene = new Scene(mainPane);
        mainPane.setPrefSize(500, 500);

        scene.getStylesheets().add(getClass().getResource("/style/AppStyle.css").toExternalForm());

        stage.setTitle("Rule Builder");
        stage.getIcons().add(new Image(AppStyle.class.getResourceAsStream("/RuleBuilderIcon.gif")));

        stage.setScene(scene);
        stage.show();
    }

}
