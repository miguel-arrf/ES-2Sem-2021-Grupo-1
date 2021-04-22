package code_smell_detection;

import RuleEditor.CustomNode;

public class RuleNode {

    private CustomNode element;
    private RuleNode left_node;
    private RuleNode right_node;

    public RuleNode(CustomNode element) {
        this.element = element;
        this.left_node = null;
        this.right_node = null;
    }


    public RuleNode(CustomNode element, RuleNode left_node, RuleNode right_node) {
        this.element = element;
        this.left_node = left_node;
        this.right_node = right_node;
    }

    public CustomNode getElement() {
        return element;
    }

    public RuleNode getLeft_node() {
        return left_node;
    }

    public RuleNode getRight_node() {
        return right_node;
    }

    public void setElement(CustomNode element) {
        this.element = element;
    }

    public void setLeft_node(RuleNode left_node) {
        this.left_node = left_node;
    }

    public void setRight_node(RuleNode right_node) {
        this.right_node = right_node;
    }

    public boolean isLeafNode() {
        return left_node == null && right_node == null;
    }

    public void saveRule(){
        System.out.println("hehe");
    }

}

