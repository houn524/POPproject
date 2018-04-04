package kr.co.idiots.model;

import javafx.scene.layout.AnchorPane;

public class POPScriptArea {
	private AnchorPane component;
	
	public POPScriptArea(AnchorPane component) {
		this.component = component;
	}
	
	public AnchorPane getComponent() { return component; }
	
	public void add(POPSymbolNode node) {
		component.getChildren().add(node.getComponent());
		if(node.getOutFlowLine() != null)
			component.getChildren().add(node.getOutFlowLine());
	}
}
