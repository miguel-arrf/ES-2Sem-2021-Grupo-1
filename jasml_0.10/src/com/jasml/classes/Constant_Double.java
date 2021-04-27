/*
 * Author jyang Created on 2006-4-3 14:38:52
 */
package com.jasml.classes;

public class Constant_Double extends ConstantPoolItem {
    public double value;

    public Constant_Double(double value) {
        super(Constants.CONSTANT_Double);
        this.value = value;
    }
    public String toString(){
        return "Constant_Double[value="+Double.toString(value)+"]";
    }
}