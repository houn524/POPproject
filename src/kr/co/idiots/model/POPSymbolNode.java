package kr.co.idiots.model;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;

public class POPSymbolNode extends POPNode {

	protected POPFlowLine inFlowLine;
	protected POPFlowLine outFlowLine;
	
	public POPSymbolNode(POPScriptArea scriptArea, POPNodeType type) {
		super(scriptArea, type);
		// TODO Auto-generated constructor stub
	}

	protected void setOnBoundChangeListener() {
		component.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {

			@Override
			public void changed(ObservableValue<? extends Bounds> arg0, Bounds oldBound, Bounds newBound) {
				// TODO Auto-generated method stub
//				System.out.println("Bound Changed!!");
//				System.out.println(oldBound + " -> " + newBound);
				if(outFlowLine != null) {
					outFlowLine.setStartX(newBound.getMinX() + (newBound.getWidth() / 2));
					outFlowLine.setStartY(newBound.getMaxY());
				}
				
//				startY.set(line.getStartY());
				
				if(inFlowLine != null) {
					inFlowLine.setEndX(newBound.getMinX() + (newBound.getWidth() / 2));
					inFlowLine.setEndY(newBound.getMinY());
//					prevNode.getInFlowLine().endY.set(getEndY());
				}
			}
			
		});
	}
	
	public POPFlowLine getOutFlowLine() { return outFlowLine; }
	public POPFlowLine getInFlowLine() { return inFlowLine; }
	public void setInFlowLine(POPFlowLine flowLine) {
		this.inFlowLine = flowLine;
	}
}
