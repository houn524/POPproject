package kr.co.idiots.model.symbol;

import kr.co.idiots.model.POPNodeType;
import kr.co.idiots.model.POPScriptArea;

public class POPStopNode extends POPSymbolNode {
	
	public POPStopNode(POPScriptArea scriptArea) {
		super(scriptArea, POPNodeType.Stop);
		
		component.setLayoutX(50d);
		component.setLayoutY(400d);
	}
}
