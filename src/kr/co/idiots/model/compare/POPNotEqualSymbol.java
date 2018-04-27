package kr.co.idiots.model.compare;

import java.io.InputStream;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import kr.co.idiots.model.POPBlank;
import kr.co.idiots.model.POPNodeType;
import kr.co.idiots.model.operation.POPOperationSymbol;

public class POPNotEqualSymbol extends POPOperationSymbol {
	
	private ImageView notEqual;
	
	public POPNotEqualSymbol() {
		super();
		InputStream stream = getClass().getResourceAsStream("/images/Compare.png");
		Image img = new Image(stream);
		imgShape.setImage(img);
		
		this.type = POPNodeType.NotEqual;
		symbol = " != ";
		
		leftBlank = new POPBlank(this);
		contents.getChildren().add(leftBlank);
		
		stream = getClass().getResourceAsStream("/images/NotEqual.png");
		img = new Image(stream);
		notEqual = new ImageView(img);
		contents.getChildren().add(notEqual);
		
		rightBlank = new POPBlank(this);
		contents.getChildren().add(rightBlank);
		
		setInitWidth();
	}
}
