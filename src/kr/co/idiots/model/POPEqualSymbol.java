package kr.co.idiots.model;

import java.io.InputStream;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPEqualSymbol extends POPOperationSymbol {
	
	private ImageView equal;
	
	public POPEqualSymbol() {
		this.type = POPNodeType.Equal;
		
		symbol = " = ";
		
		leftBlank = new POPBlank(this);
		contents.getChildren().add(leftBlank);
		
		InputStream stream = getClass().getResourceAsStream("/images/Equal.png");
		Image img = new Image(stream);
		equal = new ImageView(img);
		contents.getChildren().add(equal);
		
		rightBlank = new POPBlank(this);
		contents.getChildren().add(rightBlank);
		
		setInitWidth();
	}
	
}
