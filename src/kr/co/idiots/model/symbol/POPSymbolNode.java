package kr.co.idiots.model.symbol;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import kr.co.idiots.model.POPFlowLine;
import kr.co.idiots.model.POPNode;
import kr.co.idiots.model.POPNodeType;
import kr.co.idiots.model.POPScriptArea;
import kr.co.idiots.util.ClipboardUtil;
import kr.co.idiots.util.DragManager;
import kr.co.idiots.util.PlatformHelper;
import kr.co.idiots.view.POPSolvingLayoutController;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPSymbolNode extends POPNode {

	protected POPFlowLine inFlowLine;
	protected POPFlowLine outFlowLine;
	protected POPProcessNode dragNode;
	protected POPSymbolNode rootNode;
	protected POPSymbolNode parentNode;
	
	protected DoubleProperty topCenterX = new SimpleDoubleProperty(0);
	protected DoubleProperty topY = new SimpleDoubleProperty(0);
	protected DoubleProperty bottomCenterX = new SimpleDoubleProperty(0);
	protected DoubleProperty bottomY = new SimpleDoubleProperty(0);
	
	private boolean isException = false;
	
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
				
		if(type != POPNodeType.Start && type != POPNodeType.Stop && type != POPNodeType.DecisionSub)
			setOnNodeDrag();
		// TODO Auto-generated constructor stub
	}
	
	public void initialize() {
		MenuItem deleteItem = new MenuItem("순서도 기호 삭제");
		POPSymbolNode node = this;
		deleteItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				if(isAllocated) {
					outFlowLine.pullNodesThread();
					inFlowLine.setNextNode(outFlowLine.getNextNode());
					getScriptArea().remove(node);
					
					outFlowLine.removeNodeOfDecision();
					outFlowLine.removeNodeOfLoop();
					
					
					POPSymbolNode root = parentNode;
			    	while(true) {
			    		if(root.getParentNode() != null) {
			    			root = root.getParentNode();
			    		} else {
			    			break;
			    		}
			    	}
					root.getOutFlowLine().pullNodesThread();
					
					parentNode = null;
					
					outFlowLine = new POPFlowLine();
					outFlowLine.setPrevNode(node);
					if(node instanceof POPDecisionNode || node instanceof POPLoopNode) {
						outFlowLine.setRootNode(node);
					}
					
					isAllocated = false;
				} else if(isInitialized) {
					getScriptArea().remove(node);
				}
				event.consume();
			}
			
		});
		
		contextMenu = new ContextMenu();
		contextMenu.setAutoHide(true);
		contextMenu.getItems().add(deleteItem);
		
		component.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {

			@Override
			public void handle(ContextMenuEvent event) {
				// TODO Auto-generated method stub
				contextMenu.show(imgView, event.getScreenX(), event.getScreenY());
				event.consume();
			}
			
		});
		
		isInitialized = true;
		
		Thread thread = new Thread() {
			@Override
			public void run() {
				PlatformHelper.run(() -> {
					outFlowLine.setVisible(true);
				});
			}
		};
		
		thread.setDaemon(true);
		thread.start();
	}
	
	public void moveCenter() {
		if(inFlowLine != null && (isAllocated || this instanceof POPDecisionEndNode || this instanceof POPLoopEndNode)) {
			Bounds newBound = null;
			Bounds prevBound = null;
			
			newBound = component.getBoundsInParent();
			
			prevBound = inFlowLine.getPrevNode().getBoundsInParent();
			
			if(!component.layoutXProperty().isBound())
				component.setLayoutX((prevBound.getMinX() + (prevBound.getWidth() / 2)) - (newBound.getWidth() / 2));
		}
	}

	protected void setOnBoundChangeListener() {
		component.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {

			@Override
			public void changed(ObservableValue<? extends Bounds> arg0, Bounds oldBound, Bounds newBound) {
				// TODO Auto-generated method stub
				
				if(imgView != null && newBound.getHeight() > imgView.getBoundsInLocal().getHeight())
					return;
				
			}
		});
	}
	
	private void setOnNodeDrag() {
		
		getComponent().setOnMousePressed(event -> {
			if(event.getButton().equals(MouseButton.PRIMARY)) {
				if(contextMenu != null)
					contextMenu.hide();
			}
			
			if(isException) {
				this.imgView.setStyle("");
				isException = false;
			}
		});
		
		
		getComponent().setOnMouseDragged(event -> {
			event.setDragDetect(true);
			event.consume();
			
		});
		
		getComponent().setOnDragDone(event -> {
			
			if (DragManager.isAllocatedNode && event.getTransferMode() != TransferMode.MOVE) {
				POPSolvingLayoutController.scriptArea.addWithOutFlowLine(this);
				
				this.inFlowLine.insertNode(this);
			} else if(DragManager.dragMoving && event.getTransferMode() != TransferMode.MOVE) {
				POPSolvingLayoutController.scriptArea.add(this);
			}
			
			DragManager.dragMoving = false;
			DragManager.draggedNode = null;
			DragManager.isAllocatedNode = false;
			DragManager.isSynchronized = false;
			DragManager.isAdjustPosSync = true;
						
		});
		
		getComponent().setOnDragDetected(event -> {
			if(!event.getButton().equals(MouseButton.PRIMARY))
				return;
			
			Node on = (Node) event.getTarget();
			Dragboard db = on.startDragAndDrop(TransferMode.COPY_OR_MOVE);
			ClipboardContent content = new ClipboardContent();
			content.putString(getType().toString());
			
			db.setContent(ClipboardUtil.makeClipboardContent(event, component, getType().toString()));
			db.setContent(content);
			
			if(isAllocated) {
				outFlowLine.pullNodesThread();
				DragManager.draggedNode = this;
				DragManager.dragMoving = true;
				DragManager.isAllocatedNode = true;
				this.inFlowLine.setNextNode(outFlowLine.getNextNode());
				this.getScriptArea().remove(this);
				
				outFlowLine.removeNodeOfDecision();
				outFlowLine.removeNodeOfLoop();
				
				
				POPSymbolNode root = parentNode;
		    	while(true) {
		    		if(root.getParentNode() != null) {
		    			root = root.getParentNode();
		    		} else {
		    			break;
		    		}
		    	}
				root.getOutFlowLine().pullNodesThread();
				
				parentNode = null;
				
				this.outFlowLine = new POPFlowLine();
				this.outFlowLine.setPrevNode(this);
				if(this instanceof POPDecisionNode || this instanceof POPLoopNode) {
					outFlowLine.setRootNode(this);
				}
				
				isAllocated = false;
			} else if(isInitialized) {
				DragManager.draggedNode = this;
				DragManager.dragMoving = true;
				this.getScriptArea().remove(this);
			}
			DragManager.isAdjustPosSync = false;
			
			event.consume();
		});
	}
}
