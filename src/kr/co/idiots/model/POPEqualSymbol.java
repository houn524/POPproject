package kr.co.idiots.model;

import java.io.InputStream;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class POPEqualSymbol extends POPOperationSymbol {
	
	public POPEqualSymbol(POPDataInput parentDataInput) {
		super(parentDataInput);
		
		symbol = " = ";
		
		leftBlank = new POPBlank(this);
		getChildren().add(leftBlank);
		
		InputStream stream = getClass().getResourceAsStream("/images/Equal.png");
		Image img = new Image(stream);
		ImageView equal = new ImageView(img);
		getChildren().add(equal);
		
		rightBlank = new POPBlank(this);
		getChildren().add(rightBlank);
		
	}
	
}
