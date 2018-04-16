package kr.co.idiots.model.symbol;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import kr.co.idiots.model.POPFlowLine;
import kr.co.idiots.model.POPNodeType;
import kr.co.idiots.model.POPScriptArea;
import kr.co.idiots.model.operation.POPOutputSymbol;

public class POPDocumentNode extends POPSymbolNode {

	public POPDocumentNode(POPScriptArea scriptArea) {
		super(scriptArea, POPNodeType.Document);
		
		outFlowLine = new POPFlowLine();
		outFlowLine.setPrevNode(this);
	}
	
	@Override
	public void initialize() {
		super.initialize();
//		POPDocumentDataInput dataInput = new POPDocumentDataInput(this);
//    	setDataInput(dataInput);
//    	StackPane.setAlignment(dataInput, Pos.CENTER);
//    	component.getChildren().add(dataInput);
		
		POPOutputSymbol symbol = new POPOutputSymbol();
		symbol.initialize(this);
		this.setOperationSymbol(symbol);
		StackPane.setAlignment(symbol, Pos.CENTER);
		component.getChildren().add(symbol);
		symbol.setRootSymbol(true);
		this.setRootSymbol(symbol);
		
		this.moveCenter();
	}

}