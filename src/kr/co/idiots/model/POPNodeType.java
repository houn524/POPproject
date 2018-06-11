package kr.co.idiots.model;

import java.util.EnumSet;

public enum POPNodeType {
	Start, Stop, Process, Document, Decision, DecisionSub, Loop, LoopSub,
	FlowLine,
	IntegerVariable, DoubleVariable, StringVariable, ArraySize, InitVariable,
	Array, InitArray,
	Equal, Output, Plus, Minus, Multiply, Divide, Remainder, StringPlus, Compare,
	IsEqual, LessThan, LessThanEqual, NotEqual;
	
	public static EnumSet<POPNodeType> symbolGroup = EnumSet.of(Start, Stop, Process, Document, Decision, Loop);
	public static EnumSet<POPNodeType> operationGroup = EnumSet.of(Equal, Output, Plus, Minus, Multiply, Divide, Remainder, StringPlus, Compare);
	public static EnumSet<POPNodeType> compareGroup = EnumSet.of(IsEqual, LessThan, LessThanEqual, NotEqual);
	public static EnumSet<POPNodeType> variableGroup = EnumSet.of(IntegerVariable, DoubleVariable, StringVariable, ArraySize, InitVariable);
	public static EnumSet<POPNodeType> arrayGroup = EnumSet.of(Array, InitArray);
}