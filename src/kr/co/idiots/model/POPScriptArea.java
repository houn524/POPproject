package kr.co.idiots.model;

import java.io.IOException;

import javafx.scene.layout.AnchorPane;
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
		
		if(nodePointer instanceof POPStartNode) {
			generator.createStartSource("test");
		}
		
		System.out.println(generator.getSource());
	}
}
