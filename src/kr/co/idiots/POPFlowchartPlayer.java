package kr.co.idiots;

import java.util.HashMap;

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
		while(true) { 
			if(node instanceof POPStartNode) {
				output = new StringBuilder();
			} else if(node instanceof POPProcessNode) {
				playProcessNode((POPProcessNode) node);
			} else if(node instanceof POPDocumentNode) {
				playDocumentNode((POPDocumentNode) node);
			} 
			
			if(node instanceof POPDecisionNode) {
				if(playDecisionNode(node)) {
					node = ((POPDecisionNode) node).getLeftStartNode().getOutFlowLine().getNextNode();
				} else {
					node = ((POPDecisionNode) node).getRightStartNode().getOutFlowLine().getNextNode();
				}
			} else if(node instanceof POPDecisionEndNode) {
				node = ((POPDecisionEndNode) node).getDecisionNode().getOutFlowLine().getNextNode();
			} else if(node instanceof POPLoopNode) {
				if(playDecisionNode(node)) {
					node = ((POPLoopNode) node).getLoopStartNode().getOutFlowLine().getNextNode();
				} else {
					node = node.getOutFlowLine().getNextNode();
				}
			} else if(node instanceof POPLoopEndNode) {
				node = ((POPLoopEndNode) node).getLoopNode();
			} else if(!(node instanceof POPStopNode)) {
				node = node.getOutFlowLine().getNextNode();
			} else if(node instanceof POPStopNode) {
				break;
			}
		}
	}
	
	private void playProcessNode(POPProcessNode node) {
		POPOperationSymbol rootSymbol = node.getRootSymbol();
		
		rootSymbol.getValueString();

		POPVariableManager.declaredVars.put(rootSymbol.getLeftCode(), rootSymbol.getRightValue());
	}
	
	private void playDocumentNode(POPDocumentNode node) {
		POPOutputSymbol rootSymbol = (POPOutputSymbol) node.getRootSymbol();
		
		rootSymbol.playSymbol();
		String strResult = rootSymbol.getValueString();
		
		output.append(strResult).append(System.lineSeparator());
	}
	
	private boolean playDecisionNode(POPSymbolNode node) {
		POPOperationSymbol rootSymbol = (POPOperationSymbol) node.getRootSymbol().getContents().getChildren().get(0);
		rootSymbol.playSymbol();
		boolean result = false;
		result = Boolean.parseBoolean(rootSymbol.executeSymbol().toString());
		
		return result;
	}
}
