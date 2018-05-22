package kr.co.idiots.model;

import kr.co.idiots.POPVariableManager;
import kr.co.idiots.model.operation.POPOperationSymbol;
import kr.co.idiots.util.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPArrayNode extends POPVariableNode {

	private POPIndexBlank indexBlank;
	private final double hgap = 3;
	
	public POPArrayNode(POPScriptArea scriptArea, String name) {
		super(scriptArea, name, POPNodeType.Array);
		
		this.type = POPNodeType.Array;
		
		indexBlank = new POPIndexBlank(this);
		contents.getChildren().add(indexBlank);
		contents.setHgap(hgap);
		
		resizeContents();
	}
	
	public void resizeContents() {
		if(!(contents.getChildren().get(1) instanceof POPIndexBlank)) {
			indexBlank.getEditor().setText("");
			if(contents.getChildren().get(1) instanceof POPOperationSymbol) {
				imgView.setFitWidth(TextUtils.computeTextWidth(lbName.getFont(), lbName.getText(), 0.0D) + 
						 + 20 + ((POPOperationSymbol) contents.getChildren().get(1)).getContents().getPrefWrapLength());
				System.out.println("operation");
			} else if(contents.getChildren().get(1) instanceof POPVariableNode){
				imgView.setFitWidth(TextUtils.computeTextWidth(lbName.getFont(), lbName.getText(), 0.0D) + 
						20 + ((POPVariableNode) contents.getChildren().get(1)).getWidth());
			}
		} else {
			imgView.setFitWidth(TextUtils.computeTextWidth(lbName.getFont(), lbName.getText(), 0.0D) + 
					TextUtils.computeTextWidth(indexBlank.getEditor().getFont(), indexBlank.getEditor().getText(), 0.0D) + 20 +
					50);
		}
		
		contents.setPrefWrapLength(imgView.getBoundsInLocal().getWidth());
		contents.setMinWidth(imgView.getBoundsInLocal().getWidth());
		
		if(parentSymbol != null) {
			parentSymbol.setContentsAutoSize();
		}
	}
	
	public void initialize(POPOperationSymbol parentSymbol, POPArrayNode parentArrayNode) {
		super.initialize(parentSymbol, parentArrayNode);
		
		indexBlank.setEditable(true);
	}
	
	public String getValue() {
		if(indexBlank.getEditor().getText().equals("추가")) {
			return "";
		}
		
		if(indexBlank.getEditor().getText().equals("마지막")) {
			return POPVariableManager.declaredArrs.get(name).get(POPVariableManager.declaredArrs.get(name).size() - 1).toString();
		} else if(contents.getChildren().get(1) instanceof POPOperationSymbol) {
			((POPOperationSymbol) contents.getChildren().get(1)).playSymbol();
			return POPVariableManager.declaredArrs.get(name).get(Integer.parseInt(((POPOperationSymbol) contents.getChildren().get(1)).executeSymbol().toString())).toString();
		} else if(contents.getChildren().get(1) instanceof POPVariableNode) {
			POPVariableNode variable = (POPVariableNode) contents.getChildren().get(1);
			return POPVariableManager.declaredArrs.get(name).get(Integer.parseInt(POPVariableManager.declaredVars.get(variable.getName()))).toString();
		} else if(contents.getChildren().get(1) instanceof POPArrayNode) {
			POPArrayNode array = (POPArrayNode) contents.getChildren().get(1);
			return POPVariableManager.declaredArrs.get(name).get(Integer.parseInt(array.getValue())).toString();
		} else {
			return POPVariableManager.declaredArrs.get(name).get(Integer.parseInt(indexBlank.getEditor().getText())).toString();
		}
	}
}
