package RuleEditor;

import static org.junit.jupiter.api.Assertions.*;
import MetricExtraction.MetricExtractor;
import g1.ISCTE.MyTree;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Label;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;

class RulesManagerTest {


	@BeforeAll
	public static void testOpenAddRuleEditor() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InterruptedException {
		JFXPanel panel = new JFXPanel();
		RulesManager rulesManager = initialize();
		
		
		Method privatesetUpGUI = RulesManager.class.getDeclaredMethod("openAddRuleEditor", boolean.class);
		privatesetUpGUI.setAccessible(true);
		

        Thread.sleep(1000);
		 Platform.runLater(() -> {
			try {
				privatesetUpGUI.invoke(rulesManager, false);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
	}

	@Test
	void testSetMetricExtractor() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, URISyntaxException {
		JFXPanel jfxPanel = new JFXPanel();

		RulesManager rulesManager = new RulesManager();
		
		URL sqlScriptUrl = MyTree.class.getClassLoader().getResource("forTestPurposes");
		MetricExtractor metricExtractor = new MetricExtractor(new File(sqlScriptUrl.toURI()));
		
		
		rulesManager.setMetricExtractor(metricExtractor);
		
		Field field = RulesManager.class.getDeclaredField("metricExtractor");
		field.setAccessible(true);
		
		MetricExtractor metricExtractorInRulesManager = (MetricExtractor) field.get(rulesManager);
		assertEquals(metricExtractorInRulesManager, metricExtractor);
	}

	@Test
	void testGetRules() {
		RulesManager rulesManager = new RulesManager();
		
		assertEquals(rulesManager.getRules().size(), 0);
	}


	@Test
	void testLoadFile() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		JFXPanel panel = new JFXPanel();
		
		RulesManager rulesManager = initialize();
        
        assertEquals(rulesManager.getRules().size(), 2);
		
	}
	
	private static RulesManager initialize() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		JFXPanel panel = new JFXPanel();
		
		RulesManager rulesManager = new RulesManager();
		Method privatesetUpGUI = RulesManager.class.getDeclaredMethod("setUpGUI", null);
		privatesetUpGUI.setAccessible(true);
		
		privatesetUpGUI.invoke(rulesManager, null);
		
    	URL url = RulesManager.class.getResource("/testRule.rule");
        File testFile = new File(url.getFile());
        
        rulesManager.setFile(testFile);
        
        rulesManager.loadFile();
        
        return rulesManager;
	}
	
	@Test
	void testRulesSizedZero() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		RulesManager rulesManager = initialize();
		
		URL url = RulesManager.class.getResource("/testRuleEmpty.rule");
        File testFile = new File(url.getFile());
        
        rulesManager.setFile(testFile);
        
        rulesManager.loadFile();
        
        assertEquals(rulesManager.getRules().size(), 0);
        
	}
	
	
	@Test
	void testGetRenameTextField() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		RulesManager rulesManager = initialize();
		
		Class params[] = new Class[2];
		params[0] = Label.class;
		params[1] = JSONObject.class;
		
		Method privatesetUpGUI = RulesManager.class.getDeclaredMethod("getRenameTextField", params);
		privatesetUpGUI.setAccessible(true);
		
		JSONObject geral = new JSONObject();
		
		JSONObject json = new JSONObject();
		geral.put("outerName", json);
		
		json.put("innerName", "testName");
		
		privatesetUpGUI.invoke(rulesManager, new Label(), geral);
		
	}

	


	@Test
	void testGetResults() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		JFXPanel panel = new JFXPanel();
		
		RulesManager rulesManager = initialize();
		
		assertNotNull(rulesManager.getResults());
        
	}

	@Test
	void testCreateCodeSmells() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, URISyntaxException {
		JFXPanel panel = new JFXPanel();
		RulesManager rulesManager = initialize();
		
		URL sqlScriptUrl = MyTree.class.getClassLoader().getResource("forTestPurposes");
		MetricExtractor metricExtractor = new MetricExtractor(new File(sqlScriptUrl.toURI()));
		rulesManager.setMetricExtractor(metricExtractor);
		
		assertEquals(rulesManager.createCodeSmells().size(), 2);
	}


	@Test
	void testGetRulesFile() {
		RulesManager rulesManager = new RulesManager();
		
    	URL url = RulesManager.class.getResource("/testRule.rule");
        File testFile = new File(url.getFile());
        rulesManager.setFile(testFile);
        
        assertNotNull(rulesManager.getRulesFile());
        
	}
	

	

	
}









