package kr.co.idiots.view;

import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPLoadingLayoutController {

	@FXML private BorderPane rootPane;
	@FXML private ProgressBar progress;
	
}
