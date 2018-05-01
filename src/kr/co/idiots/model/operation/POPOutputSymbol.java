package kr.co.idiots.model.operation;

import javax.script.ScriptException;

import javafx.scene.Node;
import kr.co.idiots.POPVariableManager;
import kr.co.idiots.model.POPBlank;
import kr.co.idiots.model.POPNodeType;
import kr.co.idiots.model.POPVariableNode;
import kr.co.idiots.util.Calculator;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPOutputSymbol extends POPOperationSymbol {
	private POPBlank outputBlank;
	private String value;
	
	public POPOutputSymbol() {
		super();
		this.type = POPNodeType.Output;
		
		outputBlank = new POPBlank(this);
		outputBlank.setEditable(true);
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
		node.setParentSymbol(this);
		
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
		if(contents.getChildren().get(0) instanceof POPOperationSymbol) {
			strValue = ((POPOperationSymbol)contents.getChildren().get(0)).getValueString();
			try {
				strValue = Calculator.eval(strValue);
			} catch (ScriptException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		strCode = "";
		strValue = "";
		
		if(contents.getChildren().get(0) instanceof POPVariableNode) {
			POPVariableNode variable = (POPVariableNode) contents.getChildren().get(0);
			
			if(POPVariableManager.declaredVars.containsKey(variable.getName())) {
				strValue = POPVariableManager.declaredVars.get(variable.getName());
			} 
		} else if(contents.getChildren().get(0) instanceof POPBlank) {
			POPBlank blank = (POPBlank) contents.getChildren().get(0);
			strValue = blank.getText();
		}
		
		strCode += "System.out.println(";
		strCode += value;
		strCode += ");";
	}
}
