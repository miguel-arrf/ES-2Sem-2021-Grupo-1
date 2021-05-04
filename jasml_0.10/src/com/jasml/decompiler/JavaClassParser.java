/*
 * Author jyang Created on 2006-4-2 21:18:16
 */
package com.jasml.decompiler;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.jasml.classes.Attribute;
import com.jasml.classes.Attribute_Code;
import com.jasml.classes.Attribute_ConstantValue;
import com.jasml.classes.Attribute_Deprecated;
import com.jasml.classes.Attribute_Exceptions;
import com.jasml.classes.Attribute_InnerClasses;
import com.jasml.classes.Attribute_LineNumberTable;
import com.jasml.classes.Attribute_LocalVariableTable;
import com.jasml.classes.Attribute_SourceFile;
import com.jasml.classes.Attribute_Synthetic;
import com.jasml.classes.ConstantPool;
import com.jasml.classes.ConstantPoolItem;
import com.jasml.classes.Constant_Class;
import com.jasml.classes.Constant_Double;
import com.jasml.classes.Constant_Fieldref;
import com.jasml.classes.Constant_Float;
import com.jasml.classes.Constant_Integer;
import com.jasml.classes.Constant_InterfaceMethodref;
import com.jasml.classes.Constant_Long;
import com.jasml.classes.Constant_Methodref;
import com.jasml.classes.Constant_NameAndType;
import com.jasml.classes.Constant_String;
import com.jasml.classes.Constant_Utf8;
import com.jasml.classes.Constants;
import com.jasml.classes.Field;
import com.jasml.classes.JavaClass;
import com.jasml.classes.Method;
import com.jasml.helper.OpcodeHelper;
import com.jasml.helper.OpcodeInfo;
import com.jasml.helper.Util;


public class JavaClassParser {

	DataInputStream in;

	int magic;

	int minor_Version;

	int major_Version;

	short constant_Pool_Count;

	ConstantPool constantPool;

	short access_flags;

	int this_class;

	int super_class;

	int interfaces_count;

	// the array storing interface indexes into constant pool
	int[] interfaces;

	int fields_count;

	Field[] fields;

	int methods_count;

	Method[] methods;

	int attributes_count;

	Attribute[] attributes;

	public JavaClass parseClass(File classFile) throws IOException {
		JavaClass ret = null;
		try {
			FileInputStream fsin = new FileInputStream(classFile);
			in = new DataInputStream(fsin);

			readMagic();
			readVersion();
			readConstant_Pool_Count();
			readConstantPool();
			// prt(constantPool); // 
			readAccess_flags();
			readThis_class();
			readSuper_class();
			readInterfaces();
			readFields();
			readMethods();
			readAttributes();

			ret = new JavaClass();
			ret.magic = magic;
			ret.minor_version = minor_Version;
			ret.major_version = major_Version;
			ret.constant_pool_count = constant_Pool_Count;
			ret.constantPool = constantPool;
			ret.access_flags = access_flags;
			ret.this_class = this_class;
			ret.super_class = super_class;
			ret.interfaces_count = interfaces_count;
			ret.interfaces = interfaces;
			ret.fields_count = fields_count;
			ret.fields = fields;
			ret.methods_count = methods_count;
			ret.methods = methods;
			ret.attributes_count = attributes_count;
			ret.attributes = attributes;
		} finally {
			try {
				in.close();
			} catch (Exception e) {

			}
		}
		return ret;
	}

	private void readMagic() throws IOException {
		prt("#magic");
		magic = in.readInt();
	}

	private void readVersion() throws IOException {
		prt("#version");
		minor_Version = in.readUnsignedShort();
		major_Version = in.readUnsignedShort();
	}

	private void readConstant_Pool_Count() throws IOException {
		prt("#constant pool");
		constant_Pool_Count = (short) in.readUnsignedShort();
	}

