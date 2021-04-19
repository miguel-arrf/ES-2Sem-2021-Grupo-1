package RuleEditor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class RuleComplete {

    private CustomNodes node;
    private RuleComplete left;
    private RuleComplete right;

    public RuleComplete(CustomNodes node) {
        this.node = node;
        if(node.getType() == Types.AndBlock)
            FinalMain.search.add(this);
        this.left = null;
        this.right = null;
    }

    public void addToSide(CustomNodes toAdd, CustomNodes parent, String side) {
        if(side == "left") {
            search(parent).left = new RuleComplete(toAdd);
        }

        else {
            search(parent).right = new RuleComplete(toAdd);
        }
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

    public void saveRule(){
        File file = new File("Rules.txt");
        try {
            file.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(file, true));
            out.write(getRule() + "\n");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public RuleComplete search(CustomNodes block){
        for (RuleComplete rule : FinalMain.search){
            System.out.println(rule.node + " = " + block);
            if (rule.node.equals(block))
                return rule;
        }
        return null;
    }
}
