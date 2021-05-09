/*
 * Author jyang Created on 2006-4-3 14:41:07
 */
package com.jasml.classes;

import java.util.HashSet;

/*
 * This class defines several common used constants.
 */
public class Constants {
	public final static String LINE_SEPARATER = "\r\n";

	/**
	 * Tags in constant pool to denote type of constant.
	 */
	public final static byte CONSTANT_Utf8 = 1;

	public final static byte CONSTANT_Integer = 3;

	public final static byte CONSTANT_Float = 4;

	public final static byte CONSTANT_Long = 5;

	public final static byte CONSTANT_Double = 6;

	public final static byte CONSTANT_Class = 7;

	public final static byte CONSTANT_Fieldref = 9;

	public final static byte CONSTANT_String = 8;

	public final static byte CONSTANT_Methodref = 10;

	public final static byte CONSTANT_InterfaceMethodref = 11;

	public final static byte CONSTANT_NameAndType = 12;

	// these are names mapping to the constant tags.
	public final static String[] CONSTANT_NAMES = { "", "CONSTANT_Utf8", "", "CONSTANT_Integer", "CONSTANT_Float", "CONSTANT_Long",
			"CONSTANT_Double", "CONSTANT_Class", "CONSTANT_String", "CONSTANT_Fieldref", "CONSTANT_Methodref", "CONSTANT_InterfaceMethodref",
			"CONSTANT_NameAndType" };

	// these are attribute names and tags, the tags is not defined by jvm but
	// used only internally.
	public final static String[] ATTRIBUTE_NAMES = { "", "SourceFile", "ConstantValue", "Code", "Exceptions", "InnerClasses", "Synthetic",
			"LineNumberTable", "LocalVariableTable", "Deprecated" };

	public final static byte ATTRIBUTE_SourceFile = 1;

	public final static byte ATTRIBUTE_ConstantValue = 2;

	public final static byte ATTRIBUTE_Code = 3;

	public final static byte ATTRIBUTE_Exceptions = 4;

	public final static byte ATTRIBUTE_InnerClasses = 5;

	public final static byte ATTRIBUTE_Synthetic = 6;

	public final static byte ATTRIBUTE_LineNumberTable = 7;

	public final static byte ATTRIBUTE_LocalVariableTable = 8;

	public final static byte ATTRIBUTE_Deprecated = 9;

	// Bellow are access flags for fields
	public final static short ACCESS_FLAG_FIELD_PUBLIC = 0x0001;

	public final static short ACCESS_FLAG_FIELD_PRIVATE = 0x0002;

	public final static short ACCESS_FLAG_FIELD_PROTECTED = 0x0004;

	public final static short ACCESS_FLAG_FIELD_STATIC = 0x0008;

	public final static short ACCESS_FLAG_FIELD_FINAL = 0x0010;

	public final static short ACCESS_FLAG_FIELD_VOLATILE = 0x0040;

	public final static short ACCESS_FLAG_FIELD_TRANSIENT = 0x0080;

	// Bellow are access flags for methods
	public final static short ACCESS_FLAG_METHOD_PUBLIC = 0x0001;

	public final static short ACCESS_FLAG_METHOD_PRIVATE = 0x0002;

	public final static short ACCESS_FLAG_METHOD_PROTECTED = 0x0004;

	public final static short ACCESS_FLAG_METHOD_STATIC = 0x0008;

	public final static short ACCESS_FLAG_METHOD_FINAL = 0x0010;

	public final static short ACCESS_FLAG_METHOD_SYNCHRONIZED = 0x0020;

	public final static short ACCESS_FLAG_METHOD_NATIVE = 0x0100;

	public final static short ACCESS_FLAG_METHOD_ABSTRACT = 0x0400;

	public final static short ACCESS_FLAG_METHOD_STRICT = 0x0800;

	// Bellow are access flags for classes
	public final static short ACCESS_FLAG_CLASS_PUBLIC = 0x0001;

	public final static short ACCESS_FLAG_CLASS_FINAL = 0x0010;

	public final static short ACCESS_FLAG_CLASS_SUPER = 0x0020;

	public final static short ACCESS_FLAG_CLASS_INTERFACE = 0x0200;

	public final static short ACCESS_FLAG_CLASS_ABSTRACT = 0x0400;

	/*
	 * JVM opcodes.
	 */
	public static final byte NOP = (byte) 0;

	public static final byte ACONST_NULL = (byte) 1;

	public static final byte ICONST_M1 = (byte) 2;

	public static final byte ICONST_0 = (byte) 3;

	public static final byte ICONST_1 = (byte) 4;

	public static final byte ICONST_2 = (byte) 5;

	public static final byte ICONST_3 = (byte) 6;

	public static final byte ICONST_4 = (byte) 7;

	public static final byte ICONST_5 = (byte) 8;

	public static final byte LCONST_0 = (byte) 9;

	public static final byte LCONST_1 = (byte) 10;

	public static final byte FCONST_0 = (byte) 11;

	public static final byte FCONST_1 = (byte) 12;

	public static final byte FCONST_2 = (byte) 13;

	public static final byte DCONST_0 = (byte) 14;

	public static final byte DCONST_1 = (byte) 15;

	public static final byte BIPUSH = (byte) 16;

	public static final byte SIPUSH = (byte) 17;

	public static final byte LDC = (byte) 18;

	public static final byte LDC_W = (byte) 19;

	public static final byte LDC2_W = (byte) 20;

	public static final byte ILOAD = (byte) 21;

	public static final byte LLOAD = (byte) 22;

	public static final byte FLOAD = (byte) 23;

	public static final byte DLOAD = (byte) 24;

	public static final byte ALOAD = (byte) 25;

	public static final byte ILOAD_0 = (byte) 26;

	public static final byte ILOAD_1 = (byte) 27;

	public static final byte ILOAD_2 = (byte) 28;

	public static final byte ILOAD_3 = (byte) 29;

	public static final byte LLOAD_0 = (byte) 30;

	public static final byte LLOAD_1 = (byte) 31;

	public static final byte LLOAD_2 = (byte) 32;

	public static final byte LLOAD_3 = (byte) 33;

	public static final byte FLOAD_0 = (byte) 34;

	public static final byte FLOAD_1 = (byte) 35;

	public static final byte FLOAD_2 = (byte) 36;

	public static final byte FLOAD_3 = (byte) 37;

	public static final byte DLOAD_0 = (byte) 38;

	public static final byte DLOAD_1 = (byte) 39;

	public static final byte DLOAD_2 = (byte) 40;

	public static final byte DLOAD_3 = (byte) 41;

	public static final byte ALOAD_0 = (byte) 42;

	public static final byte ALOAD_1 = (byte) 43;

	public static final byte ALOAD_2 = (byte) 44;

	public static final byte ALOAD_3 = (byte) 45;

	public static final byte IALOAD = (byte) 46;

	public static final byte LALOAD = (byte) 47;

	public static final byte FALOAD = (byte) 48;

	public static final byte DALOAD = (byte) 49;

	public static final byte AALOAD = (byte) 50;

	public static final byte BALOAD = (byte) 51;

	public static final byte CALOAD = (byte) 52;

	public static final byte SALOAD = (byte) 53;

	public static final byte ISTORE = (byte) 54;

	public static final byte LSTORE = (byte) 55;

	public static final byte FSTORE = (byte) 56;

	public static final byte DSTORE = (byte) 57;

	public static final byte ASTORE = (byte) 58;

	public static final byte ISTORE_0 = (byte) 59;

	public static final byte ISTORE_1 = (byte) 60;

	public static final byte ISTORE_2 = (byte) 61;

	public static final byte ISTORE_3 = (byte) 62;

	public static final byte LSTORE_0 = (byte) 63;

	public static final byte LSTORE_1 = (byte) 64;

	public static final byte LSTORE_2 = (byte) 65;

	public static final byte LSTORE_3 = (byte) 66;

	public static final byte FSTORE_0 = (byte) 67;

	public static final byte FSTORE_1 = (byte) 68;

	public static final byte FSTORE_2 = (byte) 69;

	public static final byte FSTORE_3 = (byte) 70;

	public static final byte DSTORE_0 = (byte) 71;

	public static final byte DSTORE_1 = (byte) 72;

	public static final byte DSTORE_2 = (byte) 73;

	public static final byte DSTORE_3 = (byte) 74;

	public static final byte ASTORE_0 = (byte) 75;

	public static final byte ASTORE_1 = (byte) 76;

