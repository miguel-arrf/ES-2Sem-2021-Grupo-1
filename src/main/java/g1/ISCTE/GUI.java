package g1.ISCTE;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.File;

/**
 * Hello world!
 *
 */
public class GUI extends Application {
    public static void main( String[] args ) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
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
