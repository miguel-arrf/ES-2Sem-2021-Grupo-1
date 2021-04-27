package smell_detection_quality_evaluation;

import javafx.application.Application;
import javafx.stage.Stage;

public class Testing extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        QualityEvaluator evaluator = new QualityEvaluator();
        evaluator.run();
        ConfusionMatrix matrix = evaluator.getEvaluation().getConfusionMatrix();
        System.out.println("True positives : " + matrix.getTruePositives());
        System.out.println("False positives : " + matrix.getFalsePositives());
        System.out.println("True negatives : " + matrix.getTrueNegatives());
        System.out.println("False negatives : " + matrix.getFalseNegatives());
        /*for(String s : evaluator.getEvaluation().getConsoleOutputs()) {
            if(s.contains("False Positive")) System.out.println(s);
        }*/
    }
}