	private void readConstantPool() throws IOException {
		ConstantPoolItem[] items = new ConstantPoolItem[constant_Pool_Count];
		byte tag;
		for (int i = 1; i < constant_Pool_Count; i++) {
			tag = in.readByte();

			switch (tag) {
			case Constants.CONSTANT_Class:
				items[i] = new Constant_Class(in.readUnsignedShort());
				break;
			case Constants.CONSTANT_Fieldref:
				items[i] = new Constant_Fieldref(in.readUnsignedShort(), in.readUnsignedShort());
				break;
			case Constants.CONSTANT_Methodref:
				items[i] = new Constant_Methodref(in.readUnsignedShort(), in.readUnsignedShort());
				break;
			case Constants.CONSTANT_InterfaceMethodref:
				items[i] = new Constant_InterfaceMethodref(in.readUnsignedShort(), in.readUnsignedShort());
				break;
			case Constants.CONSTANT_String:
				items[i] = new Constant_String(in.readUnsignedShort());
				break;
			case Constants.CONSTANT_Integer:
				items[i] = new Constant_Integer(in.readInt());
				break;
			case Constants.CONSTANT_Float:
				items[i] = new Constant_Float(in.readFloat());
				break;
			case Constants.CONSTANT_Long:
				items[i] = new Constant_Long(in.readLong());
				i++;
				break;
			case Constants.CONSTANT_Double:
				items[i] = new Constant_Double(in.readDouble());
				i++;
				break;
			case Constants.CONSTANT_NameAndType:
				items[i] = new Constant_NameAndType(in.readUnsignedShort(), in.readUnsignedShort());
				break;
			case Constants.CONSTANT_Utf8:
				items[i] = new Constant_Utf8(in.readUTF());
				break;
			default:
				throw new IOException("Error inputing class file, unexpected tag:" + tag + ". i = " + i);
			}
		}
		constantPool = new ConstantPool(items);
	}

	private void readAccess_flags() throws IOException {
		access_flags = (short) in.readUnsignedShort();	
	}

	private void readThis_class() throws IOException {
		this_class = in.readUnsignedShort();
	}

	private void readSuper_class() throws IOException {
		super_class = in.readUnsignedShort();
	}

	private void readInterfaces() throws IOException {
		prt("#interfaces");
		interfaces_count = in.readUnsignedShort();
		if (interfaces_count != 0) {
			interfaces = new int[interfaces_count];
			for (int i = 0; i < interfaces_count; i++) {
				interfaces[i] = in.readUnsignedShort();
			}
		}
	}

	private void readFields() throws IOException {
		prt("#fields");
		fields_count = in.readUnsignedShort();
		if (fields_count != 0) {
			fields = new Field[fields_count];
			for (int i = 0; i < fields_count; i++) {
				prt("#field :" + i);
				fields[i] = readField(in);
			}
		}
	}

	private void readMethods() throws IOException {
		prt("#methods");
		methods_count = in.readUnsignedShort();
		if (methods_count != 0) {
			methods = new Method[methods_count];
			for (int i = 0; i < methods_count; i++) {
				methods[i] = readMethod(in);
			}
		}
	}

	private void readAttributes() throws IOException {
		prt("#class attributes");
		attributes_count = in.readUnsignedShort();
		if (attributes_count != 0) {
			attributes = new Attribute[attributes_count];
			for (int i = 0; i < attributes_count; i++) {
				prt("#class attribute :" + i);
				attributes[i] = readAttribute(in);
			}
		}
	}

	private Field readField(DataInputStream in) throws IOException {
		int access_flags = in.readUnsignedShort();
		int name_index = in.readUnsignedShort();
		int descriptor_index = in.readUnsignedShort();
		int attributes_count = in.readUnsignedShort();
		Attribute[] attributes = null;
		if (attributes_count != 0) {
			attributes = new Attribute[attributes_count];

			for (int i = 0; i < attributes_count; i++) {
				attributes[i] = readAttribute(in);
			}
		}
		return new Field(access_flags, name_index, descriptor_index, attributes_count, attributes);
	}

	private Method readMethod(DataInputStream in) throws IOException {
		int access_flags = in.readUnsignedShort();
		int name_index = in.readUnsignedShort();
		int descriptor_index = in.readUnsignedShort();
		int attributes_count = in.readUnsignedShort();
		prt("#method :" + constantPool.getConstant(name_index));
		Attribute[] attributes = null;
		if (attributes_count != 0) {
			attributes = new Attribute[attributes_count];

			for (int i = 0; i < attributes_count; i++) {
				attributes[i] = readAttribute(in);
			}
		}
		return new Method(access_flags, name_index, descriptor_index, attributes_count, attributes);

	}