	public static final byte ASTORE_2 = (byte) 77;

	public static final byte ASTORE_3 = (byte) 78;

	public static final byte IASTORE = (byte) 79;

	public static final byte LASTORE = (byte) 80;

	public static final byte FASTORE = (byte) 81;

	public static final byte DASTORE = (byte) 82;

	public static final byte AASTORE = (byte) 83;

	public static final byte BASTORE = (byte) 84;

	public static final byte CASTORE = (byte) 85;

	public static final byte SASTORE = (byte) 86;

	public static final byte POP = (byte) 87;

	public static final byte POP2 = (byte) 88;

	public static final byte DUP = (byte) 89;

	public static final byte DUP_X1 = (byte) 90;

	public static final byte DUP_X2 = (byte) 91;

	public static final byte DUP2 = (byte) 92;

	public static final byte DUP2_X1 = (byte) 93;

	public static final byte DUP2_X2 = (byte) 94;

	public static final byte SWAP = (byte) 95;

	public static final byte IADD = (byte) 96;

	public static final byte LADD = (byte) 97;

	public static final byte FADD = (byte) 98;

	public static final byte DADD = (byte) 99;

	public static final byte ISUB = (byte) 100;

	public static final byte LSUB = (byte) 101;

	public static final byte FSUB = (byte) 102;

	public static final byte DSUB = (byte) 103;

	public static final byte IMUL = (byte) 104;

	public static final byte LMUL = (byte) 105;

	public static final byte FMUL = (byte) 106;

	public static final byte DMUL = (byte) 107;

	public static final byte IDIV = (byte) 108;

	public static final byte LDIV = (byte) 109;

	public static final byte FDIV = (byte) 110;

	public static final byte DDIV = (byte) 111;

	public static final byte IREM = (byte) 112;

	public static final byte LREM = (byte) 113;

	public static final byte FREM = (byte) 114;

	public static final byte DREM = (byte) 115;

	public static final byte INEG = (byte) 116;

	public static final byte LNEG = (byte) 117;

	public static final byte FNEG = (byte) 118;

	public static final byte DNEG = (byte) 119;

	public static final byte ISHL = (byte) 120;

	public static final byte LSHL = (byte) 121;

	public static final byte ISHR = (byte) 122;

	public static final byte LSHR = (byte) 123;

	public static final byte IUSHR = (byte) 124;

	public static final byte LUSHR = (byte) 125;

	public static final byte IAND = (byte) 126;

	public static final byte LAND = (byte) 127;

	public static final byte IOR = (byte) 128;

	public static final byte LOR = (byte) 129;

	public static final byte IXOR = (byte) 130;

	public static final byte LXOR = (byte) 131;

	public static final byte IINC = (byte) 132;

	public static final byte I2L = (byte) 133;

	public static final byte I2F = (byte) 134;

	public static final byte I2D = (byte) 135;

	public static final byte L2I = (byte) 136;

	public static final byte L2F = (byte) 137;

	public static final byte L2D = (byte) 138;

	public static final byte F2I = (byte) 139;

	public static final byte F2L = (byte) 140;

	public static final byte F2D = (byte) 141;

	public static final byte D2I = (byte) 142;

	public static final byte D2L = (byte) 143;

	public static final byte D2F = (byte) 144;

	public static final byte I2B = (byte) 145;

	public static final byte INT2BYTE = (byte) 145;

	public static final byte I2C = (byte) 146;

	public static final byte INT2CHAR = (byte) 146;

	public static final byte I2S = (byte) 147;

	public static final byte INT2SHORT = (byte) 147;

	public static final byte LCMP = (byte) 148;

	public static final byte FCMPL = (byte) 149;

	public static final byte FCMPG = (byte) 150;

	public static final byte DCMPL = (byte) 151;

	public static final byte DCMPG = (byte) 152;

	public static final byte IFEQ = (byte) 153;

	public static final byte IFNE = (byte) 154;

	public static final byte IFLT = (byte) 155;

	public static final byte IFGE = (byte) 156;

	public static final byte IFGT = (byte) 157;

	public static final byte IFLE = (byte) 158;

	public static final byte IF_ICMPEQ = (byte) 159;

	public static final byte IF_ICMPNE = (byte) 160;

	public static final byte IF_ICMPLT = (byte) 161;

	public static final byte IF_ICMPGE = (byte) 162;

	public static final byte IF_ICMPGT = (byte) 163;

	public static final byte IF_ICMPLE = (byte) 164;

	public static final byte IF_ACMPEQ = (byte) 165;

	public static final byte IF_ACMPNE = (byte) 166;

	public static final byte GOTO = (byte) 167;

	public static final byte JSR = (byte) 168;

	public static final byte RET = (byte) 169;

	public static final byte TABLESWITCH = (byte) 170;

	public static final byte LOOKUPSWITCH = (byte) 171;

	public static final byte IRETURN = (byte) 172;

	public static final byte LRETURN = (byte) 173;

	public static final byte FRETURN = (byte) 174;

	public static final byte DRETURN = (byte) 175;

	public static final byte ARETURN = (byte) 176;

	public static final byte RETURN = (byte) 177;

	public static final byte GETSTATIC = (byte) 178;

	public static final byte PUTSTATIC = (byte) 179;

	public static final byte GETFIELD = (byte) 180;

	public static final byte PUTFIELD = (byte) 181;

	public static final byte INVOKEVIRTUAL = (byte) 182;

	public static final byte INVOKESPECIAL = (byte) 183;

	public static final byte INVOKENONVIRTUAL = (byte) 183; // Old name in JDK

	// 1.0

	public static final byte INVOKESTATIC = (byte) 184;

	public static final byte INVOKEINTERFACE = (byte) 185;

	public static final byte NEW = (byte) 187;

	public static final byte NEWARRAY = (byte) 188;

	public static final byte ANEWARRAY = (byte) 189;

	public static final byte ARRAYLENGTH = (byte) 190;

	public static final byte ATHROW = (byte) 191;

	public static final byte CHECKCAST = (byte) 192;

	public static final byte INSTANCEOF = (byte) 193;

	public static final byte MONITORENTER = (byte) 194;

	public static final byte MONITOREXIT = (byte) 195;

	public static final byte WIDE = (byte) 196;

	public static final byte MULTIANEWARRAY = (byte) 197;

	public static final byte IFNULL = (byte) 198;

	public static final byte IFNONNULL = (byte) 199;

	public static final byte GOTO_W = (byte) 200;

	public static final byte JSR_W = (byte) 201;

	/**
	 * Non-legal opcodes, may be used by JVM internally.
	 */
	public static final byte BREAKPOINT = (byte) 202;

	public static final byte LDC_QUICK = (byte) 203;

	public static final byte LDC_W_QUICK = (byte) 204;

	public static final byte LDC2_W_QUICK = (byte) 205;

	public static final byte GETFIELD_QUICK = (byte) 206;

	public static final byte PUTFIELD_QUICK = (byte) 207;

	public static final byte GETFIELD2_QUICK = (byte) 208;

	public static final byte PUTFIELD2_QUICK = (byte) 209;

	public static final byte GETSTATIC_QUICK = (byte) 210;

	public static final byte PUTSTATIC_QUICK = (byte) 211;

	public static final byte GETSTATIC2_QUICK = (byte) 212;

	public static final byte PUTSTATIC2_QUICK = (byte) 213;

	public static final byte INVOKEVIRTUAL_QUICK = (byte) 214;

	public static final byte INVOKENONVIRTUAL_QUICK = (byte) 215;

	public static final byte INVOKESUPER_QUICK = (byte) 216;

	public static final byte INVOKESTATIC_QUICK = (byte) 217;

	public static final byte INVOKEINTERFACE_QUICK = (byte) 218;

	public static final byte INVOKEVIRTUALOBJECT_QUICK = (byte) 219;

	public static final byte NEW_QUICK = (byte) 221;

	public static final byte ANEWARRAY_QUICK = (byte) 222;

	public static final byte MULTIANEWARRAY_QUICK = (byte) 223;

	public static final byte CHECKCAST_QUICK = (byte) 224;

	public static final byte INSTANCEOF_QUICK = (byte) 225;

	public static final byte INVOKEVIRTUAL_QUICK_W = (byte) 226;

	public static final byte GETFIELD_QUICK_W = (byte) 227;

	public static final byte PUTFIELD_QUICK_W = (byte) 228;

	public static final byte IMPDEP1 = (byte) 254;

