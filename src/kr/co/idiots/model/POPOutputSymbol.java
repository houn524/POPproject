package kr.co.idiots.model;

import javafx.geometry.Pos;
import javafx.scene.layout.FlowPane;

public class POPOutputSymbol extends FlowPane {
	private FlowPane component;
	private POPDataInput parentDataInput;
	private POPBlank blank;
	
	public POPOutputSymbol(POPDataInput parentDataInput) {
		this.parentDataInput = parentDataInput;
		
		component = this;
		component.setAlignment(Pos.CENTER);
		component.setPrefWrapLength(0);
		component.setHgap(5);
		component.setPrefWrapLength(parentDataInput.getParentNode().getImageView().getBoundsInLocal().getWidth());
		
		blank = new POPBlank(this);
		component.getChildren().add(blank);
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

	public POPBlank getBlank() {
		return blank;
	}

	public void setBlank(POPBlank blank) {
		this.blank = blank;
	}
}
