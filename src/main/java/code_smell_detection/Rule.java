package code_smell_detection;

public class Rule {

    private RuleCondition primaryCondition;
    private RuleCondition secondaryCondition;
    private boolean andOperator;

    public Rule(RuleCondition condition) {
        primaryCondition = condition;
        secondaryCondition = null;
    }

    public Rule(RuleCondition primaryCondition, RuleCondition secondaryCondition, boolean andOperator) {
        this.primaryCondition = primaryCondition;
        this.secondaryCondition = secondaryCondition;
        this.andOperator = andOperator;
    }

    public boolean evaluate() {
        if(secondaryCondition == null) {
            return primaryCondition.evaluate();
        } else {
            if(andOperator) return primaryCondition.evaluate() && secondaryCondition.evaluate();
            else return primaryCondition.evaluate() || secondaryCondition.evaluate();
        }
    }

    public RuleCondition getPrimaryCondition() {
        return primaryCondition;
    }

    public RuleCondition getSecondaryCondition() {
        return secondaryCondition;
    }

    public boolean isAndOperator() {
        return andOperator;
    }

    public void setAndOperator(boolean andOperator) {
        this.andOperator = andOperator;
    }

    public void setPrimaryCondition(RuleCondition primaryCondition) {
        this.primaryCondition = primaryCondition;
    }

    public void setSecondaryCondition(RuleCondition secondaryCondition) {
        this.secondaryCondition = secondaryCondition;
    }

}
