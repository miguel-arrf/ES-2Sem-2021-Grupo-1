/*
 * Author jyang Created on 2006-4-4 9:45:27
 */
package com.jasml.classes;

public class Method {
    public int access_flags;

    public int name_index;

    public int descriptor_index;

    public int attributes_count;

    // possible attributes are Code , Exceptions, Synthetic, and Deprecated
    public Attribute[] attributes;

    public Method(int access_flags, int name_index, int descriptor_index, int attributes_count, Attribute[] attributes) {
        this.access_flags = access_flags;
        this.name_index = name_index;
        this.descriptor_index = descriptor_index;
        this.attributes_count = attributes_count;
        this.attributes = attributes;
    }
}