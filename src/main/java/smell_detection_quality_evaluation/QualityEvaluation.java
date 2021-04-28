package smell_detection_quality_evaluation;

import java.util.ArrayList;

public class QualityEvaluation {

    private final ConfusionMatrix confusionMatrix;
    private final ArrayList<String> consoleOutputs;

    public QualityEvaluation(ConfusionMatrix confusionMatrix, ArrayList<String> consoleOutputs) {
        this.confusionMatrix = confusionMatrix;
        this.consoleOutputs = consoleOutputs;
    }

    public ConfusionMatrix getConfusionMatrix() {
        return confusionMatrix;
    }

    public ArrayList<String> getConsoleOutputs() {
        return consoleOutputs;
    }
}
