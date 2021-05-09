package com.jasml.helper;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class OpcodeLoader {
	OpcodeInfo[] infos;

	public OpcodeInfo[] loadOpcodes() throws IOException, SAXException, ParserConfigurationException {
		DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
		Document doc = fac.newDocumentBuilder().parse(ClassLoader.getSystemResourceAsStream("opcodes.xml"));
		NodeList nodeList = doc.getChildNodes();
		Node node = nodeList.item(0);
		infos = new OpcodeInfo[255];

		nodeList = node.getChildNodes();
		int length = nodeList.getLength();

		for (int i = 0; i < length; i++) {
			node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE)
				processOpcode(nodeList.item(i));
		}
		return infos;
	}

	public void processOpcode(Node node) {
		NodeList list = node.getChildNodes(), clist;
		int length = list.getLength();
		String nodeName;
		OpcodeInfo info;
		NamedNodeMap attributes;
		int t, counter;
		short[] operands;
		info = new OpcodeInfo();
		for (int i = 0; i < length; i++) {
			node = list.item(i);
			nodeName = node.getNodeName();
			if ("name".equals(nodeName)) {
				info.opname = node.getChildNodes().item(0).getNodeValue();
			} else if ("code".equals(nodeName)) {
				info.opcode = (byte) Integer.parseInt(node.getChildNodes().item(0).getNodeValue());
			} else if ("consumeStack".equals(nodeName)) {
				info.consumeStack = (byte) Integer.parseInt(node.getChildNodes().item(0).getNodeValue());
			} else if ("produceStack".equals(nodeName)) {
				info.produceStack = (byte) Integer.parseInt(node.getChildNodes().item(0).getNodeValue());
			} else if ("operandsInfo".equals(nodeName)) {
				attributes = node.getAttributes();
				t = Integer.parseInt(attributes.item(0).getNodeValue());
				info.operandsCount = (short) t;
				clist = node.getChildNodes();
				operands = new short[t];
				counter = 0;
				for (t = 0; t < clist.getLength(); t++) {
					node = clist.item(t);
					if ("length".equals(node.getNodeName())) {
						operands[counter++] = Short.parseShort(node.getFirstChild().getNodeValue());
					}
				}
				info.operandsLength = operands;

			} else if ("operation".equals(nodeName)) {
				clist = node.getChildNodes();
				for (t = 0; t < clist.getLength(); t++) {
					node = clist.item(t);
					if (Node.CDATA_SECTION_NODE == node.getNodeType()) {
						info.operation = node.getNodeValue();
					}
				}
			} else if ("format".equals(nodeName)) {
				clist = node.getChildNodes();
				for (t = 0; t < clist.getLength(); t++) {
					node = clist.item(t);
					if (Node.CDATA_SECTION_NODE == node.getNodeType()) {
						info.format = node.getNodeValue();
					}
				}
			} else if ("forms".equals(nodeName)) {
				clist = node.getChildNodes();
				for (t = 0; t < clist.getLength(); t++) {
					node = clist.item(t);
					if (Node.CDATA_SECTION_NODE == node.getNodeType()) {
						info.forms = node.getNodeValue();
					}
				}
			} else if ("operandStack".equals(nodeName)) {
				clist = node.getChildNodes();
				for (t = 0; t < clist.getLength(); t++) {
					node = clist.item(t);
					if (Node.CDATA_SECTION_NODE == node.getNodeType()) {
						info.operandStack = node.getNodeValue();
					}
				}
			} else if ("description".equals(nodeName)) {
				clist = node.getChildNodes();
				for (t = 0; t < clist.getLength(); t++) {
					node = clist.item(t);
					if (Node.CDATA_SECTION_NODE == node.getNodeType()) {
						info.description = node.getNodeValue();
					}
				}
			} else if ("runtimeExceptions".equals(nodeName)) {
				clist = node.getChildNodes();
				for (t = 0; t < clist.getLength(); t++) {
					node = clist.item(t);
					if (Node.CDATA_SECTION_NODE == node.getNodeType()) {
						info.runtimeExceptions = node.getNodeValue();
					}
				}

			} else if ("linkingExceptions".equals(nodeName)) {
				clist = node.getChildNodes();
				for (t = 0; t < clist.getLength(); t++) {
					node = clist.item(t);
					if (Node.CDATA_SECTION_NODE == node.getNodeType()) {
						info.linkingExceptions = node.getNodeValue();
					}
				}
			} else if ("notes".equals(nodeName)) {
				clist = node.getChildNodes();
				for (t = 0; t < clist.getLength(); t++) {
					node = clist.item(t);
					if (Node.CDATA_SECTION_NODE == node.getNodeType()) {
						info.notes = node.getNodeValue();
					}
				}
			}
		}
		infos[info.opcode & 0xFF] = info;
	}

	public static void main(String[] args) throws Exception {
		OpcodeLoader loader = new OpcodeLoader();
		OpcodeInfo[] ops = loader.loadOpcodes();
		OpcodeInfo op;
		for (int i = 0; i < ops.length; i++) {
			if (ops[i] != null) {
				op = ops[i];
				prt("<p><span class='InstructionTitle'>Name</span> : " + op.opname + "</p>");
				prt("<p><span class='InstructionTitle'>Opcode</span> : " + (op.opcode & 0xFF) + "(0x" + Integer.toHexString((op.opcode & 0xFF))
						+ ")</p>");
				prt("<p><span class='InstructionTitle'>Operation</span> : </p>");
				prt("<p>" + pr(op.operation) + "</p>");
				prt("<p><span class='InstructionTitle'>Format</span> : </p>");
				prt("<p>" + pr(op.format) + "</p>");
				prt("<p><span class='InstructionTitle'>Operand Stack</span> : </p>");
				prt("<p>" + op.operandStack + "</p>");
				prt("<p><span class='InstructionTitle'>Description</span> :</p>");
				prt("<p>" + pr(op.description) + "</p>");
				
				if (op.linkingExceptions!=null &&op.linkingExceptions.trim().length() != 0) {
					prt("<p><span class='InstructionTitle'>Linking Exceptions</span> : </p>");
					prt("<p>" + pr(op.linkingExceptions) + "</p>");
				}
				if (op.runtimeExceptions.trim().length() != 0) {
					prt("<p><span class='InstructionTitle'>Runtime Exceptions</span> : </p>");
					prt("<p>" + pr(op.runtimeExceptions) + "</p>");
				}
				if (op.notes.trim().length() != 0) {
					prt(" <p><span class='InstructionTitle'>Notes</span> : </p>");
					prt("<p>" + pr(op.notes) + "</p>");
				}
				prt("<br>");
				prt("<hr>");
			}
		}
	}

	public static void prt(String s) {
		System.out.println(s);
	}

	public static String pr(String s) {
		s = s.trim();
		s = s.replaceAll("\n", "<br>");
		s = s.replaceAll("\r", "");
		return s.trim();
	}
}
