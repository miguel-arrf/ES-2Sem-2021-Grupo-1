/*
 * Author jyang
 * Created on 2006-4-6 11:23:56
 */
package com.jasml.helper;

import java.util.StringTokenizer;

import com.jasml.classes.Constants;

public class Util {

	public static String accessFlagToString_Class(short accessFlag) {
		StringBuffer buf = new StringBuffer();
		if ((accessFlag & Constants.ACCESS_FLAG_CLASS_PUBLIC) != 0)
			buf.append("public ");
		if ((accessFlag & Constants.ACCESS_FLAG_CLASS_FINAL) != 0)
			buf.append("final ");
		if ((accessFlag & Constants.ACCESS_FLAG_CLASS_ABSTRACT) != 0)
			buf.append("abstract ");
		if ((accessFlag & Constants.ACCESS_FLAG_CLASS_INTERFACE) != 0)
			buf.append("interface ");
		else {
			buf.append("class ");
		}
		return buf.toString().trim();
	}

	public static short getAccessFlag_Class(String s) {
		short ret = 0x0;
		if (s.indexOf("public") != -1)
			ret = (short) (ret | Constants.ACCESS_FLAG_METHOD_PUBLIC);

		if (s.indexOf("final") != -1)
			ret = (short) (ret | Constants.ACCESS_FLAG_METHOD_FINAL);

		if (s.indexOf("abstract") != -1)
			ret = (short) (ret | Constants.ACCESS_FLAG_CLASS_ABSTRACT);

		if (s.indexOf("interface") != -1)
			ret = (short) (ret | Constants.ACCESS_FLAG_CLASS_INTERFACE);

		if (s.indexOf("class") != -1) {
			/**
			 * copied from jvm specification
			 * All new compilers to the instruction set of the Java virtual machine should set the ACC_SUPER flag.
			 */
			ret = (short) (ret | Constants.ACCESS_FLAG_CLASS_SUPER);
		}
		return ret;

	}

	public static String accessFlagToString_Field(short accessFlag) {
		StringBuffer buf = new StringBuffer();
		if ((accessFlag & Constants.ACCESS_FLAG_FIELD_PUBLIC) != 0)
			buf.append("public ");
		if ((accessFlag & Constants.ACCESS_FLAG_FIELD_PRIVATE) != 0)
			buf.append("private ");
		if ((accessFlag & Constants.ACCESS_FLAG_FIELD_PROTECTED) != 0)
			buf.append("protected ");
		if ((accessFlag & Constants.ACCESS_FLAG_FIELD_STATIC) != 0)
			buf.append("static ");
		if ((accessFlag & Constants.ACCESS_FLAG_FIELD_FINAL) != 0)
			buf.append("final ");
		if ((accessFlag & Constants.ACCESS_FLAG_FIELD_VOLATILE) != 0)
			buf.append("volatile ");
		if ((accessFlag & Constants.ACCESS_FLAG_FIELD_TRANSIENT) != 0)
			buf.append("transient ");
		return buf.toString().trim();
	}

	public static String accessFlagToString_Method(short accessFlag) {
		StringBuffer buf = new StringBuffer();
		if ((accessFlag & Constants.ACCESS_FLAG_METHOD_PUBLIC) != 0)
			buf.append("public ");
		if ((accessFlag & Constants.ACCESS_FLAG_METHOD_PRIVATE) != 0)
			buf.append("private ");
		if ((accessFlag & Constants.ACCESS_FLAG_METHOD_PROTECTED) != 0)
			buf.append("protected ");
		if ((accessFlag & Constants.ACCESS_FLAG_METHOD_STATIC) != 0)
			buf.append("static ");
		if ((accessFlag & Constants.ACCESS_FLAG_METHOD_FINAL) != 0)
			buf.append("final ");
		if ((accessFlag & Constants.ACCESS_FLAG_METHOD_SYNCHRONIZED) != 0)
			buf.append("synchronized ");
		if ((accessFlag & Constants.ACCESS_FLAG_METHOD_NATIVE) != 0)
			buf.append("native ");
		if ((accessFlag & Constants.ACCESS_FLAG_METHOD_ABSTRACT) != 0)
			buf.append("abstract ");
		if ((accessFlag & Constants.ACCESS_FLAG_METHOD_STRICT) != 0)
			buf.append("strictfp ");
		return buf.toString().trim();
	}

