package kr.co.idiots.model;

import java.io.InputStream;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class POPProcessDataInput extends POPDataInput {
	
	public POPProcessDataInput(POPNode parentNode) {
		super(parentNode);
		
		POPBlank blank = new POPBlank(this);
		this.add(blank);
		
		InputStream stream = getClass().getResourceAsStream("/images/Equal.png");
		Image img = new Image(stream);
		ImageView equal = new ImageView(img);
		this.add(equal);
		updateBound();
//		Label equal = new Label("=");
//		this.getChildren().add(equal);
	}
	
//	public void insertInputVariable(POPVariableNode variable) {
//		String text = variable.getName() + " = ";
//		Label label = new Label(text);
//		node.getComponent().getChildren().add(label);
//		this.getChildren().remove(this.blank);
//	}
}
