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
public class POPPlusSymbol extends POPOperationSymbol {
	
	private ImageView imgView;
	
	public POPPlusSymbol() {
		super();
		// TODO Auto-generated constructor stub
		symbol = " + ";
		
		this.type = POPNodeType.Plus;
		
		leftBlank = new POPBlank(this);
		contents.getChildren().add(leftBlank);
		
		InputStream stream = getClass().getResourceAsStream("/images/Plus.png");
		Image img = new Image(stream);
		imgView = new ImageView(img);
		contents.getChildren().add(imgView);
		
		rightBlank = new POPBlank(this);
		contents.getChildren().add(rightBlank);
		
		setInitWidth();
	}
	
}
