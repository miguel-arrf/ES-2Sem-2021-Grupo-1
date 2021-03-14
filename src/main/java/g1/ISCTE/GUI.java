package g1.ISCTE;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GUI extends Application {

    public static void main( String[] args ) {
        launch(args);
    }

    private VBox getMainControllers(){
        VBox mainVBox = new VBox();

        mainVBox.setStyle("-fx-background-radius: 18 18 18 18;\n" +
                "    -fx-border-radius: 18 18 18 18;\n" +
                "    -fx-background-color: white;");

        mainVBox.setPrefSize(400,400);
        mainVBox.setMaxWidth(200);
        mainVBox.setMaxHeight(200);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetY(10);
        dropShadow.setRadius(40);
        dropShadow.setColor(Color.web("rgba(204,246,200,0.5)"));

        mainVBox.setEffect(dropShadow);

        return mainVBox;
    }

    private MenuBar getMenu(){

        MenuBar menuBar = new MenuBar();

        Menu menu = new Menu("File");

        menuBar.getMenus().add(menu);

        return menuBar;
    }

    private VBox getCenter(){
        return new VBox();
    }

    private VBox getLeft(){
        VBox leftVBox = new VBox();

        leftVBox.setPrefWidth(250);
        leftVBox.setMaxWidth(400);

        leftVBox.setStyle("-fx-background-color: #1c1c1e");
        leftVBox.getChildren().add(getEmptyLeftPane());

        leftVBox.setPadding(new Insets(15,15,15,15));

        return leftVBox;
    }

    private Pane getSpacer(){
        Pane pane = new Pane();

        pane.setPrefHeight(5);
        return pane;
    }

    private VBox getEmptyLeftPane(){
        Font font = Font.loadFont(getClass().getResource("/fonts/SF-Pro-Rounded-Bold.ttf").toExternalForm(), 14);
        Font regular = Font.loadFont(getClass().getResource("/fonts/SFProDisplay-Medium.ttf").toExternalForm(), 12);

        Font boldButSmaller = Font.loadFont(getClass().getResource("/fonts/SF-Pro-Rounded-Bold.ttf").toExternalForm(), 10);

        VBox emptyLeftPane = new VBox();
        emptyLeftPane.setSpacing(10);

        Label selectFilesLabel = new Label("Select your files!");
        selectFilesLabel.setTextFill(Color.web("#b7b7b8"));
        selectFilesLabel.setFont(font);

        Label filesFormat = new Label("Files should be a folder or .java");
        filesFormat.setTextFill(Color.web("#76747e"));
        filesFormat.setFont(regular);


        VBox pane = new VBox();
        pane.setPrefHeight(50);
        pane.setBorder(new Border(new BorderStroke(Color.web("#76747e"), BorderStrokeStyle.DASHED, new CornerRadii(7), new BorderWidths(2))));
        pane.setAlignment(Pos.CENTER);
        pane.setBackground(new Background(new BackgroundFill(Color.web("rgba(118,116,126,0.3)"), new CornerRadii(7), Insets.EMPTY)));


        Label dragAndDrop = new Label("Drag & drop files here");
        dragAndDrop.setTextFill(Color.web("#76747e"));
        dragAndDrop.setFont(boldButSmaller);
        pane.getChildren().add(dragAndDrop);

        Button selectFolder = new Button("Select");
        selectFolder.setTextFill(Color.WHITE);
        selectFolder.setMaxWidth(Double.MAX_VALUE);
        selectFolder.setStyle("-fx-background-radius: 7 7 7 7;\n" +
                "    -fx-border-radius: 7 7 7 7;\n" +
                "    -fx-background-color: #6562fc;");
        selectFolder.setFont(boldButSmaller);


        emptyLeftPane.setPadding(new Insets(10,10,10,10));
        emptyLeftPane.getChildren().addAll(selectFilesLabel, filesFormat,getSpacer(), pane,getSpacer(), selectFolder);


        emptyLeftPane.setStyle("-fx-background-radius: 10 10 10 10;\n" +
                "    -fx-border-radius: 10 10 10 10;\n" +
                "    -fx-background-color: #3d3c40;");

        /*DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetY(5);
        dropShadow.setRadius(10);
        dropShadow.setColor(Color.web("rgba(239,176,140,0.3)"));

        emptyLeftPane.setEffect(dropShadow);*/

        emptyLeftPane.setMinHeight(100);


        return emptyLeftPane;
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("CodeSmells Detector");

        BorderPane initialView = new BorderPane();
        initialView.setStyle("-fx-background-color: #3d3c40");

        stage.getIcons().add(new Image(getClass().getResourceAsStream("/CodeSmellsIcon.gif")));

        //initialView.setTop(getMenu());
        initialView.setCenter(getCenter());
        initialView.setLeft(getLeft());

        Scene scene = new Scene(initialView,1000,800);


        initialView.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));

        stage.setScene(scene);
        stage.show();
    }
}
