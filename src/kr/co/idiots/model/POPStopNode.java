package kr.co.idiots.model;

import java.io.InputStream;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class POPStopNode extends POPNode {
	
	public POPStopNode(POPScriptArea scriptArea) {
		super(scriptArea);
		this.type = POPNodeType.STOP;
		InputStream stream = getClass().getResourceAsStream("/images/Stop.png");
		Image img = new Image(stream);
		this.component = new ImageView(img);
	}

	@Override
	public void setNextNode(POPNode nextNode) {
		// TODO Auto-generated method stub
		
	}
}
