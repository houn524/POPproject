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
		
		setOnBoundChangeListener();
		
		outFlowLine = new POPFlowLine();
		outFlowLine.setPrevNode(this);
		
		outFlowLine.setVisible(false);
	}
	
	@Override
	public void initialize() {
		super.initialize();
		
		POPOutputSymbol symbol = new POPOutputSymbol();
		symbol.initialize(this);
		this.setOperationSymbol(symbol);
		StackPane.setAlignment(symbol, Pos.CENTER);
		component.getChildren().add(symbol);
		symbol.setRootSymbol(true);
		this.setRootSymbol(symbol);
	}

}
