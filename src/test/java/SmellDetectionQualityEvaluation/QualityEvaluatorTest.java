package SmellDetectionQualityEvaluation;

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