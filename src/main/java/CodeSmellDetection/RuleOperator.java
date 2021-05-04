package CodeSmellDetection;

/**
 * Enumerate to define the various operators used in the construction of rules to evaluate/detect the presence of code smells
 */
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

