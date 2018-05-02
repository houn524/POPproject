package kr.co.idiots.model;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import kr.co.idiots.POPNodeFactory;
import kr.co.idiots.model.operation.POPOperationSymbol;
import kr.co.idiots.util.DragManager;
import kr.co.idiots.util.POPNodeDataFormat;
import kr.co.idiots.util.TextUtils;
import kr.co.idiots.view.POPSolvingLayoutController;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPBlank extends TextField {
	
	private POPOperationSymbol parentSymbol;
	
	public POPBlank(POPOperationSymbol parentSymbol) {
		this.parentSymbol = parentSymbol;
		this.setPrefSize(10, 34);
		this.setEditable(false);
		
		setOnBlankDrag();
		setOnBlankChange();
	}
	
	private void setOnBlankChange() {
		textProperty().addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue arg0, Object oldValue, Object newValue) {
				// TODO Auto-generated method stub
				
				if(getText().isEmpty())
					setPrefWidth(0);
				else
					setPrefWidth(TextUtils.computeTextWidth(getFont(), getText(), 0.0D) + 20);
				
				parentSymbol.setContentsAutoSize();
			}
		});
	}
	
	private void setOnBlankDrag() {
		setOnDragOver(event -> {
			Dragboard db = event.getDragboard();
			if(db.hasImage() && POPNodeType.variableGroup.contains(Enum.valueOf(POPNodeType.class, db.getString()))) {
				event.acceptTransferModes(TransferMode.MOVE);
			} else if(db.hasImage() && POPNodeType.operationGroup.contains(Enum.valueOf(POPNodeType.class, db.getString()))) {
				event.acceptTransferModes(TransferMode.MOVE);
			} else if(db.hasImage() && POPNodeType.compareGroup.contains(Enum.valueOf(POPNodeType.class, db.getString()))) {
				event.acceptTransferModes(TransferMode.MOVE);
			}
			
			if(this.isEditable())
				this.setFocused(true);
			
			event.consume();
		});
		
		setOnDragExited(event -> {
			if(this.isEditable())
				this.setFocused(false);
			
			event.consume();
		});
		
		setOnDragDropped(event -> {
			Dragboard db = event.getDragboard();
			boolean success = false;
			
			String clsName = db.getString();
			String varName = null;
			String varTypeName = null;
			if(db.hasContent(POPNodeDataFormat.variableNameFormat))
				varName = db.getContent(POPNodeDataFormat.variableNameFormat).toString();
			if(db.hasContent(POPNodeDataFormat.variableTypeFormat))
				varTypeName = db.getContent(POPNodeDataFormat.variableTypeFormat).toString();
			
			if(POPNodeType.variableGroup.contains(Enum.valueOf(POPNodeType.class, db.getString()))) {
				if(DragManager.dragMoving) {
					insertNode((POPVariableNode) DragManager.draggedNode);
					DragManager.dragMoving = false;
					DragManager.draggedNode = null;
					success = true;
					event.setDropCompleted(success);
					event.consume();
					return;
				}
				POPVariableNode variable = (POPVariableNode) POPNodeFactory.createNode(clsName, varName, varTypeName);
//				POPVariableNode variable = new POPVariableNode(POPSolvingLayoutController.scriptArea, 
//						(String) db.getContent(POPNodeDataFormat.variableNameFormat),
//						(Enum.valueOf(POPNodeType.class, (String) db.getContent(POPNodeDataFormat.variableTypeFormat))));
				insertNode(variable);
				success = true;
			} else {
				if(DragManager.dragMoving) {
					insertNode((POPOperationSymbol) DragManager.draggedNode);
					DragManager.dragMoving = false;
					DragManager.draggedNode = null;
					success = true;
					event.setDropCompleted(success);
					event.consume();
					return;
				}
				POPOperationSymbol symbol = null;
				
				symbol = (POPOperationSymbol) POPNodeFactory.createNode(clsName, varName, varTypeName);
				insertNode(symbol);
				success = true;
			}
			
			event.setDropCompleted(success);
			event.consume();
		});
	}
	
	public void insertNode(POPVariableNode node) {
		if(!parentSymbol.isInitialized())
			return;
		
		int index = parentSymbol.getContents().getChildren().indexOf(this);
		parentSymbol.remove(this);
		parentSymbol.add(index, node);
		node.initialize(parentSymbol);
	}
	
	public void insertNode(POPOperationSymbol node) {
		if(!parentSymbol.isInitialized()) {
			return;
		}
			
			
		int index = parentSymbol.getContents().getChildren().indexOf(this);
		
		parentSymbol.remove(this);
		
		parentSymbol.add(index, node);
		
		
		
		node.initialize(parentSymbol.getParentNode());
	}
}
