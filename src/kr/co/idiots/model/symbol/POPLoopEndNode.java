package kr.co.idiots.model.symbol;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import kr.co.idiots.model.POPNodeType;
import kr.co.idiots.model.POPScriptArea;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPLoopEndNode extends POPSymbolNode {

	private POPLoopNode loopNode;
	
	public POPLoopEndNode(POPScriptArea scriptArea) {
		super(scriptArea, POPNodeType.LoopSub);
		// TODO Auto-generated constructor stub
		
		setOnBoundChangeListener();
	}

	@Override
	protected void setOnBoundChangeListener() {
		component.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {

			@Override
			public void changed(ObservableValue<? extends Bounds> arg0, Bounds oldBound, Bounds newBound) {
				// TODO Auto-generated method stub
				
				
				if(imgView != null && newBound.getHeight() > imgView.getBoundsInLocal().getHeight())
					return;
				
//				topCenterXProperty().set(newBound.getMinX() + (newBound.getWidth() / 2));
//				topYProperty().set(newBound.getMinY());
//				
//				bottomCenterXProperty().set(newBound.getMinX() + (newBound.getWidth() / 2));
//				bottomYProperty().set(newBound.getMaxY());
				
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
//				if(outFlowLine != null && outFlowLine.getNextNode() != null) {
//					outFlowLine.getNextNode().moveCenter();
//				}
				
//				if(loopNode != null) {
//					loopNode.adjustPosition();
//					System.out.println("4");
//				}
			}
		});
	}
}
