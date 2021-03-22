package g1.ISCTE;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.File;

public class MyTree {

    private final ScrollPane scrollPane = new ScrollPane();

    private void percolateFolder(VBox rootItem, File file){

        File[] files = file.listFiles();

        for(File filename: files){
            if(filename.isDirectory()){

                Label label = new Label(filename.getName());
                label.setTextFill(Color.WHITE);
                label.setFont(AppStyle.getFont(FontType.ROUNDED_BOLD, 12));
                label.setPadding(new Insets(4, 10, 4, 10));


                Image image = new Image(MyTree.class.getResource("/icons/icons8-folder-48.png").toExternalForm());
                ImageView imageView = new ImageView(image);

                imageView.setFitHeight(15);
                imageView.setPreserveRatio(true);
                label.setGraphic(imageView);


                label.getStyleClass().add("treeLabel");

                VBox newRootItem = new VBox(label);
                newRootItem.setPadding(new Insets(5,0,0, 20));

                rootItem.getChildren().add(newRootItem);

                final boolean[] isShowing = {false};

                label.setOnMouseClicked(mouseEvent -> {

                    if(isShowing[0]){
                        newRootItem.getChildren().clear();
                        newRootItem.getChildren().add(label);
                    }else{
                        percolateFolder(newRootItem, filename);
                    }

                    isShowing[0] = !isShowing[0];
                });


            }else if(filename.isFile()){
                Label label = new Label(filename.getName());
                label.getStyleClass().add("fileLabel");

                Image image = new Image(MyTree.class.getResource("/icons/icons8-file-48.png").toExternalForm());
                ImageView imageView = new ImageView(image);

                imageView.setFitHeight(15);
                imageView.setPreserveRatio(true);
                label.setGraphic(imageView);

                label.setPadding(new Insets(4,10, 4, 10));

                VBox item = new VBox(label);

                item.setPadding(new Insets(5,0,0,20));

                rootItem.getChildren().add(item);
            }
        }

    }




    public ScrollPane getScrollPane(File file){
        VBox rootItem = new VBox();

        Pane spacer = new Pane();
        spacer.setMinHeight(10);
        VBox vBox = new VBox();

        Pane bottomSpacer = new Pane();
        bottomSpacer.setMinHeight(10);

        percolateFolder(rootItem, file);

        vBox.getChildren().addAll(spacer, rootItem, bottomSpacer);

        scrollPane.setContent(vBox);

        scrollPane.getStylesheets().add(getClass().getResource("/style/scrollPanel.css").toExternalForm());

        return scrollPane;
    }




}
