package kr.co.idiots.model;

import java.io.InputStream;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import kr.co.idiots.controller.DragManager;

public abstract class POPNode {
	
	protected POPNodeType type;
	protected ImageView imgView;
	protected Group component;
	protected POPScriptArea scriptArea;

//	protected POPNode nextNode;
//	protected POPNode prevNode;
	
	public POPNode(POPScriptArea scriptArea, POPNodeType type) {
		this.scriptArea = scriptArea;
		this.type = type;
		
		InputStream stream = getClass().getResourceAsStream("/images/" + type.toString() + ".png");
		Image img = new Image(stream);
		imgView = new ImageView(img);
		
		component = new Group(imgView);
		
		if(type != POPNodeType.Start && type != POPNodeType.Stop)
			DragManager.setOnNodeDrag(this);
	}

	public Group getComponent() { return this.component; }
	public ImageView getImageView() { return this.imgView; }
	public POPScriptArea getScriptArea() { return this.scriptArea; }

	public POPNodeType getType() { return type; }
//	public abstract void setNextNode(POPNode nextNode);
//	public void setPrevNode(POPNode prevNode) { this.prevNode = prevNode; }
}
