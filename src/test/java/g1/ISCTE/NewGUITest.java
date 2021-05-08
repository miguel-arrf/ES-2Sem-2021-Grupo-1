package g1.ISCTE;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import SmellDetectionQualityEvaluation.QualityEvaluatorApp;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

class NewGUITest {

	private static JFXPanel panel = new JFXPanel();



	@Test
	void testBlurBackground() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Node pane = new VBox();
		
		NewGUI.blurBackground(0.0, 20.0, 0.0, pane);
			
		assertNotNull(pane.getEffect());
			
		getCoverage();
		
	}
	
	private void getCoverage() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		JFXPanel panel = new JFXPanel();
		
		NewGUI ng = new NewGUI();
        
        Method method = NewGUI.class.getDeclaredMethod("getButtonsLeft", null);
        method.setAccessible(true);
        VBox getButtonsLeft = (VBox) method.invoke(ng, null);
        assertNotNull(getButtonsLeft);
		
        
        Method methodgetSpacer = NewGUI.class.getDeclaredMethod("getSpacer", null);
        methodgetSpacer.setAccessible(true);
        Pane spacer = (Pane) methodgetSpacer.invoke(ng, null);
        assertNotNull(spacer);
        
        
        Method methodGetProcessProjectButton = NewGUI.class.getDeclaredMethod("getProcessProjectButton", null);
        methodGetProcessProjectButton.setAccessible(true);
        Button buttonProcessProject = (Button) methodGetProcessProjectButton.invoke(ng, null);
        assertNotNull(buttonProcessProject);
        
        
        Method methodGetSelectFolderButton = NewGUI.class.getDeclaredMethod("getSelectFolderButton", Button.class);
        methodGetSelectFolderButton.setAccessible(true);
        Button selectFolderButton = (Button) methodGetSelectFolderButton.invoke(ng, buttonProcessProject);
        assertNotNull(selectFolderButton);
        
        
        Method methodGetDefaultProjectButton = NewGUI.class.getDeclaredMethod("getDefaultProjectButton", Button.class);
        methodGetDefaultProjectButton.setAccessible(true);
        Button getDefaultProjectButton = (Button) methodGetDefaultProjectButton.invoke(ng, buttonProcessProject);
        assertNotNull(getDefaultProjectButton);
        
	}

	@Test
	void testStartStage() throws InterruptedException {

		Thread thread = new Thread(() -> {
			 new JFXPanel(); // Initializes the JavaFx Platform
				Platform.runLater(() -> {
					try {
						new NewGUI().start(new Stage());
					} catch (IOException e) {
						fail("Should not fail!");
					}
				});

		});
		 thread.start();// Initialize the thread
	        Thread.sleep(100); // Time to use the app, with out this, the thread
	                                // will be killed before you can tell.
		
		
	}

}
