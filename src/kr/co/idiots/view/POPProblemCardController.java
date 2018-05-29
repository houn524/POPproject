package kr.co.idiots.view;


import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import kr.co.idiots.MainApp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPProblemCardController {

	@FXML
	private Label problemTitle;
	
	@FXML
	private StackPane stackPane;
	
//	@FXML
//	private Rectangle rect;
	
	private MainApp mainApp;
	
	private RootLayoutController rootController;
	
	public POPProblemCardController(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	@FXML
	private void initialize() {
		stackPane.setOnMouseEntered(event -> {
			mainApp.getPrimaryStage().getScene().setCursor(Cursor.HAND);
			problemTitle.setOpacity(0.5);//.setStyle("-fx-background-color:#dae7f3;");
		});
		
		stackPane.setOnMouseExited(event -> {
			mainApp.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
			problemTitle.setOpacity(1);
		});
		
		stackPane.setOnMouseClicked(event -> {
			mainApp.getRootController().showPOPMainLayout();
		});
	}
}
