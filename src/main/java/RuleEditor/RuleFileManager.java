package RuleEditor;

import code_smell_detection.CodeSmell;
import code_smell_detection.RuleNode;
import code_smell_detection.RuleOperator;
import javafx.collections.ObservableList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;

/**
 * Rule file manager which allows to import and export from and to Custom Node and JSON types.
 * Also allows the conversion between JSON <-> GUI.
 */
public class RuleFileManager {

    private File file;

    public File getFile() {
        return this.file;
    }

    /**
     * Sets the file that contains the rules.
     *
     * @param file the file
     */
    public void setFile(File file) {
        this.file = file;
    }


    /**
     * Loads a file into JSON array format.
     *
     * @return an array list of rules.
     * @throws IOException    the io exception
     * @throws ParseException the parse exception
     */
    public ArrayList<JSONObject> loadJSONRuleFile() throws IOException, ParseException {
        ArrayList<JSONObject> customNodes = new ArrayList<>();

        JSONParser jsonParser = new JSONParser();
        Reader reader = new FileReader(file);

        JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);

        for (Object jsonObject : jsonArray) {
            customNodes.add((JSONObject) jsonObject);
        }

        return customNodes;
    }

    /**
     * Saves a JSON list to the rules file (or updates it).
     *
     * @param jsonObjectObservableList the list of rules represented in JSONObjects.
     */
    @SuppressWarnings("unchecked")
    public void saveJSONListToFile(ObservableList<JSONObject> jsonObjectObservableList) {

        JSONArray jsonObject = new JSONArray();

        jsonObject.addAll(jsonObjectObservableList);

        try {
            saveFile(jsonObject.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * Saves the rules file.
     *
     * @param rule the rule file name.
     * @throws IOException the io exception
     */
    public void saveFile(String rule) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(rule);
        printWriter.close();
        fileWriter.close();
    }


    /**
     * GUI to JSON object.
     *
     * @param customNodeArrayList the custom node array list
     * @param name                the name
     * @param isClassSmell        if the code smell is a class or a method one.
     * @return the json object representing a given rule defined in GUI terms.
     */
    @SuppressWarnings("unchecked")
    public JSONObject guiToJSONObject(ArrayList<CustomNode> customNodeArrayList, String name, boolean isClassSmell) {

        //The first node is always in the zero index.
        CustomNode firstCustomNode = customNodeArrayList.get(0);

        JSONObject jsonObject = customNodeToJSON(firstCustomNode, customNodeArrayList);

        JSONObject outerName = new JSONObject();
        outerName.put("innerName", name);
        outerName.put("isClassSmell", isClassSmell);
        outerName.put("uniqueIdentifier", UUID.randomUUID().toString());
        jsonObject.put("outerName", outerName);


        jsonObject.replace("outerName", outerName);

        return jsonObject;

    }

    /**
     * Json object to code smell.
     *
     * @param jsonObject     the rule represented by a JSONObject type.
     * @param draggingObject the dragging object (needs to be used to properly set up the tree model in the nodes).
     * @param name           the name of the code smell.
     * @param isClassSmell   if the code smell is a class or a method one.
     * @return the code smell
     */
    public CodeSmell jsonObjectToCodeSmell(JSONObject jsonObject, DraggingObject draggingObject, String name, boolean isClassSmell) {
        CustomNode firstCustomNode = jsonObjectToCustomNode(jsonObject, draggingObject);
        System.out.println("firstCustomNode: "  + firstCustomNode);
        if (firstCustomNode.getType() == Types.LogicBlock) {
            RuleNode nodeToReturn = new RuleNode(firstCustomNode, null, null);

            //addChildrenToRuleNode(nodeToReturn, jsonObject, draggingObject);
            addChildrenToObject(nodeToReturn, jsonObject, draggingObject,
                    (a,b) -> {
                        RuleNode ruleNode = new RuleNode(b);
                        a.setRight_node(ruleNode);
                        return ruleNode;
                    },
                    (a,b) -> {
                        RuleNode ruleNode = new RuleNode(b);
                        a.setLeft_node(ruleNode);
                        return ruleNode;
                    });

            return new CodeSmell(name, nodeToReturn, isClassSmell);
        }

        return new CodeSmell(name, new RuleNode(firstCustomNode), isClassSmell);

    }

    /**
     * CustomNode to JSONObject.
     *
     * @param node   the custom node (the first one in the three model representing a rule).
     * @param others the list containing all the CustomNodes in the GUI of a rule.
     * @return the JSONObject representing the rule represented by the CustomNode node.
     */
    @SuppressWarnings("unchecked")
    public JSONObject customNodeToJSON(CustomNode node, List<CustomNode> others) {
        JSONObject json = new JSONObject();
        json.put("rule", node); // and so on

        ArrayList<CustomNode> childrenCustomNode = getChild(node, others);
        List<JSONObject> children = new ArrayList<>();

        for (CustomNode subNode : childrenCustomNode) {
            children.add(customNodeToJSON(subNode, others));
        }

        json.put("children", children);
        return json;
    }

    /**
     * Json to Custom Node (e.g: JSON to GUI)
     *
     * @param jsonObject     the json object representing a rule.
     * @param draggingObject the dragging object.
     * @return the custom node representing the rule, to be used in the GUI.
     */
    public CustomNode jsonToGUI(JSONObject jsonObject, DraggingObject draggingObject) {

        CustomNode firstCustomNode = jsonObjectToCustomNode(jsonObject, draggingObject);

        if (firstCustomNode.getType() == Types.LogicBlock) {
            //addChildrenToLogicBlock((LogicBlock) firstCustomNode, jsonObject, draggingObject);
            LogicBlock logicBlock = (LogicBlock) firstCustomNode;
            addChildrenToObject(logicBlock, jsonObject, draggingObject,
                    (a,b) -> {
                        a.addToRight(b);

                        if(b.getType() == Types.LogicBlock){
                            return (LogicBlock) b;
                        }else{
                            return null;
                        }
                    },
                    (a,b) -> {
                        a.addToLeft(b);

                        if(b.getType() == Types.LogicBlock){
                            return (LogicBlock) b;
                        }else{
                            return null;
                        }
                    });


            return firstCustomNode;
        }

        return firstCustomNode;

    }

    /**
     * Json object to customNode.
     *
     * @param jsonObject     the json object representing a given node in the GUI.
     * @param draggingObject the dragging object.
     * @return the custom node that represents the given block in the JSONObject.
     */
    public CustomNode jsonObjectToCustomNode(JSONObject jsonObject, DraggingObject draggingObject) {
        String firstCustomNodeString = ((JSONObject) jsonObject.get("rule")).get("operator").toString();

        CustomNode firstCustomNode;
        if (firstCustomNodeString.equals("AND")) {
            firstCustomNode = new LogicBlock(draggingObject, RuleOperator.AND, "red");
        } else if (firstCustomNodeString.equals("OR")) {
            firstCustomNode = new LogicBlock(draggingObject, RuleOperator.OR, "blue");
        } else {
            JSONObject parameters = ((JSONObject) jsonObject.get("rule"));
            String operator = parameters.get("operator").toString();
            String ruleBlock = parameters.get("ruleLabel").toString();
            String value = parameters.get("valueLabel").toString();

            return new ConditionBlock(getRuleOperator(operator), new MetricBlock(ruleBlock), value, draggingObject);
        }

        return firstCustomNode;
    }

    /**
     * Adds each of the parent-children to the correct side. In the end, we have a tree of parent's type.
     *
     * @param <T>            the type parameter (either CustomNode or RuleNode).
     * @param parent         the parent
     * @param jsonList       the JSONObject with the rule (to be transformed into a rule or into GUI representation).
     * @param draggingObject the dragging object
     * @param addToRight     the function that receives an element of parent type and one child,
     *                       and ands it to the right side while returning a new parent in case this child is not a leaf one, and also has children.
     * @param addToLeft      the function that receives an element of parent type and one child,
     *                       and ands it to the left side while returning a new parent in case this child is not a leaf one, and also has children.
     */
    public <T> void addChildrenToObject(T parent, JSONObject jsonList, DraggingObject draggingObject, BiFunction<T, CustomNode, T> addToRight, BiFunction<T, CustomNode, T> addToLeft){
        JSONArray children = (JSONArray) jsonList.get("children");


        if(children.size() == 1){
            CustomNode nodeToAddRight = jsonObjectToCustomNode((JSONObject) children.get(0), draggingObject);

            T whatToAdd = addToRight.apply(parent, nodeToAddRight);

            if(nodeToAddRight.getType() == Types.LogicBlock)
                addChildrenToObject(whatToAdd, (JSONObject) children.get(0), draggingObject, addToRight, addToLeft);

        }else if(children.size() == 2){
            CustomNode nodeToAddRight = jsonObjectToCustomNode((JSONObject) children.get(0), draggingObject);
            CustomNode nodeToAddLeft = jsonObjectToCustomNode((JSONObject) children.get(1), draggingObject);

            T whatToAddRight = addToRight.apply(parent, nodeToAddRight);
            T whatToAddLeft = addToLeft.apply(parent, nodeToAddLeft);

            if(nodeToAddRight.getType() == Types.LogicBlock){
                addChildrenToObject(whatToAddRight,(JSONObject) children.get(0), draggingObject, addToRight, addToLeft);
            }

            if(nodeToAddLeft.getType() == Types.LogicBlock){
                addChildrenToObject(whatToAddLeft, (JSONObject) children.get(1), draggingObject, addToRight, addToLeft);
            }


        }

    }


    /**
     * Transforms a operator represented by a string to a RuleOperator type.
     *
     * @param operator the operator.
     * @return the operator in the RuleOperator type.
     */
    protected RuleOperator getRuleOperator(String operator) {
        if (operator.equals(RuleOperator.GREATER.label)) {
            return RuleOperator.GREATER;
        }
        if (operator.equals(RuleOperator.GREATER_EQUAL.label)) {
            return RuleOperator.GREATER_EQUAL;
        }
        if (operator.equals(RuleOperator.EQUAL.label)) {
            return RuleOperator.EQUAL;
        }
        if (operator.equals(RuleOperator.LESSER.label)) {
            return RuleOperator.LESSER;
        }
        if (operator.equals(RuleOperator.LESSER_EQUAL.label)) {
            return RuleOperator.LESSER_EQUAL;
        }
        if (operator.equals(RuleOperator.DIFFERENT.label)) {
            return RuleOperator.DIFFERENT;
        }
        return RuleOperator.DEFAULT;
    }

    /**
     * Gets the parent children based on the CustomNodes in the given list.
     * If the parent is of type ConditionBlock, means there is no children (it is a leaf CustomNode),
     * otherwise, if the parent is of type LogicBlock, then there are multiple children (0, 1 or 2).
     *
     * @param parent              the parent to which we want to get the respective children.
     * @param customNodeArrayList the array list containing all the CustomNodes (brothers or children nodes).
     * @return the arraylist containing all the children.
     */
    public ArrayList<CustomNode> getChild(CustomNode parent, List<CustomNode> customNodeArrayList) {
        ArrayList<CustomNode> children = new ArrayList<>();

        if (parent.getType() == Types.ConditionBlock) {
            return new ArrayList<>();
        } else {

            //It is an AND or an OR block
            LogicBlock logicBlock = (LogicBlock) parent;

            for (CustomNode node : customNodeArrayList) {

                if (logicBlock.getLeftLabelVBox().getChildren().contains(node.getGraphicalRepresentation()) || logicBlock.getRightLabelVBox().getChildren().contains(node.getGraphicalRepresentation())) {
                    children.add(node);
                }
            }

        }

        return children;
    }

}