package kr.co.idiots.model;

import javafx.scene.image.ImageView;

public abstract class POPNode {
	
	
	protected POPNodeType type;
	protected ImageView component;
	protected POPScriptArea scriptArea;
	protected POPFlowLine flowLine;
	protected POPNode nextNode;
	
	public POPNode(POPScriptArea scriptArea) {
		this.scriptArea = scriptArea;
	}
	public ImageView getComponent() { return this.component; }
	public POPScriptArea getScriptArea() { return this.scriptArea; }
	public POPFlowLine getFlowLine() { return flowLine; }
	public abstract void setNextNode(POPNode nextNode);
}