	public static final byte IMPDEP2 = (byte) 255;

	public static final short UNDEFINED = -1;

	public static final short UNPREDICTABLE = -2;

	public static final short RESERVED = -3;

	public static final String ILLEGAL_OPCODE = "<illegal opcode>";

	public static final String ILLEGAL_TYPE = "<illegal type>";

	/**
	 * Names of opcodes.
	 */
	public static final String[] OPCODE_NAMES = { "nop", "aconst_null", "iconst_m1", "iconst_0", "iconst_1", "iconst_2", "iconst_3", "iconst_4",
			"iconst_5", "lconst_0", "lconst_1", "fconst_0", "fconst_1", "fconst_2", "dconst_0", "dconst_1", "bipush", "sipush", "ldc", "ldc_w",
			"ldc2_w", "iload", "lload", "fload", "dload", "aload", "iload_0", "iload_1", "iload_2", "iload_3", "lload_0", "lload_1", "lload_2",
			"lload_3", "fload_0", "fload_1", "fload_2", "fload_3", "dload_0", "dload_1", "dload_2", "dload_3", "aload_0", "aload_1", "aload_2",
			"aload_3", "iaload", "laload", "faload", "daload", "aaload", "baload", "caload", "saload", "istore", "lstore", "fstore", "dstore",
			"astore", "istore_0", "istore_1", "istore_2", "istore_3", "lstore_0", "lstore_1", "lstore_2", "lstore_3", "fstore_0", "fstore_1",
			"fstore_2", "fstore_3", "dstore_0", "dstore_1", "dstore_2", "dstore_3", "astore_0", "astore_1", "astore_2", "astore_3", "iastore",
			"lastore", "fastore", "dastore", "aastore", "bastore", "castore", "sastore", "pop", "pop2", "dup", "dup_x1", "dup_x2", "dup2", "dup2_x1",
			"dup2_x2", "swap", "iadd", "ladd", "fadd", "dadd", "isub", "lsub", "fsub", "dsub", "imul", "lmul", "fmul", "dmul", "idiv", "ldiv",
			"fdiv", "ddiv", "irem", "lrem", "frem", "drem", "ineg", "lneg", "fneg", "dneg", "ishl", "lshl", "ishr", "lshr", "iushr", "lushr", "iand",
			"land", "ior", "lor", "ixor", "lxor", "iinc", "i2l", "i2f", "i2d", "l2i", "l2f", "l2d", "f2i", "f2l", "f2d", "d2i", "d2l", "d2f", "i2b",
			"i2c", "i2s", "lcmp", "fcmpl", "fcmpg", "dcmpl", "dcmpg", "ifeq", "ifne", "iflt", "ifge", "ifgt", "ifle", "if_icmpeq", "if_icmpne",
			"if_icmplt", "if_icmpge", "if_icmpgt", "if_icmple", "if_acmpeq", "if_acmpne", "goto", "jsr", "ret", "tableswitch", "lookupswitch",
			"ireturn", "lreturn", "freturn", "dreturn", "areturn", "return", "getstatic", "putstatic", "getfield", "putfield", "invokevirtual",
			"invokespecial", "invokestatic", "invokeinterface", ILLEGAL_OPCODE, "new", "newarray", "anewarray", "arraylength", "athrow", "checkcast",
			"instanceof", "monitorenter", "monitorexit", "wide", "multianewarray", "ifnull", "ifnonnull", "goto_w", "jsr_w", "breakpoint",
			ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
			ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
			ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
			ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
			ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
			ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
			ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, "impdep1", "impdep2" };

