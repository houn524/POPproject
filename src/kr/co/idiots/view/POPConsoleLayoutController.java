package kr.co.idiots.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import kr.co.idiots.MainApp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPConsoleLayoutController {
	
	@FXML private TextArea txtOutput;
	
	private MainApp mainApp;
	private POPSolvingLayoutController controller;
	
	public POPConsoleLayoutController() {
		
	}
	
	public void setOutput(String output) {
		txtOutput.setText(output);
	}
}
