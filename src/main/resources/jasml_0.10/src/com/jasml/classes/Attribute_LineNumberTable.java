/*
 * Author jyang Created on 2006-4-3 19:21:43
 */
package com.jasml.classes;

public class Attribute_LineNumberTable extends Attribute {
    public int line_number_table_length;

    public LineNumber[] lineNumberTable;

    public Attribute_LineNumberTable(int attrLength, int line_number_table_length, LineNumber[] lineNumberTable) {
        super(Constants.ATTRIBUTE_LineNumberTable, attrLength );
        this.line_number_table_length = line_number_table_length;
        this.lineNumberTable = lineNumberTable;
    }

    public static class LineNumber {
        public LineNumber(int start_pc, int line_number) {
            this.start_pc = start_pc;
            this.line_number = line_number;
        }
        public int start_pc;

        public int line_number;
    }
    

}