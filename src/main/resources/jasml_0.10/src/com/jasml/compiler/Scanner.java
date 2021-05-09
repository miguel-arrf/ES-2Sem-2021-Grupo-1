/*
 * Author jyang
 * Created on 2006-5-8 16:34:48
 */
package com.jasml.compiler;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.jasml.classes.Constants;

public class Scanner implements Scannable {

	private boolean lineNumberOn = true;

	private boolean columnNumberOn = true;

	private int lineNumber, lineNumberStart;

	private int columnNumber = 0, columnNumberStart = 0;

	char[] buf;

	private int offset, tokenOffset, tokenType, contentLength;

	private int oldColumnNumber;

	private String token;

	private final static char EndChar = (char) 0;

	private int currentLineNumber1, lineNumberStart1, columnNumber1, columnNumberStart1, offset1, tokenOffset1, tokenType1, oldColumnNumber1;

	private String token1;

	/**
	 * create a scanner using the content of the Scanner passed down.
	 * the new Scanner will scan from offset, end at offset+length
	 * @param content
	 * @param offset
	 * @param length
	 * @param columnNumber
	 * @param lineNumber
	 * @return
	 * @throws ParsingException
	 */
	public static Scanner partialScanner(char[] content, int offset, int length, int columnNumber, int lineNumber) throws ParsingException {
		Scanner ret = new Scanner();
		ret.buf = content;
		ret.offset = offset;
		ret.contentLength = offset + length;
		ret.columnNumber = columnNumber;
		ret.columnNumberStart = columnNumber;
		ret.lineNumber = lineNumber;
		ret.lineNumberStart = lineNumber;
		return ret;
	}

	private Scanner() {
		// for internal use
	}

	public Scanner(String content) throws ParsingException {
		contentLength = content.length();
		buf = new char[contentLength];
		delComment(buf);
		content.getChars(0, contentLength, buf, 0);
		offset = 0;
	}

	public Scanner(String content, int startingOffset, int length) {

	}

	public Scanner(File file) throws ParsingException {
		FileReader reader;
		try {
			reader = new FileReader(file);
			char[] bs = new char[(int) file.length()];
			contentLength = reader.read(bs);
			buf = new char[contentLength];
			System.arraycopy(bs, 0, buf, 0, contentLength);
			offset = 0;
		} catch (IOException e) {
			throw new ParsingException("error.initializing.file", e);
		}
		delComment(buf);
	}

	public char[] getContent() {
		return buf;
	}

	public void mark() {
		currentLineNumber1 = lineNumber;
		lineNumberStart1 = lineNumberStart;
		columnNumber1 = columnNumber;
		columnNumberStart1 = columnNumberStart;
		offset1 = offset;
		tokenOffset1 = tokenOffset;
		tokenType1 = tokenType;
		oldColumnNumber1 = oldColumnNumber;
		token1 = token;
	}

	public void restore() {
		lineNumber = currentLineNumber1;
		lineNumberStart = lineNumberStart1;
		columnNumber = columnNumber1;
		columnNumberStart = columnNumberStart1;
		offset = offset1;
		tokenOffset = tokenOffset1;
		tokenType = tokenType1;
		oldColumnNumber = oldColumnNumber1;
		token = token1;
	}

	public void setLineNumberOn(boolean lineNumberOn) {
		this.lineNumberOn = lineNumberOn;
	}

	public void setColumnNumberOn(boolean columnNumberOn) {
		this.columnNumberOn = columnNumberOn;
	}

