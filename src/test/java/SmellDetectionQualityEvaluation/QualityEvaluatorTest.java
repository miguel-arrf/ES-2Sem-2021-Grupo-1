package SmellDetectionQualityEvaluation;

import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QualityEvaluatorTest {

    private static QualityEvaluator qualityEvaluator;

    @BeforeAll
    static void setUp() {
        qualityEvaluator = new QualityEvaluator();
    }

    @Test
    void run() {
        JFXPanel jfxPanel = new JFXPanel();
        qualityEvaluator.run();
        assertNotNull(qualityEvaluator.getEvaluation());
    }

    @Test
    void getEvaluation() {
        assertNotNull(qualityEvaluator.getEvaluation());
    }

    @Test
    void getDefaultProject() {
        assertNotNull(QualityEvaluator.getDefaultProject());
    }

}