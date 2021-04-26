package RuleEditor;

import g1.ISCTE.AppStyle;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.simple.JSONObject;

import java.io.File;

public class RuleEditor extends Application {

    private final VBox mainPane = new VBox();
    private final VBox rulesPanel = new VBox();
    private final Label numberOfRules = new Label("No Rules");

    private File rulesFile = null;

    private ObservableList<JSONObject> rules = FXCollections.observableArrayList();


    private void setUpSetRulesFileButton(){

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Rules File");

        Button setDirectoryButton = new Button("Set Rules Directory");
        setDirectoryButton.setOnAction(actionEvent -> {
            File tempFile;
            tempFile = fileChooser.showOpenDialog(this.mainPane.getScene().getWindow());

            if(tempFile != null)
                rulesFile = tempFile;
        });

        mainPane.getChildren().add(setDirectoryButton);
    }


    private void updateRulesEditorPanel(){
        rulesPanel.getChildren().clear();

        for(JSONObject entry : rules){
            JSONObject jsonObject = (JSONObject) entry.get("id");
            Label label = new Label((String) jsonObject.get("name"));
            rulesPanel.getChildren().add(label);
        }

    }

    private void setUpAddNewRuleButton(){
        Button addNewRule = new Button("Add rule");

        addNewRule.setOnAction(actionEvent -> {
            Stage popupStage = AppStyle.setUpPopupStage("New Rule", "/RuleBuilderIcon.gif", true);

            FinalMain finalMain = new FinalMain();
            SplitPane content = finalMain.getRuleEditor(popupStage);

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

                RuleComplete.arrayListToJSON(rules);

            });

            popupStage.show();

        });

        mainPane.getChildren().add(addNewRule);
    }

    private void setUpMainPane(){

        rules.addListener((ListChangeListener<JSONObject>) change -> {
            if(rules.size() == 0){
                numberOfRules.setText("No Rules");
            }else{
                numberOfRules.setText(rules.size() + " rules");
            }
        });

        mainPane.getChildren().add(numberOfRules);

        mainPane.getChildren().add(rulesPanel);
        setUpAddNewRuleButton();
        setUpSetRulesFileButton();
    }

    @Override
    public void start(Stage stage) {

        setUpMainPane();

        Scene scene = new Scene(mainPane, 500, 500);

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
