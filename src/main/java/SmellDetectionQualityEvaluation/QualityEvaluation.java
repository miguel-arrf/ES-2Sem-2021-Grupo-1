package SmellDetectionQualityEvaluation;

import java.util.ArrayList;

/**
 * Abstract data structure to represent the quality assessment made from the detection of code smells, composed by the confusion matrix for visual/graphical representation
 * of the results, and by a list of pre-formatted lines of text for textual representation of the results
 */
public class QualityEvaluation {

    private final ConfusionMatrix confusionMatrix;
    private final ArrayList<String> consoleOutputs;

    /**
     * Constructs an instance of QualityEvaluation, given the data structures returned by the execution of the quality assessment as arguments
     * @param confusionMatrix The confusion matrix resultant from the quality assessment
     * @param consoleOutputs The list of pre-formatted lines of text resultant from the quality assessment
     */
    public QualityEvaluation(ConfusionMatrix confusionMatrix, ArrayList<String> consoleOutputs) {
        this.confusionMatrix = confusionMatrix;
        this.consoleOutputs = consoleOutputs;
    }

    /**
     * Gets the confusion matrix resultant of the evaluation
     * @return A ConfusionMatrix instance
     */
    public ConfusionMatrix getConfusionMatrix() {
        return confusionMatrix;
    }

    /**
     * Gets the pre-formatted lines of text resultant of the evaluation
     * @return An ArrayList of the pre-formatted lines of text as String
     */
    public ArrayList<String> getConsoleOutputs() {
        return consoleOutputs;
    }
}
