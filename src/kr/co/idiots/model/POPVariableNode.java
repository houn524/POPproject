package kr.co.idiots.model;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import javafx.scene.text.TextAlignment;
import kr.co.idiots.model.operation.POPOperationSymbol;
import kr.co.idiots.util.ClipboardUtil;
import kr.co.idiots.util.DragManager;
import kr.co.idiots.util.POPNodeDataFormat;
import kr.co.idiots.view.POPSolvingLayoutController;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPVariableNode extends POPNode {
	
	private String name;
	private Object value;
	private Label lbName;
	private POPOperationSymbol parentSymbol;
	private int lastIndex = -1;
	
	public POPVariableNode(POPScriptArea scriptArea, String name, POPNodeType type) {
		super(scriptArea, type);
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
	
	public void initialize(POPOperationSymbol parentSymbol) {
		isInitialized = true;
		this.parentSymbol = parentSymbol;
		
		MenuItem deleteItem = new MenuItem("변수 삭제");
		POPVariableNode thisNode = this;
		deleteItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				if(isInitialized) {
					if(getParentSymbol() != null) {
						lastIndex = parentSymbol.getContents().getChildren().indexOf(thisNode);
						parentSymbol.getContents().getChildren().remove(thisNode);
						parentSymbol.getContents().getChildren().add(lastIndex, new POPBlank(parentSymbol));
						parentSymbol.initialize(parentSymbol.getParentNode());
						parentSymbol.setContentsAutoSize();
						
						event.consume();
						return;
					} else {
						lastIndex = -1;
					}
					
					POPSolvingLayoutController.scriptArea.getComponent().getChildren().remove(thisNode);
				}
			}
			
		});
		
		contextMenu = new ContextMenu();
		contextMenu.setAutoHide(true);
		contextMenu.getItems().add(deleteItem);
		
		component.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {

			@Override
			public void handle(ContextMenuEvent event) {
				// TODO Auto-generated method stub
				contextMenu.show(component, event.getScreenX(), event.getScreenY());
				event.consume();
			}
			
		});
	}
	
	private void setOnVariableNodeDrag() {
		
		getComponent().setOnMousePressed(event -> {
			if(event.getButton().equals(MouseButton.PRIMARY)) {
				if(contextMenu != null)
					contextMenu.hide();
			}
		});
		
		getComponent().setOnMouseDragged(event -> {
			event.setDragDetect(true);
			event.consume();
		});
		
		getComponent().setOnDragDetected(event -> {
			if(!event.getButton().equals(MouseButton.PRIMARY))
				return;
			
			Node on = (Node) event.getTarget();
			Dragboard db = on.startDragAndDrop(TransferMode.MOVE);
			ClipboardContent content = new ClipboardContent();
			content.putString(getType().toString());
			content.put(POPNodeDataFormat.variableNameFormat, this.name);
			content.put(POPNodeDataFormat.variableTypeFormat, this.type.toString());
				
			db.setContent(ClipboardUtil.makeClipboardContent(event, this, getType().toString()));
			db.setContent(content);
			
			if(isInitialized) {
				DragManager.draggedNode = this;
				DragManager.dragMoving = true;
				
				if(parentSymbol != null) {
					lastIndex = parentSymbol.getContents().getChildren().indexOf(this);
					parentSymbol.getContents().getChildren().remove(this);
					parentSymbol.getContents().getChildren().add(lastIndex, new POPBlank(parentSymbol));
					parentSymbol.initialize(parentSymbol.getParentNode());
					parentSymbol.setContentsAutoSize();
					
					event.consume();
					return;
				} else {
					lastIndex = -1;
				}
				
				POPSolvingLayoutController.scriptArea.getComponent().getChildren().remove(this);
			}
			
			event.consume();
		});

		getComponent().setOnDragDone(event -> {
			if(!isInitialized)
				return;
			
			if (parentSymbol != null && event.getTransferMode() != TransferMode.MOVE) {
				POPBlank lastBlank = (POPBlank) parentSymbol.getContents().getChildren().get(lastIndex);
				lastBlank.insertNode(this);
			} 
			else if(parentSymbol == null && event.getTransferMode() != TransferMode.MOVE) {
				POPSolvingLayoutController.scriptArea.getComponent().getChildren().add(this);
			}
			
			DragManager.dragMoving = false;
			DragManager.draggedNode = null;
			DragManager.isAllocatedNode = false;
			DragManager.isSynchronized = false;
		});
	}

	public String getName() { return this.name; }
	public Object getValue() { return this.value; }

}