	public static short getAccessFlag_Method(String s) {
		short ret = 0x0;
		if (s.indexOf("public") != -1)
			ret = (short) (ret | Constants.ACCESS_FLAG_METHOD_PUBLIC);
		if (s.indexOf("private") != -1)
			ret = (short) (ret | Constants.ACCESS_FLAG_METHOD_PRIVATE);

		if (s.indexOf("protected") != -1)
			ret = (short) (ret | Constants.ACCESS_FLAG_METHOD_PROTECTED);

		if (s.indexOf("static") != -1)
			ret = (short) (ret | Constants.ACCESS_FLAG_METHOD_STATIC);

		if (s.indexOf("final") != -1)
			ret = (short) (ret | Constants.ACCESS_FLAG_METHOD_FINAL);

		if (s.indexOf("synchronized") != -1)
			ret = (short) (ret | Constants.ACCESS_FLAG_METHOD_SYNCHRONIZED);

		if (s.indexOf("native") != -1)
			ret = (short) (ret | Constants.ACCESS_FLAG_METHOD_NATIVE);

		if (s.indexOf("abstract") != -1)
			ret = (short) (ret | Constants.ACCESS_FLAG_METHOD_ABSTRACT);

		if (s.indexOf("strictfp") != -1)
			ret = (short) (ret | Constants.ACCESS_FLAG_METHOD_STRICT);
		return ret;
	}

	public static short getAccessFlag_Field(String s) {
		short ret = 0x0;
		if (s.indexOf("public") != -1)
			ret = (short) (ret | Constants.ACCESS_FLAG_METHOD_PUBLIC);
		if (s.indexOf("private") != -1)
			ret = (short) (ret | Constants.ACCESS_FLAG_METHOD_PRIVATE);

		if (s.indexOf("protected") != -1)
			ret = (short) (ret | Constants.ACCESS_FLAG_METHOD_PROTECTED);

		if (s.indexOf("static") != -1)
			ret = (short) (ret | Constants.ACCESS_FLAG_METHOD_STATIC);

		if (s.indexOf("final") != -1)
			ret = (short) (ret | Constants.ACCESS_FLAG_METHOD_FINAL);

		if (s.indexOf("volatile") != -1)
			ret = (short) (ret | Constants.ACCESS_FLAG_FIELD_VOLATILE);

		if (s.indexOf("transient") != -1)
			ret = (short) (ret | Constants.ACCESS_FLAG_FIELD_TRANSIENT);
		return ret;

	}

	public static boolean hasMethodBody(short accessFlag) {
		if ((accessFlag & Constants.ACCESS_FLAG_METHOD_ABSTRACT) != 0 || (accessFlag & Constants.ACCESS_FLAG_METHOD_NATIVE) != 0) {
			return false;
		} else {
			return true;
		}
	}

	public static String methodParameterToString(String paras) {
		if (paras == null || paras.trim().length() == 0) {
			return "";
		}
		StringBuffer buf = new StringBuffer();
		String brackets;
		int ti;

		for (int i = 0; i < paras.length(); i++) {
			switch (paras.charAt(i)) {
			case 'B':
				buf.append("byte");
				break;
			case 'C':
				buf.append("char");
				break;
			case 'D':
				buf.append("double");
				break;
			case 'F':
				buf.append("float");
				break;
			case 'I':
				buf.append("int");
				break;
			case 'J':
				buf.append("long");
				break;
			case 'S':
				buf.append("short");
				break;
			case 'Z':
				buf.append("boolean");
				break;
			case 'V':
				buf.append("void");
				break;
			case '[':
				brackets = "[]";
				while (paras.charAt(++i) == '[') {
					brackets = brackets + "[]";
				}
				if (paras.charAt(i) == 'L') {
					ti = paras.indexOf((int) ';', i);
					buf.append(Util.descriptorToString(paras.substring(i, ti + 1)));
					i = ti;
				} else {
					buf.append(Util.descriptorToString(Character.toString(paras.charAt(i))));
				}
				buf.append(brackets);
				break;
			case 'L':
				ti = paras.indexOf((int) ';', i);
				buf.append(Util.descriptorToString(paras.substring(i, ti + 1)));
				i = ti;
				break;
			default:
				buf.append("[unknow paras(" + paras.charAt(i) + ")]");
			}
			buf.append(",");
		}
		buf.deleteCharAt(buf.length() - 1);
		return buf.toString();
	}

