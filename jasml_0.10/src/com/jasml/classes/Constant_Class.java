/*
 * Author jyang Created on 2006-4-3 14:36:45
 */
package com.jasml.classes;

public class Constant_Class extends ConstantPoolItem {
    public int name_index;

    public Constant_Class(int name_index) {
        super(Constants.CONSTANT_Class);
        this.name_index = name_index;
    }
    public String toString(){
        return "Constant_Class[name_index="+Integer.toString(name_index)+"]";
    }
 
}