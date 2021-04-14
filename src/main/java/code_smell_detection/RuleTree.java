package code_smell_detection;

public class RuleTree {

    private RuleElement root;
    private RuleNode left_node;
    private RuleNode right_node;
    private boolean class_rule;

    public RuleTree(RuleElement root, boolean class_rule) {
        this.root = root;
        this.left_node = null;
        this.right_node = null;
        this.class_rule = class_rule;
    }

    public RuleTree(RuleElement root, RuleNode left_node, RuleNode right_node, boolean class_rule) {
        this.root = root;
        this.left_node = left_node;
        this.right_node = right_node;
        this.class_rule = class_rule;
    }

    public RuleElement getRoot() {
        return root;
    }

    public RuleNode getLeft_node() {
        return left_node;
    }

    public RuleNode getRight_node() {
        return right_node;
    }

    public boolean isClass_rule() {
        return class_rule;
    }

    public void setRoot(RuleElement root) {
        this.root = root;
    }

    public void setLeft_node(RuleNode left_node) {
        this.left_node = left_node;
    }

    public void setRight_node(RuleNode right_node) {
        this.right_node = right_node;
    }

    public void setClass_rule(boolean class_rule) {
        this.class_rule = class_rule;
    }

    public boolean hasNoChildren() {
        return left_node == null && right_node == null;
    }

}
