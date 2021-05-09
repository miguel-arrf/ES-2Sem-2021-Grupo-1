/*
 * Author jyang
 * Created on 2006-4-3 19:19:30
 */
package com.jasml.classes;

public class Attribute_ConstantValue extends Attribute{
    public int constant_value_index;
    public Attribute_ConstantValue(int attrLength,int constant_value_index){
        super(Constants.ATTRIBUTE_ConstantValue,attrLength );
        this.constant_value_index = constant_value_index;
    }
}
