package SmellDetectionQualityEvaluation;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class QualityEvaluationTest {

    private static QualityEvaluation qualityEvaluation;

    @BeforeAll
    static void setUp() {
        qualityEvaluation = new QualityEvaluation(new ConfusionMatrix(), new ArrayList<>());
    }

    @Test
    void getConfusionMatrix() {
        assertNotNull(qualityEvaluation.getConfusionMatrix());
    }

    @Test
    void getConsoleOutputs() {
        assertNotNull(qualityEvaluation.getConsoleOutputs());
    }
}