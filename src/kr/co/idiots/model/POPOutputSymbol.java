package kr.co.idiots.model;

import javafx.scene.Node;

public class POPOutputSymbol extends POPOperationSymbol {
	private POPBlank outputBlank;
	private String value;
	
	public POPOutputSymbol(POPDataInput parentDataInput) {
		super(parentDataInput);
		
		outputBlank = new POPBlank(this);
		getChildren().add(outputBlank);
	}
	
	@Override
	public void add(int index, POPVariableNode node) {
		getChildren().add(index, node);
		value = node.getName();
	}
	
	@Override
	public void add(int index, POPOperationSymbol node) {
		getChildren().add(index, node);
	}
	
	@Override
	public void remove(Node node) {
		int index = getChildren().indexOf(node);
		getChildren().remove(node);
		value = "";
	}
	
	@Override
	public String toString() {
		String str = "";
		
		str = str + "System.out.println(";
		str = str + value;
		str = str + ");";
		
		return str;
	}

	public POPBlank getBlank() {
		return outputBlank;
	}

	public void setBlank(POPBlank blank) {
		this.outputBlank = blank;
	}
}
