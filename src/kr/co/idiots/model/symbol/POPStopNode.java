package kr.co.idiots.model.symbol;

import kr.co.idiots.model.POPNodeType;
import kr.co.idiots.model.POPScriptArea;

public class POPStopNode extends POPSymbolNode {
	
	public POPStopNode(POPScriptArea scriptArea) {
		super(scriptArea, POPNodeType.Stop);
		setOnBoundChangeListener();
		
		component.setLayoutX(50d);
		component.setLayoutY(135d);
		
		imgView.setStyle("-fx-effect: dropshadow(three-pass-box, black, 2, 0, 0, 1);");
	}
}
