package kr.co.idiots.model.symbol;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import kr.co.idiots.model.POPFlowLine;
import kr.co.idiots.model.POPNodeType;
import kr.co.idiots.model.POPScriptArea;
import kr.co.idiots.model.operation.POPEqualSymbol;

public class POPProcessNode extends POPSymbolNode {
	
	public POPProcessNode(POPScriptArea scriptArea) {
		super(scriptArea, POPNodeType.Process);

		setOnBoundChangeListener();
		
		outFlowLine = new POPFlowLine();
		outFlowLine.setPrevNode(this);
	}
	
	@Override
	public void initialize() {
		super.initialize();;
//		POPProcessDataInput dataInput = new POPProcessDataInput(this);
//    	setDataInput(dataInput);
//    	StackPane.setAlignment(dataInput, Pos.CENTER);
//    	component.getChildren().add(dataInput);
		
		POPEqualSymbol symbol = new POPEqualSymbol();
		symbol.initialize(this);
		this.setOperationSymbol(symbol);
		StackPane.setAlignment(symbol, Pos.CENTER);
		component.getChildren().add(symbol);
		symbol.setRootSymbol(true);
		this.setRootSymbol(symbol);
    	
//		this.moveCenter();
	}	
}
