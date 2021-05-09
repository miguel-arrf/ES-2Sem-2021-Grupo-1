/*
 * Author jyang Created on 2006-4-3 14:35:41
 */
package com.jasml.classes;

public class ConstantPool {
    ConstantPoolItem[] poolItems;

    public ConstantPool(ConstantPoolItem[] items) {
        poolItems = items;
    }

    public ConstantPoolItem getConstant(int index) {
        return poolItems[index];
    }

    public int getConstantPoolCount() {
        return poolItems.length;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        ConstantPoolItem item;
        for (int i = 0; i < poolItems.length; i++) {
            item = poolItems[i];
            if (item != null) {
                buf.append(i + " : " + item.toString() + "\r\n");
                if (item instanceof Constant_Double || item instanceof Constant_Long) {
                    i++;
                }
            } else {
                buf.append(i + " : N/A\r\n");
            }
        }
        return buf.toString();
    }
    

}