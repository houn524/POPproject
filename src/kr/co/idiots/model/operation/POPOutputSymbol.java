package kr.co.idiots.model.operation;

import javafx.scene.Node;
import kr.co.idiots.POPVariableManager;
import kr.co.idiots.model.POPArrayNode;
import kr.co.idiots.model.POPBlank;
import kr.co.idiots.model.POPNodeType;
import kr.co.idiots.model.POPVariableNode;
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
	public void playSymbol() throws NullPointerException, IndexOutOfBoundsException {
		if(contents.getChildren().get(0) instanceof POPOperationSymbol) {
			POPOperationSymbol symbol = (POPOperationSymbol) contents.getChildren().get(0);
			symbol.playSymbol();
			strValue = symbol.executeSymbol().toString();//Calculator.eval(strValue);
			return;
		}
		strCode = "";
		strValue = "";
		
		if(contents.getChildren().get(0) instanceof POPArrayNode) {
			POPArrayNode array = (POPArrayNode) contents.getChildren().get(0);
			
			int index = 0;
			
//			if(array.getIndexBlank().getValue().equals("마지막")) {
//				index = POPVariableManager.declaredArrs.get(array.getName()).size() - 1;
//			} else {
//				index = Integer.parseInt(array.getIndexBlank().getEditor().getText());
//			}
			
			if(POPVariableManager.declaredArrs.containsKey(array.getName())) {
				strValue = array.getValue();
			} else {
				this.parentNode.getImgView().setStyle("-fx-effect: dropshadow(three-pass-box, red, 2, 0, 0, 1);");
				this.parentNode.setException(true);
				throw new NullPointerException();
			}
			
//				strValue = POPVariableManager.declaredArrs.get(array.getName()).get(index).toString();
//			}
		} else if(contents.getChildren().get(0) instanceof POPVariableNode) {
			POPVariableNode variable = (POPVariableNode) contents.getChildren().get(0);
			
			if(POPVariableManager.declaredVars.containsKey(variable.getName())) {
				strValue = POPVariableManager.declaredVars.get(variable.getName());
			} else {
				this.parentNode.getImgView().setStyle("-fx-effect: dropshadow(three-pass-box, red, 2, 0, 0, 1);");
				this.parentNode.setException(true);
				throw new NullPointerException();
			}
//			else {
//				POPSolvingLayoutController.showErrorPopup("변수 초기화 필요");
//				POPSolvingLayoutController.scriptArea.stop();
//			}
		} else if(contents.getChildren().get(0) instanceof POPBlank) {
			POPBlank blank = (POPBlank) contents.getChildren().get(0);
			strValue = blank.getText();
		}
		
		strCode += "System.out.println(";
		strCode += value;
		strCode += ");";
	}
}
