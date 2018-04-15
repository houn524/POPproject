package kr.co.idiots.model;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
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
				POPVariableNode variable = new POPVariableNode(POPSolvingLayoutController.scriptArea, 
						(String) db.getContent(POPNodeDataFormat.variableNameFormat),
						(Enum.valueOf(POPNodeType.class, (String) db.getContent(POPNodeDataFormat.variableTypeFormat))));
				insertNode(variable);
				success = true;
			} else if(db.hasImage() && POPNodeType.operationGroup.contains(Enum.valueOf(POPNodeType.class, db.getString()))) {
				if(DragManager.dragMoving) {
					insertNode((POPOperationSymbol) DragManager.draggedNode);
					DragManager.dragMoving = false;
					DragManager.draggedNode = null;
					success = true;
					event.setDropCompleted(success);
					event.consume();
					return;
				}
				Class<? extends POPOperationSymbol> symbolClass = null;
				POPOperationSymbol symbol = null;
				try {
					symbolClass = (Class<? extends POPOperationSymbol>) Class.forName("kr.co.idiots.model.operation.POP" + db.getString() + "Symbol");
					symbol = symbolClass.newInstance();
				} catch(ClassNotFoundException | IllegalAccessException | InstantiationException e) {
					e.printStackTrace();
				}
				insertNode(symbol);
				success = true;
//				POPPlusSymbol plusSymbol = new POPPlusSymbol();
//				insertNode(plusSymbol);
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
		if(!parentSymbol.isInitialized())
			return;
		
		int index = parentSymbol.getContents().getChildren().indexOf(this);
		
		parentSymbol.remove(this);
		parentSymbol.add(index, node);
		
		node.initialize(parentSymbol.getParentNode());
	}
}
