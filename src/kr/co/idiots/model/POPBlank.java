package kr.co.idiots.model;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import kr.co.idiots.util.POPNodeDataFormat;
import kr.co.idiots.util.TextUtils;
import kr.co.idiots.view.POPSolvingLayoutController;

public class POPBlank extends TextField {
	private POPOperationSymbol parentSymbol;
	
	public POPBlank(POPOperationSymbol parentSymbol) {
		this.parentSymbol = parentSymbol;
		this.setPrefSize(10, 34);
//		InputStream stream = getClass().getResourceAsStream("/images/Blank.png");
//		Image img = new Image(stream);
//		this.getChildren().add(new ImageView(img));
//		this.setImage(img);
//		prefColumnCountProperty().bind(textProperty().length());
		
		setOnBlankDrag();
		setOnBlankChange();
	}
	
	private void setOnBlankChange() {
		textProperty().addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue arg0, Object oldValue, Object newValue) {
				// TODO Auto-generated method stub
//				int index = parentSymbol.getChildren().indexOf(this);
//				if(index == 0)
//					parentSymbol.setLeftValue(newValue.toString());
//				else
//					parentSymbol.setRightValue(newValue.toString());
				
				if(getText().isEmpty())
					setPrefWidth(0);
				else
					setPrefWidth(TextUtils.computeTextWidth(getFont(), getText(), 0.0D) + 20);
//				setPrefWidth(getText().length() * 10);
//				setPrefWidth(Control.USE_COMPUTED_SIZE);
				
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
//				this.setText((String)db.getContent(POPNodeDataFormat.variableNameFormat));
				insertNode(variable);
			} else if(db.hasImage() && db.getString().equals("Plus")) {
				POPPlusSymbol plusSymbol = new POPPlusSymbol();
				insertNode(plusSymbol);
			}
		});
	}
	
	public void insertNode(POPVariableNode node) {
//		parentDataInput.add(node.getComponent());
		int index = parentSymbol.getContents().getChildren().indexOf(this);
		parentSymbol.remove(this);
		parentSymbol.add(index, node);
//		parentDataInput.getParentNode().getImageView().setFitWidth(500);
//		parentSymbol.getParentNode().moveCenter();
//		parentDataInput.updateBound();
//		parentDataInput.updateBound();
	}
	
	public void insertNode(POPOperationSymbol node) {
		int index = parentSymbol.getContents().getChildren().indexOf(this);
		
		parentSymbol.remove(this);
		parentSymbol.add(index, node);
		node.initialize(parentSymbol.getParentDataInput());
	}

	public POPOperationSymbol getParentSymbol() {
		return parentSymbol;
	}

	public void setParentSymbol(POPOperationSymbol parentSymbol) {
		this.parentSymbol = parentSymbol;
	}
}
