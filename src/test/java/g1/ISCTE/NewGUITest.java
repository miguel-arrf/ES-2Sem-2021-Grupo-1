package g1.ISCTE;

import com.sun.javafx.application.PlatformImpl;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class NewGUITest {

	private static JFXPanel panel = new JFXPanel();



	@Test
	void testBlurBackground() {
		Node pane = new VBox();
		
		NewGUI.blurBackground(0.0, 20.0, 0.0, pane);
			
		assertNotNull(pane.getEffect());
			
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
