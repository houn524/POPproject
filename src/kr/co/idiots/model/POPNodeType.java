package kr.co.idiots.model;

import java.util.EnumSet;

public enum POPNodeType {
	Start, Stop, Process, Document,
	FlowLine,
	Variable,
	Equal, Output, Plus;
	
	public static EnumSet<POPNodeType> symbolGroup = EnumSet.of(Start, Stop, Process, Document);
}