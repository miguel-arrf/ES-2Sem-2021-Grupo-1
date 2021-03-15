package g1.ISCTE;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyTree {

    private ScrollPane scrollPane = new ScrollPane();

    private void percolateFolder(VBox rootItem, File file, int level){

        File[] files = file.listFiles();

        for(File filename: files){
            if(filename.isDirectory()){

                Label label = new Label(filename.getName());
                label.setTextFill(Color.WHITE);
                label.setFont(GUI.getFont(12));
                label.setPadding(new Insets(4, 10, 4, 10));

                label.setStyle("-fx-background-radius: 7 7 7 7;\n" +
                        "    -fx-border-radius: 7 7 7 7;\n" +
                        "    -fx-background-color: #2cadf6;");

                VBox newRootItem = new VBox(label);
                newRootItem.setPadding(new Insets(5,0,0,level));


                rootItem.getChildren().add(newRootItem);

                final boolean[] isShowing = {false};

                label.setOnMouseClicked(mouseEvent -> {
                    System.out.println("Pressed -> " +label.getText());
                    if(isShowing[0]){
                        newRootItem.getChildren().clear();
                        newRootItem.getChildren().add(label);
                    }else{
                        percolateFolder(newRootItem, filename, 20);
                    }

                    isShowing[0] = !isShowing[0];
                });


            }else if(filename.isFile()){
                Label label = new Label(filename.getName());
                label.setPadding(new Insets(0,0, 0,level));

                VBox item = new VBox(label);
                rootItem.getChildren().add(item);
            }
        }

    }




    public ScrollPane getScrollPane(File file){
        VBox rootItem = new VBox();

        Pane spacer = new Pane();
        spacer.setMinHeight(10);
        VBox vBox = new VBox();

        percolateFolder(rootItem, file, 20);

        vBox.getChildren().add(spacer);
        vBox.getChildren().add(rootItem);

        scrollPane.setContent(vBox);

        scrollPane.getStylesheets().add(getClass().getResource("/style/scrollPanel.css").toExternalForm());


        return scrollPane;
    }




}
