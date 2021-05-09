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
    	
        MetricExtractor metricExtractor = new MetricExtractor(new File("/Users/miguelferreira/Downloads/jasml_0.10"));
        
        assertNotNull(metricExtractor.getFinalPath());
    }

    @Test
    void testExecuteExtraction() throws URISyntaxException, InterruptedException {
    	
    	URL url = RulesManager.class.getResource("/forTestPurposes/testFolder");
        File testFile = new File(url.getFile());
        System.out.println(testFile.getPath());
        
        
        
        MetricExtractor metricExtractor = new MetricExtractor(new File("/Users/miguelferreira/Downloads/jasml_0.10"));
        assertDoesNotThrow(() -> {
            metricExtractor.executeExtraction();
        });

    }

    @Test
    void testGetResults() throws InterruptedException {
    	URL url = RulesManager.class.getResource("/forTestPurposes/testFolder");
        File testFile = new File(url.getFile());
        System.out.println(testFile.getPath());
    	
        MetricExtractor metricExtractor = new MetricExtractor(new File("/Users/miguelferreira/Downloads/jasml_0.10"));
        assertEquals(metricExtractor.getResults().size(),0);
        metricExtractor.executeExtraction();
        
        assertEquals(metricExtractor.getResults().size(), 50);
    }
}