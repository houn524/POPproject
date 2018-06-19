package kr.co.idiots.model.symbol;

import kr.co.idiots.model.POPFlowLine;
import kr.co.idiots.model.POPNodeType;
import kr.co.idiots.model.POPScriptArea;

public class POPDecisionStartNode extends POPSymbolNode {
	
	public POPDecisionStartNode(POPScriptArea scriptArea) {
		super(scriptArea, POPNodeType.DecisionSub);
		outFlowLine = new POPFlowLine();
		outFlowLine.setRootNode(this);
		outFlowLine.setPrevNode(this);
		
		setOnBoundChangeListener();
	}
}
