package g1.ISCTE;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

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
                label.setPadding(new Insets(10,10,10,10));

                label.setStyle("-fx-background-radius: 18 18 18 18;\n" +
                        "    -fx-border-radius: 18 18 18 18;\n" +
                        "    -fx-background-color: red;");

                VBox newRootItem = new VBox(label);
                newRootItem.setPadding(new Insets(3,3,3,level+20));


                rootItem.getChildren().add(newRootItem);

                final boolean[] isShowing = {false};

                label.setOnMouseClicked(mouseEvent -> {
                    System.out.println("Pressed -> " +label.getText());
                    if(isShowing[0]){
                        newRootItem.getChildren().clear();
                        newRootItem.getChildren().add(label);
                    }else{
                        percolateFolder(newRootItem, filename, level +1);
                    }

                    isShowing[0] = !isShowing[0];
                });


            }else if(filename.isFile()){
                Label label = new Label(filename.getName());
                label.setPadding(new Insets(0,0, 0,level+20));

                VBox item = new VBox(label);
                rootItem.getChildren().add(item);
            }
        }

    }




    public ScrollPane getScrollPane(File file){
        VBox rootItem = new VBox();

        percolateFolder(rootItem, file, 0);

        scrollPane.setContent(rootItem);

        scrollPane.getStylesheets().add(getClass().getResource("/style/scrollPanel.css").toExternalForm());


        return scrollPane;
    }




}
