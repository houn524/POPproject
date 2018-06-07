package kr.co.idiots.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import kr.co.idiots.MainApp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPConsoleLayoutController {
	
	@FXML private TextArea txtOutput;
	@FXML private Button btnCheck;
	
	private MainApp mainApp;
	private POPSolvingLayoutController controller;
	
	public POPConsoleLayoutController(POPSolvingLayoutController controller) {
		this.controller = controller;
	}
	
	@FXML
	private void initialize() {
		btnCheck.setOnAction(event -> {
			controller.checkAnswer();
		});
	}
	
	public void setOutput(String output) throws NullPointerException, NumberFormatException {
		txtOutput.setText(output);
	}
}
