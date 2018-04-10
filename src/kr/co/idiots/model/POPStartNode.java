package kr.co.idiots.model;

public class POPStartNode extends POPSymbolNode {
	
	public POPStartNode(POPScriptArea scriptArea) {
		super(scriptArea, POPNodeType.Start);
		
		component.setLayoutX(50d);
		component.setLayoutY(50d);
		
		outFlowLine = new POPFlowLine();
		outFlowLine.setPrevNode(this);
		
	}
}
