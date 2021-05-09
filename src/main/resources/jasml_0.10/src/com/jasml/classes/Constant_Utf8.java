/*
 * Author jyang Created on 2006-4-3 14:39:06
 */
package com.jasml.classes;

public class Constant_Utf8 extends ConstantPoolItem {
    public String bytes;

    public Constant_Utf8(String bytes) {
        super(Constants.CONSTANT_Utf8);
        this.bytes = bytes;
    }

    public String toString() {
        return "Constant_Utf8[bytes=" + bytes + "]";
    }
}