	public int nextToken() throws ParsingException {
		skipSpaces();
		tokenOffset = offset;
		columnNumberStart = columnNumber;
		lineNumberStart = lineNumber;
		char c = read();
		if (c == EndChar) {
			tokenType = EOF;
			return tokenType;
		}

		switch (c) {
		case ':':
			tokenType = Colon;
			token = ":";
			break;
		case ',':
			tokenType = Comma;
			token = ",";
			break;

		case '=':
			tokenType = Equal;
			token = "=";
			break;
		case ')':
			tokenType = SBracket_Right;
			token = ")";
			break;
		case '(':
			tokenType = SBracket_Left;
			token = "(";
			break;
		case '{':
			tokenType = Bracket_Left;
			token = "{";
			break;
		case '}':
			tokenType = Bracket_Right;
			token = "}";
			break;
		case '\'': // single quote
			c = read();
			if (c == '\\') {
				// escaped char
				// TODO, what if this is a /u03d3
				read();
			}
			c = read();
			if (c != '\'') {
				exception(this, "unclosed.char.definition.'''.expected");
			}
			token = new String(buf, tokenOffset, offset - tokenOffset);
			tokenType = Char;
			break;

		case '"':
			while ((c = read()) != EndChar) {
				if (c == '\\') {
					// escaping char
					read();
				} else if (c == '"') {
					break;
				}
			}
			if (c != '"') {
				exception(this, "unclosed.string.definition.'\"'.expected");
			}
			token = new String(buf, tokenOffset, offset - tokenOffset);
			tokenType = String;
			break;
		case '-':
			c = read();
			if (c == '>') {
				tokenType = Pointer;
				token = new String(buf, tokenOffset, offset - tokenOffset);
				break;
			} else if (c == 'I') {
				// float POSITIVE_INFINITY = InfinityF or float Negative_INFINITY = -InfinityF is allowed
				if (read() != 'n' || read() != 'f' || read() != 'i' || read() != 'n' || read() != 'i' || read() != 't' || read() != 'y') {
					exception(getOffset(), getLineNumberStart(), getColumnNumberStart(), "\"Infinity\".expected.here");
				}
				c = Character.toUpperCase(read());
				if (c == 'D') {
					tokenType = Number_Double_Negativ_Infinity;
				} else if (c == 'F') {
					tokenType = Number_Float_Negativ_Infinity;
				} else {
					exception(getOffset(), getLineNumberStart(), getColumnNumberStart(),
							"invalid.Infinity.definition.InfinityD.or.InfinityF.expected");
				}
				c = read();

				if (Character.isWhitespace(c) == false) {
					exception(getOffset(), getLineNumberStart(), getColumnNumberStart(),
							"invalid.Infinity.definition.InfinityD.or.InfinityF.expected");
				}
				unread();
				token = new String(buf, tokenOffset, offset - tokenOffset);
				break;
			} else if (c == '0') {
				// do not unread, for the next case
			} else if (Character.isDigit(c) == true) {
				unread();
			} else {
				exception(getOffset(), getLineNumberStart(), getColumnNumberStart(), "invalid.character.'-'");
			}
		case '0':
			c = read();
			if (c == 'x' || c == 'X') {
				// hex numbers
				do {
					c = read();
					if (c == EndChar) {
						break;
					}
					c = Character.toUpperCase(c);
				} while (Character.isDigit(c) == true || c == 'A' || c == 'B' || c == 'C' || c == 'D' || c == 'E' || c == 'F');
				if (c == 'L') {
					token = new String(buf, tokenOffset, offset - tokenOffset);
					tokenType = Number_Long;
				} else if (c != EndChar && Character.isWhitespace(c) == true) {
					unread();
					token = new String(buf, tokenOffset, offset - tokenOffset);
					tokenType = Number_Integer;
				} else if (Character.isWhitespace(c) == false) {
					exception(getOffset(), getLineNumberStart(), getColumnNumberStart(), "invalid.hex.number.format");
				}
				break;
			} else {
				unread();
			}
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
			/**
			 * 102E-34d , 
			 * 124.3e-34f,
			 * 34.34f
			 * 123.3e+56f
			 */
			consumeDigits();
			c = read();
			switch(c){
			case '.':
				//decimal point
				c = read();
				if(Character.isDigit(c)==false){
					exception(getOffset(), getLineNumberStart(), getColumnNumberStart(), "invalid.number.format");
				}
				consumeDigits();
				c = read();
				if(c=='e' || c=='E'){
					
				}else{
					break;
				}
			case 'e':
			case 'E':
				//scientific number
				c=read();
				if(c=='+' || c=='-'){
					c = read();
					if(Character.isDigit(c)==false){
						exception(getOffset(), getLineNumberStart(), getColumnNumberStart(), "invalid.number.format");
					}
					consumeDigits();
					c = read();
				}else if(Character.isDigit(c)==true){
					consumeDigits();
					c = read();
				}else{
					exception(getOffset(), getLineNumberStart(), getColumnNumberStart(), "invalid.number.format");
				}
			}

			c = Character.toUpperCase(c);
			if (c == 'L') {
				token = new String(buf, tokenOffset, offset - tokenOffset);
				tokenType = Number_Long;
			} else if (c == 'D') {
				token = new String(buf, tokenOffset, offset - tokenOffset);
				tokenType = Number_Double;
			} else if (c == 'F') {
				token = new String(buf, tokenOffset, offset - tokenOffset);
				tokenType = Number_Float;
			} else {
				if (c != EndChar)
					unread();
				token = new String(buf, tokenOffset, offset - tokenOffset);
				if (token.indexOf('.') != -1 || token.indexOf('e')!=-1 ||token.indexOf('E')!=-1) {
					tokenType = Number_Float;
				} else {
					tokenType = Number_Integer;
				}
			}
			break;
		case '[':
			//attribute definition
			int level = 1;
			while (c != EndChar && level != 0) {
				c = read();
				if (c == '[') {
					level++;
				} else if (c == ']') {
					level--;
				}
			}
			if (c == EndChar) {
				exception(getOffset(), getLineNumberStart(), getColumnNumberStart(), "unclosed.attribute.definition");
			} else {
				token = new String(buf, tokenOffset, offset - tokenOffset);
				tokenType = Attribute;
				break;
			}
		case '<':
			//<init>
			while (c != '>') {
				c = read();
			}
			token = new String(buf, tokenOffset, offset - tokenOffset);
			tokenType = Defualt;
			break;
		default:
			do {
				c = read();
			} while (c != EndChar && isSeparatingChar(c) == false);
			if (c == '[') {
				while (c == '[' || c == ']') {
					// array like int[][]
					c = read();
				}
			} else if (c == '<') {
				// '<init>'
				while (c != '>' && c != EndChar) {
					c = read();
				}
				read();
			}
			if (c != EndChar) {
				unread();
			}
			token = new String(buf, tokenOffset, offset - tokenOffset);
			if (isInstruction(token) == true) {
				tokenType = Instruction;
			} else if (isAccessFlag(token) == true) {
				tokenType = AccessFlag;
			} else if ((tokenType = specialNumberType(token)) != -1) {

			} else if (isValidName(token) == true) {
				tokenType = JavaName;
			} else {
				tokenType = Defualt;
			}
		}
		//				System.out.println(token);
		return tokenType;
	}

