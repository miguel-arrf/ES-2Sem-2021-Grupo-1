package RuleEditor;

import g1.ISCTE.AppStyle;
import javafx.application.Application;
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
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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
    private File rulesFile = null;

    private ObservableList<JSONObject> rules = FXCollections.observableArrayList();

    private Button setUpLoadRulesFileButton(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Rule file(*.txt)", "*.txt"));
        fileChooser.setInitialFileName("*.txt");
        fileChooser.setTitle("Select Rules File");

        Button setDirectoryButton = styledButton("Load Rules", "#a3ddcb");
        setDirectoryButton.setOnAction(actionEvent -> {
            File tempFile;
            tempFile = fileChooser.showOpenDialog(this.mainPane.getScene().getWindow());

            System.out.println("tempFile: " + tempFile);
            if(tempFile != null){
                if(tempFile.getName().endsWith(".txt")){
                    rulesFile = tempFile;
                    ruleComplete.setFile(rulesFile);

                    try {
                        ArrayList<JSONObject> arrayList = ruleComplete.loadJSONRuleFile();
                        rules.clear();

                        //If we don't do it this way, the listener for the label, wouldn't work.
                        rules.addAll(arrayList);

                        System.out.println("reading: ");
                        for(JSONObject jsonObject : rules){
                            System.out.println(jsonObject);
                        }

                        updateRulesEditorPanel();

                    } catch (ParseException | IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        return setDirectoryButton;
    }

    private Button setUpSetRulesFileButton(){

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Rule file(*.txt)", "*.txt"));
        fileChooser.setInitialFileName("*.txt");
        fileChooser.setTitle("Save Rules File");

        Button setDirectoryButton = styledButton("Set Rules Directory", "#d5ecc2");
        setDirectoryButton.setOnAction(actionEvent -> {
            File tempFile;
            tempFile = fileChooser.showSaveDialog(this.mainPane.getScene().getWindow());

            System.out.println("tempFile: " + tempFile);
            if(tempFile != null){
                if(tempFile.getName().endsWith(".txt")){
                    rulesFile = tempFile;
                    ruleComplete.setFile(rulesFile);

                    rules.clear();
                }
            }
        });


        return setDirectoryButton;
    }

    private Node getRulePane(JSONObject nodeJSON){
        JSONObject jsonObject = (JSONObject) nodeJSON.get("id");
        Label label = new Label((String) jsonObject.get("name"));
        label.setTextFill(Color.WHITE);

        return label;
    }

    private void updateRulesEditorPanel(){
        rulesPanel.getChildren().clear();

        for(JSONObject entry : rules){
            //JSONObject jsonObject = (JSONObject) entry.get("id");
            rulesPanel.getChildren().add(getRulePane(entry));
            //Label label = new Label((String) jsonObject.get("name"));
            //label.setTextFill(Color.WHITE);
            //rulesPanel.getChildren().add(label);
        }

        rulesPanel.getScene().getWindow().sizeToScene();

    }

    private void setUpAddNewRuleButton(){
        Button addNewRule = styledButton("Add rule", "#a29bfe");
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

            popupStage.setMinHeight(700);
            popupStage.setMinWidth(700);

            popupStage.setOnCloseRequest(windowEvent -> {
                System.out.println("Closed! Rule name: " + finalMain.getRuleName());
                rules.add(finalMain.getRule());
                updateRulesEditorPanel();

                ruleComplete.arrayListToJSON(rules);

            });

            popupStage.show();

        });

        mainPane.getChildren().add(addNewRule);
    }

    private void setUpMainPane(){

        mainPane.setSpacing(20);
        mainPane.setStyle("-fx-background-color: #3d3c40 ");
        mainPane.setAlignment(Pos.TOP_CENTER);
        mainPane.setPadding(new Insets(20));

        rules.addListener((ListChangeListener<JSONObject>) change -> {
            if(rules.size() == 0){
                numberOfRules.setText("No Rules");
            }else{
                numberOfRules.setText(rules.size() + " rules");
            }
        });

        numberOfRules.setTextFill(Color.WHITE);
        rulesPanel.getChildren().add(numberOfRules);

        rulesPanel.setMaxWidth(Double.MAX_VALUE);
        rulesPanel.setAlignment(Pos.CENTER);
        rulesPanel.setPadding(new Insets(20,100,20,100));
        rulesPanel.setBorder(new Border(new BorderStroke(Color.web("#76747e"), BorderStrokeStyle.DASHED, new CornerRadii(7), new BorderWidths(2))));
        rulesPanel.setBackground(new Background(new BackgroundFill(Color.web("rgba(118,116,126,0.3)"), new CornerRadii(7), Insets.EMPTY)));

        VBox.setVgrow(rulesPanel, Priority.ALWAYS);

        mainPane.getChildren().add(rulesPanel);
        setUpAddNewRuleButton();

        HBox saveAndLoadButtons = new HBox();
        saveAndLoadButtons.setSpacing(20);
        saveAndLoadButtons.getChildren().addAll(setUpSetRulesFileButton(), setUpLoadRulesFileButton());
        saveAndLoadButtons.setMaxHeight(30);

        mainPane.getChildren().add(saveAndLoadButtons);
    }

    private Button styledButton(String text, String color){
        Button button = new Button(text);

        button.setStyle("-fx-background-radius: 7 7 7 7;\n" +
                "    -fx-border-radius: 7 7 7 7;\n" +
                "    -fx-background-color: " + color );
        button.setMinWidth(150);
        button.setMinHeight(50);
        button.setAlignment(Pos.CENTER);

        button.setMaxWidth(Double.MAX_VALUE);

        return button;
    }


    @Override
    public void start(Stage stage) {

        setUpMainPane();

        Scene scene = new Scene(mainPane);
        mainPane.setPrefWidth(300);

        scene.getStylesheets().add(getClass().getResource("/style/AppStyle.css").toExternalForm());

        stage.setTitle("Rule Builder");
        stage.getIcons().add(new Image(AppStyle.class.getResourceAsStream("/RuleBuilderIcon.gif")));

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