	public static final HashSet OPCODE_NAMESET = new HashSet(400);
	static {
		OPCODE_NAMESET.add("nop");
		OPCODE_NAMESET.add("aconst_null");
		OPCODE_NAMESET.add("iconst_m1");
		OPCODE_NAMESET.add("iconst_0");
		OPCODE_NAMESET.add("iconst_1");
		OPCODE_NAMESET.add("iconst_2");
		OPCODE_NAMESET.add("iconst_3");
		OPCODE_NAMESET.add("iconst_4");
		OPCODE_NAMESET.add("iconst_5");
		OPCODE_NAMESET.add("lconst_0");
		OPCODE_NAMESET.add("lconst_1");
		OPCODE_NAMESET.add("fconst_0");
		OPCODE_NAMESET.add("fconst_1");
		OPCODE_NAMESET.add("fconst_2");
		OPCODE_NAMESET.add("dconst_0");
		OPCODE_NAMESET.add("dconst_1");
		OPCODE_NAMESET.add("bipush");
		OPCODE_NAMESET.add("sipush");
		OPCODE_NAMESET.add("ldc");
		OPCODE_NAMESET.add("ldc_w");
		OPCODE_NAMESET.add("ldc2_w");
		OPCODE_NAMESET.add("iload");
		OPCODE_NAMESET.add("lload");
		OPCODE_NAMESET.add("fload");
		OPCODE_NAMESET.add("dload");
		OPCODE_NAMESET.add("aload");
		OPCODE_NAMESET.add("iload_0");
		OPCODE_NAMESET.add("iload_1");
		OPCODE_NAMESET.add("iload_2");
		OPCODE_NAMESET.add("iload_3");
		OPCODE_NAMESET.add("lload_0");
		OPCODE_NAMESET.add("lload_1");
		OPCODE_NAMESET.add("lload_2");
		OPCODE_NAMESET.add("lload_3");
		OPCODE_NAMESET.add("fload_0");
		OPCODE_NAMESET.add("fload_1");
		OPCODE_NAMESET.add("fload_2");
		OPCODE_NAMESET.add("fload_3");
		OPCODE_NAMESET.add("dload_0");
		OPCODE_NAMESET.add("dload_1");
		OPCODE_NAMESET.add("dload_2");
		OPCODE_NAMESET.add("dload_3");
		OPCODE_NAMESET.add("aload_0");
		OPCODE_NAMESET.add("aload_1");
		OPCODE_NAMESET.add("aload_2");
		OPCODE_NAMESET.add("aload_3");
		OPCODE_NAMESET.add("iaload");
		OPCODE_NAMESET.add("laload");
		OPCODE_NAMESET.add("faload");
		OPCODE_NAMESET.add("daload");
		OPCODE_NAMESET.add("aaload");
		OPCODE_NAMESET.add("baload");
		OPCODE_NAMESET.add("caload");
		OPCODE_NAMESET.add("saload");
		OPCODE_NAMESET.add("istore");
		OPCODE_NAMESET.add("lstore");
		OPCODE_NAMESET.add("fstore");
		OPCODE_NAMESET.add("dstore");
		OPCODE_NAMESET.add("astore");
		OPCODE_NAMESET.add("istore_0");
		OPCODE_NAMESET.add("istore_1");
		OPCODE_NAMESET.add("istore_2");
		OPCODE_NAMESET.add("istore_3");
		OPCODE_NAMESET.add("lstore_0");
		OPCODE_NAMESET.add("lstore_1");
		OPCODE_NAMESET.add("lstore_2");
		OPCODE_NAMESET.add("lstore_3");
		OPCODE_NAMESET.add("fstore_0");
		OPCODE_NAMESET.add("fstore_1");
		OPCODE_NAMESET.add("fstore_2");
		OPCODE_NAMESET.add("fstore_3");
		OPCODE_NAMESET.add("dstore_0");
		OPCODE_NAMESET.add("dstore_1");
		OPCODE_NAMESET.add("dstore_2");
		OPCODE_NAMESET.add("dstore_3");
		OPCODE_NAMESET.add("astore_0");
		OPCODE_NAMESET.add("astore_1");
		OPCODE_NAMESET.add("astore_2");
		OPCODE_NAMESET.add("astore_3");
		OPCODE_NAMESET.add("iastore");
		OPCODE_NAMESET.add("lastore");
		OPCODE_NAMESET.add("fastore");
		OPCODE_NAMESET.add("dastore");
		OPCODE_NAMESET.add("aastore");
		OPCODE_NAMESET.add("bastore");
		OPCODE_NAMESET.add("castore");
		OPCODE_NAMESET.add("sastore");
		OPCODE_NAMESET.add("pop");
		OPCODE_NAMESET.add("pop2");
		OPCODE_NAMESET.add("dup");
		OPCODE_NAMESET.add("dup_x1");
		OPCODE_NAMESET.add("dup_x2");
		OPCODE_NAMESET.add("dup2");
		OPCODE_NAMESET.add("dup2_x1");
		OPCODE_NAMESET.add("dup2_x2");
		OPCODE_NAMESET.add("swap");
		OPCODE_NAMESET.add("iadd");
		OPCODE_NAMESET.add("ladd");
		OPCODE_NAMESET.add("fadd");
		OPCODE_NAMESET.add("dadd");
		OPCODE_NAMESET.add("isub");
		OPCODE_NAMESET.add("lsub");
		OPCODE_NAMESET.add("fsub");
		OPCODE_NAMESET.add("dsub");
		OPCODE_NAMESET.add("imul");
		OPCODE_NAMESET.add("lmul");
		OPCODE_NAMESET.add("fmul");
		OPCODE_NAMESET.add("dmul");
		OPCODE_NAMESET.add("idiv");
		OPCODE_NAMESET.add("ldiv");
		OPCODE_NAMESET.add("fdiv");
		OPCODE_NAMESET.add("ddiv");
		OPCODE_NAMESET.add("irem");
		OPCODE_NAMESET.add("lrem");
		OPCODE_NAMESET.add("frem");
		OPCODE_NAMESET.add("drem");
		OPCODE_NAMESET.add("ineg");
		OPCODE_NAMESET.add("lneg");
		OPCODE_NAMESET.add("fneg");
		OPCODE_NAMESET.add("dneg");
		OPCODE_NAMESET.add("ishl");
		OPCODE_NAMESET.add("lshl");
		OPCODE_NAMESET.add("ishr");
		OPCODE_NAMESET.add("lshr");
		OPCODE_NAMESET.add("iushr");
		OPCODE_NAMESET.add("lushr");
		OPCODE_NAMESET.add("iand");
		OPCODE_NAMESET.add("land");
		OPCODE_NAMESET.add("ior");
		OPCODE_NAMESET.add("lor");
		OPCODE_NAMESET.add("ixor");
		OPCODE_NAMESET.add("lxor");
		OPCODE_NAMESET.add("iinc");
		OPCODE_NAMESET.add("i2l");
		OPCODE_NAMESET.add("i2f");
		OPCODE_NAMESET.add("i2d");
		OPCODE_NAMESET.add("l2i");
		OPCODE_NAMESET.add("l2f");
		OPCODE_NAMESET.add("l2d");
		OPCODE_NAMESET.add("f2i");
		OPCODE_NAMESET.add("f2l");
		OPCODE_NAMESET.add("f2d");
		OPCODE_NAMESET.add("d2i");
		OPCODE_NAMESET.add("d2l");
		OPCODE_NAMESET.add("d2f");
		OPCODE_NAMESET.add("i2b");
		OPCODE_NAMESET.add("i2c");
		OPCODE_NAMESET.add("i2s");
		OPCODE_NAMESET.add("lcmp");
		OPCODE_NAMESET.add("fcmpl");
		OPCODE_NAMESET.add("fcmpg");
		OPCODE_NAMESET.add("dcmpl");
		OPCODE_NAMESET.add("dcmpg");
		OPCODE_NAMESET.add("ifeq");
		OPCODE_NAMESET.add("ifne");
		OPCODE_NAMESET.add("iflt");
		OPCODE_NAMESET.add("ifge");
		OPCODE_NAMESET.add("ifgt");
		OPCODE_NAMESET.add("ifle");
		OPCODE_NAMESET.add("if_icmpeq");
		OPCODE_NAMESET.add("if_icmpne");
		OPCODE_NAMESET.add("if_icmplt");
		OPCODE_NAMESET.add("if_icmpge");
		OPCODE_NAMESET.add("if_icmpgt");
		OPCODE_NAMESET.add("if_icmple");
		OPCODE_NAMESET.add("if_acmpeq");
		OPCODE_NAMESET.add("if_acmpne");
		OPCODE_NAMESET.add("goto");
		OPCODE_NAMESET.add("jsr");
		OPCODE_NAMESET.add("ret");
		OPCODE_NAMESET.add("tableswitch");
		OPCODE_NAMESET.add("lookupswitch");
		OPCODE_NAMESET.add("ireturn");
		OPCODE_NAMESET.add("lreturn");
		OPCODE_NAMESET.add("freturn");
		OPCODE_NAMESET.add("dreturn");
		OPCODE_NAMESET.add("areturn");
		OPCODE_NAMESET.add("return");
		OPCODE_NAMESET.add("getstatic");
		OPCODE_NAMESET.add("putstatic");
		OPCODE_NAMESET.add("getfield");
		OPCODE_NAMESET.add("putfield");
		OPCODE_NAMESET.add("invokevirtual");
		OPCODE_NAMESET.add("invokespecial");
		OPCODE_NAMESET.add("invokestatic");
		OPCODE_NAMESET.add("invokeinterface");
		OPCODE_NAMESET.add("new");
		OPCODE_NAMESET.add("newarray");
		OPCODE_NAMESET.add("anewarray");
		OPCODE_NAMESET.add("arraylength");
		OPCODE_NAMESET.add("athrow");
		OPCODE_NAMESET.add("checkcast");
		OPCODE_NAMESET.add("instanceof");
		OPCODE_NAMESET.add("monitorenter");
		OPCODE_NAMESET.add("monitorexit");
		OPCODE_NAMESET.add("wide");
		OPCODE_NAMESET.add("multianewarray");
		OPCODE_NAMESET.add("ifnull");
		OPCODE_NAMESET.add("ifnonnull");
		OPCODE_NAMESET.add("goto_w");
		OPCODE_NAMESET.add("jsr_w");
		OPCODE_NAMESET.add("breakpoint");
		OPCODE_NAMESET.add("impdep1");
		OPCODE_NAMESET.add("impdep2");
		OPCODE_NAMESET.add("NOP");
		OPCODE_NAMESET.add("ACONST_NULL");
		OPCODE_NAMESET.add("ICONST_M1");
		OPCODE_NAMESET.add("ICONST_0");
		OPCODE_NAMESET.add("ICONST_1");
		OPCODE_NAMESET.add("ICONST_2");
		OPCODE_NAMESET.add("ICONST_3");
		OPCODE_NAMESET.add("ICONST_4");
		OPCODE_NAMESET.add("ICONST_5");
		OPCODE_NAMESET.add("LCONST_0");
		OPCODE_NAMESET.add("LCONST_1");
		OPCODE_NAMESET.add("FCONST_0");
		OPCODE_NAMESET.add("FCONST_1");
		OPCODE_NAMESET.add("FCONST_2");
		OPCODE_NAMESET.add("DCONST_0");
		OPCODE_NAMESET.add("DCONST_1");
		OPCODE_NAMESET.add("BIPUSH");
		OPCODE_NAMESET.add("SIPUSH");
		OPCODE_NAMESET.add("LDC");
		OPCODE_NAMESET.add("LDC_W");
		OPCODE_NAMESET.add("LDC2_W");
		OPCODE_NAMESET.add("ILOAD");
		OPCODE_NAMESET.add("LLOAD");
		OPCODE_NAMESET.add("FLOAD");
		OPCODE_NAMESET.add("DLOAD");
		OPCODE_NAMESET.add("ALOAD");
		OPCODE_NAMESET.add("ILOAD_0");
		OPCODE_NAMESET.add("ILOAD_1");
		OPCODE_NAMESET.add("ILOAD_2");
		OPCODE_NAMESET.add("ILOAD_3");
		OPCODE_NAMESET.add("LLOAD_0");
		OPCODE_NAMESET.add("LLOAD_1");
		OPCODE_NAMESET.add("LLOAD_2");
		OPCODE_NAMESET.add("LLOAD_3");
		OPCODE_NAMESET.add("FLOAD_0");
		OPCODE_NAMESET.add("FLOAD_1");
		OPCODE_NAMESET.add("FLOAD_2");
		OPCODE_NAMESET.add("FLOAD_3");
		OPCODE_NAMESET.add("DLOAD_0");
		OPCODE_NAMESET.add("DLOAD_1");
		OPCODE_NAMESET.add("DLOAD_2");
		OPCODE_NAMESET.add("DLOAD_3");
		OPCODE_NAMESET.add("ALOAD_0");
		OPCODE_NAMESET.add("ALOAD_1");
		OPCODE_NAMESET.add("ALOAD_2");
		OPCODE_NAMESET.add("ALOAD_3");
		OPCODE_NAMESET.add("IALOAD");
		OPCODE_NAMESET.add("LALOAD");
		OPCODE_NAMESET.add("FALOAD");
		OPCODE_NAMESET.add("DALOAD");
		OPCODE_NAMESET.add("AALOAD");
		OPCODE_NAMESET.add("BALOAD");
		OPCODE_NAMESET.add("CALOAD");
		OPCODE_NAMESET.add("SALOAD");
		OPCODE_NAMESET.add("ISTORE");
		OPCODE_NAMESET.add("LSTORE");
		OPCODE_NAMESET.add("FSTORE");
		OPCODE_NAMESET.add("DSTORE");
		OPCODE_NAMESET.add("ASTORE");
		OPCODE_NAMESET.add("ISTORE_0");
		OPCODE_NAMESET.add("ISTORE_1");
		OPCODE_NAMESET.add("ISTORE_2");
		OPCODE_NAMESET.add("ISTORE_3");
		OPCODE_NAMESET.add("LSTORE_0");
		OPCODE_NAMESET.add("LSTORE_1");
		OPCODE_NAMESET.add("LSTORE_2");
		OPCODE_NAMESET.add("LSTORE_3");
		OPCODE_NAMESET.add("FSTORE_0");
		OPCODE_NAMESET.add("FSTORE_1");
		OPCODE_NAMESET.add("FSTORE_2");
		OPCODE_NAMESET.add("FSTORE_3");
		OPCODE_NAMESET.add("DSTORE_0");
		OPCODE_NAMESET.add("DSTORE_1");
		OPCODE_NAMESET.add("DSTORE_2");
		OPCODE_NAMESET.add("DSTORE_3");
		OPCODE_NAMESET.add("ASTORE_0");
		OPCODE_NAMESET.add("ASTORE_1");
		OPCODE_NAMESET.add("ASTORE_2");
		OPCODE_NAMESET.add("ASTORE_3");
		OPCODE_NAMESET.add("IASTORE");
		OPCODE_NAMESET.add("LASTORE");
		OPCODE_NAMESET.add("FASTORE");
		OPCODE_NAMESET.add("DASTORE");
		OPCODE_NAMESET.add("AASTORE");
		OPCODE_NAMESET.add("BASTORE");
		OPCODE_NAMESET.add("CASTORE");
		OPCODE_NAMESET.add("SASTORE");
		OPCODE_NAMESET.add("POP");
		OPCODE_NAMESET.add("POP2");
		OPCODE_NAMESET.add("DUP");
		OPCODE_NAMESET.add("DUP_X1");
		OPCODE_NAMESET.add("DUP_X2");
		OPCODE_NAMESET.add("DUP2");
		OPCODE_NAMESET.add("DUP2_X1");
		OPCODE_NAMESET.add("DUP2_X2");
		OPCODE_NAMESET.add("SWAP");
		OPCODE_NAMESET.add("IADD");
		OPCODE_NAMESET.add("LADD");
		OPCODE_NAMESET.add("FADD");
		OPCODE_NAMESET.add("DADD");
		OPCODE_NAMESET.add("ISUB");
		OPCODE_NAMESET.add("LSUB");
		OPCODE_NAMESET.add("FSUB");
		OPCODE_NAMESET.add("DSUB");
		OPCODE_NAMESET.add("IMUL");
		OPCODE_NAMESET.add("LMUL");
		OPCODE_NAMESET.add("FMUL");
		OPCODE_NAMESET.add("DMUL");
		OPCODE_NAMESET.add("IDIV");
		OPCODE_NAMESET.add("LDIV");
		OPCODE_NAMESET.add("FDIV");
		OPCODE_NAMESET.add("DDIV");
		OPCODE_NAMESET.add("IREM");
		OPCODE_NAMESET.add("LREM");
		OPCODE_NAMESET.add("FREM");
		OPCODE_NAMESET.add("DREM");
		OPCODE_NAMESET.add("INEG");
		OPCODE_NAMESET.add("LNEG");
		OPCODE_NAMESET.add("FNEG");
		OPCODE_NAMESET.add("DNEG");
		OPCODE_NAMESET.add("ISHL");
		OPCODE_NAMESET.add("LSHL");
		OPCODE_NAMESET.add("ISHR");
		OPCODE_NAMESET.add("LSHR");
		OPCODE_NAMESET.add("IUSHR");
		OPCODE_NAMESET.add("LUSHR");
		OPCODE_NAMESET.add("IAND");
		OPCODE_NAMESET.add("LAND");
		OPCODE_NAMESET.add("IOR");
		OPCODE_NAMESET.add("LOR");
		OPCODE_NAMESET.add("IXOR");
		OPCODE_NAMESET.add("LXOR");
		OPCODE_NAMESET.add("IINC");
		OPCODE_NAMESET.add("I2L");
		OPCODE_NAMESET.add("I2F");
		OPCODE_NAMESET.add("I2D");
		OPCODE_NAMESET.add("L2I");
		OPCODE_NAMESET.add("L2F");
		OPCODE_NAMESET.add("L2D");
		OPCODE_NAMESET.add("F2I");
		OPCODE_NAMESET.add("F2L");
		OPCODE_NAMESET.add("F2D");
		OPCODE_NAMESET.add("D2I");
		OPCODE_NAMESET.add("D2L");
		OPCODE_NAMESET.add("D2F");
		OPCODE_NAMESET.add("I2B");
		OPCODE_NAMESET.add("I2C");
		OPCODE_NAMESET.add("I2S");
		OPCODE_NAMESET.add("LCMP");
		OPCODE_NAMESET.add("FCMPL");
		OPCODE_NAMESET.add("FCMPG");
		OPCODE_NAMESET.add("DCMPL");
		OPCODE_NAMESET.add("DCMPG");
		OPCODE_NAMESET.add("IFEQ");
		OPCODE_NAMESET.add("IFNE");
		OPCODE_NAMESET.add("IFLT");
		OPCODE_NAMESET.add("IFGE");
		OPCODE_NAMESET.add("IFGT");
		OPCODE_NAMESET.add("IFLE");
		OPCODE_NAMESET.add("IF_ICMPEQ");
		OPCODE_NAMESET.add("IF_ICMPNE");
		OPCODE_NAMESET.add("IF_ICMPLT");
		OPCODE_NAMESET.add("IF_ICMPGE");
		OPCODE_NAMESET.add("IF_ICMPGT");
		OPCODE_NAMESET.add("IF_ICMPLE");
		OPCODE_NAMESET.add("IF_ACMPEQ");
		OPCODE_NAMESET.add("IF_ACMPNE");
		OPCODE_NAMESET.add("GOTO");
		OPCODE_NAMESET.add("JSR");
		OPCODE_NAMESET.add("RET");
		OPCODE_NAMESET.add("TABLESWITCH");
		OPCODE_NAMESET.add("LOOKUPSWITCH");
		OPCODE_NAMESET.add("IRETURN");
		OPCODE_NAMESET.add("LRETURN");
		OPCODE_NAMESET.add("FRETURN");
		OPCODE_NAMESET.add("DRETURN");
		OPCODE_NAMESET.add("ARETURN");
		OPCODE_NAMESET.add("RETURN");
		OPCODE_NAMESET.add("GETSTATIC");
		OPCODE_NAMESET.add("PUTSTATIC");
		OPCODE_NAMESET.add("GETFIELD");
		OPCODE_NAMESET.add("PUTFIELD");
		OPCODE_NAMESET.add("INVOKEVIRTUAL");
		OPCODE_NAMESET.add("INVOKESPECIAL");
		OPCODE_NAMESET.add("INVOKESTATIC");
		OPCODE_NAMESET.add("INVOKEINTERFACE");
		OPCODE_NAMESET.add("NEW");
		OPCODE_NAMESET.add("NEWARRAY");
		OPCODE_NAMESET.add("ANEWARRAY");
		OPCODE_NAMESET.add("ARRAYLENGTH");
		OPCODE_NAMESET.add("ATHROW");
		OPCODE_NAMESET.add("CHECKCAST");
		OPCODE_NAMESET.add("INSTANCEOF");
		OPCODE_NAMESET.add("MONITORENTER");
		OPCODE_NAMESET.add("MONITOREXIT");
		OPCODE_NAMESET.add("WIDE");
		OPCODE_NAMESET.add("MULTIANEWARRAY");
		OPCODE_NAMESET.add("IFNULL");
		OPCODE_NAMESET.add("IFNONNULL");
		OPCODE_NAMESET.add("GOTO_W");
		OPCODE_NAMESET.add("JSR_W");
		OPCODE_NAMESET.add("BREAKPOINT");
		OPCODE_NAMESET.add("IMPDEP1");
		OPCODE_NAMESET.add("IMPDEP2");
	}

