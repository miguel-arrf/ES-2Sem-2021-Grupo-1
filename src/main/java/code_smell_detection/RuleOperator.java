package code_smell_detection;

public enum RuleOperator {

    AND("AND"),
    OR("OR"),
    GREATER(">"),
    GREATER_EQUAL(">="),
    EQUAL("="),
    LESSER("<"),
    LESSER_EQUAL("<="),
    DIFFERENT("!="),
    DEFAULT("Operator");

    public final String label;

    RuleOperator(String label){
        this.label = label;
    }
}