	public static String descriptorToString(String type) {
		if (type == null || type.trim().length() == 0) {
			return "";
		}
		switch (type.charAt(0)) {
		case 'B':
			return "byte";
		case 'C':
			return "char";
		case 'D':
			return "double";
		case 'F':
			return "float";
		case 'I':
			return "int";
		case 'J':
			return "long";
		case 'L':
			return type.substring(1, type.length() - 1).replace('/', '.');
		case 'S':
			return "short";
		case 'Z':
			return "boolean";

		case '[':
			int dim = 1;
			String brackets = "[]";
			while (type.charAt(dim) == '[') {
				dim++;
				brackets = brackets + "[]";
			}
			return descriptorToString(type.substring(dim)) + brackets;
		case 'V':
			return "void";
		default:
			return "[unknow type(" + type + ")]";
		}
	}

	/*
	 * A type of any class must starts with 'L'
	 */
	public static String toInnerType(String normalType) {
		if (normalType == null || normalType.length() == 0)
			return "";

		String ret = "";
		int i;
		for (i = normalType.length() - 1; i >= 0; i--) {
			if (normalType.charAt(i) == '[') {
				ret = ret + "[";
			} else if (normalType.charAt(i) != ']') {
				break;
			}
		}
		normalType = normalType.substring(0, i + 1).trim();

		if (normalType.equals("byte") == true)
			ret = ret + "B";
		else if (normalType.equals("char") == true)
			ret = ret + "C";
		else if (normalType.equals("double") == true)
			ret = ret + "D";
		else if (normalType.equals("float") == true)
			ret = ret + "F";
		else if (normalType.equals("int") == true)
			ret = ret + "I";
		else if (normalType.equals("long") == true)
			ret = ret + "J";
		else if (normalType.equals("short") == true)
			ret = ret + "S";
		else if (normalType.equals("boolean") == true)
			ret = ret + "Z";
		else if (normalType.equals("void") == true)
			ret = "V";
		else
			ret = ret + "L" + normalType.replace('.', '/') + ";";
		return ret;
	}

	/*
	 * 
	 * It is used in constant_class_info structure. Can be like
	 * java.lang.Object, java.lang.Object[][], int[][]. the difference with
	 * toInnerType() is when translating java.lang.Object, the toInnerType()
	 * will get Ljava/lang/Object; while this method will get java/lang/Object.
	 */
	public static String toInnerClassName(String className) {
		int i, dim = 0;

		for (i = 0; i < className.length(); i++) {
			if (className.charAt(i) == '[')
				dim++;
		}

		i = className.indexOf('[');

		if (i != -1) {
			className = className.substring(0, i);
		} else {
			return className.replace('.', '/');
		}

		className = toInnerType(className);
		while (dim-- > 0) {
			className = "[" + className;
		}
		return className;
	}

	public static String toInnerParameterTypes(String paras) {
		if (paras == null || paras.length() == 0) {
			return "";
		}
		StringBuffer buf = new StringBuffer();
		StringTokenizer token = new StringTokenizer(paras, ",");

		while (token.hasMoreTokens() == true) {
			buf.append(toInnerType(token.nextToken()));
		}
		return buf.toString();
	}

	public static String getInnerMethodDescriptor(String retType, String paras) {
		StringBuffer buf = new StringBuffer();
		buf.append('(');
		buf.append(toInnerParameterTypes(paras));
		buf.append(')');
		buf.append(toInnerType(retType));
		return buf.toString();
	}

