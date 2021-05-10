package g1.ISCTE;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import MetricExtraction.MetricExtractor;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

class NewGUITest {

	private static JFXPanel panel = new JFXPanel();



	@Test
	void testBlurBackground() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		Node pane = new VBox();
		
		NewGUI.blurBackground(0.0, 20.0, 0.0, pane);
			
		assertNotNull(pane.getEffect());
			
		getCoverage();
		
	}
	
	private void getCoverage() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		
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
        
        
        Method methodGetProcessProjectButton = NewGUI.class.getDeclaredMethod("getShowConfusionMatrixButton", null);
        methodGetProcessProjectButton.setAccessible(true);
        Button buttonProcessProject = (Button) methodGetProcessProjectButton.invoke(ng, null);
        assertNotNull(buttonProcessProject);
        
        
        File projectFile = new File("C:\\Users\\mferr\\Downloads\\jasml_0.10\\");
        
        Field f = NewGUI.class.getDeclaredField("selectedFile"); //NoSuchFieldException
        f.setAccessible(true);
        f.set(ng, projectFile);
        
        
        Method updateFilePane = NewGUI.class.getDeclaredMethod("updateFilePane", null);
        updateFilePane.setAccessible(true);
        Button voidUpdateFilePane = (Button) updateFilePane.invoke(ng, null);        
        assertNotNull(voidUpdateFilePane);
        
	}
	
	@Test
	void testUpdateCenterPane() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		NewGUI ng = new NewGUI();
		
		File projectFile = new File("C:\\Users\\mferr\\Downloads\\jasml_0.10\\");
        
        Field f = NewGUI.class.getDeclaredField("selectedFile"); //NoSuchFieldException
        f.setAccessible(true);
        f.set(ng, projectFile);
        
        
        MetricExtractor metricExtractor = new MetricExtractor(projectFile);

        Field docPath = NewGUI.class.getDeclaredField("docPath");
        docPath.setAccessible(true);
       
        
        try {
            metricExtractor.executeExtraction();
            docPath.set(ng,  metricExtractor.getFinalPath());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        Method updateCenterPane = NewGUI.class.getDeclaredMethod("updateCenterPane", null);
        updateCenterPane.setAccessible(true);
        updateCenterPane.invoke(ng, null); 
        
        
        assertDoesNotThrow(() -> updateCenterPane.invoke(ng,  null));
        
		
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
