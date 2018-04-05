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
		
		this.component.getChildren().add(lbName);
		
		Bounds lbBound  = lbName.getBoundsInParent();
		Bounds compBound = component.getBoundsInParent();
		Bounds imgBound = imgView.getBoundsInParent();
		lbName.setTextAlignment(TextAlignment.CENTER);
		lbName.setPrefSize(compBound.getWidth(), compBound.getHeight());
		lbName.setAlignment(Pos.CENTER);
		
		setOnVariableNodeDrag();
		
//		ColorAdjust colorAdjust = new ColorAdjust();
//		colorAdjust.setSaturation(-1);
//		Blend blush = new Blend(BlendMode.MULTIPLY, colorAdjust, new ColorInput(
//				//imgBound.getMinX(), imgBound.getMinY(), 
//				0, 0, imgBound.getWidth(), imgBound.getHeight(), Color.BLACK));
//		imgView.setEffect(blush);
//		imgView.setCache(true);
//		imgView.setCacheHint(CacheHint.SPEED);
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
			content.putImage(getImageView().getImage());
			System.out.println(name + " " + value);
			content.put(POPNodeDataFormat.variableNameFormat, this.name);
//			content.put(POPNodeDataFormat.variableValueFormat, this.value);
//			Class<? extends POPNode> nodeClass = null;
//			Class[] paramTypes = { POPNode.class };
//			try {
//				nodeClass = (Class<? extends POPNode>) Class.forName("kr.co.idiots.model.POP" + getType().toString() + "Node");
//			} catch (ClassNotFoundException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			POPNode node = null;
//			try {
//				node = nodeClass.getConstructor(paramTypes).newInstance(this);
//			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
//					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			if(node instanceof POPSymbolNode)
//				DragManager.draggedSymbolNode = (POPSymbolNode) node;
//			else if(node instanceof POPVariableNode)
//				DragManager.draggedVariableNode = (POPVariableNode) node;
			
			
//			content.put(POPNodeDataFormat.nodeFormat, this);
//			if(getType() == POPNodeType.Variable) {
//				POPVariableNode varNode = (POPVariableNode) node;
//				Map<String, Object> map = new HashMap<String, Object>();
//				map.put(varNode.getName(), varNode.getValue());
//				content.putAll(map);
//			}
				
			db.setContent(content);
			event.consume();
		});
	}

	public String getName() { return this.name; }
	public Object getValue() { return this.value; }
}
