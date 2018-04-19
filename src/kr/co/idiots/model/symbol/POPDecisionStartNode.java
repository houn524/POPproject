package kr.co.idiots.model.symbol;

import kr.co.idiots.model.POPFlowLine;
import kr.co.idiots.model.POPNodeType;
import kr.co.idiots.model.POPScriptArea;

public class POPDecisionStartNode extends POPSymbolNode {

	public POPDecisionStartNode(POPScriptArea scriptArea) {
		super(scriptArea, POPNodeType.DecisionSub);
		// TODO Auto-generated constructor stub
		outFlowLine = new POPFlowLine();
		outFlowLine.setPrevNode(this);
		
		setOnBoundChangeListener();
//		outFlowLine.setEndX(outFlowLine.getStartX());
//		outFlowLine.setEndY(outFlowLine.getStartY() + 10);
	}
	
//	public void setOnBoundChangeListener() {
//		component.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {
//
//			@Override
//			public void changed(ObservableValue<? extends Bounds> arg0, Bounds oldBound, Bounds newBound) {
//				// TODO Auto-generated method stub
//				if(outFlowLine != null) {
//					outFlowLine.setStartX(newBound.getMinX() + (newBound.getWidth() / 2));
//					outFlowLine.setStartY(newBound.getMaxY());
//				}
//				
//				if(inFlowLine != null) {
//					inFlowLine.setEndX(newBound.getMinX() + (newBound.getWidth() / 2));
//					inFlowLine.setEndY(newBound.getMinY());
//				}
//				moveCenter();
//			}
//			
//		});
//	}

}
