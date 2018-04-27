package kr.co.idiots.model.compare;

import java.io.InputStream;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import kr.co.idiots.model.POPBlank;
import kr.co.idiots.model.POPNodeType;
import kr.co.idiots.model.operation.POPOperationSymbol;

public class POPLessThanEqualSymbol extends POPOperationSymbol {

	private ImageView lessThanEqual;
	
	public POPLessThanEqualSymbol() {
		super();
		InputStream stream = getClass().getResourceAsStream("/images/Compare.png");
		Image img = new Image(stream);
		imgShape.setImage(img);
		
		this.type = POPNodeType.LessThanEqual;
		symbol = " <= ";
		
		leftBlank = new POPBlank(this);
		contents.getChildren().add(leftBlank);
		
		stream = getClass().getResourceAsStream("/images/LessThanEqual.png");
		img = new Image(stream);
		lessThanEqual = new ImageView(img);
		contents.getChildren().add(lessThanEqual);
		
		rightBlank = new POPBlank(this);
		contents.getChildren().add(rightBlank);
		
		setInitWidth();
	}
}
