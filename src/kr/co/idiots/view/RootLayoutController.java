package kr.co.idiots.view;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import kr.co.idiots.MainApp;
import kr.co.idiots.model.POPNodeType;

public class RootLayoutController {
	private BorderPane rootLayout;
	private Stage popup;
	
	private POPSolvingLayoutController solvingLayoutController;
	private POPCreateVariableLayoutController createVariableController;
	
	public RootLayoutController(BorderPane rootLayout) {
		this.rootLayout = rootLayout;
	}
	
	/*
	상위 레이아웃 안에 메인화면(기호들을 배치할 수 있는 페이지)을 보여준다.
	 */
	public void showPOPMainLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/POPSolvingLayout.fxml"));
			AnchorPane popMainLayout = (AnchorPane)loader.load();
			
			rootLayout.setCenter(popMainLayout);
			
			solvingLayoutController = loader.getController();
			solvingLayoutController.setRootController(this);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showCreateVariablePopup() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/POPCreateVariableLayout.fxml"));
			AnchorPane createVariablePopup = (AnchorPane)loader.load();
			
			popup = new Stage();
			popup.setScene(new Scene(createVariablePopup));
			popup.show();
			
			createVariableController = new POPCreateVariableLayoutController();
			createVariableController.setRootController(this);
			loader.setController(createVariableController);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void createVariable(String name, POPNodeType type) {
		solvingLayoutController.addVariable(name, type);
	}
}
