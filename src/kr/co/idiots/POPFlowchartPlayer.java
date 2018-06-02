package kr.co.idiots;

import java.util.ArrayList;
import java.util.HashMap;

import kr.co.idiots.model.POPArrayNode;
import kr.co.idiots.model.POPBlank;
import kr.co.idiots.model.POPVariableNode;
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
import kr.co.idiots.view.POPSolvingLayoutController;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPFlowchartPlayer {
	
	private StringBuilder output;
	private boolean isStop = false;
	
	private POPSolvingLayoutController solvingController;
	
	private boolean isLoop = false;
	private int loopCount = 0;
	private final long MAX_LOOP_COUNT = 100000;
	
	private POPSymbolNode loopNode;
	
	public int sigCount = 10;
	
	public POPFlowchartPlayer(POPSolvingLayoutController solvingController) {
		this.solvingController = solvingController;
	}
	
	public void playFlowChart(POPSymbolNode node) {
		POPVariableManager.declaredVars = new HashMap<>();
		POPVariableManager.declaredArrs = new HashMap<>();
		playNode(node);
	}
	
	public void saveOperationSymbol(StringBuilder content, POPOperationSymbol symbol) {
		content.append(symbol.getType()).append("(");
		
		if(symbol.getContents().getChildren().get(0) instanceof POPOperationSymbol) {
			POPOperationSymbol leftSymbol = (POPOperationSymbol) symbol.getContents().getChildren().get(0);
			saveOperationSymbol(content, leftSymbol);
		} else if(symbol.getContents().getChildren().get(0) instanceof POPArrayNode) {
			POPArrayNode array = (POPArrayNode) symbol.getContents().getChildren().get(0);
			content.append("Arr('").append(array.getName()).append("', ");
			if(array.getContents().getChildren().get(1) instanceof POPOperationSymbol) {
				saveOperationSymbol(content, (POPOperationSymbol) array.getContents().getChildren().get(1));
			} else if(array.getContents().getChildren().get(1) instanceof POPVariableNode) {
				POPVariableNode variable = (POPVariableNode) array.getContents().getChildren().get(1);
				content.append("Var('").append(variable.getName()).append("')");
			} else {
				content.append("Blank('").append(array.getIndexBlank().getEditor().getText()).append("')");
			}
			
			content.append(")");
		} else if(symbol.getContents().getChildren().get(0) instanceof POPVariableNode) {
			POPVariableNode variable = (POPVariableNode) symbol.getContents().getChildren().get(0);
			content.append("Var('").append(variable.getName()).append("')");
		} else if(symbol.getContents().getChildren().get(0) instanceof POPBlank) {
			POPBlank blank = (POPBlank) symbol.getContents().getChildren().get(0);
			content.append("Blank('").append(blank.getText()).append("')");
		}
		
		if(symbol.getContents().getChildren().size() > 2) {
			content.append(", ");
		} else {
			content.append(")");
			return;
		}
		
		if(symbol.getContents().getChildren().get(2) instanceof POPOperationSymbol) {
			POPOperationSymbol leftSymbol = (POPOperationSymbol) symbol.getContents().getChildren().get(2);
			saveOperationSymbol(content, leftSymbol);
		} else if(symbol.getContents().getChildren().get(2) instanceof POPArrayNode) {
			POPArrayNode array = (POPArrayNode) symbol.getContents().getChildren().get(2);
			content.append("Arr('").append(array.getName()).append("', ");
			if(array.getContents().getChildren().get(1) instanceof POPOperationSymbol) {
				saveOperationSymbol(content, (POPOperationSymbol) array.getContents().getChildren().get(1));
			} else if(array.getContents().getChildren().get(1) instanceof POPVariableNode) {
				POPVariableNode variable = (POPVariableNode) array.getContents().getChildren().get(1);
				content.append("Var('").append(variable.getName()).append("')");
			} else {
				content.append("Blank('").append(array.getIndexBlank().getEditor().getText()).append("')");
			}
			
			content.append(")");
		}  else if(symbol.getContents().getChildren().get(2) instanceof POPVariableNode) {
			POPVariableNode variable = (POPVariableNode) symbol.getContents().getChildren().get(2);
			content.append("Var('").append(variable.getName()).append("')");
		} else if(symbol.getContents().getChildren().get(2) instanceof POPBlank) {
			POPBlank blank = (POPBlank) symbol.getContents().getChildren().get(2);
			content.append("Blank('").append(blank.getText()).append("')");
		}
		
		content.append(")");
	}
	
	public StringBuilder saveFlowchart(POPStartNode startNode) {
		sigCount = 10;
		
		return saveFlowchartSymbol(startNode, new String(new char[sigCount]).replace("\0", ":"));
	}
	
	public StringBuilder saveFlowchartSymbol(POPSymbolNode node, String sig) {
		StringBuilder content = new StringBuilder();
		while(true) {
			if(node instanceof POPStartNode) {
				content.append("Start");
			} else if(node instanceof POPProcessNode) {
				content.append(sig).append("Process(");
				
				saveOperationSymbol(content, node.getRootSymbol());
//				if(node.getRootSymbol().getContents().getChildren().get(i) instanceof POPBlank) {
//					content.append("Blank('").append(((POPBlank) node.getRootSymbol().getContents().getChildren().get(0)).getText()).append("')");
//				} else if(node.getRootSymbol().getContents().getChildren().get(i) instanceof POPOperationSymbol) {
//					POPOperationSymbol symbol = (POPOperationSymbol) node.getRootSymbol().getContents().getChildren().get(0);
//					saveOperationSymbol(content, symbol);
//				} else if(node.getRootSymbol().getContents().getChildren().get(i) instanceof POPVariableNode) {
//					POPVariableNode variable = (POPVariableNode) node.getRootSymbol().getContents().getChildren().get(0);
//					content.append(variable.getType().toString()).append("('").append(variable.getName()).append("')");
//				}
				content.append(")");
			} else if(node instanceof POPDocumentNode) {
				content.append(sig).append("Document(");
				
				saveOperationSymbol(content, node.getRootSymbol());
				
				content.append(")");
			} else if(node instanceof POPDecisionNode) {
				content.append(sig).append("Decision(");
				
				saveOperationSymbol(content, node.getRootSymbol());
				
				content.append(")");
				
				sigCount -= 1;
				int count = sigCount;
				
				content.append(saveFlowchartSymbol(((POPDecisionNode) node).getLeftStartNode().getOutFlowLine().getNextNode(), new String(new char[count]).replace("\0", ":")));
				content.append(saveFlowchartSymbol(((POPDecisionNode) node).getRightStartNode().getOutFlowLine().getNextNode(), new String(new char[count]).replace("\0", ":")));
				
			} else if(node instanceof POPLoopNode) {
				content.append(sig).append("Loop(");
				
				saveOperationSymbol(content, node.getRootSymbol());
				
				content.append(")");
				
				sigCount -= 1;
				int count = sigCount;
				
				content.append(saveFlowchartSymbol(((POPLoopNode) node).getLoopStartNode().getOutFlowLine().getNextNode(), new String(new char[count]).replace("\0", ":")));
				
			} else if(node instanceof POPStopNode) {
				content.append(sig).append("Stop");
				break;
			} else if(node instanceof POPDecisionEndNode) {
				content.append(sig).append("DecisionEnd");
				break;
			} else if(node instanceof POPLoopEndNode) {
				content.append(sig).append("LoopEnd");
				break;
			}
			
			node = node.getOutFlowLine().getNextNode();
		}
		
		POPSolvingLayoutController.mainApp.getConnector().saveFlowchart(content.toString());
		
		return content;
	}
	
	public void playNode(POPSymbolNode node) {
		isStop = false;
		
		try {
			while(!isStop) { 
				if(node instanceof POPStartNode) {
					saveFlowchart((POPStartNode) node);
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
						if(!isLoop) {
							isLoop = true;
							loopNode = node;
							loopCount = 0;
						}
						node = ((POPLoopNode) node).getLoopStartNode().getOutFlowLine().getNextNode();
					} else {
						node = node.getOutFlowLine().getNextNode();
						isLoop = false;
					}
				} else if(node instanceof POPLoopEndNode) {
					node = ((POPLoopEndNode) node).getLoopNode();
					if(isLoop) {
						loopCount++;
						if(loopCount >= MAX_LOOP_COUNT) {
							isLoop = false;
							loopNode.getImgView().setStyle("-fx-effect: dropshadow(three-pass-box, red, 10, 0.5, 1, 1);");
							loopNode.setException(true);
							solvingController.showErrorPopup("무한 루프");
							break;
						}
					}
				} else if(!(node instanceof POPStopNode)) {
					node = node.getOutFlowLine().getNextNode();
				} else if(node instanceof POPStopNode) {
					break;
				}
			}
		} catch(NullPointerException | NumberFormatException e) {
			if(e.getMessage() == null) {
				e.printStackTrace();
				POPSolvingLayoutController.showErrorPopup("변수 초기화 필요");
			} else {
				POPSolvingLayoutController.showErrorPopup(e.getMessage());
			}
			POPSolvingLayoutController.scriptArea.stop();
			System.out.println(e);
		}
		
		
	}
	
	private void playProcessNode(POPProcessNode node) throws NullPointerException, NumberFormatException {
		
		POPOperationSymbol rootSymbol = node.getRootSymbol();
		rootSymbol.getValueString();
		
		int index = 0;
		
		if(rootSymbol.getContents().getChildren().get(0) instanceof POPArrayNode) {
			
			POPArrayNode array = (POPArrayNode) rootSymbol.getContents().getChildren().get(0);
			if(POPVariableManager.declaredArrs.containsKey(array.getName())) {
				if(array.getIndexBlank().getEditor().getText().equals("마지막")) {
					POPVariableManager.declaredArrs.get(array.getName()).set(POPVariableManager.declaredArrs.get(array.getName()).size() - 1, rootSymbol.getRightValue());
				} else if(array.getIndexBlank().getEditor().getText().equals("추가")) {
					POPVariableManager.declaredArrs.get(array.getName()).add(rootSymbol.getRightValue());
					POPVariableManager.declaredVars.put(array.getName() + "의 크기", String.valueOf(Integer.parseInt(POPVariableManager.declaredVars.get(array.getName() + "의 크기")) + 1));
				} else if(array.getContents().getChildren().get(1) instanceof POPVariableNode) {
					POPVariableNode variable = (POPVariableNode) array.getContents().getChildren().get(1);
					POPVariableManager.declaredArrs.get(array.getName()).set(Integer.parseInt(POPVariableManager.declaredVars.get(variable.getName())), rootSymbol.getRightValue());
				} else {
					POPVariableManager.declaredArrs.get(array.getName()).add(Integer.parseInt(array.getIndexBlank().getEditor().getText().toString()), rootSymbol.getRightValue());
					POPVariableManager.declaredVars.put(array.getName() + "의 크기", String.valueOf(Integer.parseInt(POPVariableManager.declaredVars.get(array.getName() + "의 크기")) + 1));
				}
				
			} else {
				ArrayList<Object> list = new ArrayList<>();
				POPVariableManager.declaredArrs.put(array.getName(), list);
				POPVariableManager.declaredVars.put(array.getName() + "의 크기", "0");
				
				if(array.getIndexBlank().getEditor().getText().equals("마지막")) {
					POPVariableManager.declaredArrs.get(array.getName()).set(POPVariableManager.declaredArrs.get(array.getName()).size() - 1, rootSymbol.getRightValue());
				} else if(array.getIndexBlank().getEditor().getText().equals("추가")) {
					POPVariableManager.declaredArrs.get(array.getName()).add(rootSymbol.getRightValue());
					POPVariableManager.declaredVars.put(array.getName() + "의 크기", String.valueOf(Integer.parseInt(POPVariableManager.declaredVars.get(array.getName() + "의 크기")) + 1));
				} else if(array.getContents().getChildren().get(1) instanceof POPVariableNode) {
					POPVariableNode variable = (POPVariableNode) array.getContents().getChildren().get(1);
					POPVariableManager.declaredArrs.get(array.getName()).set(Integer.parseInt(POPVariableManager.declaredVars.get(variable.getName())), rootSymbol.getRightValue());
				} else {
					POPVariableManager.declaredArrs.get(array.getName()).add(Integer.parseInt(array.getIndexBlank().getEditor().getText()), rootSymbol.getRightValue());
					POPVariableManager.declaredVars.put(array.getName() + "의 크기", String.valueOf(Integer.parseInt(POPVariableManager.declaredVars.get(array.getName() + "의 크기")) + 1));
				}
			}
		} else if(rootSymbol.getContents().getChildren().get(0) instanceof POPVariableNode) {
			
			POPVariableManager.declaredVars.put(rootSymbol.getLeftCode(), rootSymbol.getRightValue());
		}
		
	}
	
	private void playDocumentNode(POPDocumentNode node) throws NullPointerException {
		POPOutputSymbol rootSymbol = (POPOutputSymbol) node.getRootSymbol();
		
		rootSymbol.playSymbol();
		String strResult = rootSymbol.getValueString();
		
		output.append(strResult).append(System.lineSeparator());
	}
	
	private boolean playDecisionNode(POPSymbolNode node) throws NullPointerException {
		POPOperationSymbol rootSymbol = (POPOperationSymbol) node.getRootSymbol();//.getContents().getChildren().get(0);
		rootSymbol.playSymbol();
		boolean result = false;
		
		result = Boolean.parseBoolean(rootSymbol.getLeftValue());
		
		return result;
	}
	
	public void stop() {
		isStop = true;
	}
}