	/**
	 * processing speical number types. see Negativ_Infinity Positive_Infinity NaN definitions in Scannable.
	 * @param s
	 * @return String
	 */
	private int specialNumberType(String s) {
		if ("InfinityD".equals(token) == true || "Infinityd".equals(token) == true) {
			return Number_Double_Positive_Infinity;
		} else if ("InfinityF".equals(token) == true || "Infinityf".equals(token) == true) {
			return Number_Double_Positive_Infinity;
		} else if ("NaND".equals(token) == true || "NaNd".equals(token) == true) {
			return Number_Double_NaN;
		} else if ("NaNF".equals(token) == true || "NaNf".equals(token) == true) {
			return Number_Float_NaN;
		}
		return -1;
	}

	private boolean isValidName(String s) {
		if (s == null || s.length() == 0) {
			return false;
		}
		char c = s.charAt(0);
		if (Character.isLetter(c) == false) {
			return false;
		}
		for (int i = 1; i < s.length(); i++) {
			c = s.charAt(i);
			if (Character.isJavaIdentifierPart(c) == false) {
				return false;
			}
		}
		return true;
	}

	public int getOffset() {
		return tokenOffset;
	}

	public int getLength() {
		return offset - tokenOffset;
	}

	public int tokenType() {
		return tokenType;
	}

	public String token() {
		return token;
	}

