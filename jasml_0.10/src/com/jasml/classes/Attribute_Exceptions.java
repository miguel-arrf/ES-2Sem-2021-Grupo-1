/*
 * Author jyang
 * Created on 2006-4-3 19:21:05
 */
package com.jasml.classes;

public class Attribute_Exceptions extends Attribute{
    public int number_of_exceptions;
    public int[] exception_index_table;

    public Attribute_Exceptions(int attrLength, int number_of_exceptions, int[] exception_index_table){
        super(Constants.ATTRIBUTE_Exceptions,attrLength );
        this.number_of_exceptions = number_of_exceptions;
        this.exception_index_table = exception_index_table;
    }

}
