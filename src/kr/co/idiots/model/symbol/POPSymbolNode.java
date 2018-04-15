package kr.co.idiots.model.symbol;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import kr.co.idiots.model.POPFlowLine;
import kr.co.idiots.model.POPNode;
import kr.co.idiots.model.POPNodeType;
import kr.co.idiots.model.POPScriptArea;
import kr.co.idiots.util.ClipboardUtil;
import kr.co.idiots.util.DragManager;
import kr.co.idiots.view.POPSolvingLayoutController;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPSymbolNode extends POPNode {

	protected POPFlowLine inFlowLine;
	protected POPFlowLine outFlowLine;
	protected POPProcessNode dragNode;
	
	protected DoubleProperty topCenterX = new SimpleDoubleProperty(0);
	protected DoubleProperty topY = new SimpleDoubleProperty(0);
	protected DoubleProperty bottomCenterX = new SimpleDoubleProperty(0);
	protected DoubleProperty bottomY = new SimpleDoubleProperty(0);
	
	public DoubleProperty topCenterXProperty() {
    	return topCenterX;
    }
    
    public DoubleProperty topYProperty() {
    	return topY;
    }
    
    public DoubleProperty bottomCenterXProperty() {
    	return bottomCenterX;
    }
    
    public DoubleProperty bottomYProperty() {
    	return bottomY;
    }
	
	public POPSymbolNode(POPScriptArea scriptArea, POPNodeType type) {
		super(scriptArea, type);
		
		setOnBoundChangeListener();
		
		if(type != POPNodeType.Start && type != POPNodeType.Stop)
			setOnNodeDrag();
		// TODO Auto-generated constructor stub
	}
	
	public void initialize() {
		isInitialized = true;
	}
	
	public void moveCenter() {
		if(inFlowLine != null && isAllocated) {
			
			Bounds newBound = component.getBoundsInParent();
			Bounds prevBound = inFlowLine.getPrevNode().getBoundsInParent();
		
			if(outFlowLine != null) {
				outFlowLine.setStartX(newBound.getMinX() + (newBound.getWidth() / 2));
				outFlowLine.setStartY(newBound.getMaxY());
			}
			
			if(inFlowLine != null) {
				inFlowLine.setEndX(newBound.getMinX() + (newBound.getWidth() / 2));
				inFlowLine.setEndY(newBound.getMinY());
			}
			
			component.setLayoutX((prevBound.getMinX() + (prevBound.getWidth() / 2)) - (newBound.getWidth() / 2));
		}
	}

	private void setOnBoundChangeListener() {
		component.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {

			@Override
			public void changed(ObservableValue<? extends Bounds> arg0, Bounds oldBound, Bounds newBound) {
				// TODO Auto-generated method stub
				
				if(newBound.getHeight() > imgView.getBoundsInLocal().getHeight())
					return;
				
//				topCenterXProperty().set(newBound.getMinX() + (newBound.getWidth() / 2));
//				topYProperty().set(newBound.getMinY());
//				
//				bottomCenterXProperty().set(newBound.getMinX() + (newBound.getWidth() / 2));
//				bottomYProperty().set(newBound.getMaxY());
				
				if(outFlowLine != null) {
					outFlowLine.setStartX(newBound.getMinX() + (newBound.getWidth() / 2));
					outFlowLine.setStartY(newBound.getMaxY());
				}
				
				if(inFlowLine != null) {
					inFlowLine.setEndX(newBound.getMinX() + (newBound.getWidth() / 2));
					inFlowLine.setEndY(newBound.getMinY());
				}
				moveCenter();
			}
		});
	}
	
	
	protected void setOnSubSymbolBoundChangeListener() {
		operationSymbol.boundsInLocalProperty().addListener(new ChangeListener<Bounds>() {
			@Override
			public void changed(ObservableValue<? extends Bounds> arg0, Bounds oldBound, Bounds newBound) {
				// TODO Auto-generated method stub
				moveCenter();
			}
		});
	}
	
	private void setOnNodeDrag() {
		
//		getComponent().setOnMousePressed(event -> {
//			if(DragManager.draggedNode == null && !isInitialized) {
//				Class<? extends POPSymbolNode> nodeClass = null;
//				POPSymbolNode node = null;
//				try {
//					nodeClass = (Class<? extends POPSymbolNode>) Class.forName("kr.co.idiots.model.symbol.POP" + getType().toString() + "Node");
//					node = nodeClass.getDeclaredConstructor(POPScriptArea.class).newInstance(scriptArea);
//				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
//						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//				DragManager.draggedNode = node;
//				double y = this.getBoundsInParent().getMinY();
//				System.out.println(y);
//				DragManager.draggedNode.setTranslateY(y + 45);
//				DragManager.dragRootPane.getChildren().add(DragManager.draggedNode);
//				DragManager.isInitializing = true;
//				DragManager.dragMoving = true;
//			} else {
//				DragManager.draggedNode = this;
//				DragManager.dragMoving = true;
//			}
////			event.consume();
//		});
		
		getComponent().setOnMouseDragged(event -> {
			event.setDragDetect(true);
			event.consume();
			
//			POPSymbolNode node = null;
//			if(DragManager.dragMoving) {
//				node = (POPSymbolNode) DragManager.draggedNode;
//			} 
//			
//			if(!DragManager.isInitializing) {
//				event.setDragDetect(true);
//			}
//			
////			node.setLayoutX(getComponent().getBoundsInParent().getMinX());
////			node.setLayoutY(getComponent().getBoundsInParent().getMinY());
//			Node on = (Node) event.getTarget();
//			if(node.lastXY == null) {
//				node.lastXY = new Point2D(event.getSceneX(), event.getSceneY());
//			}
//			double dx = event.getSceneX() - node.lastXY.getX();
//			double dy = event.getSceneY() - node.lastXY.getY();
////			node.setLayoutX(node.getLayoutX() + dx);
////			node.setLayoutY(node.getLayoutY() + dy);
//			node.setTranslateX(node.getTranslateX() + dx);
//			node.setTranslateY(node.getTranslateY() + dy);
//			node.lastXY = new Point2D(event.getSceneX(), event.getSceneY());
////			event.setDragDetect(true);
//			event.consume();
		});
		
//		getComponent().setOnMouseReleased(event -> {
//			((POPSymbolNode) DragManager.draggedNode).lastXY = null;
//			
////			if(DragManager.isInitializing) {
////				scriptArea.getComponent().getChildren().add(DragManager.draggedNode);
////				DragManager.draggedNode.setTranslateX(DragManager.draggedNode.getTranslateX() - DragManager.tabPane.getWidth());
////				System.out.println(DragManager.tabPane.getWidth());
////				((POPSymbolNode) DragManager.draggedNode).initialize();
////				DragManager.isInitializing = false;
////			}
////			
//			if(scriptArea.getScrollPane().intersects(event.getSceneX(), event.getSceneY(), 0, 0)) {
//				System.out.println("tt");
//				if(DragManager.dragMoving) {
//					if(DragManager.isInitializing) {
//						double x = DragManager.draggedNode.getTranslateX();
//						scriptArea.getComponent().getChildren().add(DragManager.draggedNode);
//						DragManager.draggedNode.setTranslateX(x - DragManager.tabPane.getWidth());
//						((POPSymbolNode) DragManager.draggedNode).initialize();
//						DragManager.isInitializing = false;
//					}
//				}
//			} else {
//				DragManager.dragRootPane.getChildren().remove(DragManager.draggedNode);
//			}
//			
//			if(DragManager.draggedNode != null) {
//				DragManager.draggedNode = null;
//			}
//			
//			DragManager.dragMoving = false;
//			
//		});
		
		getComponent().setOnDragDone(event -> {
			
			if (DragManager.isAllocatedNode && event.getTransferMode() != TransferMode.MOVE) {
				POPSolvingLayoutController.scriptArea.add(this);
				this.inFlowLine.insertNode(this);
			} else if(DragManager.dragMoving && event.getTransferMode() != TransferMode.MOVE) {
				POPSolvingLayoutController.scriptArea.getComponent().getChildren().add(this);
			}
			
			
			DragManager.dragMoving = false;
			DragManager.draggedNode = null;
			DragManager.isAllocatedNode = false;
		});
		
		getComponent().setOnDragDetected(event -> {
//			Node on = (Node) event.getTarget();
//			if(!scriptArea.getComponent().getChildren().contains(dragImgView)) {
//				dragImgView = new ImageView(ClipboardUtil.makeSnapShot(on));
//				scriptArea.getComponent().getChildren().add(dragImgView);
//			}
//			
//			dragImgView.toFront();
//			dragImgView.setMouseTransparent(true);
//			dragImgView.setVisible(true);
//			dragImgView.relocate(
//					(int) (event.getSceneX() - dragImgView.getBoundsInLocal().getWidth() / 2),
//					(int) (event.getSceneY() - dragImgView.getBoundsInLocal().getHeight() / 2));
//			
//			Dragboard db = on.startDragAndDrop(TransferMode.ANY);
//			ClipboardContent content = new ClipboardContent();
//			
//			content.putString("dd");
//			db.setContent(content);
//			
//			event.consume();
			
			Node on = (Node) event.getTarget();
			Dragboard db = on.startDragAndDrop(TransferMode.MOVE);
			ClipboardContent content = new ClipboardContent();
			content.putString(getType().toString());
			
			db.setContent(ClipboardUtil.makeClipboardContent(event, component, getType().toString()));
			db.setContent(content);
			
			if(isAllocated) {
				DragManager.draggedNode = this;
				DragManager.dragMoving = true;
				DragManager.isAllocatedNode = true;
				this.inFlowLine.setNextNode(outFlowLine.getNextNode());
				this.getScriptArea().remove(this);
				
				this.outFlowLine = new POPFlowLine();
				this.outFlowLine.setPrevNode(this);
				isAllocated = false;
			} else if(isInitialized) {
				DragManager.draggedNode = this;
				DragManager.dragMoving = true;
				this.getScriptArea().remove(this);
			}
			
			event.consume();
			
		});
	}
}
