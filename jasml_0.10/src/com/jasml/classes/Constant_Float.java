/*
 * Author jyang Created on 2006-4-3 14:38:39
 */
package com.jasml.classes;

public class Constant_Float extends ConstantPoolItem {
    public float value;

    public Constant_Float( float value) {
        super(Constants.CONSTANT_Float);
        this.value = value;
    }
    public String toString(){
        return "Constant_Float[value="+Float.toString(value)+"]";
    }
}