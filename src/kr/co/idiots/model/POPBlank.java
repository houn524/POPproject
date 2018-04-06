package kr.co.idiots.model;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import kr.co.idiots.util.POPNodeDataFormat;
import kr.co.idiots.view.POPSolvingLayoutController;

public class POPBlank extends TextField {
	private POPOperationSymbol parentSymbol;
	
	public POPBlank(POPOperationSymbol parentSymbol) {
		this.parentSymbol = parentSymbol;
		this.setPrefSize(61, 34);
//		InputStream stream = getClass().getResourceAsStream("/images/Blank.png");
//		Image img = new Image(stream);
//		this.getChildren().add(new ImageView(img));
//		this.setImage(img);
		
		setOnBlankDrag();
		setOnBlankChange();
	}
	
	private void setOnBlankChange() {
		textProperty().addListener(new ChangeListener() {

			@Override
			public void changed(ObservableValue arg0, Object oldValue, Object newValue) {
				// TODO Auto-generated method stub
				int index = parentSymbol.getChildren().indexOf(this);
				if(index == 0)
					parentSymbol.setLeftValue(newValue.toString());
				else
					parentSymbol.setRightValue(newValue.toString());
			}
			
		});
	}
	
	private void setOnBlankDrag() {
		setOnDragOver(event -> {
			Dragboard db = event.getDragboard();
			if(db.hasImage() && db.getString().equals("Variable")) {
				event.acceptTransferModes(TransferMode.COPY);
			}
		});
		
		setOnDragDropped(event -> {
			Dragboard db = event.getDragboard();
			if(db.hasImage() && db.getString().equals("Variable")) {
				POPVariableNode variable = new POPVariableNode(POPSolvingLayoutController.scriptArea, (String)db.getContent(POPNodeDataFormat.variableNameFormat));
//				this.setText((String)db.getContent(POPNodeDataFormat.variableNameFormat));
				insertNode(variable);
			}
		});
	}
	
	public void insertNode(POPVariableNode node) {
//		parentDataInput.add(node.getComponent());
		int index = parentSymbol.getChildren().indexOf(this);
		parentSymbol.remove(this);
		parentSymbol.add(index, node);
//		parentDataInput.getParentNode().getImageView().setFitWidth(500);
//		parentSymbol.getParentNode().moveCenter();
//		parentDataInput.updateBound();
//		parentDataInput.updateBound();
	}
	
	public void insertNode(POPOperationSymbol node) {
		int index = parentSymbol.getChildren().indexOf(this);
		parentSymbol.getChildren().remove(this);
	}

	public POPOperationSymbol getParentSymbol() {
		return parentSymbol;
	}

	public void setParentSymbol(POPOperationSymbol parentSymbol) {
		this.parentSymbol = parentSymbol;
	}
}
