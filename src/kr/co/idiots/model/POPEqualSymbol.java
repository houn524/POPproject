package kr.co.idiots.model;

import java.io.InputStream;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;

public class POPEqualSymbol extends FlowPane {
	private FlowPane component;
	private POPDataInput parentDataInput;
	private POPBlank leftBlank;
	private POPBlank rightBlank;
	
	public POPEqualSymbol(POPDataInput parentDataInput) {
		this.parentDataInput = parentDataInput;
		component = this;
		component.setAlignment(Pos.CENTER);
		component.setPrefWrapLength(0);
		component.setHgap(5);
		component.setPrefWrapLength(parentDataInput.getParentNode().getImageView().getBoundsInLocal().getWidth());
		
		leftBlank = new POPBlank(this);
		component.getChildren().add(leftBlank);
		
		InputStream stream = getClass().getResourceAsStream("/images/Equal.png");
		Image img = new Image(stream);
		ImageView equal = new ImageView(img);
		component.getChildren().add(equal);
		
		rightBlank = new POPBlank(this);
		component.getChildren().add(rightBlank);
	}

	public FlowPane getComponent() {
		return component;
	}

	public void setComponent(FlowPane component) {
		this.component = component;
	}

	public POPDataInput getParentDataInput() {
		return parentDataInput;
	}

	public void setParentDataInput(POPDataInput parentDataInput) {
		this.parentDataInput = parentDataInput;
	}

	public POPBlank getLeftBlank() {
		return leftBlank;
	}

	public void setLeftBlank(POPBlank leftBlank) {
		this.leftBlank = leftBlank;
	}

	public POPBlank getRightBlank() {
		return rightBlank;
	}

	public void setRightBlank(POPBlank rightBlank) {
		this.rightBlank = rightBlank;
	}
}
