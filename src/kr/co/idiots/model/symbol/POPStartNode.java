package kr.co.idiots.model.symbol;

import kr.co.idiots.model.POPFlowLine;
import kr.co.idiots.model.POPNodeType;
import kr.co.idiots.model.POPScriptArea;

public class POPStartNode extends POPSymbolNode {
	
	public POPStartNode(POPScriptArea scriptArea) {
		super(scriptArea, POPNodeType.Start);
		
		component.setLayoutX(50d);
		component.setLayoutY(50d);
		
		outFlowLine = new POPFlowLine();
		outFlowLine.setPrevNode(this);
		
	}
}
