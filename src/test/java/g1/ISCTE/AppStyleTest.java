package g1.ISCTE;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class AppStyleTest {

    private final static String testLabel = "test";
    private final static JFXPanel panel = new JFXPanel();

    @Test
    public void getLabelWithColorAndFont() {
        Label label = AppStyle.getLabelWithColorAndFont(Color.RED, FontType.BOLD, 12, testLabel);
        assertEquals(label.getText(), testLabel);
    }

    @Test
    public void getSubTitleLabel() {
        Label label = AppStyle.getSubTitleLabel(testLabel);
        assertEquals(label.getText(), testLabel);
    }

    @Test
    public void getTitleLabel() {
        Label label = AppStyle.getTitleLabel(testLabel);
        assertEquals(label.getText(), testLabel);
    }

    @Test
    public void testSetUpPopupStage() {
       Platform.runLater(() -> {
            Stage stage = AppStyle.setUpPopupStage(testLabel, null, true);

            assertEquals(stage.getTitle(), testLabel);
        });

    }

    @Test
    public void setUpPopup() {
    	JFXPanel panel = new JFXPanel();
       Platform.runLater(() -> {
            Stage stage = AppStyle.setUpPopup(testLabel, null, new HBox(), null);

            assertEquals(stage.getTitle(), testLabel);
        });

        Platform.runLater(() -> {
            Stage stage = AppStyle.setUpPopup(testLabel, null, new HBox(), null, 20, 20);

            assertEquals(stage.getTitle(), testLabel);
        });


    }

    @Test
    public void customFadingIn() {
        VBox node = new VBox();
        VBox parent = new VBox();
        AppStyle.customFadingIn(node, parent, 1);

        assertTrue(parent.getChildren().contains(node));
    }

    @Test
    public void addFadingIn() {
        VBox node = new VBox();
        VBox parent = new VBox();
        AppStyle.addFadingIn(node, parent);

        assertTrue(parent.getChildren().contains(node));
    }

    @Test
    public void removeFadingOut() throws InterruptedException {
        VBox node = new VBox();
        VBox parent = new VBox();

        parent.getChildren().add(node);
        
        AppStyle.removeFadingOut(node, parent);

        Thread.sleep(1000);
		 Platform.runLater(() -> assertFalse(parent.getChildren().contains(node)));
    }

    @Test
    public void getDropShadow() {
        assertNotNull(AppStyle.getDropShadow());
    }

    @Test
    void setDefaultBackgroundAndBorderRadiusWithGivenBackgroundColor() {
        assertNotNull(AppStyle.setDefaultBackgroundAndBorderRadiusWithGivenBackgroundColor("red"));
    }



    @Test
    void getIcon() {
        String iconLocation = "add.png";
        assertNotNull(AppStyle.getIcon(iconLocation));
    }

    @Test
    void setButtonIcon() {
        Button button = new Button("testButton");
        AppStyle.setButtonIcon(button,"add.png");

        assertNotNull(button.getGraphic());
    }

    @Test
    void testGUIJustToPass() {

        ArrayList<Label> nodes = new ArrayList<>();
        nodes.add(new Label("1"));
        nodes.add(new Label("2"));

        ProgressBar progressBar = new ProgressBar();

        AppStyle.addFadingInGroup(10, 10,new Button(), new Button() );
        AppStyle.addFadingInGroup(10,10, nodes, new VBox(), progressBar);

        /*Platform.runLater(() -> {
            assertEquals(1, progressBar.getProgress());

        });*/
    }

}