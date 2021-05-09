package MetricExtraction;

import g1.ISCTE.MyTree;
import org.junit.jupiter.api.Test;

import RuleEditor.RulesManager;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class MetricExtractorTest {

    @Test
    void testGetFinalPath() {
    	 URL url = RulesManager.class.getResource("/forTestPurposes/testFolder");
         File testFile = new File(url.getFile());
         System.out.println(testFile.getPath());
    	
        MetricExtractor metricExtractor = new MetricExtractor(new File(System.getProperty("user.dir") + "\\jasml_0.10"));
        String resultado = testFile.getPath() + "/jasml_0.10_metrics.xlsx";
        
        assertEquals(resultado, metricExtractor.getFinalPath());
    }

    @Test
    void testExecuteExtraction() throws URISyntaxException, InterruptedException {
    	
    	URL url = RulesManager.class.getResource("/forTestPurposes/testFolder");
        File testFile = new File(url.getFile());
        System.out.println(testFile.getPath());
        
        
        
        MetricExtractor metricExtractor = new MetricExtractor(new File(System.getProperty("user.dir") + "\\jasml_0.10"));
        assertDoesNotThrow(() -> {
            metricExtractor.executeExtraction();
        });
//        URL sqlScriptUrl1 = MetricExtractor.class.getClassLoader().getResource("/forTestPurposes/testFolder/somethingHere");
//        File file = new File(sqlScriptUrl1.toURI());
//
//        MetricExtractor metricExtractor2 = new MetricExtractor(file, "test");
//        metricExtractor2.executeExtraction();
    }

    @Test
    void testGetResults() throws InterruptedException {
    	URL url = RulesManager.class.getResource("/forTestPurposes/testFolder");
        File testFile = new File(url.getFile());
        System.out.println(testFile.getPath());
    	
        MetricExtractor metricExtractor = new MetricExtractor(new File(System.getProperty("user.dir") + "\\jasml_0.10"));
        assertEquals(metricExtractor.getResults().size(),0);
        metricExtractor.executeExtraction();
        
        assertEquals(metricExtractor.getResults().size(), 50);
    }
}