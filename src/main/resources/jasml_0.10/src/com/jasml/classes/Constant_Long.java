/*
 * Author jyang Created on 2006-4-3 14:38:46
 */
package com.jasml.classes;

public class Constant_Long extends ConstantPoolItem {
    public long value;

    public Constant_Long( long value) {
        super(Constants.CONSTANT_Long);
        this.value = value;
    }

    public long getValue() {
        return value;
    }
    public String toString(){
        return "Constant_Long[value="+Long.toString(value)+"]";
    }
}