/*
 * Author jyang
 * Created on 2006-4-5 9:29:37
 */
package com.jasml.helper;

import java.util.HashMap;

public class OpcodeHelper {

	public static OpcodeInfo[] OPCODES;

	public static HashMap OPCODES_MAP = new HashMap(256);
	//    static{        
	//        for(int i=0;i<Constants.OPCODE_NAMES.length;i++){
	//           OPCODES[i] = new OpcodeInfo((byte)i,
	//                   Constants.OPCODE_NAMES[i],
	//                   (short)(Constants.LENGTH_OF_OPERANDS[i]==null?0:Constants.LENGTH_OF_OPERANDS[i].length),
	//                   Constants.LENGTH_OF_OPERANDS[i]);
	//           OPCODES_MAP.put(Constants.OPCODE_NAMES[i], new Byte((byte)i));
	//        }
	//    } 

	static {
		OpcodeLoader loader = new OpcodeLoader();
		try {
			OPCODES = loader.loadOpcodes();
			for(int i=0;i<OPCODES.length;i++){
				if(OPCODES[i]!=null){
					OPCODES_MAP.put(OPCODES[i].opname, new Byte(OPCODES[i].opcode));
				}
			}
		} catch (Exception e) {
			System.out.println("Can not initializing opcode data.");
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static OpcodeInfo getOpcodeInfo(byte opcode) {
		return OPCODES[opcode&0xFF];
	}

	public static OpcodeInfo getOpcodeInfo(String opname) {
		Object o = OPCODES_MAP.get(opname);
		if (o == null) {
			return null;
		}
		return OPCODES[((Byte) o).byteValue() & 0xFF];
	}
}