	public static boolean isDigit(String s) {
		char c;
		for (int i = 0; i < s.length(); i++) {
			c = s.charAt(i);
			if (Character.isDigit(c) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * given an integer number, parse it in byte array, the highest values at
	 * front
	 * 
	 * @param num
	 * @param dim
	 * @return byte[]
	 */
	public static byte[] getBytes(int num, int dim) {
		byte[] ret = new byte[dim];
		if (dim == 1) {
			ret[0] = (byte) num;
		} else if (dim == 2) {
			ret[0] = (byte) ((num >> 8) & 0xFF);
			ret[1] = (byte) (num & 0xFF);
		} else if (dim == 4) {
			ret[0] = (byte) ((num >> 24) & 0xFF);
			ret[1] = (byte) ((num >> 16) & 0xFF);
			ret[2] = (byte) ((num >> 8) & 0xFF);
			ret[3] = (byte) (num & 0xFF);
		}
		return ret;
	}

	/**
	 * A constant class object contains inner representation of a class, like
	 * java/lang/Object, or inner representation of a class array, like
	 * Ljava/lang/Object; , or that of a primitive type array, like [[I
	 * 
	 * @param s
	 * @return String
	 */
	public static String constantClassToString(String s) {
		int i = s.indexOf('[');
		if (i == -1) {
			// not an array
			return s.replace('/', '.');
		} else {
			return descriptorToString(s);
		}
	}

	public static byte getPrimitiveTypeCode(String primitiveType) {
		int i;
		for (i = 0; i < Constants.TYPE_NAMES.length; i++) {
			if (Constants.TYPE_NAMES[i].equals(primitiveType) == true) {
				return (byte) i;
			}
		}
		// TODO:, throws exception
		return (byte) 0;
	}

	/**
	 * replaces any \b \t \n \f \r \" \' \\ chars to a viewable string.
	 * 
	 * @param s
	 * @return string
	 */
	public static String toViewableString(String s) {
		StringBuffer buf = new StringBuffer(s.length() + 10);
		char c;
		buf.append('"');
		int len = s.length();
		for (int i = 0; i < len; i++) {
			c = s.charAt(i);
			switch (c) {
			case '\b':
				buf.append("\\b");
				break;
			case '\t':
				buf.append("\\t");
				break;
			case '\n':
				buf.append("\\n");
				break;
			case '\f':
				buf.append("\\f");
				break;
			case '\r':
				buf.append("\\r");
				break;
			case '\\':
				buf.append("\\\\");
				break;
			case '"':
				buf.append("\\\"");
				break;
			case '!':
			case '\'':
			case '#':
			case '$':
			case '%':
			case '&':
			case '(':
			case ')':
			case '*':
			case '+':
			case ',':
			case '-':
			case '.':
			case '/':
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			case ':':
			case ';':
			case '<':
			case '=':
			case '>':
			case '?':
			case '@':
			case 'A':
			case 'B':
			case 'C':
			case 'D':
			case 'E':
			case 'F':
			case 'G':
			case 'H':
			case 'I':
			case 'J':
			case 'K':
			case 'L':
			case 'M':
			case 'N':
			case 'O':
			case 'P':
			case 'Q':
			case 'R':
			case 'S':
			case 'T':
			case 'U':
			case 'V':
			case 'W':
			case 'X':
			case 'Y':
			case 'Z':
			case '[':
			case ']':
			case '^':
			case '_':
			case '`':
			case 'a':
			case 'b':
			case 'c':
			case 'd':
			case 'e':
			case 'f':
			case 'g':
			case 'h':
			case 'i':
			case 'j':
			case 'k':
			case 'l':
			case 'm':
			case 'n':
			case 'o':
			case 'p':
			case 'q':
			case 'r':
			case 's':
			case 't':
			case 'u':
			case 'v':
			case 'w':
			case 'x':
			case 'y':
			case 'z':
			case '{':
			case '|':
			case '}':
			case '~':
			case ' ':
				buf.append(c);
				break;
			default:
				buf.append(getUnicodeChar(c));
			}

		}
		buf.append('"');
		return buf.toString();
	}

	public static String getUnicodeChar(char c) {
		return "\\u" + Digits[(c & 0xF000) >> 12] + Digits[(c & 0x0F00) >> 8] + Digits[(c & 0x00F0) >> 4] + Digits[(c & 0x000F)];
	}

	public final static char[] Digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	public final static int[] Numbers = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0/*'0'*/, 1/*'1'*/, 2/*'2'*/, 3/*'3'*/, 4/*'4'*/, 5/*'5'*/, 6/*'6'*/, 7/*'7'*/, 8/*'8'*/,
			9/*'9'*/, 0, 0, 0, 0, 0, 0, 0, 10/*'A'*/, 11/*'B'*/, 12/*'C'*/, 13/*'D'*/, 14/*'E'*/, 15/*'F'*/, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10/*'a'*/, 11/*'b'*/, 12/*'c'*/, 13/*'d'*/, 14/*'e'*/, 15 /*'f'*/};

	// \b \t \n \f \r \"  \\ chars to a viewable string
	public static String parseViewableString(String s) {
		int len = s.length();
		StringBuffer buf = new StringBuffer(len);
		char c;
		int ti;
		for (int i = 0; i < len; i++) {
			c = s.charAt(i);
			if (c == '\\') {
				i++;
				if (i < len) {
					switch (s.charAt((i))) {
					case 'b':
						buf.append('\b');
						break;
					case 't':
						buf.append('\t');
						break;
					case 'n':
						buf.append('\n');
						break;
					case 'f':
						buf.append('\f');
						break;
					case 'r':
						buf.append('\r');
						break;
					case '"':
						buf.append('\"');
						break;
					case '\\':
						buf.append('\\');
						break;
					case 'u':
						//four digits
						ti = 0;
						i++;
						c = s.charAt(i);
						ti = Numbers[(int) c] << 12;
						i++;
						c = s.charAt(i);
						ti = ti | (Numbers[(int) c] << 8);
						i++;
						c = s.charAt(i);
						ti = ti | (Numbers[(int) c] << 4);
						i++;
						c = s.charAt(i);
						ti = ti | Numbers[(int) c];
						buf.append((char) ti);
					}
				}
			} else {
				buf.append(c);
			}
		}
		return buf.toString();
	}

	public static String padChar(String s, int len, char padChar) {
		if (s.length() >= len) {
			return s;
		}
		StringBuffer buf = new StringBuffer(len);
		buf.append(s);
		for (int i = 0; i < len - s.length(); i++) {
			buf.append(padChar);
		}
		return buf.toString();
	}

	public static int getNum(byte[] bytes) {
		if (bytes.length == 1) {
			return bytes[0] & 0xFF;
		} else if (bytes.length == 2) {
			return ((bytes[0] & 0xff) << 8) + (bytes[1] & 0xff);
		} else if (bytes.length == 4) {
			return (((bytes[0] & 0xff) << 24) + ((bytes[1] & 0xff) << 16) + ((bytes[2] & 0xff) << 8) + (bytes[3] & 0xff));
		}
		return -1;
	}

	public static int getSignedNum(byte[] bytes) {
		if (bytes.length == 1) {
			return bytes[0];
		} else if (bytes.length == 2) {
			return ((bytes[0]) << 8) | ((bytes[1]) & 0xFF);
		} else if (bytes.length == 4) {
			return (bytes[0] << 24) | ((bytes[1] << 16) & 0xFF0000) | ((bytes[2] << 8) & 0xFF00) + (bytes[3] & 0xFF);
		}
		return -1;
	}

	public static void main(String[] args) {
		int i = 0;
		switch (i) {
		case 0:
			System.out.println(i++);
		case 1:
			System.out.println(i++);
		case 2:
			System.out.println(i);
		case 3:
			System.out.println(i);
		case 4:
			System.out.println(i);
		}

	}

}