	private Attribute readAttribute(DataInputStream in) throws IOException {
		prt("#Attribute");
		Attribute attribute = null;
		int attribute_name_index = in.readUnsignedShort();
		int attribute_length = in.readInt();
		String attribute_name = ((Constant_Utf8) constantPool.getConstant(attribute_name_index)).bytes;
		int i = 0;

		// get the attribute names
		for (; i < Constants.ATTRIBUTE_NAMES.length; i++) {
			if (attribute_name.equals(Constants.ATTRIBUTE_NAMES[i]) == true) {
				break;
			}
		}

		if (i != Constants.ATTRIBUTE_NAMES.length) {
			// known attribute
			switch (i) {
			case Constants.ATTRIBUTE_SourceFile:
				attribute = new Attribute_SourceFile(attribute_length, in.readUnsignedShort());
				break;
			case Constants.ATTRIBUTE_ConstantValue:
				attribute = new Attribute_ConstantValue(attribute_length, in.readUnsignedShort());
				break;
			case Constants.ATTRIBUTE_Code:
				int max_stack = in.readUnsignedShort();
				int max_locals = in.readUnsignedShort();
				int code_length = in.readInt();
				Attribute_Code.Opcode[] codes = null;
				if (code_length != 0) {
					byte[] bcode = new byte[code_length];
					in.read(bcode);
					codes = parseOpcodes(bcode);
				}
				int exception_table_length = in.readUnsignedShort();
				Attribute_Code.ExceptionTableItem[] exceptionTable = null;
				if (exception_table_length != 0) {
					exceptionTable = new Attribute_Code.ExceptionTableItem[exception_table_length];
					for (int counter = 0; counter < exception_table_length; counter++) {
						exceptionTable[counter] = readExceptionTableItem(in);
					}
				}

				int attributes_count = in.readUnsignedShort();
				Attribute[] attributes = null;
				if (attributes_count != 0) {
					attributes = new Attribute[attributes_count];
					for (int counter = 0; counter < attributes_count; counter++) {
						attributes[counter] = readAttribute(in);
					}
				}
				attribute = new Attribute_Code(attribute_length, max_stack, max_locals, code_length, codes, exception_table_length, exceptionTable,
						attributes_count, attributes);
				break;

			case Constants.ATTRIBUTE_Exceptions:
				int number_of_exceptions = in.readUnsignedShort();
				int[] exception_index_table = null;
				if (number_of_exceptions != 0) {
					exception_index_table = new int[number_of_exceptions];
					for (int counter = 0; counter < number_of_exceptions; counter++) {
						exception_index_table[counter] = in.readUnsignedShort();
					}
				}
				attribute = new Attribute_Exceptions(attribute_length, number_of_exceptions, exception_index_table);
				break;

			case Constants.ATTRIBUTE_InnerClasses:
				int number_of_classes = in.readUnsignedShort();
				Attribute_InnerClasses.InnerClass[] innerClasses = null;
				if (number_of_classes != 0) {
					innerClasses = new Attribute_InnerClasses.InnerClass[number_of_classes];
					for (int counter = 0; counter < number_of_classes; counter++) {
						innerClasses[counter] = readInnerClass(in);
					}
				}
				attribute = new Attribute_InnerClasses(attribute_length, number_of_classes, innerClasses);
				break;

			case Constants.ATTRIBUTE_Synthetic:
				attribute = new Attribute_Synthetic();
				break;

			case Constants.ATTRIBUTE_LineNumberTable:
				int line_number_table_length = in.readUnsignedShort();
				Attribute_LineNumberTable.LineNumber[] line_number_table = null;
				if (line_number_table_length != 0) {
					line_number_table = new Attribute_LineNumberTable.LineNumber[line_number_table_length];
					for (int counter = 0; counter < line_number_table_length; counter++) {
						line_number_table[counter] = readLineNumber(in);
					}
				}
				attribute = new Attribute_LineNumberTable(attribute_length, line_number_table_length, line_number_table);
				break;

			case Constants.ATTRIBUTE_LocalVariableTable:
				int local_variable_table_length = in.readUnsignedShort();
				Attribute_LocalVariableTable.LocalVariable[] local_variable_table = null;
				if (local_variable_table_length != 0) {
					local_variable_table = new Attribute_LocalVariableTable.LocalVariable[local_variable_table_length];
					for (int counter = 0; counter < local_variable_table_length; counter++) {
						local_variable_table[counter] = readLocalVariable(in);
					}
				}
				attribute = new Attribute_LocalVariableTable(attribute_length, local_variable_table_length, local_variable_table);
				break;
			case Constants.ATTRIBUTE_Deprecated:
				attribute = new Attribute_Deprecated();
				break;
			}
		} else {
			byte[] info = new byte[attribute_length];
			in.read(info);
			attribute = new Attribute(attribute_name_index, attribute_length, info);
		}
		return attribute;
	}

