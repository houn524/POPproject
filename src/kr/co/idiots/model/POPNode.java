package kr.co.idiots.model;

import java.io.InputStream;
import java.io.Serializable;

import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public abstract class POPNode extends StackPane implements Serializable, Cloneable {
	
	protected POPNodeType type;
	protected ImageView imgView;
	protected POPDataInput dataInput;
	protected Label label;
	protected StackPane component;
	protected POPScriptArea scriptArea;

//	protected POPNode nextNode;
//	protected POPNode prevNode;
	
	public POPNode(POPScriptArea scriptArea, POPNodeType type) {
		this.scriptArea = scriptArea;
		this.type = type;
		
		InputStream stream = getClass().getResourceAsStream("/images/" + type.toString() + ".png");
		Image img = new Image(stream);
		imgView = new ImageView(img);

		
		component = this;
		component.setPrefWidth(Control.USE_COMPUTED_SIZE);
		component.setPrefHeight(Control.USE_COMPUTED_SIZE);
		component.setAlignment(Pos.CENTER);
		
		component.getChildren().add(imgView);
		StackPane.setAlignment(imgView, Pos.CENTER);
		
//		component.autosize();
//		System.out.println("BorderPane : " + component.getBoundsInParent());
//		component.setPrefSize(imgView.get, imgView.getHeight());
		
//		if(type != POPNodeType.Start && type != POPNodeType.Stop)
//			setOnNodeDrag();
	}
	
	public POPNode(POPNode another) {
		this.type = another.type;
		this.imgView = new ImageView(another.imgView.getImage());
		this.dataInput = another.dataInput;
		this.label = another.label;
		this.component = another.component;
		this.scriptArea = another.scriptArea;
	}
	
	public void moveCenter() {
		
	}

//	private void setOnNodeDrag() {
//		
//		getComponent().setOnMouseDragged(event -> {
//			event.setDragDetect(true);
//			event.consume();
//		});
//		
//		getComponent().setOnDragDetected(event -> {
//			Node on = (Node) event.getTarget();
//			Dragboard db = on.startDragAndDrop(TransferMode.COPY);
//			ClipboardContent content = new ClipboardContent();
//			content.putString(getType().toString());
//			content.putImage(getImageView().getImage());
////			Class<? extends POPNode> nodeClass = null;
////			Class[] paramTypes = { POPNode.class };
////			try {
////				nodeClass = (Class<? extends POPNode>) Class.forName("kr.co.idiots.model.POP" + getType().toString() + "Node");
////			} catch (ClassNotFoundException e1) {
////				// TODO Auto-generated catch block
////				e1.printStackTrace();
////			}
////			POPNode node = null;
////			try {
////				node = nodeClass.getConstructor(paramTypes).newInstance(this);
////			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
////					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
////			
////			if(node instanceof POPSymbolNode)
////				DragManager.draggedSymbolNode = (POPSymbolNode) node;
////			else if(node instanceof POPVariableNode)
////				DragManager.draggedVariableNode = (POPVariableNode) node;
//			
//			
////			content.put(POPNodeDataFormat.nodeFormat, this);
//			if(getType() == POPNodeType.Variable) {
////				POPVariableNode varNode = (POPVariableNode) node;
////				Map<String, Object> map = new HashMap<String, Object>();
////				map.put(varNode.getName(), varNode.getValue());
////				content.putAll(map);
//			}
//				
//			db.setContent(content);
//			event.consume();
//		});
//	}

	public StackPane getComponent() { return this.component; }
	public ImageView getImageView() { return this.imgView; }
	public POPScriptArea getScriptArea() { return this.scriptArea; }
	public void setDataInput(POPDataInput dataInput) { this.dataInput = dataInput; }
	public POPDataInput getDataInput() { return this.dataInput; }
	public POPNodeType getType() { return type; }
//	public abstract void setNextNode(POPNode nextNode);
//	public void setPrevNode(POPNode prevNode) { this.prevNode = prevNode; }
}
