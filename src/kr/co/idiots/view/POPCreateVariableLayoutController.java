package kr.co.idiots.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import kr.co.idiots.MainApp;
import kr.co.idiots.model.POPNodeType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPCreateVariableLayoutController {
	@FXML
	private ComboBox comboBox;
	
	@FXML
	private TextField textField;
	
	private MainApp mainApp;
	private POPSolvingLayoutController controller;
	
	private RootLayoutController rootController;
	
	ObservableList<String> list = FXCollections.observableArrayList("정수", "실수", "문자");
	
	
	@FXML
	private void initialize() {
		comboBox.setItems(list);
		comboBox.getSelectionModel().select("정수");
	}
	
	@FXML
	public void createVariable() {
		POPNodeType type = null;
		
		switch(comboBox.getValue().toString()) {
		case "정수" :
			type = POPNodeType.IntegerVariable;
			break;
		case "실수" :
			type = POPNodeType.DoubleVariable;
			break;
		case "문자" :
			type = POPNodeType.StringVariable;
			break;
		default :
			type = null;
			break;
		}
		
		controller.addVariable(textField.getText(), type);
	}
}