	private Attribute_Code.ExceptionTableItem readExceptionTableItem(DataInputStream in) throws IOException {
		return new Attribute_Code.ExceptionTableItem(in.readUnsignedShort(), in.readUnsignedShort(), in.readUnsignedShort(), in.readUnsignedShort());
	}

	private Attribute_InnerClasses.InnerClass readInnerClass(DataInputStream in) throws IOException {
		return new Attribute_InnerClasses.InnerClass(in.readUnsignedShort(), in.readUnsignedShort(), in.readUnsignedShort(), in.readUnsignedShort());
	}

	private Attribute_LineNumberTable.LineNumber readLineNumber(DataInputStream in) throws IOException {
		return new Attribute_LineNumberTable.LineNumber(in.readUnsignedShort(), in.readUnsignedShort());
	}

	private Attribute_LocalVariableTable.LocalVariable readLocalVariable(DataInputStream in) throws IOException {
		return new Attribute_LocalVariableTable.LocalVariable(in.readUnsignedShort(), in.readUnsignedShort(), in.readUnsignedShort(), in
				.readUnsignedShort(), in.readUnsignedShort());
	}

	private Attribute_Code.Opcode[] parseOpcodes(byte[] bytes) {
		ArrayList ret = new ArrayList(bytes.length);
		Attribute_Code.Opcode op;
		OpcodeInfo opInfo;
		int offset;
		byte[][] operands = null;
		boolean wide = false;

		for (int i = 0; i < bytes.length; i++) {
			offset = i;
			opInfo = OpcodeHelper.OPCODES[0xFF & bytes[i]];
			if (opInfo.operandsLength == null) {
				operands = null;
			} else {
				if (opInfo.opcode == Constants.TABLESWITCH) {
					int padnum = i % 4;
					padnum = 3 - padnum;
					i = i + padnum + 1;

					// defualt value
					byte[] defaultb = new byte[4];
					for (int t = 0; t < 4; t++) {
						defaultb[t] = bytes[i + t];
					}
					i = i + 4;

					// low value
					byte[] lowb = new byte[4];
					for (int t = 0; t < 4; t++) {
						lowb[t] = bytes[i + t];
					}
					i = i + 4;

					// high byte
					byte[] highb = new byte[4];
					for (int t = 0; t < 4; t++) {
						highb[t] = bytes[i + t];
					}
					i = i + 4;

					int high = Util.getNum(highb);
					int low = Util.getNum(lowb);
					int total = high - low + 1 + 3 + 1; // number of jump offsets + one byte of opcode + high byte +low byte+defualt byte+padding byte
					if (total < 0) {
						total = 1;
					}
					operands = new byte[total][4];
					operands[0] = new byte[padnum];
					for (int ti = 0; ti < padnum; ti++) {
						operands[0][ti] = (byte) 0;
					}
					operands[1] = defaultb;
					operands[2] = lowb;
					operands[3] = highb;

					for (int t = 4; t < total; t++) {
						operands[t][0] = bytes[i++];
						operands[t][1] = bytes[i++];
						operands[t][2] = bytes[i++];
						operands[t][3] = bytes[i++];
					}
					i--;
				} else if (opInfo.opcode == Constants.LOOKUPSWITCH) {
					int padnum = i % 4;
					padnum = 3 - padnum;
					i = i + padnum + 1;

					// defualt value
					byte[] defaultb = new byte[4];
					for (int t = 0; t < 4; t++) {
						defaultb[t] = bytes[i + t];
					}
					i = i + 4;

					// npair value
					byte[] npairb = new byte[4];
					for (int t = 0; t < 4; t++) {
						npairb[t] = bytes[i + t];
					}
					i = i + 4;

					int npair = Util.getNum(npairb);

					int total = npair * 2 + 3; // npair *2 +defualt byte+one byte of opcode+padding bytes
					operands = new byte[total][4];
					operands[0] = new byte[padnum];
					for (int ti = 0; ti < padnum; ti++) {
						operands[0][ti] = (byte) 0;
					}
					operands[1] = defaultb;
					operands[2] = npairb;
					for (int t = 3; t < total; t++) {
						operands[t][0] = bytes[i++];
						operands[t][1] = bytes[i++];
						operands[t][2] = bytes[i++];
						operands[t][3] = bytes[i++];
					}
					i--;
				} else if (opInfo.opcode == Constants.WIDE) {
					wide = true;
				} else if (wide == true) {
					operands = new byte[opInfo.operandsLength.length][];
					for (int j = 0; j < opInfo.operandsLength.length; j++) {
						operands[j] = new byte[opInfo.operandsLength[j]];
						for (int t = 0; t < opInfo.operandsLength[j] + 1; t++) {
							operands[j][t] = bytes[++i];
						}
					}
					wide = false;
				} else {

					operands = new byte[opInfo.operandsLength.length][];
					for (int j = 0; j < opInfo.operandsLength.length; j++) {
						operands[j] = new byte[opInfo.operandsLength[j]];
						for (int t = 0; t < opInfo.operandsLength[j]; t++) {
							operands[j][t] = bytes[++i];
						}
					}
				}
			}
			op = new Attribute_Code.Opcode(offset, opInfo.opcode, operands);
			ret.add(op);
		}
		return (Attribute_Code.Opcode[]) ret.toArray(new Attribute_Code.Opcode[0]);
	}

