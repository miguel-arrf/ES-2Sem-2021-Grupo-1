package SmellDetectionQualityEvaluation;

import RuleEditor.RulesManager;
import g1.ISCTE.MyTree;
import javafx.embed.swing.JFXPanel;
import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import org.junit.jupiter.api.Test;

import MetricExtraction.MetricExtractor;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;

class QualityEvaluatorAppTest {

	
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
    void initializeMainPane() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InterruptedException, URISyntaxException, NoSuchMethodException, InvocationTargetException {
        JFXPanel jfxPanel = new JFXPanel();


        QualityEvaluatorApp qEA = new QualityEvaluatorApp();
        
        RulesManager rulesManager = initialize();
		
        URL url = RulesManager.class.getResource("/forTestPurposes/testFolder");
        File testFile = new File(url.getFile());
        System.out.println(testFile.getPath());
        
        File projectFile = new File("C:\\Users\\mferr\\Downloads\\ES-2Sem-2021-Grupo-1-e1e0d12b5bc8ee1df4610651d3a5af1c2d356e95\\jasml_0.10");

        MetricExtractor metricExtractor = new MetricExtractor(projectFile, testFile.getPath());
        //metricExtractor.executeExtraction();
        
        rulesManager.setMetricExtractor(metricExtractor);
        
        qEA.initializeMainPane(rulesManager, projectFile);

        
        Field field = QualityEvaluatorApp.class.getDeclaredField("detectionButton");
        field.setAccessible(true);
        
        Button button = (Button) field.get(qEA);
        assertNotNull(button);
        
        Thread.sleep(100);
        
       button.fire();
        
       testOtherBranch();
    }
    
    void testOtherBranch() throws InterruptedException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, NoSuchMethodException, InvocationTargetException {
    	 JFXPanel jfxPanel = new JFXPanel();

         File selectedFile = new File("C:\\Users\\mferr\\Downloads\\ES-2Sem-2021-Grupo-1-e1e0d12b5bc8ee1df4610651d3a5af1c2d356e95\\jasml_0.10");

         QualityEvaluatorApp qEA = new QualityEvaluatorApp();
         
         RulesManager rulesManager = new RulesManager();
 		
         MetricExtractor metricExtractor = new MetricExtractor(selectedFile, "test");
         //metricExtractor.executeExtraction();
         
         rulesManager.setMetricExtractor(metricExtractor);
         
         qEA.initializeMainPane(rulesManager,selectedFile);

         
         Field field = QualityEvaluatorApp.class.getDeclaredField("detectionButton");
         field.setAccessible(true);
         
         Button button = (Button) field.get(qEA);
         assertNotNull(button);
         
         Thread.sleep(100);
         
        button.fire();
    }
    
    
    @Test
    void testGetStackPane() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        JFXPanel jfxPanel = new JFXPanel();

    	RulesManager rulesManager = new RulesManager();
    	
        File selectedFile = new File("C:\\Users\\mferr\\Downloads\\ES-2Sem-2021-Grupo-1-e1e0d12b5bc8ee1df4610651d3a5af1c2d356e95\\jasml_0.10");

    	QualityEvaluatorApp qualityEvaluatorApp = new QualityEvaluatorApp();
        qualityEvaluatorApp.initializeMainPane(rulesManager, selectedFile);
        
        Method method = QualityEvaluatorApp.class.getDeclaredMethod("getStackPane", VBox.class);
        method.setAccessible(true);

        StackPane stackPane = (StackPane) method.invoke(qualityEvaluatorApp, new VBox());
        assertNotNull(stackPane);
    	
    }
    
}