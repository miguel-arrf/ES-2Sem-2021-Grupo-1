package g1.ISCTE;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;

/**
 * Hello world!
 *
 */
public class oldGUI extends Application {
    public static void main( String[] args ) {
        launch(args);
    }


    private HBox getMainControllers(Stage primaryStage){
        HBox controllers = new HBox();
        controllers.setPadding(new Insets(10,10,10,10));




        Button fechar = new Button();
        fechar.setOnMouseClicked(pressEvent -> {
            primaryStage.close();
        });



        Button minimize = new Button();
        minimize.setOnMouseClicked(event -> {
           primaryStage.setIconified(true);
        });



        fechar.setStyle(
                "-fx-background-radius: 5em; " +
                        "-fx-min-width: 15px; " +
                        "-fx-min-height: 15px; " +
                        "-fx-max-width: 15px; " +
                        "-fx-max-height: 15px;" +
                        "-fx-background-color: #ff7171;"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button mover = new Button("mover");
        mover.setStyle(
                "-fx-background-radius: 5em; " +
                        "-fx-min-width: 35px; " +
                        "-fx-min-height: 15px; " +
                        "-fx-max-width: 35px; " +
                        "-fx-max-height: 15px;" +
                        "-fx-background-color: #5c5c5c;"
        );


        mover.setOnMousePressed(pressEvent -> {
            mover.setOnMouseDragged(dragEvent -> {
                primaryStage.setX(dragEvent.getScreenX() - pressEvent.getSceneX());
                primaryStage.setY(dragEvent.getScreenY() - pressEvent.getSceneY());
            });
        });

        controllers.getChildren().addAll(mover,spacer,minimize, fechar);


        return controllers;
    }



    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("CodeSmells Detector");

        VBox initialView = new VBox();

        initialView.getChildren().add(getMainControllers(primaryStage));

        initialView.setPrefSize(400,600);
        initialView.setStyle("-fx-background-size: 1200 900;\n" +
                "    -fx-background-radius: 18 18 18 18;\n" +
                "    -fx-border-radius: 18 18 18 18;\n" +
                "    -fx-background-color: rgba(61,61,61,0.98);");

        Scene scene = new Scene(initialView,400,600);

        scene.setFill(Color.TRANSPARENT);
        //primaryStage.initStyle(StageStyle.TRANSPARENT);

        initialView.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));

        primaryStage.setScene(scene);

        getWebView(primaryStage);
        primaryStage.show();
    }

    private void getWebView(Stage primaryStage){
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        File f = new File("C:\\Users\\mferr\\IdeaProjects\\ES-2Sem-2021-Grupo-1\\src\\teste.html");
        webEngine.load(f.toURI().toString());
        Scene scene = new Scene(webView,600,600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Hello World");
        primaryStage.show();
    }

}
