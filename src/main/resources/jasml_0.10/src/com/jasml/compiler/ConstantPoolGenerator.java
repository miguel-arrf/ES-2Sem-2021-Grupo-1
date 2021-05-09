/*
 * Author jyang
 * Created on 2006-4-7 11:08:44
 */
package com.jasml.compiler;

import java.util.HashMap;

import com.jasml.classes.*;
import com.jasml.helper.Util;


public class ConstantPoolGenerator {
	private ConstantPoolItem[] items;

	private int count = 1;

	private HashMap classes = new HashMap();

	private HashMap nameTypes = new HashMap();

	private HashMap mifRef = new HashMap(); // methodRef, fieldRef, interfaceMethodRef

	private HashMap strings = new HashMap();

	public ConstantPoolGenerator() {
		items = new ConstantPoolItem[30];
		items[0] = new ConstantPoolItem((byte) 0);
	}

	public ConstantPool getConstantPool() {
		ConstantPoolItem[] ret = new ConstantPoolItem[count];
		System.arraycopy(items, 0, ret, 0, count);
		ConstantPool cpl = new ConstantPool(ret);
		return cpl;
	}

	/**
	 * this can be used to add a Constant_Class entry into constant pool.
	 * a constant class could be of a class type( like java/lang/Object),
	 * or of an array type of class(like [Ljava/lang/Object;),
	 * or array type of a primitive type(like [[I)
	 * @param className can be Strings like java.lang.Object, java.lang.Object[][], int[][].
	 * @return
	 */
	public int addClass(String className) {
		int index = 0;
		className = Util.toInnerClassName(className);
		index = lookupClass(className);
		if (index == -1) {
			// add the class
			int class_name_index = addUtf8(className);
			ensureCapacity();
			items[count] = new Constant_Class(class_name_index);
			classes.put(className, new Integer(count));
			return count++;
		} else {
			return index;
		}

	}

	public int addDouble(double var) {
		int index = lookupDouble(var);
		if (index == -1) {
			ensureCapacity();
			items[count] = new Constant_Double(var);
			index = count;
			count = count + 2;
		}
		return index;
	}

	public int addFieldref(String name, String className, String type) {
		int class_index, name_and_type_index, index;
		class_index = addClass(className);
		name_and_type_index = addFieldNameAndType(name, type);
		index = lookupMIFref("F_" + class_index + "_" + name_and_type_index);
		if (index == -1) {
			ensureCapacity();
			items[count] = new Constant_Fieldref(class_index, name_and_type_index);
			mifRef.put("F_" + class_index + "_" + name_and_type_index, new Integer(count));
			index = count;
			count++;
		}
		return index;
	}

	public int addFloat(float var) {
		int index = lookupFloat(var);
		if (index == -1) {
			ensureCapacity();
			items[count] = new Constant_Float(var);
			index = count;
			count++;
		}
		return index;
	}

	public int addInteger(int var) {
		int index = lookupInt(var);
		if (index == -1) {
			ensureCapacity();
			items[count] = new Constant_Integer(var);
			index = count;
			count++;
		}
		return index;
	}

	public int addInterfaceMethodref(String name, String interfaceName, String retType, String paras) {
		int class_index, name_and_type_index, index;
		class_index = addClass(interfaceName);
		name_and_type_index = addMethodNameAndType(name, retType, paras);
		index = lookupMIFref("I_" + class_index + "_" + name_and_type_index);
		if (index == -1) {
			ensureCapacity();
			items[count] = new Constant_InterfaceMethodref(class_index, name_and_type_index);
			mifRef.put("I_" + class_index + "_" + name_and_type_index, new Integer(count));
			index = count;
			count++;
		}
		return index;
	}

	public int addLong(long var) {
		int index = lookupLong(var);
		if (index == -1) {
			ensureCapacity();
			items[count] = new Constant_Long(var);
			index = count;
			count = count + 2;
		}
		return index;
	}

	public int addMethodref(String name, String className, String retType, String paras) {
		int class_index, name_and_type_index, index;
		class_index = addClass(className);
		name_and_type_index = addMethodNameAndType(name, retType, paras);
		index = lookupMIFref("M_" + class_index + "_" + name_and_type_index);
		if (index == -1) {
			ensureCapacity();
			items[count] = new Constant_Methodref(class_index, name_and_type_index);
			mifRef.put("M_" + class_index + "_" + name_and_type_index, new Integer(count));
			index = count;
			count++;
		}
		return index;
	}

