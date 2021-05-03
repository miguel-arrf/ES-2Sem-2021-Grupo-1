package RuleEditor;

/**
 * This class represents the current node in drag in the GUI.
 * This is needed to allow each CustomNode put in the Rule editor to be able to know which custom node it
 * is receiving and correctly parse it.
 */
public class DraggingObject {

    /**
     * The current node in drag.
     */
    CustomNode node;

    /**
     * Sets the current node in drag.
     *
     * @param node the node in drag.
     */
    public void setNode(CustomNode node) {
        this.node = node;
    }

    /**
     * Gets the current node in drag.
     *
     * @return the node in drag.
     */
    public CustomNode getNode() {
        return node;
    }

}
