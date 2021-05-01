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

public class RuleComplete implements Serializable {

    private File file;

    public void setFile(File file) {
        this.file = file;
    }


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


    public void saveFile(String rule) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(rule);
        printWriter.close();
        fileWriter.close();
    }


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

    public CodeSmell jsonObjectToCodeSmell(JSONObject jsonObject, DraggingObject draggingObject, String name, boolean isClassSmell) {
        CustomNode firstCustomNode = jsonObjectToCustomNode(jsonObject, draggingObject);

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

            return new ConditionBlock(getRuleOperator(operator), new RuleBlock(ruleBlock), value, draggingObject);
        }

        return firstCustomNode;
    }

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


    private RuleOperator getRuleOperator(String operator) {

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
