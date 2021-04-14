package code_smell_detection;

public class CodeSmell {

    private String name;
    private RuleTree rule;

    public CodeSmell(String name, RuleTree rule) {
        this.name = name;
        this.rule = rule;
    }

    public boolean isClassSmell() {
        return rule.isClass_rule();
    }

    public String getName() {
        return name;
    }

    public RuleTree getRule() {
        return rule;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRule(RuleTree rule) {
        this.rule = rule;
    }

}
