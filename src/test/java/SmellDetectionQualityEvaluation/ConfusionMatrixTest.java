package SmellDetectionQualityEvaluation;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfusionMatrixTest {

    private static ConfusionMatrix confusionMatrix;

    @BeforeAll
    static void setUp() {
        confusionMatrix = new ConfusionMatrix();
    }

    @Test
    void getTruePositives() {
        assertTrue(confusionMatrix.getTruePositives() == 0 || confusionMatrix.getTruePositives() == 1);
    }

    @Test
    void getFalsePositives() {
        assertTrue(confusionMatrix.getFalsePositives() == 0 || confusionMatrix.getFalsePositives() == 1);
    }

    @Test
    void getTrueNegatives() {
        assertTrue(confusionMatrix.getTrueNegatives() == 0 || confusionMatrix.getTrueNegatives() == 1);
    }

    @Test
    void getFalseNegatives() {
        assertTrue(confusionMatrix.getFalseNegatives() == 0 || confusionMatrix.getFalseNegatives() == 1);
    }

    @Test
    void incrementTruePositives() {
        confusionMatrix.incrementTruePositives();
        assertEquals(confusionMatrix.getTruePositives(), 1);
    }

    @Test
    void incrementFalsePositives() {
        confusionMatrix.incrementFalsePositives();
        assertEquals(confusionMatrix.getFalsePositives(), 1);
    }

    @Test
    void incrementTrueNegatives() {
        confusionMatrix.incrementTrueNegatives();
        assertEquals(confusionMatrix.getTrueNegatives(), 1);
    }

    @Test
    void incrementFalseNegatives() {
        confusionMatrix.incrementFalseNegatives();
        assertEquals(confusionMatrix.getFalseNegatives(), 1);
    }
}