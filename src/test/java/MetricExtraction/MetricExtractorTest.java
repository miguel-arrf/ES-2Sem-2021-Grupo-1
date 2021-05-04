package MetricExtraction;

import g1.ISCTE.MyTree;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class MetricExtractorTest {

    @Test
    void testGetFinalPath() {
        MetricExtractor metricExtractor = new MetricExtractor(new File(System.getProperty("user.dir") + "\\jasml_0.10"), "test");
        String resultado = "test/jasml_0.10_metrics.xlsx";
        assertEquals(resultado, metricExtractor.getFinalPath());
    }

    @Test
    void testExecuteExtraction() throws URISyntaxException, InterruptedException {
        MetricExtractor metricExtractor = new MetricExtractor(new File(System.getProperty("user.dir") + "\\jasml_0.10"), "test");
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
        MetricExtractor metricExtractor = new MetricExtractor(new File(System.getProperty("user.dir") + "\\jasml_0.10"), "test");
        assertEquals(metricExtractor.getResults().size(),0);
        metricExtractor.executeExtraction();
        assertTrue(metricExtractor.getResults().size() > 0);
    }
}