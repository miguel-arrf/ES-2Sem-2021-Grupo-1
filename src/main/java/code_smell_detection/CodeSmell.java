package code_smell_detection;

public class CodeSmell {

    private String name;
    private Rule rule;

    public CodeSmell(String name, Rule rule) {
        this.name = name;
        this.rule = rule;
    }

    public boolean isClassSmell() {
        return rule.getPrimaryCondition().getMetric().contains("class");
    }

    public String getName() {
        return name;
    }

    public Rule getRule() {
        return rule;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

}
