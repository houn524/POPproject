package kr.co.idiots.model;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPSymbolNode extends POPNode {

	protected POPFlowLine inFlowLine;
	protected POPFlowLine outFlowLine;
	
	public POPSymbolNode(POPScriptArea scriptArea, POPNodeType type) {
		super(scriptArea, type);
		
		setOnBoundChangeListener();
		
		if(type != POPNodeType.Start && type != POPNodeType.Stop)
			setOnNodeDrag();
		// TODO Auto-generated constructor stub
	}
	
	public void initialize() {
		
	}
	
	public void moveCenter() {
		if(inFlowLine != null) {
			
			
			Bounds newBound = component.getBoundsInParent();
			
			if(outFlowLine != null) {
				outFlowLine.setStartX(newBound.getMinX() + (newBound.getWidth() / 2));
				outFlowLine.setStartY(newBound.getMaxY());
			}
			
			if(inFlowLine != null) {
				inFlowLine.setEndX(newBound.getMinX() + (newBound.getWidth() / 2));
				inFlowLine.setEndY(newBound.getMinY());
			}
			
			component.setLayoutX(inFlowLine.getStartX() - (component.getWidth() / 2));
		}
	}

	private void setOnBoundChangeListener() {
		component.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {

			@Override
			public void changed(ObservableValue<? extends Bounds> arg0, Bounds oldBound, Bounds newBound) {
				// TODO Auto-generated method stub
				
				if(newBound.getHeight() > imgView.getBoundsInLocal().getHeight())
					return;
				
				if(outFlowLine != null) {
					outFlowLine.setStartX(newBound.getMinX() + (newBound.getWidth() / 2));
					outFlowLine.setStartY(newBound.getMaxY());
				}
				
				if(inFlowLine != null) {
					inFlowLine.setEndX(newBound.getMinX() + (newBound.getWidth() / 2));
					inFlowLine.setEndY(newBound.getMinY());
				}
				moveCenter();
			}
		});
	}
	
	
	protected void setOnDataInputBoundChangeListener() {
		dataInput.boundsInLocalProperty().addListener(new ChangeListener<Bounds>() {
			@Override
			public void changed(ObservableValue<? extends Bounds> arg0, Bounds oldBound, Bounds newBound) {
				// TODO Auto-generated method stub
				moveCenter();
			}
		});
	}
	
	private void setOnNodeDrag() {
		
		getComponent().setOnMouseDragged(event -> {
			event.setDragDetect(true);
			event.consume();
		});
		
		getComponent().setOnDragDetected(event -> {
			Node on = (Node) event.getTarget();
			Dragboard db = on.startDragAndDrop(TransferMode.COPY);
			ClipboardContent content = new ClipboardContent();
			content.putString(getType().toString());
			content.putImage(getImgView().getImage());
				
			db.setContent(content);
			event.consume();
		});
	}
}
