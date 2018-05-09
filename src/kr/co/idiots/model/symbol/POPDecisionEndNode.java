package kr.co.idiots.model.symbol;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import kr.co.idiots.model.POPNodeType;
import kr.co.idiots.model.POPScriptArea;
import kr.co.idiots.model.POPSideFlowLine;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPDecisionEndNode extends POPSymbolNode {
	private POPDecisionEndNode sideNode;
	private POPDecisionStartNode startNode;
	private POPDecisionNode decisionNode;
	private POPSideFlowLine sideFlowLine;
	
	public POPDecisionEndNode(POPScriptArea scriptArea, POPDecisionNode decisionNode, POPDecisionEndNode sideNode) {
		super(scriptArea, POPNodeType.DecisionSub);
		this.sideNode = sideNode;
		this.decisionNode = decisionNode;
		setOnBoundChangeListener();
		// TODO Auto-generated constructor stub
	}
	
	public POPDecisionEndNode createSideFlowLine() {
		sideFlowLine = new POPSideFlowLine(this);
		
		return this;
	}
	
	public double getLength() {
		double length;
		
		return inFlowLine.getEndY() - inFlowLine.getStartY();
		
	}
	
	public boolean hasSpace() {
		if(sideNode.getInFlowLine().getStartY() > inFlowLine.getStartY())
			return true;
		else
			return false;
	}
	
	public boolean isAllEmpty() {
		if(inFlowLine.getPrevNode() instanceof POPDecisionStartNode) {
			if(sideNode.inFlowLine.getPrevNode() instanceof POPDecisionStartNode) {
				return true;
			}
		}
		 return false;
	}
	
	@Override
	protected void setOnBoundChangeListener() {
		component.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {

			@Override
			public void changed(ObservableValue<? extends Bounds> arg0, Bounds oldBound, Bounds newBound) {
				// TODO Auto-generated method stub
				
				if(inFlowLine != null) {
//					inFlowLine.setEndX(newBound.getMinX());
					inFlowLine.setEndY(newBound.getMinY());
				}
			}
		});
	}

}
