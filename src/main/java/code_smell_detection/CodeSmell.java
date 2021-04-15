package code_smell_detection;

public class CodeSmell {

    private String name;
    private RuleNode rule;
    private boolean class_smell;

    public CodeSmell(String name, RuleNode rule, boolean class_smell) {
        this.name = name;
        this.rule = rule;
        this.class_smell = class_smell;
    }

    public boolean isClassSmell() {
        return class_smell;
    }

    public void setClass_smell(boolean class_smell) {
        this.class_smell = class_smell;
    }

    public String getName() {
        return name;
    }

    public RuleNode getRule() {
        return rule;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRule(RuleNode rule) {
        this.rule = rule;
    }

}
