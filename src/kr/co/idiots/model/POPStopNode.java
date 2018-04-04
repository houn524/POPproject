package kr.co.idiots.model;

public class POPStopNode extends POPSymbolNode {
	
	public POPStopNode(POPScriptArea scriptArea) {
		super(scriptArea, POPNodeType.Stop);
		
		component.setTranslateX(50d);
		component.setTranslateY(400d);
		
		setOnBoundChangeListener();
	}

//	@Override
//	public void setNextNode(POPNode nextNode) {
//		// TODO Auto-generated method stub
//		
//	}
}
