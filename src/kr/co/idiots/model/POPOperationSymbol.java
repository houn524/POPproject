package kr.co.idiots.model;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;

enum POPSymbolState {
	AllVariable, leftVariable, RightVariable
}

public class POPOperationSymbol extends FlowPane {
	protected POPDataInput parentDataInput;
	protected POPBlank leftBlank;
	protected POPBlank rightBlank;
	protected String leftValue;
	protected String rightValue;
	protected String symbol;
	protected POPSymbolState state;
	
	public POPOperationSymbol(POPDataInput parentDataInput) {
		this.parentDataInput = parentDataInput;
		
		setAlignment(Pos.CENTER);
		setPrefWrapLength(0);
		setHgap(5);
		setPrefWrapLength(parentDataInput.getParentNode().getImageView().getBoundsInLocal().getWidth());
	}
	
	public void add(int index, POPVariableNode node) {
		getChildren().add(index, node);
		if(index == 0)
			leftValue = node.getName();
		else
			rightValue = node.getName();
	}
	
	public void add(int index, POPOperationSymbol node) {
		getChildren().add(index, node);
	}
	
	public void remove(Node node) {
		int index = getChildren().indexOf(node);
		getChildren().remove(node);
		if(index == 0)
			leftValue = "";
		else
			rightValue = "";
	}
	
	public String toString() {
		String str = "";
		if(getChildren().get(0) instanceof POPOperationSymbol) {
			POPOperationSymbol symbol = (POPOperationSymbol) getChildren().get(0);
			str = str + symbol.toString();
		}
		else
			str = str + leftValue;
		
		str = str + symbol;
		
		if(getChildren().get(2) instanceof POPOperationSymbol) {
			POPOperationSymbol symbol = (POPOperationSymbol) getChildren().get(2);
			str = str + symbol.toString();
		} else
			str = str + rightValue;
		
		if(symbol.equals(" = ")) {
			str = "int " + str;
			str = str + ";";
		}
			
		return str;
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

	public String getLeftValue() {
		return leftValue;
	}

	public void setLeftValue(String leftValue) {
		this.leftValue = leftValue;
	}

	public String getRightValue() {
		return rightValue;
	}

	public void setRightValue(String rightValue) {
		this.rightValue = rightValue;
	}

	public POPSymbolState getState() {
		return state;
	}

	public void setState(POPSymbolState state) {
		this.state = state;
	}
}
