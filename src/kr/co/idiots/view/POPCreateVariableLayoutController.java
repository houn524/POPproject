package kr.co.idiots.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import kr.co.idiots.MainApp;
import kr.co.idiots.model.POPNodeType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPCreateVariableLayoutController {
	
	@FXML
	private TextField textField;
	
	private MainApp mainApp;
	private POPSolvingLayoutController controller;
	
	private RootLayoutController rootController;
	
	@FXML
	public void createVariable() {
		POPNodeType type = POPNodeType.IntegerVariable;
		
//		switch(comboBox.getValue().toString()) {
//		case "정수" :
//			type = POPNodeType.IntegerVariable;
//			break;
//		case "실수" :
//			type = POPNodeType.DoubleVariable;
//			break;
//		case "문자" :
//			type = POPNodeType.StringVariable;
//			break;
//		default :
//			type = null;
//			break;
//		}
		
		controller.addVariable(textField.getText(), type, "");
	}
}
