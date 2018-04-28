package kr.co.idiots.model.symbol;

import kr.co.idiots.model.POPFlowLine;
import kr.co.idiots.model.POPNodeType;
import kr.co.idiots.model.POPScriptArea;

public class POPLoopStartNode extends POPSymbolNode {

	private POPLoopNode loopNode;
	
	public POPLoopStartNode(POPScriptArea scriptArea) {
		super(scriptArea, POPNodeType.LoopSub);
		// TODO Auto-generated constructor stub
		
		outFlowLine = new POPFlowLine();
		outFlowLine.setRootNode(this);
		outFlowLine.setPrevNode(this);
		
		setOnBoundChangeListener();
	}

}
