package CodeSmellDetection;

/**
 * Abstract data structure to represent a code smell, consisting of it's name, the rule that's used for detection of the smell on source code, and a boolean that indicates whether the smell is detected on classes or on methods
 */
public class CodeSmell {

    private String name;
    private RuleNode rule;
    private boolean class_smell;

    /**
     * @param name The code smell's name
     * @param rule The code smell's rule for evaluation purposes
     * @param class_smell Whether the code smell is detected on classes or on methods
     */
    public CodeSmell(String name, RuleNode rule, boolean class_smell) {
        this.name = name;
        this.rule = rule;
        this.class_smell = class_smell;
    }

    /**
     * Checks if the code smell is detected on classes or on methods
     * @return Boolean value. If true, the smell is detected on classes. If false, the smell is detected on methods.
     */
    public boolean isClassSmell() {
        return class_smell;
    }

    /**
     * Changes the code smell's detection targetting to class, if true, or method if false
     * @param class_smell Boolean value to be assigned
     */
    public void setClass_smell(boolean class_smell) {
        this.class_smell = class_smell;
    }

    /**
     * Gets the name of the code smell
     * @return The name of the code smell as String
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the rule used in evaluation and detection of the code smell on source code
     * @return The rule used in detection of the code smell on source code
     */
    public RuleNode getRule() {
        return rule;
    }

    /**
     * Sets the code smell's name
     * @param name The name to be assigned to the code smell
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the code smell's rule to be used in detection of the code smell on source code
     * @param rule The rule to be assigned to the code smell
     */
    public void setRule(RuleNode rule) {
        this.rule = rule;
    }

}
