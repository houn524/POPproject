package kr.co.idiots.view;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.Bloom;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import kr.co.idiots.MainApp;
import kr.co.idiots.model.POPNodeType;
import kr.co.idiots.model.POPProblem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RootLayoutController {
	private MainApp mainApp;
	private BorderPane rootLayout;
	private Stage popup;
	
	@FXML
	private Label lbHome;
	
	@FXML
	private Label lbSolving;
	
	@FXML
	private BorderPane rootPane;
	
	private POPSelectProblemLayoutController selectProblemLayoutController;
	private POPSolvingLayoutController solvingLayoutController;
	private POPCreateVariableLayoutController createVariableController;
	private POPLoadingLayoutController loadingController;
	
	public RootLayoutController() {
	}
	
	@FXML
	private void initialize() {
		lbHome.setOnMouseEntered(event -> {
			Bloom bloom = new Bloom();
			bloom.setThreshold(0.1);
			lbHome.setEffect(bloom);
		});
		
		lbHome.setOnMouseExited(event -> {
			Bloom bloom = new Bloom();
			bloom.setThreshold(0.2);
			lbHome.setEffect(bloom);
		});
		
		lbHome.setOnMouseClicked(event -> {
			showPOPHomeLayout();
		});
		
		lbSolving.setOnMouseEntered(event -> {
			Bloom bloom = new Bloom();
			bloom.setThreshold(0.1);
			lbSolving.setEffect(bloom);
		});
		
		lbSolving.setOnMouseExited(event -> {
			Bloom bloom = new Bloom();
			bloom.setThreshold(0.2);
			lbSolving.setEffect(bloom);
		});
		
		lbSolving.setOnMouseClicked(event -> {
			showPOPSelectProblemLayout();
		});
		
	}
	
	public void showPOPHomeLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/POPHomeLayout.fxml"));
			BorderPane popHomeLayout = (BorderPane)loader.load();
			
			rootLayout.setCenter(popHomeLayout);
			
			//loader.getController();
			
//			loader.setController(solvingLayoutController);
			
//			solvingLayoutController.setRootController(this);
//			solvingLayoutController.setMainApp(mainApp);
			
			
//			solvingLayoutController.setMainApp(mainApp);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showPOPSelectProblemLayout() {
		try {
			selectProblemLayoutController = new POPSelectProblemLayoutController(mainApp);
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/POPSelectProblemLayout.fxml"));
			loader.setControllerFactory(c -> {
				return selectProblemLayoutController;
			});
			BorderPane popSelectProblemLayout = (BorderPane)loader.load();
			
			rootLayout.setCenter(popSelectProblemLayout);
			
			selectProblemLayoutController.setRootController(this);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	상위 레이아웃 안에 메인화면(기호들을 배치할 수 있는 페이지)을 보여준다.
	 */
	public void showPOPMainLayout(POPProblem problem) {
		try {
			solvingLayoutController = new POPSolvingLayoutController(mainApp, problem);
			solvingLayoutController.setRootController(this);
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/POPSolvingLayout.fxml"));
			loader.setControllerFactory(c -> {
				return solvingLayoutController;
			});
			AnchorPane popMainLayout = (AnchorPane)loader.load();
			
			
			String content = mainApp.getConnector().loadFlowchart(1);
			
			solvingLayoutController.loadFlowchart(content);
			
//			solvingLayoutController.showScriptArea();
//			rootLayout.setCenter(popMainLayout);
			
			//loader.getController();
			
//			loader.setController(solvingLayoutController);
			
//			solvingLayoutController.setMainApp(mainApp);
			
			
//			solvingLayoutController.setMainApp(mainApp);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
//	public void showPOPLoadingLayout() {
//		try {
//			if(loadingController == null)
//				loadingController = new POPLoadingLayoutController();
//			
//				
//			FXMLLoader loader = new FXMLLoader();
//			loader.setLocation(MainApp.class.getResource("view/POPLoadingLayout.fxml"));
//			loader.setControllerFactory(c -> {
//				return loadingController;
//			});
//			BorderPane loadingPane = (BorderPane)loader.load();
//			
//			rootLayout.setCenter(loadingController.getRootPane());
//			
//		} catch(IOException e) {
//			e.printStackTrace();
//		}
//	}
	
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
