package kr.co.idiots;

import javax.script.ScriptException;

import kr.co.idiots.model.operation.POPOperationSymbol;
import kr.co.idiots.model.operation.POPOutputSymbol;
import kr.co.idiots.model.symbol.POPDocumentNode;
import kr.co.idiots.model.symbol.POPProcessNode;
import kr.co.idiots.model.symbol.POPStartNode;
import kr.co.idiots.model.symbol.POPStopNode;
import kr.co.idiots.model.symbol.POPSymbolNode;
import kr.co.idiots.util.Calculator;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPFlowchartPlayer {
	
	private StringBuilder output;
	
	public POPFlowchartPlayer() {
	}
	
	public void playNode(POPSymbolNode node) {
		
		if(node instanceof POPStartNode) {
			output = new StringBuilder();
		} else if(node instanceof POPProcessNode) {
			playProcessNode((POPProcessNode) node);
		} else if(node instanceof POPDocumentNode) {
			playDocumentNode((POPDocumentNode) node);
		}

		if(!(node instanceof POPStopNode))
			playNode(node.getOutFlowLine().getNextNode());
	}
	
	private void playProcessNode(POPProcessNode node) {
		POPOperationSymbol rootSymbol = node.getRootSymbol();
		
		rootSymbol.getValueString();
		try {

			POPVariableManager.declaredVars.put(rootSymbol.getLeftCode(), Calculator.eval(rootSymbol.getRightValue()));
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void playDocumentNode(POPDocumentNode node) {
		POPOutputSymbol rootSymbol = (POPOutputSymbol) node.getRootSymbol();
		
		String strResult = POPVariableManager.declaredVars.get(rootSymbol.getValue());
		
		output.append(strResult);
	}
}
