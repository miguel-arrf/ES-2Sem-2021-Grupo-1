package code_smell_detection;

public class RuleCondition {

    private String metric;
    private int threshold;

    public RuleCondition(String metric, int threshold) {
        this.metric = metric;
        this.threshold = threshold;
    }

    public String getMetric() {
        return metric;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public boolean evaluate() {
        return false;
    }

}
