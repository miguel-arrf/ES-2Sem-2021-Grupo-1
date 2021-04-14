package code_smell_detection;

import metric_extraction.ClassMetrics;
import metric_extraction.Method;

import java.util.ArrayList;
import java.util.HashMap;

public class DetectionWorker implements Runnable {

    private CodeSmell codeSmell;
    private ArrayList<ClassMetrics> metrics;
    private ArrayList<String> results = new ArrayList<>();

    public DetectionWorker(CodeSmell codeSmell, ArrayList<ClassMetrics> metrics) {
        this.codeSmell = codeSmell;
        this.metrics = metrics;
    }

    public CodeSmell getCodeSmell() {
        return codeSmell;
    }

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

    private void detect(Object values) {
        HashMap<String, Integer> metrics = getMetricsMap(values);
        boolean result = evaluateRule(metrics, codeSmell.getRule());
        if(result && values instanceof ClassMetrics) results.add( ((ClassMetrics)values).getClass_name() );
        else if (result && values instanceof Method) results.add( ((Method)values).getMethod_name() );
    }

    private boolean evaluateRule(HashMap<String, Integer> metrics, RuleTree rule) {
        boolean result = false;
        if(rule.hasNoChildren()) {//Regra é composta apenas por uma RuleCondition
            RuleCondition condition = (RuleCondition)rule.getRoot();
            result = evaluateCondition(condition, metrics);
        } else {//Regra contém 2 nós e a root é uma instância de RuleOperator
            RuleOperator operator = (RuleOperator)rule.getRoot();
            boolean left_result = evaluateNode(rule.getLeft_node(), metrics);
            boolean right_result = evaluateNode(rule.getRight_node(), metrics);
            result = evaluateOperator(operator, left_result, right_result);
        }
        return result;
    }

    private boolean evaluateOperator(RuleOperator operator, boolean left_result, boolean right_result) {
        if(operator.equals(RuleOperator.AND)) return left_result && right_result;
        else if(operator.equals(RuleOperator.OR)) return left_result || right_result;
        else return false;
    }

    private boolean evaluateNode(RuleNode node, HashMap<String, Integer> metrics) {
        boolean result = false;
        if(node.isLeafNode()) {//Nó representa uma condição
            RuleCondition condition = (RuleCondition)node.getElement();
            result = evaluateCondition(condition, metrics);
        } else {//Avaliar o próprio nó, o nó esquerdo e o nó direito
            RuleOperator operator = (RuleOperator)node.getElement();
            boolean left_result = evaluateNode(node.getLeft_node(), metrics);
            boolean right_result = evaluateNode(node.getRight_node(), metrics);
            result = evaluateOperator(operator, left_result, right_result);
        }
        return result;
    }

    private boolean evaluateCondition(RuleCondition condition, HashMap<String, Integer> metrics) {
        int value = metrics.get(condition.getMetric());
        return condition.evaluate(value);
    }

    private HashMap<String, Integer> getMetricsMap(Object values) {
        HashMap<String, Integer> metrics = new HashMap<>();
        if(values instanceof ClassMetrics) {
            metrics = ((ClassMetrics)values).getMetricsForDetection();
        } else if (values instanceof Method) {
            metrics = ((Method)values).getMetricsForDetection();
        }
        return metrics;
    }

    private ArrayList<Method> extractMethods() {
        ArrayList<Method> methods = new ArrayList<>();
        for(ClassMetrics classMetrics : metrics) {
            methods.addAll(classMetrics.getClass_methods());
        }
        return methods;
    }

    public ArrayList<String> getResults() {
        return results;
    }

}
