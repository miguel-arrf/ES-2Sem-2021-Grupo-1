/*
 * Author jyang Created on 2006-4-3 14:38:30
 */
package com.jasml.classes;

public class Constant_Integer extends ConstantPoolItem {

    public int value;

    public Constant_Integer(int value) {
        super(Constants.CONSTANT_Integer);
        this.value = value;
    }
    public String toString(){
        return "Constant_Integer[value="+Integer.toString(value)+"]";
    }
}