	/**
	 * Number of byte code operands, i.e., number of bytes after the tag byte
	 * itself.
	 */
	public static final short[] NO_OF_OPERANDS = { 0/* nop */, 0/* aconst_null */, 0/* iconst_m1 */, 0/* iconst_0 */, 0/* iconst_1 */,
			0/* iconst_2 */, 0/* iconst_3 */, 0/* iconst_4 */, 0/* iconst_5 */, 0/* lconst_0 */, 0/* lconst_1 */, 0/* fconst_0 */,
			0/* fconst_1 */, 0/* fconst_2 */, 0/* dconst_0 */, 0/* dconst_1 */, 1/* bipush */, 2/* sipush */, 1/* ldc */, 2/* ldc_w */,
			2/* ldc2_w */, 1/* iload */, 1/* lload */, 1/* fload */, 1/* dload */, 1/* aload */, 0/* iload_0 */, 0/* iload_1 */,
			0/* iload_2 */, 0/* iload_3 */, 0/* lload_0 */, 0/* lload_1 */, 0/* lload_2 */, 0/* lload_3 */, 0/* fload_0 */, 0/* fload_1 */,
			0/* fload_2 */, 0/* fload_3 */, 0/* dload_0 */, 0/* dload_1 */, 0/* dload_2 */, 0/* dload_3 */, 0/* aload_0 */, 0/* aload_1 */,
			0/* aload_2 */, 0/* aload_3 */, 0/* iaload */, 0/* laload */, 0/* faload */, 0/* daload */, 0/* aaload */, 0/* baload */,
			0/* caload */, 0/* saload */, 1/* istore */, 1/* lstore */, 1/* fstore */, 1/* dstore */, 1/* astore */, 0/* istore_0 */,
			0/* istore_1 */, 0/* istore_2 */, 0/* istore_3 */, 0/* lstore_0 */, 0/* lstore_1 */, 0/* lstore_2 */, 0/* lstore_3 */,
			0/* fstore_0 */, 0/* fstore_1 */, 0/* fstore_2 */, 0/* fstore_3 */, 0/* dstore_0 */, 0/* dstore_1 */, 0/* dstore_2 */,
			0/* dstore_3 */, 0/* astore_0 */, 0/* astore_1 */, 0/* astore_2 */, 0/* astore_3 */, 0/* iastore */, 0/* lastore */,
			0/* fastore */, 0/* dastore */, 0/* aastore */, 0/* bastore */, 0/* castore */, 0/* sastore */, 0/* pop */, 0/* pop2 */,
			0/* dup */, 0/* dup_x1 */, 0/* dup_x2 */, 0/* dup2 */, 0/* dup2_x1 */, 0/* dup2_x2 */, 0/* swap */, 0/* iadd */, 0/* ladd */,
			0/* fadd */, 0/* dadd */, 0/* isub */, 0/* lsub */, 0/* fsub */, 0/* dsub */, 0/* imul */, 0/* lmul */, 0/* fmul */,
			0/* dmul */, 0/* idiv */, 0/* ldiv */, 0/* fdiv */, 0/* ddiv */, 0/* irem */, 0/* lrem */, 0/* frem */, 0/* drem */,
			0/* ineg */, 0/* lneg */, 0/* fneg */, 0/* dneg */, 0/* ishl */, 0/* lshl */, 0/* ishr */, 0/* lshr */, 0/* iushr */,
			0/* lushr */, 0/* iand */, 0/* land */, 0/* ior */, 0/* lor */, 0/* ixor */, 0/* lxor */, 2/* iinc */, 0/* i2l */, 0/* i2f */,
			0/* i2d */, 0/* l2i */, 0/* l2f */, 0/* l2d */, 0/* f2i */, 0/* f2l */, 0/* f2d */, 0/* d2i */, 0/* d2l */, 0/* d2f */,
			0/* i2b */, 0/* i2c */, 0/* i2s */, 0/* lcmp */, 0/* fcmpl */, 0/* fcmpg */, 0/* dcmpl */, 0/* dcmpg */, 2/* ifeq */,
			2/* ifne */, 2/* iflt */, 2/* ifge */, 2/* ifgt */, 2/* ifle */, 2/* if_icmpeq */, 2/* if_icmpne */, 2/* if_icmplt */,
			2/* if_icmpge */, 2/* if_icmpgt */, 2/* if_icmple */, 2/* if_acmpeq */, 2/* if_acmpne */, 2/* goto */, 2/* jsr */, 1/* ret */,
			UNPREDICTABLE/* tableswitch */, UNPREDICTABLE/* lookupswitch */, 0/* ireturn */, 0/* lreturn */, 0/* freturn */, 0/* dreturn */,
			0/* areturn */, 0/* return */, 2/* getstatic */, 2/* putstatic */, 2/* getfield */, 2/* putfield */, 2/* invokevirtual */,
			2/* invokespecial */, 2/* invokestatic */, 4/* invokeinterface */, UNDEFINED, 2/* new */, 1/* newarray */, 2/* anewarray */,
			0/* arraylength */, 0/* athrow */, 2/* checkcast */, 2/* instanceof */, 0/* monitorenter */, 0/* monitorexit */,
			UNPREDICTABLE/* wide */, 3/* multianewarray */, 2/* ifnull */, 2/* ifnonnull */, 4/* goto_w */, 4/* jsr_w */, 0/* breakpoint */,
			UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
			UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
			UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
			UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
			UNDEFINED, UNDEFINED, UNDEFINED, RESERVED/* impdep1 */, RESERVED /* impdep2 */
	};

