package kr.co.idiots.model;

import javafx.scene.layout.Pane;

public class POPScriptArea {
	private Pane component;
	
	public POPScriptArea(Pane component) {
		this.component = component;
	}
	
	public Pane getComponent() { return component; }
	
	public void add(POPNode node) {
		component.getChildren().add(node.getComponent());
		if(node.getFlowLine() != null)
			component.getChildren().add(node.getFlowLine().getComponent());
	}
}
