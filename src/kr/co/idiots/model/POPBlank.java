package kr.co.idiots.model;

import javafx.scene.control.TextField;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import kr.co.idiots.util.POPNodeDataFormat;
import kr.co.idiots.view.POPSolvingLayoutController;

public class POPBlank extends TextField {
	private FlowPane parentSymbol;
	
	public POPBlank(FlowPane parentSymbol) {
		this.parentSymbol = parentSymbol;
		this.setPrefSize(61, 34);
//		InputStream stream = getClass().getResourceAsStream("/images/Blank.png");
//		Image img = new Image(stream);
//		this.getChildren().add(new ImageView(img));
//		this.setImage(img);
		
		setOnBlankDrag();
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
			System.out.println("=================drop=============");
			if(db.hasImage() && db.getString().equals("Variable")) {
				POPVariableNode variable = new POPVariableNode(POPSolvingLayoutController.scriptArea, (String)db.getContent(POPNodeDataFormat.variableNameFormat));
//				this.setText((String)db.getContent(POPNodeDataFormat.variableNameFormat));
				insertNode(variable);
			}
		});
	}
	
	public void insertNode(POPNode node) {
		System.out.println("????");
//		parentDataInput.add(node.getComponent());
		int index = parentSymbol.getChildren().indexOf(this);
		parentSymbol.getChildren().remove(this);
		parentSymbol.getChildren().add(index, node.getComponent());
//		parentDataInput.getParentNode().getImageView().setFitWidth(500);
//		parentSymbol.getParentNode().moveCenter();
//		parentDataInput.updateBound();
//		parentDataInput.updateBound();
	}

	public FlowPane getParentSymbol() {
		return parentSymbol;
	}

	public void setParentNode(FlowPane parentSymbol) {
		this.parentSymbol = parentSymbol;
	}
}