	/**
	 * The length of each operand used by opcode, in byte.
	 */
	public static final short[][] LENGTH_OF_OPERANDS = { {}/* nop */, {}/* aconst_null */, {}/* iconst_m1 */, {}/* iconst_0 */,
			{}/* iconst_1 */, {}/* iconst_2 */, {}/* iconst_3 */, {}/* iconst_4 */, {}/* iconst_5 */, {}/* lconst_0 */, {}/* lconst_1 */,
			{}/* fconst_0 */, {}/* fconst_1 */, {}/* fconst_2 */, {}/* dconst_0 */, {}/* dconst_1 */, { 1 }/* bipush */, { 2 }/* sipush */,
			{ 1 }/* ldc */, { 2 }/* ldc_w */, { 2 }/* ldc2_w */, { 1 }/* iload */, { 1 }/* lload */, { 1 }/* fload */, { 1 }/* dload */,
			{ 1 }/* aload */, {}/* iload_0 */, {}/* iload_1 */, {}/* iload_2 */, {}/* iload_3 */, {}/* lload_0 */, {}/* lload_1 */,
			{}/* lload_2 */, {}/* lload_3 */, {}/* fload_0 */, {}/* fload_1 */, {}/* fload_2 */, {}/* fload_3 */, {}/* dload_0 */,
			{}/* dload_1 */, {}/* dload_2 */, {}/* dload_3 */, {}/* aload_0 */, {}/* aload_1 */, {}/* aload_2 */, {}/* aload_3 */,
			{}/* iaload */, {}/* laload */, {}/* faload */, {}/* daload */, {}/* aaload */, {}/* baload */, {}/* caload */, {}/* saload */,
			{ 1 }/* istore */, { 1 }/* lstore */, { 1 }/* fstore */, { 1 }/* dstore */, { 1 }/* astore */, {}/* istore_0 */, {}/* istore_1 */,
			{}/* istore_2 */, {}/* istore_3 */, {}/* lstore_0 */, {}/* lstore_1 */, {}/* lstore_2 */, {}/* lstore_3 */, {}/* fstore_0 */,
			{}/* fstore_1 */, {}/* fstore_2 */, {}/* fstore_3 */, {}/* dstore_0 */, {}/* dstore_1 */, {}/* dstore_2 */, {}/* dstore_3 */,
			{}/* astore_0 */, {}/* astore_1 */, {}/* astore_2 */, {}/* astore_3 */, {}/* iastore */, {}/* lastore */, {}/* fastore */,
			{}/* dastore */, {}/* aastore */, {}/* bastore */, {}/* castore */, {}/* sastore */, {}/* pop */, {}/* pop2 */, {}/* dup */,
			{}/* dup_x1 */, {}/* dup_x2 */, {}/* dup2 */, {}/* dup2_x1 */, {}/* dup2_x2 */, {}/* swap */, {}/* iadd */, {}/* ladd */,
			{}/* fadd */, {}/* dadd */, {}/* isub */, {}/* lsub */, {}/* fsub */, {}/* dsub */, {}/* imul */, {}/* lmul */, {}/* fmul */,
			{}/* dmul */, {}/* idiv */, {}/* ldiv */, {}/* fdiv */, {}/* ddiv */, {}/* irem */, {}/* lrem */, {}/* frem */, {}/* drem */,
			{}/* ineg */, {}/* lneg */, {}/* fneg */, {}/* dneg */, {}/* ishl */, {}/* lshl */, {}/* ishr */, {}/* lshr */, {}/* iushr */,
			{}/* lushr */, {}/* iand */, {}/* land */, {}/* ior */, {}/* lor */, {}/* ixor */, {}/* lxor */, { 1, 1 }/* iinc */,
			{}/* i2l */, {}/* i2f */, {}/* i2d */, {}/* l2i */, {}/* l2f */, {}/* l2d */, {}/* f2i */, {}/* f2l */, {}/* f2d */,
			{}/* d2i */, {}/* d2l */, {}/* d2f */, {}/* i2b */, {}/* i2c */, {}/* i2s */, {}/* lcmp */, {}/* fcmpl */, {}/* fcmpg */,
			{}/* dcmpl */, {}/* dcmpg */, { 2 }/* ifeq */, { 2 }/* ifne */, { 2 }/* iflt */, { 2 }/* ifge */, { 2 }/* ifgt */,
			{ 2 }/* ifle */, { 2 }/* if_icmpeq */, { 2 }/* if_icmpne */, { 2 }/* if_icmplt */, { 2 }/* if_icmpge */, { 2 }/* if_icmpgt */,
			{ 2 }/* if_icmple */, { 2 }/* if_acmpeq */, { 2 }/* if_acmpne */, { 2 }/* goto */, { 2 }/* jsr */, { 1 }/* ret */,
			{}/* tableswitch */, {}/* lookupswitch */, {}/* ireturn */, {}/* lreturn */, {}/* freturn */, {}/* dreturn */, {}/* areturn */,
			{}/* return */, { 2 }/* getstatic */, { 2 }/* putstatic */, { 2 }/* getfield */, { 2 }/* putfield */, { 2 }/* invokevirtual */,
			{ 2 }/* invokespecial */, { 2 }/* invokestatic */, { 2, 1, 1 }/* invokeinterface */, {}, { 2 }/* new */, { 1 }/* newarray */,
			{ 2 }/* anewarray */, {}/* arraylength */, {}/* athrow */, { 2 }/* checkcast */, { 2 }/* instanceof */, {}/* monitorenter */,
			{}/* monitorexit */, { 1 }/* wide */, { 2, 1 }/* multianewarray */, { 2 }/* ifnull */, { 2 }/* ifnonnull */, { 4 }/* goto_w */,
			{ 4 }/* jsr_w */, {}/* breakpoint */, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {},
			{}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}/* impdep1 */, {} /* impdep2 */
	};

	/**
	 * Number of words consumed on operand stack by instructions.
	 */
	public static final int[] CONSUME_STACK = { 0/*nop*/, 0/*aconst_null*/, 0/*iconst_m1*/, 0/*iconst_0*/, 0/*iconst_1*/, 0/*iconst_2*/,
			0/*iconst_3*/, 0/*iconst_4*/, 0/*iconst_5*/, 0/*lconst_0*/, 0/*lconst_1*/, 0/*fconst_0*/, 0/*fconst_1*/, 0/*fconst_2*/,
			0/*dconst_0*/, 0/*dconst_1*/, 0/*bipush*/, 0/*sipush*/, 0/*ldc*/, 0/*ldc_w*/, 0/*ldc2_w*/, 0/*iload*/, 0/*lload*/, 0/*fload*/,
			0/*dload*/, 0/*aload*/, 0/*iload_0*/, 0/*iload_1*/, 0/*iload_2*/, 0/*iload_3*/, 0/*lload_0*/, 0/*lload_1*/, 0/*lload_2*/,
			0/*lload_3*/, 0/*fload_0*/, 0/*fload_1*/, 0/*fload_2*/, 0/*fload_3*/, 0/*dload_0*/, 0/*dload_1*/, 0/*dload_2*/, 0/*dload_3*/,
			0/*aload_0*/, 0/*aload_1*/, 0/*aload_2*/, 0/*aload_3*/, 2/*iaload*/, 2/*laload*/, 2/*faload*/, 2/*daload*/, 2/*aaload*/,
			2/*baload*/, 2/*caload*/, 2/*saload*/, 1/*istore*/, 2/*lstore*/, 1/*fstore*/, 2/*dstore*/, 1/*astore*/, 1/*istore_0*/,
			1/*istore_1*/, 1/*istore_2*/, 1/*istore_3*/, 2/*lstore_0*/, 2/*lstore_1*/, 2/*lstore_2*/, 2/*lstore_3*/, 1/*fstore_0*/,
			1/*fstore_1*/, 1/*fstore_2*/, 1/*fstore_3*/, 2/*dstore_0*/, 2/*dstore_1*/, 2/*dstore_2*/, 2/*dstore_3*/, 1/*astore_0*/,
			1/*astore_1*/, 1/*astore_2*/, 1/*astore_3*/, 3/*iastore*/, 4/*lastore*/, 3/*fastore*/, 4/*dastore*/, 3/*aastore*/, 3/*bastore*/,
			3/*castore*/, 3/*sastore*/, 1/*pop*/, 2/*pop2*/, 1/*dup*/, 2/*dup_x1*/, 3/*dup_x2*/, 2/*dup2*/, 3/*dup2_x1*/, 4/*dup2_x2*/,
			2/*swap*/, 2/*iadd*/, 4/*ladd*/, 2/*fadd*/, 4/*dadd*/, 2/*isub*/, 4/*lsub*/, 2/*fsub*/, 4/*dsub*/, 2/*imul*/, 4/*lmul*/,
			2/*fmul*/, 4/*dmul*/, 2/*idiv*/, 4/*ldiv*/, 2/*fdiv*/, 4/*ddiv*/, 2/*irem*/, 4/*lrem*/, 2/*frem*/, 4/*drem*/, 1/*ineg*/,
			2/*lneg*/, 1/*fneg*/, 2/*dneg*/, 2/*ishl*/, 3/*lshl*/, 2/*ishr*/, 3/*lshr*/, 2/*iushr*/, 3/*lushr*/, 2/*iand*/, 4/*land*/,
			2/*ior*/, 4/*lor*/, 2/*ixor*/, 4/*lxor*/, 0/*iinc*/, 1/*i2l*/, 1/*i2f*/, 1/*i2d*/, 2/*l2i*/, 2/*l2f*/, 2/*l2d*/, 1/*f2i*/,
			1/*f2l*/, 1/*f2d*/, 2/*d2i*/, 2/*d2l*/, 2/*d2f*/, 1/*i2b*/, 1/*i2c*/, 1/*i2s*/, 4/*lcmp*/, 2/*fcmpl*/, 2/*fcmpg*/,
			4/*dcmpl*/, 4/*dcmpg*/, 1/*ifeq*/, 1/*ifne*/, 1/*iflt*/, 1/*ifge*/, 1/*ifgt*/, 1/*ifle*/, 2/*if_icmpeq*/, 2/*if_icmpne*/,
			2/*if_icmplt*/, 2 /*if_icmpge*/, 2/*if_icmpgt*/, 2/*if_icmple*/, 2/*if_acmpeq*/, 2/*if_acmpne*/, 0/*goto*/, 0/*jsr*/, 0/*ret*/,
			1/*tableswitch*/, 1/*lookupswitch*/, 1/*ireturn*/, 2/*lreturn*/, 1/*freturn*/, 2/*dreturn*/, 1/*areturn*/, 0/*return*/,
			0/*getstatic*/, UNPREDICTABLE/*putstatic*/, 1/*getfield*/, UNPREDICTABLE/*putfield*/, UNPREDICTABLE/*invokevirtual*/,
			UNPREDICTABLE/*invokespecial*/, UNPREDICTABLE/*invokestatic*/, UNPREDICTABLE/*invokeinterface*/, UNDEFINED, 0/*new*/, 1/*newarray*/,
			1/*anewarray*/, 1/*arraylength*/, 1/*athrow*/, 1/*checkcast*/, 1/*instanceof*/, 1/*monitorenter*/, 1/*monitorexit*/, 0/*wide*/,
			UNPREDICTABLE/*multianewarray*/, 1/*ifnull*/, 1/*ifnonnull*/, 0/*goto_w*/, 0/*jsr_w*/, 0/*breakpoint*/, UNDEFINED, UNDEFINED,
			UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
			UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
			UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
			UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
			UNDEFINED, UNPREDICTABLE/*impdep1*/, UNPREDICTABLE /*impdep2*/
	};

	/**
	 * Number of words produced onto operand stack by instructions.
	 */
	public static final int[] PRODUCE_STACK = { 0/*nop*/, 1/*aconst_null*/, 1/*iconst_m1*/, 1/*iconst_0*/, 1/*iconst_1*/, 1/*iconst_2*/,
			1/*iconst_3*/, 1/*iconst_4*/, 1/*iconst_5*/, 2/*lconst_0*/, 2/*lconst_1*/, 1/*fconst_0*/, 1/*fconst_1*/, 1/*fconst_2*/,
			2/*dconst_0*/, 2/*dconst_1*/, 1/*bipush*/, 1/*sipush*/, 1/*ldc*/, 1/*ldc_w*/, 2/*ldc2_w*/, 1/*iload*/, 2/*lload*/, 1/*fload*/,
			2/*dload*/, 1/*aload*/, 1/*iload_0*/, 1/*iload_1*/, 1/*iload_2*/, 1/*iload_3*/, 2/*lload_0*/, 2/*lload_1*/, 2/*lload_2*/,
			2/*lload_3*/, 1/*fload_0*/, 1/*fload_1*/, 1/*fload_2*/, 1/*fload_3*/, 2/*dload_0*/, 2/*dload_1*/, 2/*dload_2*/, 2/*dload_3*/,
			1/*aload_0*/, 1/*aload_1*/, 1/*aload_2*/, 1/*aload_3*/, 1/*iaload*/, 2/*laload*/, 1/*faload*/, 2/*daload*/, 1/*aaload*/,
			1/*baload*/, 1/*caload*/, 1/*saload*/, 0/*istore*/, 0/*lstore*/, 0/*fstore*/, 0/*dstore*/, 0/*astore*/, 0/*istore_0*/,
			0/*istore_1*/, 0/*istore_2*/, 0/*istore_3*/, 0/*lstore_0*/, 0/*lstore_1*/, 0/*lstore_2*/, 0/*lstore_3*/, 0/*fstore_0*/,
			0/*fstore_1*/, 0/*fstore_2*/, 0/*fstore_3*/, 0/*dstore_0*/, 0/*dstore_1*/, 0/*dstore_2*/, 0/*dstore_3*/, 0/*astore_0*/,
			0/*astore_1*/, 0/*astore_2*/, 0/*astore_3*/, 0/*iastore*/, 0/*lastore*/, 0/*fastore*/, 0/*dastore*/, 0/*aastore*/, 0/*bastore*/,
			0/*castore*/, 0/*sastore*/, 0/*pop*/, 0/*pop2*/, 2/*dup*/, 3/*dup_x1*/, 4/*dup_x2*/, 4/*dup2*/, 5/*dup2_x1*/, 6/*dup2_x2*/,
			2/*swap*/, 1/*iadd*/, 2/*ladd*/, 1/*fadd*/, 2/*dadd*/, 1/*isub*/, 2/*lsub*/, 1/*fsub*/, 2/*dsub*/, 1/*imul*/, 2/*lmul*/,
			1/*fmul*/, 2/*dmul*/, 1/*idiv*/, 2/*ldiv*/, 1/*fdiv*/, 2/*ddiv*/, 1/*irem*/, 2/*lrem*/, 1/*frem*/, 2/*drem*/, 1/*ineg*/,
			2/*lneg*/, 1/*fneg*/, 2/*dneg*/, 1/*ishl*/, 2/*lshl*/, 1/*ishr*/, 2/*lshr*/, 1/*iushr*/, 2/*lushr*/, 1/*iand*/, 2/*land*/,
			1/*ior*/, 2/*lor*/, 1/*ixor*/, 2/*lxor*/, 0/*iinc*/, 2/*i2l*/, 1/*i2f*/, 2/*i2d*/, 1/*l2i*/, 1/*l2f*/, 2/*l2d*/, 1/*f2i*/,
			2/*f2l*/, 2/*f2d*/, 1/*d2i*/, 2/*d2l*/, 1/*d2f*/, 1/*i2b*/, 1/*i2c*/, 1/*i2s*/, 1/*lcmp*/, 1/*fcmpl*/, 1/*fcmpg*/,
			1/*dcmpl*/, 1/*dcmpg*/, 0/*ifeq*/, 0/*ifne*/, 0/*iflt*/, 0/*ifge*/, 0/*ifgt*/, 0/*ifle*/, 0/*if_icmpeq*/, 0/*if_icmpne*/,
			0/*if_icmplt*/, 0/*if_icmpge*/, 0/*if_icmpgt*/, 0/*if_icmple*/, 0/*if_acmpeq*/, 0/*if_acmpne*/, 0/*goto*/, 1/*jsr*/, 0/*ret*/,
			0/*tableswitch*/, 0/*lookupswitch*/, 0/*ireturn*/, 0/*lreturn*/, 0/*freturn*/, 0/*dreturn*/, 0/*areturn*/, 0/*return*/,
			UNPREDICTABLE/*getstatic*/, 0/*putstatic*/, UNPREDICTABLE/*getfield*/, 0/*putfield*/, UNPREDICTABLE/*invokevirtual*/,
			UNPREDICTABLE/*invokespecial*/, UNPREDICTABLE/*invokestatic*/, UNPREDICTABLE/*invokeinterface*/, UNDEFINED, 1/*new*/, 1/*newarray*/,
			1/*anewarray*/, 1/*arraylength*/, 1/*athrow*/, 1/*checkcast*/, 1/*instanceof*/, 0/*monitorenter*/, 0/*monitorexit*/, 0/*wide*/,
			1/*multianewarray*/, 0/*ifnull*/, 0/*ifnonnull*/, 0/*goto_w*/, 1/*jsr_w*/, 0/*breakpoint*/, UNDEFINED, UNDEFINED, UNDEFINED,
			UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
			UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
			UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
			UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
			UNPREDICTABLE/*impdep1*/, UNPREDICTABLE /*impdep2*/
	};

	public static final String[] TYPE_NAMES = { ILLEGAL_TYPE, ILLEGAL_TYPE, ILLEGAL_TYPE, ILLEGAL_TYPE, "boolean", "char", "float", "double", "byte",
			"short", "int", "long", "void", "array", "object", "unknown" // Non-standard
	};

	public static final HashSet ACCESS_FLAG_SET = new HashSet();
	static {
		ACCESS_FLAG_SET.add("volatile");
		ACCESS_FLAG_SET.add("public");
		ACCESS_FLAG_SET.add("private");
		ACCESS_FLAG_SET.add("protected");
		ACCESS_FLAG_SET.add("static");
		ACCESS_FLAG_SET.add("final");
		ACCESS_FLAG_SET.add("synchronized");
		ACCESS_FLAG_SET.add("native");
		ACCESS_FLAG_SET.add("abstract");
		ACCESS_FLAG_SET.add("strictfp");
		ACCESS_FLAG_SET.add("transient");
		ACCESS_FLAG_SET.add("class");
		ACCESS_FLAG_SET.add("interface");
	}

	public static final String ATTRIBUTE_NAME_LOCAL_VARIABLE = "LocalVariables";

	public static final String ATTRIBUTE_NAME_MAX_STACK = "MaxStack";

	public static final String ATTRIBUTE_NAME_MAX_LOCAL = "MaxLocal";

	public static final String ATTRIBUTE_NAME_DEPRECATED = "Deprecated";

	public static final String ATTRIBUTE_NAME_SYNTHETIC = "Synthetic";

	public static final String ATTRIBUTE_NAME_LINE_NUMBER_TABLE = "LineNumber";

	public static final String ATTRIBUTE_NAME_EXCEPTION_TABLE = "Exceptions";

	public static final String ATTRIBUTE_NAME_SOURCE_FILE = "SourceFile";

	public static final String ATTRIBUTE_NAME_INNER_CLASSES = "InnerClasses";

	public static final String ATTRIBUTE_NAME_MAJOR_VERSION = "Major";

	public static final String ATTRIBUTE_NAME_MINOR_VERSION = "Minor";
}