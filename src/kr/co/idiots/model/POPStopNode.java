package kr.co.idiots.model;

public class POPStopNode extends POPSymbolNode {
	
	public POPStopNode(POPScriptArea scriptArea) {
		super(scriptArea, POPNodeType.Stop);
		
		component.setLayoutX(50d);
		component.setLayoutY(400d);
	}
}
