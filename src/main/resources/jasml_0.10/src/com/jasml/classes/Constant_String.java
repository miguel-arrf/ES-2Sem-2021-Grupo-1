/*
 * Author jyang Created on 2006-4-3 14:38:17
 */
package com.jasml.classes;

public class Constant_String extends ConstantPoolItem {
    public int string_index;

    public Constant_String( int string_index) {
        super(Constants.CONSTANT_String);
        this.string_index = string_index;
    }
    public String toString(){
        return "Constant_String[string_index="+string_index+"]";
    }
}