package kr.co.idiots.model;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;

public class POPDocumentNode extends POPSymbolNode {

	public POPDocumentNode(POPScriptArea scriptArea) {
		super(scriptArea, POPNodeType.Document);
		
		outFlowLine = new POPFlowLine();
		outFlowLine.setPrevNode(this);
	}
	
	@Override
	public void initialize() {
		POPDocumentDataInput dataInput = new POPDocumentDataInput(this);
    	setDataInput(dataInput);
    	StackPane.setAlignment(dataInput, Pos.CENTER);
    	component.getChildren().add(dataInput);
	}

}
