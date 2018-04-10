package kr.co.idiots.model;

import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.text.TextAlignment;
import kr.co.idiots.util.POPNodeDataFormat;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPVariableNode extends POPNode {
	
	private String name;
	private Object value;
	private Label lbName;
	
	public POPVariableNode(POPScriptArea scriptArea, String name) {
		super(scriptArea, POPNodeType.Variable);
		// TODO Auto-generated constructor stub
		this.name = name;
		this.value = value;
		
		lbName = new Label(name);
		
		component.getChildren().add(lbName);
		
		Bounds lbBound  = lbName.getBoundsInParent();
		Bounds compBound = component.getBoundsInParent();
		Bounds imgBound = imgView.getBoundsInParent();
		lbName.setTextAlignment(TextAlignment.CENTER);
		lbName.setPrefSize(compBound.getWidth(), compBound.getHeight());
		lbName.setAlignment(Pos.CENTER);
		
		setOnVariableNodeDrag();
	}
	
	public POPVariableNode(POPNode another) {
		super(another);
	}
	
	private void setOnVariableNodeDrag() {
		
		getComponent().setOnMouseDragged(event -> {
			event.setDragDetect(true);
			event.consume();
		});
		
		getComponent().setOnDragDetected(event -> {
			Node on = (Node) event.getTarget();
			Dragboard db = on.startDragAndDrop(TransferMode.COPY);
			ClipboardContent content = new ClipboardContent();
			content.putString(getType().toString());
			content.putImage(getImgView().getImage());
			content.put(POPNodeDataFormat.variableNameFormat, this.name);
				
			db.setContent(content);
			event.consume();
		});
	}

	public String getName() { return this.name; }
	public Object getValue() { return this.value; }
}
