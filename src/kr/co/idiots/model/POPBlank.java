package kr.co.idiots.model;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import kr.co.idiots.POPNodeFactory;
import kr.co.idiots.model.operation.POPEqualSymbol;
import kr.co.idiots.model.operation.POPOperationSymbol;
import kr.co.idiots.util.DragManager;
import kr.co.idiots.util.POPNodeDataFormat;
import kr.co.idiots.util.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPBlank extends TextField {
	
	private POPOperationSymbol parentSymbol;
	
	public POPBlank(POPOperationSymbol parentSymbol) {
		this.parentSymbol = parentSymbol;
		this.setPrefSize(10, 20);
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
		setOnMousePressed(event -> {
			if(parentSymbol.getParentNode() != null) {
				if(parentSymbol.getParentNode().isException()) {
					parentSymbol.getParentNode().getImgView().setStyle("-fx-effect: dropshadow(three-pass-box, black, 2, 0, 0, 1);");;
					parentSymbol.getParentNode().setException(false);
				}
			}
		});
		
		setOnDragOver(event -> {
			Dragboard db = event.getDragboard();
			if(db.hasImage() && POPNodeType.variableGroup.contains(Enum.valueOf(POPNodeType.class, db.getString()))) {
				event.acceptTransferModes(TransferMode.COPY);
			} else if(db.hasImage() && POPNodeType.operationGroup.contains(Enum.valueOf(POPNodeType.class, db.getString()))) {
				event.acceptTransferModes(TransferMode.COPY);
			} else if(db.hasImage() && POPNodeType.compareGroup.contains(Enum.valueOf(POPNodeType.class, db.getString()))) {
				event.acceptTransferModes(TransferMode.COPY);
			} else if(db.hasImage() && POPNodeType.arrayGroup.contains(Enum.valueOf(POPNodeType.class, db.getString()))) {
				event.acceptTransferModes(TransferMode.COPY);
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
			if(parentSymbol.getParentNode() != null) {
				if(parentSymbol.getParentNode().isException()) {
					parentSymbol.getParentNode().getImgView().setStyle("-fx-effect: dropshadow(three-pass-box, black, 2, 0, 0, 1);");;
					parentSymbol.getParentNode().setException(false);
				}
			}
			
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
				insertNode(variable);
				success = true;
			} else if(POPNodeType.arrayGroup.contains(Enum.valueOf(POPNodeType.class, db.getString()))) {
				if(DragManager.dragMoving) {
					insertNode((POPArrayNode) DragManager.draggedNode);
					DragManager.dragMoving = false;
					DragManager.draggedNode = null;
					success = true;
					event.setDropCompleted(success);
					event.consume();
					return;
				}
				POPArrayNode array = (POPArrayNode) POPNodeFactory.createNode(clsName, varName, varTypeName);
				if(parentSymbol instanceof POPEqualSymbol) {
					if(!array.getIndexBlank().getOptions().contains("추가"))
						array.getIndexBlank().getOptions().add("추가");
				} else {
					array.getIndexBlank().getOptions().remove("추가");
				}
				insertNode(array);
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
		node.initialize(parentSymbol, null);
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
