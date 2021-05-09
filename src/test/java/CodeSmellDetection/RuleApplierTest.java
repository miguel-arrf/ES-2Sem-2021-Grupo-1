package CodeSmellDetection;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;

import org.apache.poi.xssf.usermodel.XSSFSheet;
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
	void testProcessRules() throws IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InterruptedException, NoSuchFieldException {
		RulesManager rulesManager = initialize();
		
        File directory_src = new File("/Users/miguelferreira/Downloads/jasml_0.10/");



		MetricExtractor metricExtractor = new MetricExtractor(directory_src);
		metricExtractor.executeExtraction();
		
		String docPath = metricExtractor.getFinalPath();
		
		URL url = RuleApplierTest.class.getResource("/forTestPurposes/jasml_0.10_metrics.xlsx");
        File testFile = new File(url.getFile());

        String path = testFile.getPath();
        
        URL url2 = RuleApplierTest.class.getResource("/testRulesFinal.rule");
        File testFile2 = new File(url2.getFile());
        rulesManager.setFile(testFile2);

        rulesManager.setMetricExtractor(metricExtractor);
		rulesManager.createCodeSmells();
	
    	RuleApplier ruleApplier = new RuleApplier(rulesManager.getResults(), docPath);
    	    	
    	Field f = RuleApplier.class.getDeclaredField("mySheet"); //NoSuchFieldException
    	f.setAccessible(true);
    	XSSFSheet mySheet = (XSSFSheet) f.get(ruleApplier); //IllegalAccessException
    	
    	int columnNumBefore = mySheet.getRow(0).getLastCellNum();

    	ruleApplier.processRules();
    	
    	int columnNumAfter = mySheet.getRow(0).getLastCellNum();
    	
    	assertNotEquals(columnNumBefore, columnNumAfter);
        
	}

}
