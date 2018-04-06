package kr.co.idiots.model;

public class POPProcessDataInput extends POPDataInput {
	
	public POPProcessDataInput(POPNode parentNode) {
		super(parentNode);
		
		this.add(new POPEqualSymbol(this));
//		updateBound();
//		Label equal = new Label("=");
//		this.getChildren().add(equal);
	}
	
//	public void insertInputVariable(POPVariableNode variable) {
//		String text = variable.getName() + " = ";
//		Label label = new Label(text);
//		node.getComponent().getChildren().add(label);
//		this.getChildren().remove(this.blank);
//	}
}
