package kr.co.idiots.model;

import javafx.scene.Node;

public class POPOutputSymbol extends POPOperationSymbol {
	private POPBlank outputBlank;
	private String value;
	
	public POPOutputSymbol(POPDataInput parentDataInput) {
		super(parentDataInput);
		
		outputBlank = new POPBlank(this);
		contents.getChildren().add(outputBlank);
	}
	
	@Override
	public void add(int index, POPVariableNode node) {
		contents.getChildren().add(index, node);
		value = node.getName();
	}
	
	@Override
	public void add(int index, POPOperationSymbol node) {
		contents.getChildren().add(index, node);
	}
	
	@Override
	public void remove(Node node) {
		int index = contents.getChildren().indexOf(node);
		contents.getChildren().remove(node);
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
