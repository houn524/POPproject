package kr.co.idiots.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.layout.FlowPane;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class POPDataInput extends FlowPane {
	private FlowPane component;
	private IntegerProperty childrenCount;
	protected String content;
	protected POPNode parentNode;
	protected POPOperationSymbol rootSymbol;
	
	public POPDataInput(POPNode node) {
		this.parentNode = node;
		component = this;
		component.setAlignment(Pos.CENTER);
		component.setPrefSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
		component.setPrefWrapLength(0);
		component.setHgap(5);
		component.setPrefWrapLength(getParentNode().getImgView().getBoundsInLocal().getWidth());
		
		childrenCount = new SimpleIntegerProperty(component.getChildren().toArray().length);
		content = "";
	}
		
//	public void updateBound() {
//		
//		Iterator i = component.getChildren().iterator();
//		Bounds prevBound = null;
//		while(i.hasNext()) {
//			Node child = (Node) i.next();
//			if(prevBound != null) {
//				child.setLayoutX(prevBound.getMaxX() + 10);
//				child.setLayoutY(prevBound.getMinY() + (prevBound.getHeight() / 2) - (child.getBoundsInLocal().getHeight() / 2));
//			}
//			
//			prevBound = child.getBoundsInLocal();
//		}
//		
//		Bounds parentBound = parentNode.getComponent().getBoundsInLocal();
//		Bounds myBound = component.getBoundsInLocal();
//		component.setLayoutX(parentBound.getMinX() + (parentBound.getWidth() / 2) - (myBound.getWidth() / 2));
//		component.setLayoutY(parentBound.getMinY() + (parentBound.getHeight() / 2) - (myBound.getHeight() / 2));
//	}
	
	public void add(int index, Node node) {
		getChildren().add(index, node);
		childrenCount.setValue(childrenCount.getValue() + 1);
	}
	
	public void add(Node node) {
		getChildren().add(node);
		rootSymbol = (POPOperationSymbol) node;
		childrenCount.setValue(childrenCount.getValue() + 1);
		rootSymbol.setRootSymbol(true);
		setRootSymbol(rootSymbol);
	}
	
	public void remove(Node node) {
		getChildren().remove(node);
		childrenCount.setValue(childrenCount.getValue() - 1);
	}
	
	public String getCodeString() {
		return rootSymbol.getCodeString();
	}
	
	public final int getChildrenCount() { return childrenCount.get(); }
	public final void setChildrenCount(int value) { childrenCount.set(value); }
	public IntegerProperty childrenCountProperty() { return childrenCount; }	
}
