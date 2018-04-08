package kr.co.idiots.model;

public class POPStartNode extends POPSymbolNode {
	
	public POPStartNode(POPScriptArea scriptArea) {
		super(scriptArea, POPNodeType.Start);
		
		component.setLayoutX(50d);
		component.setLayoutY(50d);
		
		//flowLine = new POPFlowLine();
		outFlowLine = new POPFlowLine();
		outFlowLine.setPrevNode(this);
		
		//scriptArea.getComponent().getChildren().add(flowLine);
//		double x, y;
//		Bounds nodeBound = component.getBoundsInParent();
//		x = nodeBound.getMinX() + (nodeBound.getWidth() / 2) - (flowLine.getComponent().getBoundsInParent().getWidth() / 2);
//		y = nodeBound.getMaxY();
//		System.out.println(nodeBound.getMinX());
//		System.out.println(x + ", " + y);
//		flowLine.getComponent().setX(x);
//		flowLine.getComponent().setY(y);
	}
	
//	@Override
//	public void setNextNode(POPNode nextNode) {
//		this.nextNode = nextNode;
//		flowLine.setNextNode(nextNode);
////		this.nextNode = nextNode;
////		
////		double x, y;
////		
////		Bounds nodeBound = component.getBoundsInParent();
////		Bounds nextNodeBound = nextNode.getComponent().getBoundsInParent();
////		//Bounds flowLineBound = flowLine.getComponent().getBoundsInParent();
////		x = nextNodeBound.getMinX() + (nextNodeBound.getWidth() / 2);// - (nextNode.getComponent().getBoundsInParent().getWidth() / 2);
////		//y = flowLineBound.getMaxY();
////		
////		flowLine.setEndX(nextNodeBound.getMinX() + (nextNodeBound.getWidth() / 2));
////		flowLine.setEndY(nextNodeBound.getMinY());
////		
////		flowLine.setStartX(nodeBound.getMinX() + (nodeBound.getWidth() / 2));
////		flowLine.setStartY(nodeBound.getMaxY());
////		
//////		nextNode.getComponent().setX(x);
//////		nextNode.getComponent().setY(y);
//	}
}
