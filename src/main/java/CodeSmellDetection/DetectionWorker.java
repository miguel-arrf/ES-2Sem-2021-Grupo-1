package CodeSmellDetection;

import RuleEditor.ConditionBlock;
import MetricExtraction.ClassMetrics;
import MetricExtraction.Method;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Worker thread to the behavior of CodeSmellDetector. Given a code smell and the code metrics extracted from source code, detects if the code smell is present or not in the code, by evaluating the truth value of the code smell's rule using the code's metrics as parameters to the rule evaluation. The result is a list of
 * instances of String, corresponding to the names of the classes/methods where the code smell was detected, as well as a list of the same nature with the names of those where it wasn't detected
 */
public class DetectionWorker implements Runnable {

    private final CodeSmell codeSmell;
    private final ArrayList<ClassMetrics> metrics;
    private final ArrayList<String> results = new ArrayList<>();
    private final ArrayList<String> undetectedSmells = new ArrayList<>();

    /**
     * Constructs an instance of DetectionWorker
     * @param codeSmell The code smell to be detected
     * @param metrics The metrics extracted from the source code
     */
    public DetectionWorker(CodeSmell codeSmell, ArrayList<ClassMetrics> metrics) {
        this.codeSmell = codeSmell;
        this.metrics = metrics;
    }

    /**
     * Gets the code smell assigned to this worker for detection
     * @return The code smell assigned to this worker
     */
    public CodeSmell getCodeSmell() {
        return codeSmell;
    }

    /**
     * Gets the list of the classes/methods where the code smell was not detected
     * @return A list of the classes/methods where the code smell was not detected
     */
    public ArrayList<String> getUndetectedSmells() {
        return undetectedSmells;
    }

    /**
     * Runs the detection and generates the results
     */
    @Override
    public void run() {
        if(codeSmell.isClassSmell()) {
            for(ClassMetrics classMetrics : metrics) {
                detect(classMetrics);
            }
        } else {
            ArrayList<Method> methods = extractMethods();
            for(Method method : methods) {
                detect(method);
            }
        }
    }

    /**
     * Detects the presence of the code smell on a class or method
     * @param values A class or method and it's metrics
     */
    private void detect(Object values) {
        HashMap<String, Integer> metrics = getMetricsMap(values);
        boolean result = evaluateNode(metrics, codeSmell.getRule());
        ArrayList<String> listToAddTo;
        if(result) listToAddTo = results; else listToAddTo = undetectedSmells;
        if(values instanceof ClassMetrics) listToAddTo.add( ((ClassMetrics)values).getClass_name() );
        else if (values instanceof Method) listToAddTo.add( ((Method)values).getMethod_name() + "/" + ((Method)values).getClass_name());
    }

    /**
     * Evaluates the boolean value of the logical operation between two boolean parameters
     * @param operator The logic operator (AND/OR) used between both parameters
     * @param left_result Left parameter
     * @param right_result Right parameter
     * @return The boolean value of the junction of the left and right parameter by use of the logic operator
     */
    private boolean evaluateOperator(RuleOperator operator, boolean left_result, boolean right_result) {
        if(operator.equals(RuleOperator.AND)) return left_result && right_result;
        else if(operator.equals(RuleOperator.OR)) return left_result || right_result;
        else return false;
    }

    /**
     * Evaluates a node with the code's metrics as parameters to determine the presence or absence of the code smell
     * @param metrics A HashMap mapping the metrics' names to their numeric values
     * @param node The node to be evaluated
     * @return The boolean value of the rule's node evaluation
     */
    private boolean evaluateNode(HashMap<String, Integer> metrics, RuleNode node) {
        boolean result;
        if(node.isLeafNode()) {//Nó representa uma condição
            ConditionBlock condition = (ConditionBlock)node.getElement();
            result = evaluateCondition(condition, metrics);
        } else {//Avaliar o próprio nó, o nó esquerdo e o nó direito
            RuleOperator operator = node.getElement().getOperator();
            boolean left_result = evaluateNode(metrics, node.getLeft_node());
            boolean right_result = evaluateNode(metrics, node.getRight_node());
            result = evaluateOperator(operator, left_result, right_result);
        }
        return result;
    }

    /**
     * Evaluates the boolean value of a condition, given the required metrics numeric value as a parameter to compare with the other numeric value defined in the rule's condition
     * @param condition A condition, which compares two numeric values by means of an operator
     * @param metrics A HashMap mapping the metrics' names to their numeric values
     * @return The boolean value of the condition's evaluation
     */
    private boolean evaluateCondition(ConditionBlock condition, HashMap<String, Integer> metrics) {
        int value = metrics.get(condition.getRule());
        return condition.evaluate(value);
    }

    /**
     * Gets a HashMap mapping a class/method's metrics numeric values to their names, for use in the evaluation of the rule
     * @param values A class or method
     * @return The class/method's metrics numeric values mapped to their names, to be used in the evaluation of the rule
     */
    private HashMap<String, Integer> getMetricsMap(Object values) {
        HashMap<String, Integer> metrics = new HashMap<>();
        if(values instanceof ClassMetrics) {
            metrics = ((ClassMetrics)values).getMetricsForDetection();
        } else if (values instanceof Method) {
            metrics = ((Method)values).getMetricsForDetection();
        }
        return metrics;
    }

    /**
     * Given a class, extracts its methods and stores them in a list data structure
     * @return A list of a class's methods
     */
    private ArrayList<Method> extractMethods() {
        ArrayList<Method> methods = new ArrayList<>();
        for(ClassMetrics classMetrics : metrics) {
            methods.addAll(classMetrics.getClass_methods());
        }
        return methods;
    }

    /**
     * Gets the list of the classes/methods where the code smell was detected
     * @return A list of the classes/methods where the code smell was detected
     */
    public ArrayList<String> getResults() {
        return results;
    }

}
