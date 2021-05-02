package RuleEditor;

import code_smell_detection.CodeSmell;
import code_smell_detection.RuleOperator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


class RuleFileManagerTest {

    private static final RuleFileManager manager = new RuleFileManager();
    private static File testFile;

    private final JFXPanel panel = new JFXPanel();


    @Test
    void testLoading() {
        URL url = this.getClass().getResource("/testRule.rule");
        testFile = new File(url.getFile());
        manager.setFile(testFile);

        Assertions.assertNotNull(manager.getFile());

        ArrayList<JSONObject> arrayListJson;
        try {
            arrayListJson = manager.loadJSONRuleFile();
            Assertions.assertEquals(arrayListJson.size(), 2);

            testSave(arrayListJson);

            jsonObjectToCodeSmell(arrayListJson.get(0));

            jsonObjectToCodeSmell(arrayListJson.get(1));
        } catch (IOException | ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }



    void testSave(ArrayList<JSONObject> customNodes) {
        ObservableList<JSONObject> jsonObservableList = FXCollections.observableArrayList(customNodes);
        manager.saveJSONListToFile(jsonObservableList);

        manager.setFile(null);

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
    void testSaveJSONListToFile() {return ;
    }

    @Test
    void testSaveFile() {
        return ;
    }


    @Test
    void testJsonObjectToCodeSmell() {
        return ;
    }

    @Test
    void testCustomNodeToJSON() {
        return ;
    }

    @Test
    void testJsonToGUI() {
        return ;    }

    @Test
    void testJsonObjectToCustomNode() {
        return ;    }

    @Test
    void testAddChildrenToObject() {
        return ;    }

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

