package kr.co.idiots.model;

import java.util.EnumSet;

public enum POPNodeType {
	Start, Stop, Process, Document,
	FlowLine,
	IntegerVariable, DoubleVariable, StringVariable,
	Equal, Output, Plus, Minus, Multiply, Divide;
	
	public static EnumSet<POPNodeType> symbolGroup = EnumSet.of(Start, Stop, Process, Document);
	public static EnumSet<POPNodeType> operationGroup = EnumSet.of(Equal, Output, Plus, Minus, Multiply, Divide);
	public static EnumSet<POPNodeType> variableGroup = EnumSet.of(IntegerVariable, DoubleVariable, StringVariable);
}