	/*
	 * lookup existing field, method, interfaceMethod references
	 * in the format of [X]_[class_index]_[name_and_type_index]
	 * for method_ref X='M', field_ref X='F', interfaceMethod_ref X='I'
	 */
	private int lookupMIFref(String s) {
		Object obj = mifRef.get(s);
		if (obj == null) {
			return -1;
		} else {
			return ((Integer) obj).intValue();
		}

	}

	private int addFieldNameAndType(String name, String type) {
		int name_index, type_index, index;

		type = Util.toInnerType(type);
		index = lookupNameAndType(name + " " + type);
		if (index == -1) {
			name_index = addUtf8(name);
			type_index = addUtf8(type);
			ensureCapacity();
			items[count] = new Constant_NameAndType(name_index, type_index);
			nameTypes.put(name + " " + type, new Integer(count));
			index = count;
			count++;
		}
		return index;
	}

	private int addMethodNameAndType(String name, String retType, String paras) {
		int name_index, type_index, index;
		String type;

		retType = Util.toInnerType(retType);
		paras = Util.toInnerParameterTypes(paras);
		type = "(" + paras + ")" + retType;
		index = lookupNameAndType(name + type);
		if (index == -1) {
			name_index = addUtf8(name);
			type_index = addUtf8(type);
			ensureCapacity();
			items[count] = new Constant_NameAndType(name_index, type_index);
			nameTypes.put(name + type, new Integer(count));
			index = count;
			count++;
		}
		return index;
	}

	public int addString(String s) {
		int ret = lookupString(s);
		if (ret == -1) {
			ConstantPoolItem item = new Constant_String(addUtf8(s));
			ensureCapacity();
			ret = count;
			items[count] = item;
			strings.put(s, new Integer(ret));
			count ++;
		}
		return ret;
	}

	private int lookupString(String s) {
		Object obj = strings.get(s);
		if (obj != null) {
			return ((Integer) obj).intValue();
		}
		return -1;
	}

	public int addUtf8(String s) {
		int ret = lookupUtf8(s);
		if (ret == -1) {
			ensureCapacity();
			ConstantPoolItem item = new Constant_Utf8(s);
			ret = count;
			items[count++] = item;
		}
		return ret;
	}

	private int lookupUtf8(String s) {
		ConstantPoolItem item;
		for (int i = 0; i < count; i++) {
			item = items[i];
			if (item != null && item.tag == Constants.CONSTANT_Utf8 && ((Constant_Utf8) item).bytes.equals(s) == true) {
				return i;
			}
		}
		return -1;
	}

	private int lookupClass(String className) {
		Object obj = classes.get(className);
		if (obj == null) {
			return -1;
		} else {
			return ((Integer) obj).intValue();
		}
	}

	private int lookupNameAndType(String nameType) {
		Object obj = nameTypes.get(nameType);
		if (obj == null) {
			return -1;
		} else {
			return ((Integer) obj).intValue();
		}

	}

	private int lookupDouble(double var) {
		ConstantPoolItem item;
		for (int i = 0; i < count; i++) {
			item = items[i];
			if (item != null && item.tag == Constants.CONSTANT_Double && ((Constant_Double) item).value == var) {
				return i;
			}
		}
		return -1;
	}

	private int lookupInt(int var) {
		ConstantPoolItem item;
		for (int i = 0; i < count; i++) {
			item = items[i];
			if (item != null && item.tag == Constants.CONSTANT_Integer && ((Constant_Integer) item).value == var) {
				return i;
			}
		}
		return -1;
	}

	private int lookupFloat(float var) {
		ConstantPoolItem item;
		for (int i = 0; i < count; i++) {
			item = items[i];
			if (item != null && item.tag == Constants.CONSTANT_Float && ((Constant_Float) item).value == var) {
				return i;
			}
		}
		return -1;
	}

	private int lookupLong(long var) {
		ConstantPoolItem item;
		for (int i = 0; i < count; i++) {
			item = items[i];
			if (item != null && item.tag == Constants.CONSTANT_Long && ((Constant_Long) item).value == var) {
				return i;
			}
		}
		return -1;
	}

	private void ensureCapacity() {
		if (items.length < count + 3) {
			ConstantPoolItem[] ni = new ConstantPoolItem[items.length + 20];
			System.arraycopy(items, 0, ni, 0, items.length);
			items = ni;
		}
	}

	public static void main(String[] args) {
		ConstantPoolGenerator gen = new ConstantPoolGenerator();
		System.out.println(gen.addString("."));
		System.out.println(gen.addString("."));

	}
}