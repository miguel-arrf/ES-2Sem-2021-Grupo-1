package CodeSmellDetection;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;

import org.junit.jupiter.api.Test;

import MetricExtraction.MetricExtractor;
import RuleEditor.RulesManager;
import javafx.embed.swing.JFXPanel;

class RuleApplierTest {

	@Test
	void testRuleApplier() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
		RulesManager rulesManager = initialize();
		
		URL url = RuleApplierTest.class.getResource("/forTestPurposes/test.xlsx");
        File testFile = new File(url.getFile());

        String path = testFile.getPath();
        
        assertDoesNotThrow(() -> {
    		RuleApplier ruleApplier = new RuleApplier(rulesManager.getResults(),path);

        });
		
	}
	
	
	private RulesManager initialize() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		JFXPanel panel = new JFXPanel();
		
		RulesManager rulesManager = new RulesManager();
		Method privatesetUpGUI = RulesManager.class.getDeclaredMethod("setUpGUI", null);
		privatesetUpGUI.setAccessible(true);
		
		privatesetUpGUI.invoke(rulesManager, null);
		
    	URL url = RulesManager.class.getResource("/testRulesFinal.rule");
        File testFile = new File(url.getFile());
        
        rulesManager.setFile(testFile);
        
        rulesManager.loadFile();
        
        return rulesManager;
	}

	@Test
	void testProcessRules() throws IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InterruptedException {
		RulesManager rulesManager = initialize();

        String directory_src = System.getProperty("user.dir") + "\\jasml_0.10";

		MetricExtractor metricExtractor = new MetricExtractor(new File(directory_src), "src/main/Created_Excels");
		metricExtractor.executeExtraction();
		
		String docPath = metricExtractor.getFinalPath();
		
		URL url = RuleApplierTest.class.getResource("/forTestPurposes/jasml_0.10_metrics.xlsx");
        File testFile = new File(url.getFile());

        String path = testFile.getPath();

        rulesManager.setMetricExtractor(metricExtractor);
		rulesManager.createCodeSmells();

    	RuleApplier ruleApplier = new RuleApplier(rulesManager.getResults(),docPath);

    	ruleApplier.processRules();
    	
    	assertNotNull(ruleApplier);
        
        
	}

}
