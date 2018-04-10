package kr.co.idiots.model;

import java.io.InputStream;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class POPPlusSymbol extends POPOperationSymbol {
	
	private ImageView plus;
	
	public POPPlusSymbol() {
		super();
		// TODO Auto-generated constructor stub
		symbol = " + ";
		
		this.type = POPSymbolType.Plus;
		
		leftBlank = new POPBlank(this);
		contents.getChildren().add(leftBlank);
		
		InputStream stream = getClass().getResourceAsStream("/images/Plus.png");
		Image img = new Image(stream);
		plus = new ImageView(img);
		contents.getChildren().add(plus);
		
		rightBlank = new POPBlank(this);
		contents.getChildren().add(rightBlank);
		
		setInitWidth();
	}
	
}
