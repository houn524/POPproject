package kr.co.idiots.model.compare;

import java.io.InputStream;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import kr.co.idiots.model.POPBlank;
import kr.co.idiots.model.POPNodeType;
import kr.co.idiots.model.operation.POPOperationSymbol;

public class POPLessThanSymbol extends POPOperationSymbol {
	
	private ImageView lessThan;
	
	public POPLessThanSymbol() {
		super();
		InputStream stream = getClass().getResourceAsStream("/images/Compare.png");
		Image img = new Image(stream);
		imgShape.setImage(img);
		
		this.type = POPNodeType.LessThan;
		symbol = " < ";
		
		leftBlank = new POPBlank(this);
		contents.getChildren().add(leftBlank);
		
		stream = getClass().getResourceAsStream("/images/LessThan.png");
		img = new Image(stream);
		lessThan = new ImageView(img);
		contents.getChildren().add(lessThan);
		
		rightBlank = new POPBlank(this);
		contents.getChildren().add(rightBlank);
		
		setInitWidth();
	}
}
