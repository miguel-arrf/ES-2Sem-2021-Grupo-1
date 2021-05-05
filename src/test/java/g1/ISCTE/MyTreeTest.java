package g1.ISCTE;


import javafx.embed.swing.JFXPanel;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

class MyTreeTest {
	


	@Test
	void testGetScrollPane() throws URISyntaxException {
		JFXPanel panel = new JFXPanel();

		URL sqlScriptUrl = MyTree.class.getClassLoader().getResource("forTestPurposes");
		
		MyTree myTree = new MyTree();
				
		ScrollPane scrollPane = myTree.getScrollPane(new File(sqlScriptUrl.toURI()));
		
		VBox scrollPaneContent = (VBox) scrollPane.getContent();
		
		VBox firstItem = (VBox)  scrollPaneContent.getChildren().get(1);
	
		assertEquals(firstItem.getChildren().size(),5);
	}

}
