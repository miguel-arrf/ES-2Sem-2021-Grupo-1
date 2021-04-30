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

public class RuleComplete implements Serializable {


    private File file;

    public RuleComplete(){

    }

    public RuleComplete(File file){
        this.file = file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }


    public  JSONObject createCodeSmell(ArrayList<CustomNode> customNodeArrayList, String name, boolean isClassSmell){

        //The first node is always in the zero index.
        CustomNode firstCustomNode = customNodeArrayList.get(0);

        RuleNode rule = createRuleNode(firstCustomNode, customNodeArrayList);

        StringBuilder stringBuilder = new StringBuilder("");
        print(rule, "", stringBuilder);
        System.out.println(stringBuilder);


        JSONObject jsonObject = toJSON(firstCustomNode, customNodeArrayList);

        JSONObject outerName = new JSONObject();
        outerName.put("innerName", name);
        outerName.put("isClassSmell", isClassSmell);
        outerName.put("uniqueIdentifier", UUID.randomUUID().toString());
        jsonObject.put("outerName", outerName);


        jsonObject.replace("outerName", outerName);

        return jsonObject;

    }

    public CodeSmell pipi(JSONObject jsonObject, DraggingObject draggingObject, String name, boolean isClassSmell){

        CustomNode firstCustomNode = jsonObjectToCustomNode(jsonObject, draggingObject);

        if(firstCustomNode.getType() ==  Types.LogicBlock){

            RuleNode nodeToReturn = new RuleNode(firstCustomNode, null, null);

            toCustomNodeNovo(nodeToReturn, jsonObject, draggingObject);

            CodeSmell codeSmell = new CodeSmell(name, nodeToReturn, isClassSmell);
            return codeSmell;
        }

        CodeSmell codeSmell = new CodeSmell(name, new RuleNode(firstCustomNode), isClassSmell);

        return codeSmell;

    }

    public CodeSmell createRuleNodeCodeSmell(ArrayList<CustomNode> customNodeArrayList, String name, boolean isClassSmell){

        //The first node is always in the zero index.
        CustomNode firstCustomNode = customNodeArrayList.get(0);
        RuleNode rule = createRuleNode(firstCustomNode, customNodeArrayList);

        StringBuilder builder = new StringBuilder("");
        print(rule, "\t", builder);


        CodeSmell codeSmell = new CodeSmell(name, rule, isClassSmell);


        return codeSmell;

    }

