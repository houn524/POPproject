package kr.co.idiots.model;

public class POPProcessNode extends POPSymbolNode {

	public POPProcessNode(POPScriptArea scriptArea) {
		super(scriptArea, POPNodeType.Process);
		
		//component.setTranslateX(50d);
		//component.setTranslateY(50d);
		
		outFlowLine = new POPFlowLine();
		outFlowLine.setPrevNode(this);
		
		setOnBoundChangeListener();
//		flowLine.setPrevNode(this);
	}

//	@Override
//	public void setNextNode(POPNode nextNode) {
//		// TODO Auto-generated method stub
//		this.nextNode = nextNode;
//		flowLine.setNextNode(nextNode);
//	}
	
}
