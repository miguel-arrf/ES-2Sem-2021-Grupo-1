package RuleEditor;

import code_smell_detection.RuleNode;
import code_smell_detection.RuleOperator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class RuleComplete {

    private CustomNode node;
    private RuleComplete left;
    private RuleComplete right;


    public static void createCodeSmell(ArrayList<CustomNode> customNodeArrayList){
        CustomNode firstCustomNode = customNodeArrayList.get(0); // Yes, we need a auxiliary variable to hold which one is the first....
        System.out.println("firstCustomNode: " + firstCustomNode);

        RuleNode rule = createRuleNode(firstCustomNode, customNodeArrayList);

        System.out.println("Rule: "+ rule);
        print(rule, "\t");

    }

    private static void print(RuleNode ruleNode, String tab){
        System.out.println(tab + "RuleNode-element: " + ruleNode.getElement());
        if(ruleNode.getLeft_node() != null){
            print(ruleNode.getLeft_node(), tab + "\t");
        }
        if(ruleNode.getRight_node() != null){
            print(ruleNode.getRight_node(), tab + "\t");
        }


        System.out.println(tab + "---");


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

    public static ArrayList<CustomNode> getChild(CustomNode parent, ArrayList<CustomNode> customNodeArrayList){
        ArrayList<CustomNode> childrens = new ArrayList<>();

        if (parent.getType() == Types.ConditionBlock){
            return  new ArrayList<>();
        }else{
            //É And ou OR block
            AndBlock andBlock = (AndBlock) parent;

            for(CustomNode node: customNodeArrayList){

                if(andBlock.getLeftLabelVBox().getChildren().contains(node.getGraphicalRepresentation()) || andBlock.getRightLabelVBox().getChildren().contains(node.getGraphicalRepresentation())){
                    childrens.add(node);
                }
            }

        }

        return childrens;
    }

    private String getRule(){
        String rule = "";
        if(node.getType() == Types.AndBlock) {
            AndBlock c;
            c = (AndBlock) node;
            rule = c.getLabel();
        }
        if(node.getType() == Types.ConditionBlock) {
            ConditionBlock c;
            c = (ConditionBlock) node;
            if(c.getRuleBlock() == null)
                rule = "() " + c.getOperator() + " " + c.getValue();
            else
                rule = c.getRuleBlock().getRuleMessage() + " " + c.getOperator() + " " + c.getValue();
        }
        if (left == null && right == null)
            return rule;
        if (left == null)
            return rule + " (" + right.getRule() + ")";
        if (right == null)
            return "(" + left.getRule() + ") " + rule;
        return "(" + left.getRule() + ") " + rule + " (" + right.getRule() + ")";
    }



}
