package kr.co.idiots.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPProblemDetailLayoutController {

	@FXML private TextArea txtContent;
	@FXML private TextArea txtInputExample;
	@FXML private TextArea txtOutputExample;
	
}