	public int getLineNumberStart() {
		return lineNumberStart;
	}

	public int getLineNumberEnd() {
		return lineNumber;
	}

	public int getColumnNumberStart() {
		return columnNumberStart;
	}

	public int getColumnNumberEnd() {
		return columnNumber;
	}

	private void skipSpaces() {
		char c = read();
		while (c != EndChar && Character.isWhitespace(c) == true) {
			c = read();
		}

		if (c != EndChar) {
			unread();
		}
	}

	private char read() {
		if (offset >= contentLength) {
			return EndChar;
		} else {
			char c = buf[offset++];
			if (lineNumberOn == true && c == '\n') {
				lineNumber++;
			}
			if (columnNumberOn == true) {
				oldColumnNumber = columnNumber;
				if (c == '\r' || c == '\n') {
					columnNumber = 0;
				} else {
					columnNumber++;
				}
			}
			return c;
		}
	}

	private void consumeDigits() {
		char c;
		do {
			c = read();
		} while (c != EndChar && Character.isDigit(c) == true);
		if (c != EndChar) {
			unread();
		}
	}

	private void unread() {
		offset--;
		char c = buf[offset];
		if (lineNumberOn == true && c == '\n') {
			lineNumber--;
		}
		if (columnNumberOn == true) {
			columnNumber = oldColumnNumber;
		}

	}

	/**
	 * delete the single line and multi line comment,  
	 * if multiline comment is not closed.
	 *  TODO: this can be merged into read()
	 * @param cs
	 */
	private static void delComment(char[] cs) throws ParsingException {
		boolean multiLine = false;
		boolean singleLine = false;
		boolean inQuote = false;
		char c;
		int len = cs.length;
		int multiLineStarting = 0; // for error reporting
		for (int i = 0; i < len; i++) {
			c = cs[i];
			if (inQuote == true) {
				if (c == '\\' && i < len - 1) {
					i++; // escaped chars
				} else if (c == '"') {
					inQuote = false;
				}
			} else if (c == '"') {
				inQuote = true;
			} else if (multiLine == true) {
				if (c == '*' && i < len - 1 && cs[i + i] == '/') {
					multiLine = false;
					cs[i] = ' ';
					i++;
				}
				cs[i] = ' ';
			} else if (singleLine == true) {
				if (c == '\n') {
					singleLine = false;
				} else {
					cs[i] = ' ';
				}
			} else if (c == '/' && i < len - 1) {
				if (cs[i + 1] == '*') {
					multiLineStarting = i;
					cs[i++] = ' ';
					cs[i] = ' ';
					multiLine = true;
				} else if (cs[i + 1] == '/') {
					cs[i++] = ' ';
					cs[i] = ' ';
					singleLine = true;
				}
			}
		}
		if (multiLine == true) {
			exception(multiLineStarting, -1, -1, "unclosed.multi.line.comment");
		}
	}

	private boolean isSeparatingChar(char c) {
		return !(Character.isJavaIdentifierPart(c) || c == '.');
	}

	private boolean isInstruction(String s) {
		return Constants.OPCODE_NAMESET.contains(s);
	}

	private boolean isAccessFlag(String s) {
		return Constants.ACCESS_FLAG_SET.contains(s);
	}

	private static void exception(Scanner sc, String msg) throws ParsingException {
		throw new ParsingException(sc.offset, sc.lineNumber, sc.columnNumber, msg);
	}

	private static void exception(int offset, int line, int column, String msg) throws ParsingException {
		throw new ParsingException(offset, line, column, msg);
	}

	public static void main(String[] s) throws Exception {
		Scanner sc = new Scanner("234.331 12.3e-334 -334.3e-34 0x33Fe 0334->deas  16   ");
		while (true) {
			try {
				sc.nextToken();			
			} catch (ParsingException e) {

				return;
			}
			System.out.print(sc.tokenType() + "," + sc.token() + "," + sc.getOffset() + "," + sc.getColumnNumberStart() + ","
					+ sc.getLineNumberStart() + '\n');
			if (sc.tokenType() == EOF) {
				break;
			}
		}
	}
}