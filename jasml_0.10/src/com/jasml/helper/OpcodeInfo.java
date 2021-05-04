package com.jasml.helper;

public class OpcodeInfo {
	public byte opcode;

	public String opname;

	public short operandsCount; // the number of operands this opcode has

	public short[] operandsLength; // length of each operands, in byte

	public int consumeStack;

	public int produceStack;

	//bellow are descriptions copied from Java Language Specification
	public String operation;

	public String format;

	public String forms;

	public String operandStack;

	public String description;

	public String runtimeExceptions;

	public String linkingExceptions;

	public String notes;
}
