package g1.ISCTE;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AppStyle {


    /**
     * @param fontType font style to be applied
     * @param size the display size of the font
     * @return the font with the specified style and size
     */
    public static Font getFont(FontType fontType, int size){
        return new Font(size);
        //TODO Add fonts back!
        //return Font.loadFont(AppStyle.class.getResource("/fonts/" + fontType.font).toExternalForm(), size);
    }


    /**
     * @param paint color for the label
     * @param fontType font style to be applied
     * @param size the display size of the font
     * @param string text to be displayed
     * @return a label with the specified color, size, text and font type
     */
    public static Label getLabelWithColorAndFont(Paint paint, FontType fontType, int size, String string){
        Label label = new Label(string);
        label.setTextFill(paint);
        label.setFont(getFont(fontType, size));

        return label;
    }

    /**
     * Label with given text and default style for sub title placeholders.
     *
     * @param message text to be displayed
     * @return sub title label
     */
    public static Label getSubTitleLabel(String message){
        Label subTitleLabel = new Label(message);
        subTitleLabel.setTextFill(Color.web("#76747e"));
        subTitleLabel.setFont(AppStyle.getFont(FontType.DISPLAY_MEDIUM, 12));

        return subTitleLabel;
    }

    /**
     * Label with given text and default style for title placeholders.
     *
     * @param message text to be displayed
     * @return title label
     */
    public static Label getTitleLabel(String message){
        Label titleLabel = new Label(message);
        titleLabel.setTextFill(Color.web("#b7b7b8"));
        titleLabel.setFont(AppStyle.getFont(FontType.ROUNDED_BOLD, 14));

        return titleLabel;
    }


    /**
     * Creates stage with given title, icon and resizable option.
     *
     * @param popupTitle Popup title
     * @param iconPlace path for popup icon
     * @param resizable popup resizable configuration
     * @return popup stage
     */
    public static Stage setUpPopupStage(String popupTitle, String iconPlace, boolean resizable){
        Stage popupStage = new Stage();
        popupStage.setTitle(popupTitle);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setResizable(resizable);

        return popupStage;
    }

    /**
     * Creates popup with given title, icon, content and style.
     *
     * @param popupTitle Popup title
     * @param iconPlace path for popup icon
     * @param content content to be displayed in the popup
     * @param styleSheet pop stylesheet name
     * @return popup stage
     */
    public static Stage setUpPopup(String popupTitle, String iconPlace, HBox content, String styleSheet){
        Stage popupStage = setUpPopupStage(popupTitle, iconPlace, false);

        setUpPoupScene(content, popupStage, styleSheet);

        popupStage.show();

        return popupStage;
    }

    /**
     * Sets popup content into the respective stage and with the given style.
     *
     * @param content popup content to be displayed
     * @param popupStage popup stage
     * @param styleSheet pop stylesheet name
     */
    private static void setUpPoupScene(HBox content, Stage popupStage, String styleSheet) {

        VBox.setMargin(content, new Insets(20));
        content.setPadding(new Insets(10));

        Scene scene = new Scene(content);
        scene.getStylesheets().add(styleSheet);

        popupStage.setScene(scene);


    }

    /**
     * Adds a fading in animation and adds the node to the parent.
     *
     * @param node child where the animation is added
     * @param parent parent node where the child node with the animation is added
     * @param multiplier parametrization of the animation duration
     */
    public static void customFadingIn(final Node node, final VBox parent, int multiplier){
        final FadeTransition transition = new FadeTransition(Duration.millis(100*multiplier), node);
        transition.setFromValue(0);
        transition.setToValue(1);
        transition.setInterpolator(Interpolator.EASE_IN);
        parent.getChildren().add(node);
        transition.play();
    }

    /**
     * Adds a fading in animation for 250 milliseconds and adds the node to the parent.
     *
     * @param node child where the animation is added
     * @param parent parent node where the child node with the fade int animation is added
     */
    public static void addFadingIn(final Node node, final Pane parent) {
        final FadeTransition transition = new FadeTransition(Duration.millis(250), node);
        transition.setFromValue(0);
        transition.setToValue(1);
        transition.setInterpolator(Interpolator.EASE_IN);
        parent.getChildren().add(node);
        transition.play();
    }

    /**
     * Adds a fading out animation for 250 milliseconds and removes the node from the parent after it.
     *
     * @param node child where the animation is added
     * @param parent parent node where the child node with the fade out animation is removed
     */
    public static void removeFadingOut(final Node node, final Pane parent) {
        if (parent.getChildren().contains(node)) {
            final FadeTransition transition = new FadeTransition(Duration.millis(250), node);
            transition.setFromValue(node.getOpacity());
            transition.setToValue(0);
            transition.setInterpolator(Interpolator.EASE_BOTH);
            transition.setOnFinished(finishHim -> parent.getChildren().remove(node));
            transition.play();
        }
    }


    /**
     * Default drop shadow style with a 10 pixel radius and y-axis offset.
     *
     * @return default drop shadow style
     */
    public static DropShadow getDropShadow(){
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(10.0);
        dropShadow.setOffsetY(10.0);
        dropShadow.setColor(Color.color(0,0,0,0.10));

        return dropShadow;
    }

    public static void addFadingInGroup(double duration, double delayDuration, Node... nodes){
        double currentMoment = 0;

        for(Node node: nodes){
            node.setOpacity(0);

            FadeTransition fadeTransition = new FadeTransition(Duration.millis(duration), node);
            fadeTransition.setFromValue(0);
            fadeTransition.setToValue(1);
            fadeTransition.setDelay(Duration.millis(currentMoment));
            fadeTransition.play();

           currentMoment += delayDuration;

        }

    }

}

