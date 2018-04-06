package kr.co.idiots.model;

import java.util.Iterator;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.layout.FlowPane;

public abstract class POPDataInput extends FlowPane {
	private FlowPane component;
	private IntegerProperty childrenCount;
	protected String content;
	protected POPNode parentNode;
	
	public POPDataInput(POPNode node) {
		this.parentNode = node;
		component = this;
		component.setAlignment(Pos.CENTER);
		component.setPrefSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
		component.setPrefWrapLength(0);
		component.setHgap(5);
		component.setPrefWrapLength(getParentNode().getImageView().getBoundsInLocal().getWidth());
		
		childrenCount = new SimpleIntegerProperty(component.getChildren().toArray().length);
		content = "";
//		InputStream stream = getClass().getResourceAsStream("/images/Blank.png");
//		Image img = new Image(stream);
//		blank = new ImageView(img);
		
//		this.getChildren().add(blank);
//		DragManager.setOnBlankDrag(this);
//		updateBound();
//		setOnAddListener();
//		setOnBoundChangeListener();
	}
	
	private void setOnAddListener() {
		childrenCountProperty().addListener(new ChangeListener() {

			@Override
			public void changed(ObservableValue arg0, Object arg1, Object arg2) {
				// TODO Auto-generated method stub
				System.out.println("datainput changed");
				updateBound();
			}
			
		});
	}
	
	private void setOnBoundChangeListener() {
		boundsInParentProperty().addListener(new ChangeListener<Bounds>() {

			@Override
			public void changed(ObservableValue<? extends Bounds> arg0, Bounds oldBound, Bounds newBound) {
				// TODO Auto-generated method stub
//				System.out.println("Bound Changed!!");
//				System.out.println(oldBound + " -> " + newBound);
				
				
//				System.out.println("scale : " + (newBound.getWidth() - oldBound.getWidth()));
//				updateBound();
//				Bounds parentBound = parentNode.getComponent().getBoundsInParent();
//				component.setLayoutX(parentBound.getMinX() + (parentBound.getWidth() / 2) - (newBound.getWidth() / 2));
//				component.setLayoutY(parentBound.getMinY() + (parentBound.getHeight() / 2) - (newBound.getHeight() / 2));
//				if(outFlowLine != null) {
//					outFlowLine.setStartX(newBound.getMinX() + (newBound.getWidth() / 2));
//					outFlowLine.setStartY(newBound.getMaxY());
//				}
//				
////				startY.set(line.getStartY());
//				
//				if(inFlowLine != null) {
//					inFlowLine.setEndX(newBound.getMinX() + (newBound.getWidth() / 2));
//					inFlowLine.setEndY(newBound.getMinY());
////					prevNode.getInFlowLine().endY.set(getEndY());
//				}
			}
			
		});
	}
	
	public void updateBound() {
		
		Iterator i = component.getChildren().iterator();
		Bounds prevBound = null;
		while(i.hasNext()) {
			Node child = (Node) i.next();
//			System.out.println(child);
			if(prevBound != null) {
				child.setLayoutX(prevBound.getMaxX() + 10);
				child.setLayoutY(prevBound.getMinY() + (prevBound.getHeight() / 2) - (child.getBoundsInLocal().getHeight() / 2));
			}
//			System.out.println(child.getBoundsInLocal());
			
			prevBound = child.getBoundsInLocal();
		}
		
		Bounds parentBound = parentNode.getComponent().getBoundsInLocal();
		Bounds myBound = component.getBoundsInLocal();
		component.setLayoutX(parentBound.getMinX() + (parentBound.getWidth() / 2) - (myBound.getWidth() / 2));
		component.setLayoutY(parentBound.getMinY() + (parentBound.getHeight() / 2) - (myBound.getHeight() / 2));
//		component.maxWidth(getParent().getBoundsInParent().getWidth());
		System.out.println("--DataInput : " + component.getBoundsInParent());
	}
	
	public void add(int index, Node node) {
		getChildren().add(index, node);
		childrenCount.setValue(childrenCount.getValue() + 1);
	}
	
	public void add(Node node) {
		getChildren().add(node);
		childrenCount.setValue(childrenCount.getValue() + 1);
	}
	
	public void remove(Node node) {
		System.out.println("delete : " + node);
		getChildren().remove(node);
		childrenCount.setValue(childrenCount.getValue() - 1);
	}
	
	public final int getChildrenCount() { return childrenCount.get(); }
	public final void setChildrenCount(int value) { childrenCount.set(value); }
	public IntegerProperty childrenCountProperty() { return childrenCount; }
	
//	public abstract void insertInputVariable(POPVariableNode variable);
	
	public void setParentNode(POPNode node) { this.parentNode = node; }
	public POPNode getParentNode() { return this.parentNode; }
}
