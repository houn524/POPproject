package kr.co.idiots.model;

import java.io.InputStream;

import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class POPStartNode extends POPNode {
	
	public POPStartNode(POPScriptArea scriptArea) {
		super(scriptArea);
		
		this.type = POPNodeType.START;
		InputStream stream = getClass().getResourceAsStream("/images/Start.png");
		Image img = new Image(stream);
		this.component = new ImageView(img);
		
		component.setTranslateX(50d);
		component.setTranslateY(50d);
		
		flowLine = new POPFlowLine();
		
		double x, y;
		Bounds nodeBound = component.getBoundsInParent();
		x = nodeBound.getMinX() + (nodeBound.getWidth() / 2) - (flowLine.getComponent().getBoundsInParent().getWidth() / 2);
		y = nodeBound.getMaxY();
		System.out.println(nodeBound.getMinX());
		System.out.println(x + ", " + y);
		flowLine.getComponent().setX(x);
		flowLine.getComponent().setY(y);
	}
	
	@Override
	public void setNextNode(POPNode nextNode) {
		this.nextNode = nextNode;
		
		double x, y;
		
		Bounds nodeBound = component.getBoundsInParent();
		Bounds flowLineBound = flowLine.getComponent().getBoundsInParent();
		x = nodeBound.getMinX() + (nodeBound.getWidth() / 2) - (nextNode.getComponent().getBoundsInParent().getWidth() / 2);
		y = flowLineBound.getMaxY();
		
		nextNode.getComponent().setX(x);
		nextNode.getComponent().setY(y);
	}
}
