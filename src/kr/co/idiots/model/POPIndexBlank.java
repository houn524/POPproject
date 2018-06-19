package kr.co.idiots.model;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import kr.co.idiots.POPNodeFactory;
import kr.co.idiots.model.operation.POPOperationSymbol;
import kr.co.idiots.util.DragManager;
import kr.co.idiots.util.POPNodeDataFormat;
import kr.co.idiots.util.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPIndexBlank extends ComboBox {

	private POPArrayNode parentNode;
	private ObservableList<String> options;
	
	public POPIndexBlank(POPArrayNode parentNode) {
		this.parentNode = parentNode;
		this.setPrefSize(50, 20);
		this.setScaleX(0.8);
		this.setScaleY(0.8);
		this.setEditable(false);
		
		options = FXCollections.observableArrayList("마지막");
		
		this.setItems(options);
		
		setOnIndexBlankDrag();
		setOnIndexBlankChange();
	}

	private void setOnIndexBlankChange() {
		this.getEditor().textProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue arg0, Object oldValue, Object newValue) {
				if(getEditor().getText().isEmpty())
					setPrefWidth(0);
				else
					setPrefWidth(TextUtils.computeTextWidth(getEditor().getFont(), getEditor().getText(), 0.0D) + 50);
				
				if(parentNode.getParentSymbol() != null && parentNode.getParentSymbol().getParentNode() != null && parentNode.getParentSymbol().getParentNode().isException()) {
					parentNode.getParentSymbol().getParentNode().getImgView().setStyle("-fx-effect: dropshadow(three-pass-box, black, 2, 0, 0, 1);");;
					parentNode.getParentSymbol().getParentNode().setException(false);
				}
				parentNode.resizeContents();
			}
		});
	}

	private void setOnIndexBlankDrag() {
		
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
				array.getIndexBlank().getOptions().remove("추가");
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
		if(!parentNode.isInitialized())
			return;
		
		int index = parentNode.getContents().getChildren().indexOf(this);
		parentNode.getContents().getChildren().remove(this);
		parentNode.getContents().getChildren().add(index, node);
		parentNode.resizeContents();
		node.initialize(null, parentNode);
	}
	
	public void insertNode(POPOperationSymbol node) {
		if(!parentNode.isInitialized()) {
			return;
		}
		int index = parentNode.getContents().getChildren().indexOf(this);
		parentNode.getContents().getChildren().remove(this);
		parentNode.getContents().getChildren().add(index, node);
		parentNode.resizeContents();
		node.initialize(parentNode);
	}
}
