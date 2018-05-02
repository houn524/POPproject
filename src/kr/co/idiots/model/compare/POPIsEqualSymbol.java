package kr.co.idiots.model.compare;

import java.io.InputStream;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import kr.co.idiots.model.POPBlank;
import kr.co.idiots.model.POPNodeType;
import kr.co.idiots.model.operation.POPOperationSymbol;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPIsEqualSymbol extends POPOperationSymbol {
	
	private ImageView isEqual;
	
	public POPIsEqualSymbol() {
		super();
		InputStream stream = getClass().getResourceAsStream("/images/Compare.png");
		Image img = new Image(stream);
		imgShape.setImage(img);
		
		this.type = POPNodeType.IsEqual;
		symbol = " == ";
		
		leftBlank = new POPBlank(this);
		contents.getChildren().add(leftBlank);
		
		stream = getClass().getResourceAsStream("/images/Equal.png");
		img = new Image(stream);
		isEqual = new ImageView(img);
		contents.getChildren().add(isEqual);
		
		rightBlank = new POPBlank(this);
		contents.getChildren().add(rightBlank);
		
		setInitWidth();
	}
	
	@Override
	public Object executeSymbol() {
		if(Integer.parseInt(leftValue) == Integer.parseInt(rightValue))
			return "true";
		else
			return "false";
	}
}
