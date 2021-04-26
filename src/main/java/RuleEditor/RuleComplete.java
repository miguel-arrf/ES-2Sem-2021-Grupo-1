package RuleEditor;

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

public class RuleComplete implements Serializable {

//    public static void loadDataToGUI() throws FileNotFoundException {
//        File file = new File("Rules.txt");
//        Scanner myReader = new Scanner(file);
//
//        CustomNode parent = null;
//
//        while(myReader.hasNextLine()){
//            String line = myReader.nextLine();
//
//            if(line.contains("LogicBlock")){
//                CustomNode logicBlock =
//            }else if(line.contains("ConditionBlock")){
//
//            }
//
//        }
//
//
//    }



    public static JSONObject createCodeSmell(ArrayList<CustomNode> customNodeArrayList, String name){
        //The first node is always in the zero index.
        CustomNode firstCustomNode = customNodeArrayList.get(0);

        RuleNode rule = createRuleNode(firstCustomNode, customNodeArrayList);


        StringBuilder stringBuilder = new StringBuilder("");
        print(rule, "", stringBuilder);
        System.out.println(stringBuilder);

        JSONObject jsonObject = toJSON(firstCustomNode, customNodeArrayList);

        CustomNode id = (CustomNode) jsonObject.get("id");
        JSONObject newID = new JSONObject();
        newID.put("id", id);
        newID.put("name", name);
        jsonObject.replace("id", newID);



        /*try {
            saveFile(jsonObject.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        return jsonObject;

    }

    public static void arrayListToJSON(ObservableList<JSONObject> jsonObjectObservableList){

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


    public static JSONObject toJSON(CustomNode node, List<CustomNode> others) {
        JSONObject json = new JSONObject();
        json.put("id", node); // and so on

        ArrayList<CustomNode> childrenCustomNode = RuleComplete.getChild(node, others);
        List<JSONObject> children = new ArrayList<JSONObject>();

        for(CustomNode subnode : childrenCustomNode) {
            children.add(toJSON(subnode, others));
        }

        json.put("children", children);
        return json;
    }

    public static ArrayList<CustomNode> loadJSONRuleFile() throws FileNotFoundException {
        ArrayList<CustomNode> customNodes = new ArrayList<>();

        JSONParser jsonParser = new JSONParser();
        Reader reader = new FileReader("Rules.txt");



        return customNodes;
    }

    public static CustomNode loadJSONFile(File file, DraggingObject draggingObject) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        Reader reader = new FileReader(file);

        JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

        CustomNode firstCustomNode = jsonObjectToCustomNode(jsonObject, draggingObject);

        System.out.println("firstCustomNode :" + firstCustomNode);
        if(firstCustomNode.getType() ==  Types.LogicBlock){
            toCustomNode((LogicBlock) firstCustomNode, jsonObject, draggingObject);

            return firstCustomNode;
        }

        return firstCustomNode;

    }

    public static CustomNode loadJSONFile(DraggingObject draggingObject) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        Reader reader = new FileReader("Rules.txt");

        JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
        JSONObject idWithoutName = (JSONObject) jsonObject.get("id");
        jsonObject.replace("id", (JSONObject)idWithoutName.get("id"));

        CustomNode firstCustomNode = jsonObjectToCustomNode(jsonObject, draggingObject);

        System.out.println("firstCustomNode :" + firstCustomNode);
        if(firstCustomNode.getType() ==  Types.LogicBlock){
            toCustomNode((LogicBlock) firstCustomNode, jsonObject, draggingObject);

            return firstCustomNode;
        }

        return firstCustomNode;

    }

    public static void toCustomNode(LogicBlock parent, JSONObject jsonList, DraggingObject draggingObject){
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
                toCustomNode((LogicBlock) nodeToAddRight, (JSONObject) child.get(0), draggingObject );
            }

            CustomNode nodeToAddLeft = jsonObjectToCustomNode((JSONObject) child.get(1), draggingObject);
            parent.addToLeft(nodeToAddLeft);

            if(nodeToAddLeft.getType() == Types.LogicBlock){
                toCustomNode((LogicBlock) nodeToAddLeft, (JSONObject) child.get(0), draggingObject );
            }

        }

    }

    public static CustomNode jsonObjectToCustomNode(JSONObject jsonObject, DraggingObject draggingObject){
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


    private static RuleOperator getRuleOperator(String operator){

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

    public static void saveFile(String rule) throws IOException {
        FileWriter fileWriter = new FileWriter("rules.txt");
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(rule);
        printWriter.close();
        fileWriter.close();
    }

    public static RuleNode createRuleNode(CustomNode customNode, ArrayList<CustomNode> customNodeArrayList){

        if(customNode.getType() == Types.ConditionBlock){
            ConditionBlock conditionBlock = (ConditionBlock) customNode;


            return new RuleNode(conditionBlock);
        }else{
            //significa que é um ANd ou um OR block
            ArrayList<CustomNode> children = getChild(customNode, customNodeArrayList);

            if(children.size() == 0)
                return null;

            if(children.size() == 1){
                return new RuleNode(customNode, createRuleNode(children.get(0), customNodeArrayList), null);

            }else{
                return new RuleNode(customNode, createRuleNode(children.get(0), customNodeArrayList),
                        createRuleNode(children.get(1), customNodeArrayList));

            }


        }

    }


    private static void print(RuleNode ruleNode, String tab, StringBuilder representation){
        representation.append(tab).append(ruleNode.getElement());
        //System.out.println(tab + ruleNode.getElement());
        if(ruleNode.getLeft_node() != null){
            print(ruleNode.getLeft_node(), tab + "\n\t", representation);
        }
        if(ruleNode.getRight_node() != null){
            print(ruleNode.getRight_node(), tab + "\n\t", representation);
        }

    }

    public static ArrayList<CustomNode> getChild(CustomNode parent, List<CustomNode> customNodeArrayList){
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
