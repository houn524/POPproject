package kr.co.idiots.model.operation;

import java.io.InputStream;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import kr.co.idiots.model.POPBlank;
import kr.co.idiots.model.POPNodeType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPRemainderSymbol extends POPOperationSymbol {
	
	private ImageView imgView;
	
	public POPRemainderSymbol() {
		super();
		symbol = " % ";
		
		this.type = POPNodeType.Remainder;
		
		leftBlank = new POPBlank(this);
		contents.getChildren().add(leftBlank);
		
		InputStream stream = getClass().getResourceAsStream("/images/Remainder.png");
		Image img = new Image(stream);
		imgView = new ImageView(img);
		contents.getChildren().add(imgView);
		
		rightBlank = new POPBlank(this);
		contents.getChildren().add(rightBlank);
		
		setInitWidth();
	}
}
