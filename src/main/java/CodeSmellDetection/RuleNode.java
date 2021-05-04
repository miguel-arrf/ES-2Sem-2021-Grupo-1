package CodeSmellDetection;

import RuleEditor.CustomNode;

/**
 * Abstract data structure with tree-like behavior to represent a rule, made of an element(either an operator or a condition/expression), and, if necessary, left or right nodes that combined result in the rule
 */
public class RuleNode {

    private CustomNode element;
    private RuleNode left_node;
    private RuleNode right_node;

    /**
     * Constructs a single RuleNode without left or right nodes
     * @param element An element of the rule instantiated on the GUI
     */
    public RuleNode(CustomNode element) {
        this.element = element;
        this.left_node = null;
        this.right_node = null;
    }


    /**
     * Constructs a RuleNode with a left and a right RuleNode assigned
     * @param element An element of the rule instantiated on the GUI
     * @param left_node Another instance of RuleNode analyzed from the left
     * @param right_node Another instance of RuleNode analyzed from the right
     */
    public RuleNode(CustomNode element, RuleNode left_node, RuleNode right_node) {
        this.element = element;
        this.left_node = left_node;
        this.right_node = right_node;
    }

    /**
     * Gets the element stored in this node
     * @return The element of the node as instance of CustomNode
     */
    public CustomNode getElement() {
        return element;
    }

    /**
     * Gets the node to the left of this node
     * @return The node assigned to the left of this node
     */
    public RuleNode getLeft_node() {
        return left_node;
    }

    /**
     * Gets the node to the right of this node
     * @return The node assigned to the right of this node
     */
    public RuleNode getRight_node() {
        return right_node;
    }

    /**
     * Assigns an element to be stored in the node
     * @param element The new element to be stored in the node
     */
    public void setElement(CustomNode element) {
        this.element = element;
    }

    /**
     * Assigns a node to the left of this node
     * @param left_node An instance of RuleNode
     */
    public void setLeft_node(RuleNode left_node) {
        this.left_node = left_node;
    }

    /**
     * Assigns a node to the right of this node
     * @param right_node An instance of RuleNode
     */
    public void setRight_node(RuleNode right_node) {
        this.right_node = right_node;
    }

    /**
     * Checks whether the node is a single node or if it has a left/right node assigned
     * @return Whether or not the node is a leaf node
     */
    public boolean isLeafNode() {
        return left_node == null && right_node == null;
    }

    /**
     * Huh??
     */
    public void saveRule(){
        System.out.println("hehe");
    }

}

