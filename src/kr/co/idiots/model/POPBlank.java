package kr.co.idiots.model;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
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
			if(db.hasImage() && db.getString().equals("Variable")) {
				event.acceptTransferModes(TransferMode.COPY);
			} else if(db.hasImage() && db.getString().equals("Plus")) {
				event.acceptTransferModes(TransferMode.COPY);
			}
		});
		
		setOnDragDropped(event -> {
			Dragboard db = event.getDragboard();
			if(db.hasImage() && db.getString().equals("Variable")) {
				POPVariableNode variable = new POPVariableNode(POPSolvingLayoutController.scriptArea, (String)db.getContent(POPNodeDataFormat.variableNameFormat));
				insertNode(variable);
			} else if(db.hasImage() && db.getString().equals("Plus")) {
				POPPlusSymbol plusSymbol = new POPPlusSymbol();
				insertNode(plusSymbol);
			}
		});
	}
	
	public void insertNode(POPVariableNode node) {
		int index = parentSymbol.getContents().getChildren().indexOf(this);
		parentSymbol.remove(this);
		parentSymbol.add(index, node);
	}
	
	public void insertNode(POPOperationSymbol node) {
		int index = parentSymbol.getContents().getChildren().indexOf(this);
		
		parentSymbol.remove(this);
		parentSymbol.add(index, node);
		node.initialize(parentSymbol.getParentDataInput());
	}
}
