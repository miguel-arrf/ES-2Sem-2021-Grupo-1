package smell_detection_quality_evaluation;

public class ConfusionMatrix {

    private int truePositives;
    private int falsePositives;
    private int trueNegatives;
    private int falseNegatives;

    public ConfusionMatrix() {
        this.truePositives = 0;
        this.falsePositives = 0;
        this.trueNegatives = 0;
        this.falseNegatives = 0;
    }

    public int getTruePositives() {
        return truePositives;
    }

    public int getFalsePositives() {
        return falsePositives;
    }

    public int getTrueNegatives() {
        return trueNegatives;
    }

    public int getFalseNegatives() {
        return falseNegatives;
    }

    public void incrementTruePositives() {
        truePositives++;
    }

    public void incrementFalsePositives() {
        falsePositives++;
    }

    public void incrementTrueNegatives() {
        trueNegatives++;
    }

    public void incrementFalseNegatives() {
        falseNegatives++;
    }

}
