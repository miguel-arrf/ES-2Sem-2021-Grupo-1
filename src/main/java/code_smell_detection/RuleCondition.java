package code_smell_detection;

public class RuleCondition implements RuleElement {

    private String metric;
    private RuleOperator operator;
    private int threshold;

    public RuleCondition(String metric, int threshold, RuleOperator operator) {
        this.metric = metric;
        this.operator = operator;
        this.threshold = threshold;
    }

    public String getMetric() {
        return metric;
    }

    public RuleOperator getOperator() {
        return operator;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public void setOperator(RuleOperator operator) {
        this.operator = operator;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public boolean evaluate(int value) {
        switch (operator) {
            case EQUAL: return value == threshold;
            case GREATER: return value > threshold;
            case GREATER_EQUAL: return value >= threshold;
            case LESSER: return value < threshold;
            case LESSER_EQUAL: return value <= threshold;
            case DIFFERENT: return value != threshold;
        }
        return false;
    }

}
