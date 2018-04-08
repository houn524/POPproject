package kr.co.idiots.model;

import java.io.IOException;

import javafx.scene.layout.AnchorPane;
import kr.co.idiots.CodeGenerator;

public class POPScriptArea {
	private AnchorPane component;
	private POPSymbolNode startNode;
	private POPNode nodePointer;
	private CodeGenerator generator;
	
	public POPScriptArea(AnchorPane component) {
		this.component = component;
		
		generator = new CodeGenerator();
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

	public void setStartNode(POPSymbolNode startNode) {
		this.startNode = startNode;
		nodePointer = startNode;
	}
	
	public String generate() throws IOException, NoSuchFieldException {
		
		return generator.generate(startNode);
	}
}
