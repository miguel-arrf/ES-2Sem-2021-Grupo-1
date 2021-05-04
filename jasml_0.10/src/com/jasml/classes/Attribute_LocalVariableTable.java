/*
 * Author jyang Created on 2006-4-3 19:21:58
 */
package com.jasml.classes;

public class Attribute_LocalVariableTable extends Attribute {
    public int local_variable_table_length;

    public LocalVariable[] local_variable_table;

    public Attribute_LocalVariableTable(int attrLength, int local_variable_table_length, LocalVariable[] local_variable_table) {
        super(Constants.ATTRIBUTE_LocalVariableTable, attrLength );
        this.local_variable_table_length = local_variable_table_length;
        this.local_variable_table = local_variable_table;
    }

    public static class LocalVariable {
        public int start_pc;

        public int length;

        public int name_index;

        public int descriptor_index;

        public int index;

        public LocalVariable(int start_pc, int length, int name_index, int descriptor_index, int index) {
            this.start_pc = start_pc;
            this.length = length;
            this.name_index = name_index;
            this.descriptor_index = descriptor_index;
            this.index = index;
        }
    }

}