package com.jasml.decompiler;

import com.jasml.helper.Util;

public class SourceCodeBuilderConfiguration {
	protected boolean showVersion = true;

	protected boolean showLineNumber = false;

	protected boolean labelInSingleLine = false;

	protected boolean showInfo = true;

	protected String labelPrefix = "line";

	protected int labelLength = 8;

	protected String instructionPadding = Util.padChar("", labelLength + 3, ' ');

	public SourceCodeBuilderConfiguration(boolean showVersion,boolean showLineNumber,boolean labelInSingleLine,boolean showInfo){
		this.showVersion = showVersion;
		this.showLineNumber = showLineNumber;
		this.labelInSingleLine = labelInSingleLine;
		this.showInfo = showInfo;
	}
	public SourceCodeBuilderConfiguration(){
		
	}
}
