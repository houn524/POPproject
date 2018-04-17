package kr.co.idiots.model;

import java.util.EnumSet;

public enum POPNodeType {
	Start, Stop, Process, Document, Decision, DecisionSub,
	FlowLine,
	IntegerVariable, DoubleVariable, StringVariable,
	Equal, Output, Plus, Minus, Multiply, Divide, Compare;
	
	public static EnumSet<POPNodeType> symbolGroup = EnumSet.of(Start, Stop, Process, Document, Decision);
	public static EnumSet<POPNodeType> operationGroup = EnumSet.of(Equal, Output, Plus, Minus, Multiply, Divide, Compare);
	public static EnumSet<POPNodeType> variableGroup = EnumSet.of(IntegerVariable, DoubleVariable, StringVariable);
}