package smell_detection_quality_evaluation;

/**
 * Abstract data structure to represent the confusion matrix resultant of the quality assessment made from the detection of code smells
 */
public class ConfusionMatrix {

    private int truePositives;
    private int falsePositives;
    private int trueNegatives;
    private int falseNegatives;

    /**
     * Constructs an instance of ConfusionMatrix (all values initialized at 0)
     */
    public ConfusionMatrix() {
        this.truePositives = 0;
        this.falsePositives = 0;
        this.trueNegatives = 0;
        this.falseNegatives = 0;
    }

    /**
     * Gets the total number of true positive occurrences
     * @return The number of true positives as int
     */
    public int getTruePositives() {
        return truePositives;
    }

    /**
     * Gets the total number of false positive occurrences
     * @return The number of false positives as int
     */
    public int getFalsePositives() {
        return falsePositives;
    }

    /**
     * Gets the total number of true negative occurrences
     * @return The number of true negatives as int
     */
    public int getTrueNegatives() {
        return trueNegatives;
    }

    /**
     * Gets the total number of false negative occurrences
     * @return The number of false negatives as int
     */
    public int getFalseNegatives() {
        return falseNegatives;
    }

    /**
     * Increments the total number of true positive occurrences by 1
     */
    public void incrementTruePositives() {
        truePositives++;
    }

    /**
     * Increments the total number of false positive occurrences by 1
     */
    public void incrementFalsePositives() {
        falsePositives++;
    }

    /**
     * Increments the total number of true negative occurrences by 1
     */
    public void incrementTrueNegatives() {
        trueNegatives++;
    }

    /**
     * Increments the total number of false negative occurrences by 1
     */
    public void incrementFalseNegatives() {
        falseNegatives++;
    }

}
