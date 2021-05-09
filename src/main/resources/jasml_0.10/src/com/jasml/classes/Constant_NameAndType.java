/*
 * Author jyang
 * Created on 2006-4-3 14:38:59
 */
package com.jasml.classes;

public class Constant_NameAndType  extends ConstantPoolItem {
    public int name_index;
    public int descriptor_index;
    public Constant_NameAndType( int name_index, int descriptor_index){
        super(Constants.CONSTANT_NameAndType);
        this.name_index = name_index;
        this.descriptor_index = descriptor_index;
    }
    public String toString(){
        return "Constant_NameAndType[name_index="+name_index+" , descriptor_index="+descriptor_index+"]";
    }
}
