/*
 * Author jyang
 * Created on 2006-4-3 14:35:22
 */
package com.jasml.classes;
/**
 * The super class for all the constant pool items.
 *
 */
public class ConstantPoolItem {
    public byte tag;
    public String tagName;
    public ConstantPoolItem(byte tag){
        this.tag = tag;
        tagName = Constants.CONSTANT_NAMES[tag];
    }
}
