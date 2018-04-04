package kr.co.idiots.model;

import java.io.InputStream;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import kr.co.idiots.controller.DragManager;

public abstract class POPDataInput extends Group {
	protected ImageView blank;
	protected POPNode node;
	
	public POPDataInput() {
		InputStream stream = getClass().getResourceAsStream("/images/Blank.png");
		Image img = new Image(stream);
		blank = new ImageView(img);
		
		this.getChildren().add(blank);
		DragManager.setOnBlankDrag(this);
	}
	
	public abstract void insertInputVariable(POPVariableNode variable);
	
	public void setNode(POPNode node) { this.node = node; }
	public POPNode getNode() { return this.node; }
}
