package RuleEditor;

import CodeSmellDetection.CodeSmell;
import CodeSmellDetection.RuleOperator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


class RuleFileManagerTest {

    private static RuleFileManager manager;
    private static File testFile;

    private final JFXPanel panel = new JFXPanel();

    @BeforeAll
    private static void setUP() {
        RuleFileManagerTest.manager = new RuleFileManager();

        URL url = RuleFileManagerTest.class.getResource("/testRule.rule");
        testFile = new File(url.getFile());
        manager.setFile(testFile);
    }

    @Test
    void testLoading() {
        Assertions.assertNotNull(manager.getFile());

        ArrayList<JSONObject> arrayListJson;
        try {
            arrayListJson = manager.loadJSONRuleFile();
            Assertions.assertEquals(arrayListJson.size(), 2);


            jsonObjectToCodeSmell(arrayListJson.get(0));

            jsonObjectToCodeSmell(arrayListJson.get(1));
        } catch (IOException | ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }


    @Test
    void testSaveFile() {
        assertAll(() -> {
            ArrayList<JSONObject> customNodes;
            customNodes = manager.loadJSONRuleFile();
            ObservableList<JSONObject> jsonObservableList = FXCollections.observableArrayList(customNodes);
            manager.saveJSONListToFile(jsonObservableList);
        });

    }

    void jsonObjectToCodeSmell(JSONObject object) {
        System.out.println(object.toJSONString());
        CodeSmell codeSmell = manager.jsonObjectToCodeSmell(object, new DraggingObject(), "codeSmell", true);

        Assertions.assertNotNull(codeSmell);
    }


    @Test
    void testRuleOperator() {
        String greater = ">";
        String greaterEqual = ">=";
        String equal = "=";
        String less = "<";
        String lessEqual = "<=";
        String different = "!=";

        Assertions.assertEquals(RuleOperator.GREATER, manager.getRuleOperator(greater));
        Assertions.assertEquals(RuleOperator.GREATER_EQUAL, manager.getRuleOperator(greaterEqual));
        Assertions.assertEquals(RuleOperator.EQUAL, manager.getRuleOperator(equal));
        Assertions.assertEquals(RuleOperator.LESSER, manager.getRuleOperator(less));
        Assertions.assertEquals(RuleOperator.LESSER_EQUAL, manager.getRuleOperator(lessEqual));
        Assertions.assertEquals(RuleOperator.DIFFERENT, manager.getRuleOperator(different));
        Assertions.assertEquals(RuleOperator.DEFAULT, manager.getRuleOperator("default"));
    }


    @Test
    void testJsonToGUI() {
        try {
            CustomNode firstNode = manager.jsonToGUI(manager.loadJSONRuleFile().get(0), new DraggingObject());
            assertEquals(firstNode.getOperator(), RuleOperator.LESSER);
            assertEquals(firstNode.getType(), Types.ConditionBlock);

            CustomNode secondNode = manager.jsonToGUI(manager.loadJSONRuleFile().get(1), new DraggingObject());
            assertEquals(secondNode.getOperator(), RuleOperator.AND);
            assertEquals(secondNode.getType(), Types.LogicBlock);


            String jsonNode = "{\"children\":[{\"children\":[{\"children\":[],\"rule\":{\"ruleLabel\":\"NOM_Class\",\"valueLabel\":\"1\",\"operator\":\"<=\"}},{\"children\":[],\"rule\":{\"ruleLabel\":\"LOC_Class\",\"valueLabel\":\"2\",\"operator\":\"=\"}}],\"rule\":{\"operator\":\"OR\"}},{\"children\":[],\"rule\":{\"ruleLabel\":\"WMC_Class\",\"valueLabel\":\"1\",\"operator\":\"!=\"}}],\"rule\":{\"operator\":\"AND\"},\"outerName\":{\"innerName\":\"regraTeste\",\"isClassSmell\":true,\"uniqueIdentifier\":\"60786bd7-1bca-4fcd-a549-c329ad7306f4\"}}";
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(jsonNode);
            CustomNode node = manager.jsonToGUI(json, new DraggingObject());
            //TODO adicionar aqui verificações


            //Left side:
            String jsonNodeLeft = "{\"children\":[{\"children\":[{\"children\":[],\"rule\":{\"ruleLabel\":\"NOM_Class\",\"valueLabel\":\"1\",\"operator\":\"<=\"}},{\"children\":[],\"rule\":{\"ruleLabel\":\"LOC_Class\",\"valueLabel\":\"2\",\"operator\":\"=\"}}],\"rule\":{\"operator\":\"OR\"}},{\"children\":[{\"children\":[],\"rule\":{\"ruleLabel\":\"NOM_Class\",\"valueLabel\":\"1\",\"operator\":\"<=\"}},{\"children\":[],\"rule\":{\"ruleLabel\":\"LOC_Class\",\"valueLabel\":\"2\",\"operator\":\"=\"}}],\"rule\":{\"operator\":\"OR\"}}],\"rule\":{\"operator\":\"AND\"},\"outerName\":{\"innerName\":\"regraTeste\",\"isClassSmell\":true,\"uniqueIdentifier\":\"60786bd7-1bca-4fcd-a549-c329ad7306f4\"}}";
            JSONObject jsonLeft = (JSONObject) parser.parse(jsonNodeLeft);
            CustomNode nodeLeft = manager.jsonToGUI(jsonLeft, new DraggingObject());


            //To test with children size == 1
            String jsonNodeOneChild = "{\"children\":[{\"children\":[{\"children\":[{\"children\":[],\"rule\":{\"ruleLabel\":\"NOM_Class\",\"valueLabel\":\"1\",\"operator\":\"<=\"}},{\"children\":[],\"rule\":{\"ruleLabel\":\"LOC_Class\",\"valueLabel\":\"2\",\"operator\":\"=\"}}],\"rule\":{\"operator\":\"OR\"}},{\"children\":[{\"children\":[],\"rule\":{\"ruleLabel\":\"NOM_Class\",\"valueLabel\":\"1\",\"operator\":\"<=\"}},{\"children\":[],\"rule\":{\"ruleLabel\":\"LOC_Class\",\"valueLabel\":\"2\",\"operator\":\"=\"}}],\"rule\":{\"operator\":\"OR\"}}],\"rule\":{\"operator\":\"OR\"}}],\"rule\":{\"operator\":\"AND\"},\"outerName\":{\"innerName\":\"regraTeste\",\"isClassSmell\":true,\"uniqueIdentifier\":\"60786bd7-1bca-4fcd-a549-c329ad7306f4\"}}";
            JSONParser parserOneChild = new JSONParser();
            JSONObject jsonOneChild = (JSONObject) parser.parse(jsonNodeOneChild);
            CustomNode nodeOneChild = manager.jsonToGUI(jsonOneChild, new DraggingObject());


            //TODO should we test the other branches?!


        } catch (IOException | ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ;

    }

    @Test
    void testJsonObjectToCustomNode() throws ParseException{
        String jsonNode = "{\"children\":[{\"children\":[],\"rule\":{\"ruleLabel\":\"NOM_Class\",\"valueLabel\":\"1\",\"operator\":\"<=\"}},{\"children\":[],\"rule\":{\"ruleLabel\":\"LOC_Class\",\"valueLabel\":\"2\",\"operator\":\">=\"}}],\"rule\":{\"operator\":\"OR\"},\"outerName\":{\"innerName\":\"regra2\",\"isClassSmell\":true,\"uniqueIdentifier\":\"67052887-d0ee-4803-940d-366cde54c7b6\"}}";

        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(jsonNode);


        CustomNode node = manager.jsonObjectToCustomNode(json, new DraggingObject());
        assertEquals(node.getOperator(), RuleOperator.OR);


    }



    @Test
    void testGetChild() {
        DraggingObject draggingObject = new DraggingObject();

        ConditionBlock conditionBlock = new ConditionBlock(RuleOperator.EQUAL, new MetricBlock("class_test_rule"), "test_value", draggingObject);
        ArrayList<CustomNode> allBlocks = new ArrayList<>();
        allBlocks.add(conditionBlock);

        testGuiToJSONObject(allBlocks,0);
        Assertions.assertEquals(0, manager.getChild(conditionBlock, allBlocks).size());


        LogicBlock andBlock = new LogicBlock(draggingObject, RuleOperator.OR, "redTestColor");
        andBlock.addToRight(conditionBlock);

        allBlocks.add(andBlock);

        Assertions.assertEquals(1, manager.getChild(andBlock, allBlocks).size());


        ConditionBlock newConditionBlock = new ConditionBlock(RuleOperator.EQUAL, new MetricBlock("class_test_rule"), "test_value", draggingObject);
        allBlocks.add(newConditionBlock);
        andBlock.addToLeft(newConditionBlock);

        Assertions.assertEquals(2, manager.getChild(andBlock, allBlocks).size());

        allBlocks.remove(andBlock);
        allBlocks.add(0, andBlock); // To have this as the first and , and, therefor, as the parent!
        testGuiToJSONObject(allBlocks,2);
    }


    void testGuiToJSONObject(ArrayList<CustomNode> customNodeArrayList, int childrenSize) {
        String ruleName = "testRule";
        boolean isClassSmell = true;
        JSONObject json = manager.guiToJSONObject(customNodeArrayList, ruleName, isClassSmell);

        Assertions.assertEquals((String)((JSONObject)json.get("outerName")).get("innerName"),ruleName );
        Assertions.assertEquals((Boolean)((JSONObject)json.get("outerName")).get("isClassSmell"),isClassSmell );

        Assertions.assertEquals(childrenSize,((List) json.get("children")).size());

    }

}

