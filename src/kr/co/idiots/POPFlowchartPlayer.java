package kr.co.idiots;

import java.util.HashMap;

import javax.script.ScriptException;

import kr.co.idiots.model.operation.POPOperationSymbol;
import kr.co.idiots.model.operation.POPOutputSymbol;
import kr.co.idiots.model.symbol.POPDecisionEndNode;
import kr.co.idiots.model.symbol.POPDecisionNode;
import kr.co.idiots.model.symbol.POPDocumentNode;
import kr.co.idiots.model.symbol.POPLoopEndNode;
import kr.co.idiots.model.symbol.POPLoopNode;
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
	
	public void playFlowChart(POPSymbolNode node) {
		POPVariableManager.declaredVars = new HashMap<>();
		
		playNode(node);
	}
	
	public void playNode(POPSymbolNode node) {
		
		if(node instanceof POPStartNode) {
			output = new StringBuilder();
		} else if(node instanceof POPProcessNode) {
			playProcessNode((POPProcessNode) node);
		} else if(node instanceof POPDocumentNode) {
			playDocumentNode((POPDocumentNode) node);
		} 
		
		if(node instanceof POPDecisionNode) {
			try {
				if(playDecisionNode(node)) {
					playNode(((POPDecisionNode) node).getLeftStartNode().getOutFlowLine().getNextNode());
				} else {
					playNode(((POPDecisionNode) node).getRightStartNode().getOutFlowLine().getNextNode());
				}
			} catch (ScriptException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(node instanceof POPDecisionEndNode) {
			playNode(((POPDecisionEndNode) node).getDecisionNode().getOutFlowLine().getNextNode());
		} else if(node instanceof POPLoopNode) {
			try {
				if(playDecisionNode(node)) {
					playNode(((POPLoopNode) node).getLoopStartNode().getOutFlowLine().getNextNode());
				} else {
					playNode(node.getOutFlowLine().getNextNode());
				}
			} catch (ScriptException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(node instanceof POPLoopEndNode) {
			playNode(((POPLoopEndNode) node).getLoopNode());
		} else if(!(node instanceof POPStopNode)) {
			playNode(node.getOutFlowLine().getNextNode());
		}
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
		
		rootSymbol.generateString();
		String strResult = rootSymbol.getValueString();
		
		output.append(strResult).append(System.lineSeparator());
	}
	
	private boolean playDecisionNode(POPSymbolNode node) throws ScriptException {
		POPOperationSymbol rootSymbol = (POPOperationSymbol) node.getRootSymbol().getContents().getChildren().get(0);
		rootSymbol.getValueString();
		boolean result = false;
		result = Calculator.compare(rootSymbol.getValueString());
		
		return result;
	}
}
