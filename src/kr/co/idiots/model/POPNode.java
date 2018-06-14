package kr.co.idiots.model;

import java.io.InputStream;
import java.io.Serializable;

import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import kr.co.idiots.model.operation.POPOperationSymbol;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class POPNode extends StackPane implements Serializable, Cloneable {
	
	protected POPNodeType type;
	protected ImageView imgView;
//	protected POPDataInput dataInput;
	protected POPOperationSymbol operationSymbol;
	protected POPOperationSymbol rootSymbol;
	protected Label label;
	protected StackPane component;
	protected POPScriptArea scriptArea;
	protected double initWidth;
	protected boolean isInitialized = false;
	protected boolean isAllocated = false;
	protected Point2D lastXY = null;
	
	protected ContextMenu contextMenu;
	
	public POPNode(POPScriptArea scriptArea, POPNodeType type) {
		this.scriptArea = scriptArea;
		this.type = type;
		
		component = this;
		component.setPrefWidth(Control.USE_COMPUTED_SIZE);
		component.setPrefHeight(Control.USE_COMPUTED_SIZE);
		component.setAlignment(Pos.CENTER);
		
		if(type != POPNodeType.DecisionSub && type != POPNodeType.LoopSub) {
			InputStream stream = getClass().getResourceAsStream("/images/" + type.toString() + ".png");
			Image img = new Image(stream);
			imgView = new ImageView(img);
			
			initWidth = imgView.getBoundsInLocal().getWidth();
			imgView.setFitWidth(initWidth);
			component.getChildren().add(imgView);
			StackPane.setAlignment(imgView, Pos.CENTER);
			component.setMaxHeight(imgView.getBoundsInLocal().getHeight());
			
			
		}
		
	}
	
	public void moveCenter() {
		
	}
	
	public void setNodeAutoSize(double width) {
		imgView.setFitWidth(Math.max(width + 40, initWidth));
	}

}
