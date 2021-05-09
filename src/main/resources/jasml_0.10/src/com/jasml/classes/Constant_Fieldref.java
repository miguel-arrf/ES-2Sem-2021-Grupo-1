/*
 * Author jyang Created on 2006-4-3 14:37:25
 */
package com.jasml.classes;

public class Constant_Fieldref extends ConstantPoolItem {
    public int class_index;

    public int name_and_type_index;

    public Constant_Fieldref(int class_index, int name_and_type_index) {
        super(Constants.CONSTANT_Fieldref);
        this.class_index = class_index;
        this.name_and_type_index = name_and_type_index;
    }
    public String toString(){
        return "Constant_Fieldref[class_index="+Integer.toString(class_index)+" , name_and_type_index="+name_and_type_index+"]";
    }
}