package kr.co.idiots.view;

import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import kr.co.idiots.MainApp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPSelectProblemLayoutController {
//	@FXML
//	private Pane cardPane;
	
	@FXML
	private FlowPane flowPane;
	
	private MainApp mainApp;
	
	private RootLayoutController rootController;
	
	public POPSelectProblemLayoutController(MainApp mainApp) {
		this.mainApp = mainApp;
		
		
	}
	
	@FXML
	private void initialize() {
		flowPane.getChildren().add(new POPProblemCard(mainApp).setText("1. 구구단").getComponent());
		flowPane.getChildren().add(new POPProblemCard(mainApp).setText("2. 문제1").getComponent());
		flowPane.getChildren().add(new POPProblemCard(mainApp).setText("3. 문제2").getComponent());
		flowPane.getChildren().add(new POPProblemCard(mainApp).setText("4. 문제3").getComponent());
	}
}
