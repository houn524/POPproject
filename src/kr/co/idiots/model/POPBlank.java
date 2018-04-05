package kr.co.idiots.model;

import java.io.InputStream;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import kr.co.idiots.util.POPNodeDataFormat;
import kr.co.idiots.view.POPSolvingLayoutController;

public class POPBlank extends ImageView {
	private POPDataInput parentDataInput;
	
	public POPBlank(POPDataInput dataInput) {
		this.parentDataInput = dataInput;
		InputStream stream = getClass().getResourceAsStream("/images/Blank.png");
		Image img = new Image(stream);
		this.setImage(img);
		
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
			if(db.hasImage() && db.getString().equals("Variable")) {
				POPVariableNode variable = new POPVariableNode(POPSolvingLayoutController.scriptArea, (String)db.getContent(POPNodeDataFormat.variableNameFormat));
				insertNode(variable);
			}
		});
	}
	
	public void insertNode(POPNode node) {
//		parentDataInput.add(node.getComponent());
		int index = parentDataInput.getChildren().indexOf(this);
		parentDataInput.remove(this);
		parentDataInput.add(index, node.getComponent());
//		parentDataInput.updateBound();
	}

	public POPDataInput getParentDataInput() {
		return parentDataInput;
	}

	public void setParentNode(POPDataInput parentDataInput) {
		this.parentDataInput = parentDataInput;
	}
}
