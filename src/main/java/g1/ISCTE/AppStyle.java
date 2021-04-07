package g1.ISCTE;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AppStyle {

    public static Font getFont(FontType fontType, int size){
        return new Font(size);
        //TODO Add fonts back!
        //return Font.loadFont(AppStyle.class.getResource("/fonts/" + fontType.font).toExternalForm(), size);
    }

    public static Label getLabelWithColorAndFont(Paint paint, FontType fontType, int size, String string){
        Label label = new Label(string);
        label.setTextFill(paint);
        label.setFont(getFont(fontType, size));

        return label;
    }

    public static Label getSubTitleLabel(String message){
        Label subTitleLabel = new Label(message);
        subTitleLabel.setTextFill(Color.web("#76747e"));
        subTitleLabel.setFont(AppStyle.getFont(FontType.DISPLAY_MEDIUM, 12));

        return subTitleLabel;
    }

    public static Label getTitleLabel(String message){
        Label titleLabel = new Label(message);
        titleLabel.setTextFill(Color.web("#b7b7b8"));
        titleLabel.setFont(AppStyle.getFont(FontType.ROUNDED_BOLD, 14));

        return titleLabel;
    }

    public static Stage setUpPopupStage(String popupTitle, String iconPlace, boolean resizable){
        Stage popupStage = new Stage();
        popupStage.setTitle(popupTitle);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setResizable(resizable);

        //popupStage.getIcons().add(new Image(iconPlace));

        return popupStage;
    }

    public static Stage setUpPopup(String popupTitle, String iconPlace, HBox content, String styleSheet){
        Stage popupStage = setUpPopupStage(popupTitle, iconPlace, false);


        setUpPoupScene(content, popupStage, styleSheet);

        //popupStage.initStyle(StageStyle.TRANSPARENT);

        popupStage.show();

        return popupStage;
    }

    private static Scene setUpPoupScene(HBox content, Stage popupStage, String styleSheet) {

        VBox.setMargin(content, new Insets(20));
        content.setPadding(new Insets(10));

        Scene scene = new Scene(content);
        scene.getStylesheets().add(styleSheet);

        popupStage.setScene(scene);


        return scene;
    }

    public static void customFadingIn(final Node node, final VBox parent, int multiplier){
        final FadeTransition transition = new FadeTransition(Duration.millis(100*multiplier), node);
        transition.setFromValue(0);
        transition.setToValue(1);
        transition.setInterpolator(Interpolator.EASE_IN);
        parent.getChildren().add(node);
        transition.play();
    }

    public static void addFadingIn(final Node node, final StackPane parent) {
        final FadeTransition transition = new FadeTransition(Duration.millis(250), node);
        transition.setFromValue(0);
        transition.setToValue(1);
        transition.setInterpolator(Interpolator.EASE_IN);
        parent.getChildren().add(node);
        transition.play();
    }

    public static void removeFadingOut(final Node node, final StackPane parent) {
        if (parent.getChildren().contains(node)) {
            final FadeTransition transition = new FadeTransition(Duration.millis(250), node);
            transition.setFromValue(node.getOpacity());
            transition.setToValue(0);
            transition.setInterpolator(Interpolator.EASE_BOTH);
            transition.setOnFinished(finishHim -> {
                parent.getChildren().remove(node);
            });
            transition.play();
        }
    }

    public static DropShadow getDropShadow(){
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(10.0);
        dropShadow.setOffsetY(10.0);
        dropShadow.setColor(Color.color(0,0,0,0.10));

        return dropShadow;
    }

}

