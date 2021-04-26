package RuleEditor;

import g1.ISCTE.AppStyle;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class RuleEditor extends Application {

    private final VBox mainPane = new VBox();
    private File rulesFile = null;


    private void setUpSetRulesFileButton(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Rules File");

        Button setDirectoryButton = new Button("Set Rules Directory");
        setDirectoryButton.setOnAction(actionEvent -> {
            File tempFile;
            tempFile = fileChooser.showOpenDialog(this.mainPane.getScene().getWindow());

            if(tempFile != null)
                this.rulesFile = tempFile;

        });

        mainPane.getChildren().add(setDirectoryButton);
    }

    private void setUpNumberOfRulesDisplay(){

    }

    private void setUpMainPane(){
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
