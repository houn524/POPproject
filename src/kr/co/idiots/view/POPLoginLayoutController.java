package kr.co.idiots.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import kr.co.idiots.MainApp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPLoginLayoutController {
	
	@FXML private TextField emailField;
	@FXML private TextField pwField;
	@FXML private Button btnLogin;
	
	private MainApp mainApp;
	
	public POPLoginLayoutController(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	@FXML
	private void initialize() {
		btnLogin.setOnAction(event -> {
			mainApp.initRootLayout();
		});
	}
}
