package kr.co.idiots.model;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class POPSymbolNode extends POPNode {

	protected POPFlowLine inFlowLine;
	protected POPFlowLine outFlowLine;
	
	public POPSymbolNode(POPScriptArea scriptArea, POPNodeType type) {
		super(scriptArea, type);
		
		setOnBoundChangeListener();
		
		if(type != POPNodeType.Start && type != POPNodeType.Stop)
			setOnNodeDrag();
		// TODO Auto-generated constructor stub
	}
	
	public void initialize() {
		
	}

	private void setOnBoundChangeListener() {
		component.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {

			@Override
			public void changed(ObservableValue<? extends Bounds> arg0, Bounds oldBound, Bounds newBound) {
				// TODO Auto-generated method stub
//				System.out.println("Bound Changed!!");
//				System.out.println(oldBound + " -> " + newBound);
				if(outFlowLine != null) {
					outFlowLine.setStartX(newBound.getMinX() + (newBound.getWidth() / 2));
					outFlowLine.setStartY(newBound.getMaxY());
				}
				
//				startY.set(line.getStartY());
				
				if(inFlowLine != null) {
					inFlowLine.setEndX(newBound.getMinX() + (newBound.getWidth() / 2));
					inFlowLine.setEndY(newBound.getMinY());
//					prevNode.getInFlowLine().endY.set(getEndY());
				}

			}
			
		});
	}
	
	private void setOnNodeDrag() {
		
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
			if(getType() == POPNodeType.Variable) {
//				POPVariableNode varNode = (POPVariableNode) node;
//				Map<String, Object> map = new HashMap<String, Object>();
//				map.put(varNode.getName(), varNode.getValue());
//				content.putAll(map);
			}
				
			db.setContent(content);
			event.consume();
		});
	}
	
	public POPFlowLine getOutFlowLine() { return outFlowLine; }
	public POPFlowLine getInFlowLine() { return inFlowLine; }
	public void setInFlowLine(POPFlowLine flowLine) {
		this.inFlowLine = flowLine;
	}
}
