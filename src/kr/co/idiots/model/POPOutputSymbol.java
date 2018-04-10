package kr.co.idiots.model;

import javafx.scene.Node;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPOutputSymbol extends POPOperationSymbol {
	private POPBlank outputBlank;
	private String value;
	
	public POPOutputSymbol() {
		this.type = POPNodeType.Output;
		
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
}
