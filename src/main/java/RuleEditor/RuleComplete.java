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
import java.util.function.BiConsumer;

public class RuleComplete implements Serializable {

    private File file;

    public void setFile(File file) {
        this.file = file;
    }


    public  ArrayList<JSONObject> loadJSONRuleFile() throws IOException, ParseException {
        ArrayList<JSONObject> customNodes = new ArrayList<>();

        JSONParser jsonParser = new JSONParser();
        Reader reader = new FileReader(file);

        JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);

        for(Object jsonObject : jsonArray){
            customNodes.add((JSONObject) jsonObject);
        }

        return customNodes;
    }

    @SuppressWarnings("unchecked")
    public  void saveJSONListToFile(ObservableList<JSONObject> jsonObjectObservableList){

        JSONArray jsonObject = new JSONArray();

        jsonObject.addAll(jsonObjectObservableList);

        try {
            saveFile(jsonObject.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public  void saveFile(String rule) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(rule);
        printWriter.close();
        fileWriter.close();
    }


    @SuppressWarnings("unchecked")
    public  JSONObject guiToJSONObject(ArrayList<CustomNode> customNodeArrayList, String name, boolean isClassSmell){

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

    public CodeSmell jsonObjectToCodeSmell(JSONObject jsonObject, DraggingObject draggingObject, String name, boolean isClassSmell){
        CustomNode firstCustomNode = jsonObjectToCustomNode(jsonObject, draggingObject);

        if(firstCustomNode.getType() ==  Types.LogicBlock){
            RuleNode nodeToReturn = new RuleNode(firstCustomNode, null, null);

            //addChildrenToRuleNode(nodeToReturn, jsonObject, draggingObject);
            addChildrenToObject(nodeToReturn, jsonObject, draggingObject, RuleNode::setRight_node, RuleNode::setLeft_node);

            return new CodeSmell(name, nodeToReturn, isClassSmell);
        }

        return new CodeSmell(name, new RuleNode(firstCustomNode), isClassSmell);

    }

    @SuppressWarnings("unchecked")
    public  JSONObject customNodeToJSON(CustomNode node, List<CustomNode> others) {
        JSONObject json = new JSONObject();
        json.put("rule", node); // and so on

        ArrayList<CustomNode> childrenCustomNode = getChild(node, others);
        List<JSONObject> children = new ArrayList<>();

        for(CustomNode subNode : childrenCustomNode) {
            children.add(customNodeToJSON(subNode, others));
        }

        json.put("children", children);
        return json;
    }

    public  CustomNode jsonToGUI(JSONObject jsonObject, DraggingObject draggingObject) {

        CustomNode firstCustomNode = jsonObjectToCustomNode(jsonObject, draggingObject);

        if(firstCustomNode.getType() ==  Types.LogicBlock){
            //addChildrenToLogicBlock((LogicBlock) firstCustomNode, jsonObject, draggingObject);
            addChildrenToObject((LogicBlock) firstCustomNode, jsonObject, draggingObject, (a, b) -> a.addToRight(b), (a, b) -> a.addToRight(b));


            return firstCustomNode;
        }

        return firstCustomNode;

    }

    public  CustomNode jsonObjectToCustomNode(JSONObject jsonObject, DraggingObject draggingObject){
        String firstCustomNodeString = ((JSONObject) jsonObject.get("rule")).get("operator").toString();

        CustomNode firstCustomNode;
        if (firstCustomNodeString.equals("AND")){
            firstCustomNode = new LogicBlock(draggingObject,RuleOperator.AND, "red" );
        }else if(firstCustomNodeString.equals("OR")){
            firstCustomNode = new LogicBlock(draggingObject,RuleOperator.OR, "blue" );
        }else{
            JSONObject parameters = ((JSONObject) jsonObject.get("rule"));
            String operator = parameters.get("operator").toString();
            String ruleBlock = parameters.get("ruleLabel").toString();
            String value = parameters.get("valueLabel").toString();

            return new ConditionBlock(getRuleOperator(operator),new RuleBlock(ruleBlock),value,draggingObject);
        }

        return firstCustomNode;
    }


    public <T> void addChildrenToObject(T realParent, JSONObject jsonList, DraggingObject draggingObject, BiConsumer<T, T> addToRight, BiConsumer<T,T> addToLeft){
        JSONArray child = (JSONArray) jsonList.get("children");
        if(child.size() == 1){
            CustomNode nodeToAddRight = jsonObjectToCustomNode((JSONObject) child.get(0), draggingObject);
            addToRight.accept(realParent, (T) new RuleNode(nodeToAddRight));

            if(nodeToAddRight.getType() == Types.LogicBlock){
                addChildrenToObject(realParent, (JSONObject) child.get(0), draggingObject, addToRight, addToLeft );
            }

        }else if(child.size() == 2){
            CustomNode nodeToAddRight = jsonObjectToCustomNode((JSONObject) child.get(0), draggingObject);
            addToRight.accept(realParent, (T) new RuleNode(nodeToAddRight));

            if(nodeToAddRight.getType() == Types.LogicBlock){
                addChildrenToObject(realParent, (JSONObject) child.get(0), draggingObject, addToRight, addToLeft);
            }

            CustomNode nodeToAddLeft = jsonObjectToCustomNode((JSONObject) child.get(1), draggingObject);
            addToLeft.accept(realParent, (T) new RuleNode(nodeToAddRight));

            if(nodeToAddLeft.getType() == Types.LogicBlock){
                addChildrenToObject(realParent, (JSONObject) child.get(1), draggingObject, addToRight, addToLeft );
            }



        }

    }

    public  void addChildrenToRuleNode(RuleNode realParent, JSONObject jsonList, DraggingObject draggingObject){
        JSONArray child = (JSONArray) jsonList.get("children");
        if(child.size() == 1){
            CustomNode nodeToAddRight = jsonObjectToCustomNode((JSONObject) child.get(0), draggingObject);
            realParent.setRight_node(new RuleNode(nodeToAddRight));

            if(nodeToAddRight.getType() == Types.LogicBlock){
                addChildrenToRuleNode(realParent, (JSONObject) child.get(0), draggingObject );
            }

        }else if(child.size() == 2){
            CustomNode nodeToAddRight = jsonObjectToCustomNode((JSONObject) child.get(0), draggingObject);
            realParent.setRight_node(new RuleNode(nodeToAddRight));

            if(nodeToAddRight.getType() == Types.LogicBlock){
                addChildrenToRuleNode(realParent, (JSONObject) child.get(0), draggingObject);
            }

            CustomNode nodeToAddLeft = jsonObjectToCustomNode((JSONObject) child.get(1), draggingObject);
            realParent.setLeft_node(new RuleNode(nodeToAddLeft));

            if(nodeToAddLeft.getType() == Types.LogicBlock){
                addChildrenToRuleNode(realParent, (JSONObject) child.get(1), draggingObject );
            }



        }

    }

    public  void addChildrenToLogicBlock(LogicBlock parent, JSONObject jsonList, DraggingObject draggingObject){
        JSONArray child = (JSONArray) jsonList.get("children");
        if(child.size() == 1){
            CustomNode nodeToAddRight = jsonObjectToCustomNode((JSONObject) child.get(0), draggingObject);
            parent.addToRight(nodeToAddRight);

            if(nodeToAddRight.getType() == Types.LogicBlock){
                addChildrenToLogicBlock((LogicBlock) nodeToAddRight, (JSONObject) child.get(0), draggingObject );
            }

        }else if(child.size() == 2){
            CustomNode nodeToAddRight = jsonObjectToCustomNode((JSONObject) child.get(0), draggingObject);
            parent.addToRight(nodeToAddRight);

            if(nodeToAddRight.getType() == Types.LogicBlock){
                addChildrenToLogicBlock((LogicBlock) nodeToAddRight, (JSONObject) child.get(0), draggingObject);
            }

            CustomNode nodeToAddLeft = jsonObjectToCustomNode((JSONObject) child.get(1), draggingObject);
            parent.addToLeft(nodeToAddLeft);

            if(nodeToAddLeft.getType() == Types.LogicBlock){
                addChildrenToLogicBlock((LogicBlock) nodeToAddLeft, (JSONObject) child.get(1), draggingObject );
            }



        }

    }

    private  RuleOperator getRuleOperator(String operator){

        if(operator.equals(RuleOperator.GREATER.label)){
            return RuleOperator.GREATER;
        }
        if(operator.equals(RuleOperator.GREATER_EQUAL.label)){
            return RuleOperator.GREATER_EQUAL;
        }
        if(operator.equals(RuleOperator.EQUAL.label)){
            return RuleOperator.EQUAL;
        }
        if(operator.equals(RuleOperator.LESSER.label)){
            return RuleOperator.LESSER;
        }
        if(operator.equals(RuleOperator.LESSER_EQUAL.label)){
            return RuleOperator.LESSER_EQUAL;
        }
        if(operator.equals(RuleOperator.DIFFERENT.label)){
            return RuleOperator.DIFFERENT;
        }

        return RuleOperator.DEFAULT;
    }

    public  ArrayList<CustomNode> getChild(CustomNode parent, List<CustomNode> customNodeArrayList){
        ArrayList<CustomNode> children = new ArrayList<>();

        if (parent.getType() == Types.ConditionBlock){
            return  new ArrayList<>();
        }else{

            //It is an AND or an OR block
            LogicBlock logicBlock = (LogicBlock) parent;

            for(CustomNode node: customNodeArrayList){

                if(logicBlock.getLeftLabelVBox().getChildren().contains(node.getGraphicalRepresentation()) || logicBlock.getRightLabelVBox().getChildren().contains(node.getGraphicalRepresentation())){
                    children.add(node);
                }
            }

        }

        return children;
    }

}
