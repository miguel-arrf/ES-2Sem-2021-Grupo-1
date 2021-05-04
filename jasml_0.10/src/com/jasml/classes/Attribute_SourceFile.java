/*
 * Author jyang Created on 2006-4-3 19:20:26
 */
package com.jasml.classes;

public class Attribute_SourceFile extends Attribute {
    public int sourcefile_index;

    public Attribute_SourceFile(int attrLength, int sourcefile_index) {
        super(Constants.ATTRIBUTE_SourceFile, attrLength );
        this.sourcefile_index = sourcefile_index;
    }

}