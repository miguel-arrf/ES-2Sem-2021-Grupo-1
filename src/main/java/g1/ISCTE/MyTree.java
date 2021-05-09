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
import java.util.Optional;

/**
 * The type MyTree, that displays a tree representation of a given folder.
 */

public class MyTree {

    private final ScrollPane scrollPane = new ScrollPane();

    /**
     * Gets the folder image with the proper size.
     *
     * @return a ImageView representing a folder.
     */
    private ImageView getFolderImage(){

        Image image = new Image(MyTree.class.getResource("/icons/icons8-folder-48.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        setImageViewStyle(imageView);

        return  imageView;
    }

    /**
     * Sets the default style for the icons.
     *
     * @param imageView the imageView to set up the size.
     */
    private void setImageViewStyle(ImageView imageView){
        imageView.setFitHeight(15);
        imageView.setPreserveRatio(true);
    }


    /**
     * Percolates a file, creating the respective GUI in a recursive way.
     *
     * @param rootItem the root item.
     * @param file the current file.
     */
    private void percolateFolder(VBox rootItem, File file){

        File[] files = file.listFiles();

        for(int i = 0; i < (files != null ? files.length : 0); i++){
            File filename = files[i];

            if(filename.isDirectory()){

                Label label = new Label(filename.getName());
                label.setTextFill(Color.WHITE);
                label.setPadding(new Insets(4, 10, 4, 10));
                label.setGraphic(getFolderImage());
                label.getStyleClass().add("treeLabel");

                VBox newRootItem = new VBox(label);
                newRootItem.setPadding(new Insets(5,0,0, 20));

                AppStyle.customFadingIn(newRootItem, rootItem, i);

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

                Optional<String> fileExtension = Helpers.getExtensionByStringHandling(filename.getName());

                Image image;
                ImageView imageView;

                if(fileExtension.isPresent() && fileExtension.get().equals("xlsx")){

                    label.getStyleClass().add("excelLabel");
                    image = new Image(MyTree.class.getResource("/icons/icons8-microsoft-excel-90.png").toExternalForm());
                }else{

                    label.getStyleClass().add("fileLabel");
                    image = new Image(MyTree.class.getResource("/icons/icons8-file-48.png").toExternalForm());
                }

                imageView = new ImageView(image);
                setImageViewStyle(imageView);

                label.setGraphic(imageView);
                label.setPadding(new Insets(4,10, 4, 10));

                VBox item = new VBox(label);
                item.setPadding(new Insets(5,0,0,20));

                AppStyle.customFadingIn(item, rootItem, i);

            }
        }

    }

    /**
     * Get scroll pane scroll pane.
     *
     * @param file the file
     * @return the scroll pane
     */
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
