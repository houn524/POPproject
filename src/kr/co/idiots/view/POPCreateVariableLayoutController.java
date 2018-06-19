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
		
		controller.addVariable(textField.getText(), type, "");
	}
}
