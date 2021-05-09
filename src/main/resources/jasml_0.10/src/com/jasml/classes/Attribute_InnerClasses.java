/*
 * Author jyang Created on 2006-4-3 19:21:21
 */
package com.jasml.classes;

public class Attribute_InnerClasses extends Attribute {
    public int number_of_classes;

    public InnerClass[] innerClasses;

    public Attribute_InnerClasses(int attrLength, int number_of_classes, InnerClass[] innerClasses) {        
        super(Constants.ATTRIBUTE_InnerClasses, attrLength );
        this.number_of_classes = number_of_classes;
        this.innerClasses = innerClasses;
    }

    public static class InnerClass {
        public int inner_class_info_index;

        public int outer_class_info_index;

        public int inner_name_index;

        public int inner_class_access_flags;

        public InnerClass(int inner_class_info_index, int outer_class_info_index, int inner_name_index, int inner_class_access_flags) {
            this.inner_class_info_index = inner_class_info_index;
            this.outer_class_info_index = outer_class_info_index;
            this.inner_name_index = inner_name_index;
            this.inner_class_access_flags = inner_class_access_flags;
        }
    }

}