	private static void prt(Object s) {
		//				 System.out.println(s);
	} //	class DataInputStream {
	//		java.io.DataInputStream in;
	//
	//		public DataInputStream(FileInputStream ins) {
	//			in = new java.io.DataInputStream(ins);
	//		}
	//
	//		public int readInt() throws IOException {
	//			int i = in.readInt();
	//			prt("int:" + i);
	//			return i;
	//		}
	//
	//		public int readUnsignedShort() throws IOException {
	//			int i = in.readUnsignedShort();
	//			prt("sho:" + i);
	//			return i;
	//		}
	//
	//		public byte readByte() throws IOException {
	//			byte b = in.readByte();
	//			prt("bye:" + b);
	//			return b;
	//		}
	//
	//		public int read(byte[] b) throws IOException {
	//			int i = in.read(b);
	//			StringBuffer buf = new StringBuffer();
	//			for (int t = 0; t < i; t++) {
	//				buf.append(Integer.toString(b[t] & 0xFF) + ",");
	//			}
	//			prt(buf.toString());
	//			return i;
	//		}
	//
	//		public float readFloat() throws IOException {
	//			float f = in.readFloat();
	//			prt("flo:" + f);
	//			return f;
	//
	//		}
	//
	//		public long readLong() throws IOException {
	//			long l = in.readLong();
	//			prt("long:" + l);
	//			return l;
	//		}
	//
	//		public double readDouble() throws IOException {
	//			double d = in.readDouble();
	//			prt("dou:" + d);
	//			return d;
	//		}
	//
	//		public String readUTF() throws IOException {
	//			String d = in.readUTF();
	//			prt("str:" + d);
	//			return d;
	//		}
	//
	//		public void close() throws IOException {
	//			in.close();
	//		}
	//
	//	}
}