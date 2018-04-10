package kr.co.idiots.model;

import javafx.scene.Node;

public class POPOutputSymbol extends POPOperationSymbol {
	private POPBlank outputBlank;
	private String value;
	
	public POPOutputSymbol(POPDataInput parentDataInput) {
		super(parentDataInput);
		
		outputBlank = new POPBlank(this);
		contents.getChildren().add(outputBlank);
		
		setInitWidth();
	}
	
	@Override
	public void add(int index, POPVariableNode node) {
		contents.getChildren().add(index, node);
		value = node.getName();
		
		setContentsAutoSize();
	}
	
	@Override
	public void add(int index, POPOperationSymbol node) {
		contents.getChildren().add(index, node);
		
		setContentsAutoSize();
	}
	
	@Override
	public void remove(Node node) {
		int index = contents.getChildren().indexOf(node);
		contents.getChildren().remove(node);
		value = "";
		
		setContentsAutoSize();
	}
	
	@Override
	public void generateString() {
		strCode = "";
		
		strCode += "System.out.println(";
		strCode += value;
		strCode += ");";
	}

	public POPBlank getBlank() {
		return outputBlank;
	}

	public void setBlank(POPBlank blank) {
		this.outputBlank = blank;
	}
}
