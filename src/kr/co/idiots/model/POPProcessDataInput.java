package kr.co.idiots.model;

import javafx.scene.control.Label;

public class POPProcessDataInput extends POPDataInput {
	
	public POPProcessDataInput() {
		super();
	}
	
	public void insertInputVariable(POPVariableNode variable) {
		String text = variable.getName() + " = ";
		Label label = new Label(text);
		node.getComponent().getChildren().add(label);
		this.getChildren().remove(this.blank);
	}
}
