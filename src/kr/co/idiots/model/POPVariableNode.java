package kr.co.idiots.model;

import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;

public class POPVariableNode extends POPNode {
	
	private String name;
	private Object value;
	private Label lbName;
	
	public POPVariableNode(POPScriptArea scriptArea, String name, Object Value) {
		super(scriptArea, POPNodeType.Variable);
		// TODO Auto-generated constructor stub
		this.name = name;
		this.value = value;
		
		lbName = new Label(name);
		
		this.component.getChildren().add(lbName);
		
		Bounds lbBound  = lbName.getBoundsInParent();
		Bounds compBound = component.getBoundsInParent();
		Bounds imgBound = imgView.getBoundsInParent();
		lbName.setTextAlignment(TextAlignment.CENTER);
		lbName.setPrefSize(compBound.getWidth(), compBound.getHeight());
		lbName.setAlignment(Pos.CENTER);
		
//		ColorAdjust colorAdjust = new ColorAdjust();
//		colorAdjust.setSaturation(-1);
//		Blend blush = new Blend(BlendMode.MULTIPLY, colorAdjust, new ColorInput(
//				//imgBound.getMinX(), imgBound.getMinY(), 
//				0, 0, imgBound.getWidth(), imgBound.getHeight(), Color.BLACK));
//		imgView.setEffect(blush);
//		imgView.setCache(true);
//		imgView.setCacheHint(CacheHint.SPEED);
	}

	public String getName() { return this.name; }
	public Object getValue() { return this.value; }
}