    public  void arrayListToJSON(ObservableList<JSONObject> jsonObjectObservableList){

        JSONArray jsonObject = new JSONArray();

        for(JSONObject object : jsonObjectObservableList){
            jsonObject.add(object);
        }

        try {
            saveFile(jsonObject.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public  JSONObject toJSON(CustomNode node, List<CustomNode> others) {
        JSONObject json = new JSONObject();
        json.put("id", node); // and so on

        ArrayList<CustomNode> childrenCustomNode = getChild(node, others);
        List<JSONObject> children = new ArrayList<JSONObject>();

        for(CustomNode subnode : childrenCustomNode) {
            children.add(toJSON(subnode, others));
        }

        json.put("children", children);
        return json;
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

    public  CustomNode teste(JSONObject jsonObject, DraggingObject draggingObject) {

        CustomNode firstCustomNode = jsonObjectToCustomNode(jsonObject, draggingObject);

        if(firstCustomNode.getType() ==  Types.LogicBlock){
            toCustomNode((LogicBlock) firstCustomNode, jsonObject, draggingObject);

            return firstCustomNode;
        }

        return firstCustomNode;

    }


    public  void toCustomNodeNovo(RuleNode realParent, JSONObject jsonList, DraggingObject draggingObject){
        JSONArray child = (JSONArray) jsonList.get("children");
        if(child.size() == 1){
            CustomNode nodeToAddRight = jsonObjectToCustomNode((JSONObject) child.get(0), draggingObject);
            realParent.setRight_node(new RuleNode(nodeToAddRight));

            if(nodeToAddRight.getType() == Types.LogicBlock){
                toCustomNodeNovo(realParent, (JSONObject) child.get(0), draggingObject );
            }

        }else if(child.size() == 2){
            CustomNode nodeToAddRight = jsonObjectToCustomNode((JSONObject) child.get(0), draggingObject);
            realParent.setRight_node(new RuleNode(nodeToAddRight));

            if(nodeToAddRight.getType() == Types.LogicBlock){

                toCustomNodeNovo(realParent, (JSONObject) child.get(0), draggingObject);
            }


            CustomNode nodeToAddLeft = jsonObjectToCustomNode((JSONObject) child.get(1), draggingObject);
            realParent.setLeft_node(new RuleNode(nodeToAddLeft));


            if(nodeToAddLeft.getType() == Types.LogicBlock){
                toCustomNodeNovo(realParent, (JSONObject) child.get(1), draggingObject );
            }



        }

    }

    public  void toCustomNode(LogicBlock parent, JSONObject jsonList, DraggingObject draggingObject){
        JSONArray child = (JSONArray) jsonList.get("children");
        if(child.size() == 1){
            CustomNode nodeToAddRight = jsonObjectToCustomNode((JSONObject) child.get(0), draggingObject);
            parent.addToRight(nodeToAddRight);

            if(nodeToAddRight.getType() == Types.LogicBlock){
                toCustomNode((LogicBlock) nodeToAddRight, (JSONObject) child.get(0), draggingObject );
            }


        }else if(child.size() == 2){
            CustomNode nodeToAddRight = jsonObjectToCustomNode((JSONObject) child.get(0), draggingObject);
            parent.addToRight(nodeToAddRight);

            if(nodeToAddRight.getType() == Types.LogicBlock){

                toCustomNode((LogicBlock) nodeToAddRight, (JSONObject) child.get(0), draggingObject);
            }



            CustomNode nodeToAddLeft = jsonObjectToCustomNode((JSONObject) child.get(1), draggingObject);
            parent.addToLeft(nodeToAddLeft);

            if(nodeToAddLeft.getType() == Types.LogicBlock){
                toCustomNode((LogicBlock) nodeToAddLeft, (JSONObject) child.get(1), draggingObject );
            }



        }

    }

    public  CustomNode jsonObjectToCustomNode(JSONObject jsonObject, DraggingObject draggingObject){
        String firstCustomNodeString = ((JSONObject) jsonObject.get("id")).get("operator").toString();

        CustomNode firstCustomNode = null;
        if (firstCustomNodeString.equals("AND")){
            firstCustomNode = new LogicBlock(draggingObject,RuleOperator.AND, "red" );
        }else if(firstCustomNodeString.equals("OR")){
            firstCustomNode = new LogicBlock(draggingObject,RuleOperator.OR, "blue" );
        }else{
            JSONObject parameters = ((JSONObject) jsonObject.get("id"));
            String operator = parameters.get("operator").toString();
            String ruleBlock = parameters.get("ruleLabel").toString();
            String value = parameters.get("valueLabel").toString();

            return new ConditionBlock(getRuleOperator(operator),new RuleBlock(ruleBlock),value,draggingObject);
        }

        return firstCustomNode;
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

    public  void saveFile(String rule) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(rule);
        printWriter.close();
        fileWriter.close();
    }

    public  RuleNode createRuleNode(CustomNode customNode, ArrayList<CustomNode> customNodeArrayList){

        if(customNode.getType() == Types.ConditionBlock){
            ConditionBlock conditionBlock = (ConditionBlock) customNode;


            return new RuleNode(conditionBlock);
        }else{
            //significa que é um ANd ou um OR block
            ArrayList<CustomNode> children = getChild(customNode, customNodeArrayList);

            if(children.size() == 0){

                return null;
            }


            if(children.size() == 1){

                return new RuleNode(customNode, createRuleNode(children.get(0), customNodeArrayList), null);

            }else{

                return new RuleNode(customNode, createRuleNode(children.get(0), customNodeArrayList),
                        createRuleNode(children.get(1), customNodeArrayList));

            }


        }

    }


    private  void print(RuleNode ruleNode, String tab, StringBuilder representation){
        representation.append(tab).append(ruleNode.getElement());
        if(ruleNode.getLeft_node() != null){
            print(ruleNode.getLeft_node(), tab + "\n\t", representation);
        }
        if(ruleNode.getRight_node() != null){
            print(ruleNode.getRight_node(), tab + "\n\t", representation);
        }

    }

    public  ArrayList<CustomNode> getChild(CustomNode parent, List<CustomNode> customNodeArrayList){
        ArrayList<CustomNode> childrens = new ArrayList<>();

        if (parent.getType() == Types.ConditionBlock){
            return  new ArrayList<>();
        }else{

            //É And ou OR block
            LogicBlock logicBlock = (LogicBlock) parent;

            for(CustomNode node: customNodeArrayList){

                if(logicBlock.getLeftLabelVBox().getChildren().contains(node.getGraphicalRepresentation()) || logicBlock.getRightLabelVBox().getChildren().contains(node.getGraphicalRepresentation())){
                    childrens.add(node);
                }
            }

        }

        return childrens;
    }


}
