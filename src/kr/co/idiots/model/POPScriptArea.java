package kr.co.idiots.model;

import java.io.IOException;

import javafx.scene.layout.AnchorPane;
import javafx.*;
import kr.co.idiots.CodeGenerator;

public class POPScriptArea {
	private AnchorPane component;
	private POPNode startNode;
	private POPNode nodePointer;
	private CodeGenerator generator;
	
	public POPScriptArea(AnchorPane component) {
		this.component = component;
	}
	
	public AnchorPane getComponent() { return component; }
	
	public void add(POPSymbolNode node) {
		component.getChildren().add(node.getComponent());
		if(node.getOutFlowLine() != null)
			component.getChildren().add(node.getOutFlowLine());
	}

	public POPNode getStartNode() {
		return startNode;
	}

	public void setStartNode(POPNode startNode) {
		this.startNode = startNode;
		nodePointer = startNode;
	}
	
	public void generate() throws IOException {
		generator = new CodeGenerator();
		generator.createJavaFile();
		
		generateNode(startNode);
		
		System.out.println(generator.getSource());
	}
	
	private void generateNode(POPNode node) {
		
		if(node instanceof POPStartNode) {
			generator.createStartSource("text");
		} else if(node instanceof POPProcessNode) {
			POPDataInput dataInput = node.getDataInput();
			int count = dataInput.getChildrenCount();
			for(int i = 0; i < count; i++) {
				POPEqualSymbol symbol = (POPEqualSymbol) dataInput.getChildren().get(i);
				
			}
		}
	}
}
