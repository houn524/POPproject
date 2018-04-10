package kr.co.idiots.model;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;

public class POPProcessNode extends POPSymbolNode {
	
	public POPProcessNode(POPScriptArea scriptArea) {
		super(scriptArea, POPNodeType.Process);

		outFlowLine = new POPFlowLine();
		outFlowLine.setPrevNode(this);
	}
	
	@Override
	public void initialize() {
		
		POPProcessDataInput dataInput = new POPProcessDataInput(this);
    	setDataInput(dataInput);
    	StackPane.setAlignment(dataInput, Pos.CENTER);
    	component.getChildren().add(dataInput);
    	
		this.moveCenter();
	}	
}
