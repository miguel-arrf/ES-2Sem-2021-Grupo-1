/*
 * Author jyang Created on 2006-4-3 14:37:48
 */
package com.jasml.classes;

public class Constant_Methodref extends ConstantPoolItem {
    public int class_index;

    public int name_and_type_index;

    public Constant_Methodref(int class_index, int name_and_type_index) {
        super(Constants.CONSTANT_Methodref);
        this.class_index = class_index;
        this.name_and_type_index = name_and_type_index;
    }
    public String toString(){
        return "Constant_Methodref[class_index="+Integer.toString(class_index)+" , name_and_type_index="+name_and_type_index+"]";
    }
}