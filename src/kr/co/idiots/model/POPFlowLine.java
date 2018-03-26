package kr.co.idiots.model;

import java.io.InputStream;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class POPFlowLine {
	
	private ImageView component;
	
	public POPFlowLine() {
		InputStream stream = getClass().getResourceAsStream("/images/FlowLine.png");
		Image img = new Image(stream);
		component = new ImageView(img);
	}
	
	public ImageView getComponent() { return component; }
}
