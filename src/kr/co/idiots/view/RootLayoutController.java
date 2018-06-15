package kr.co.idiots.view;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Bloom;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import kr.co.idiots.MainApp;
import kr.co.idiots.model.POPLoggedInMember;
import kr.co.idiots.model.POPNodeType;
import kr.co.idiots.model.POPPost;
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
	private Label lbBoard;
	
	@FXML
	private BorderPane rootPane;
	
	@FXML
	private Button btnLogout;

	private POPPostLayoutController postController;
	private POPPreviewImageLayoutController previewImageController;
	private POPWritePostLayoutController writePostController;
	private POPBoardLayoutController boardLayoutController;
	private POPSelectProblemLayoutController selectProblemLayoutController;
	private POPSolvingLayoutController solvingLayoutController;
	private POPCreateVariableLayoutController createVariableController;
	private POPLoadingLayoutController loadingController;

	private Stage selectImageStage;

	private ImageView selectedImageView;
	
	public RootLayoutController(MainApp mainApp) {
		this.mainApp = mainApp;
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

		lbBoard.setOnMouseEntered(event -> {
			Bloom bloom = new Bloom();
			bloom.setThreshold(0.1);
			lbBoard.setEffect(bloom);
		});

		lbBoard.setOnMouseExited(event -> {
			Bloom bloom = new Bloom();
			bloom.setThreshold(0.2);
			lbBoard.setEffect(bloom);
		});

		lbBoard.setOnMouseClicked(event -> {
			showPOPBoardLayout();
		});
		
		btnLogout.setOnAction(event -> {
			POPLoggedInMember.getInstance().setMember(null);
			mainApp.showLoginLayout();
		});
		
	}

	public void showPOPPreviewImageLayout(int flowchartId) {
		try {
			previewImageController = new POPPreviewImageLayoutController(mainApp, flowchartId);
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/POPPreviewImageLayout.fxml"));
			loader.setControllerFactory(c -> {
				return previewImageController;
			});
			AnchorPane popSelectImagePane = (AnchorPane)loader.load();
			selectImageStage = new Stage();

			selectImageStage.setScene(new Scene(popSelectImagePane));
			selectImageStage.show();
//			rootLayout.setCenter(popSelectImagePane);

			previewImageController.setRootController(this);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void showPOPPostLayout(POPPost post) {
		try {
			postController = new POPPostLayoutController(mainApp, post);
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/POPPostLayout.fxml"));
			loader.setControllerFactory(c -> {
				return postController;
			});
			AnchorPane popPostPane = (AnchorPane)loader.load();

			rootLayout.setCenter(popPostPane);

			postController.setRootController(this);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void showPOPBoardLayout() {
		try {
			boardLayoutController = new POPBoardLayoutController(mainApp);
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/POPBoardLayout.fxml"));
			loader.setControllerFactory(c -> {
				return boardLayoutController;
			});
			AnchorPane popBoardPane = (AnchorPane)loader.load();

			rootLayout.setCenter(popBoardPane);

			boardLayoutController.setRootController(this);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void showPOPWritePostLayout() {
		try {
			writePostController = new POPWritePostLayoutController(mainApp);
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/POPWritePostLayout.fxml"));
			loader.setControllerFactory(c -> {
				return writePostController;
			});
			AnchorPane popWritePostPane = (AnchorPane)loader.load();

			rootLayout.setCenter(popWritePostPane);

			writePostController.setRootController(this);
		} catch(IOException e) {
			e.printStackTrace();
		}
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
			
			
//			String content = mainApp.getConnector().loadFlowchart(1);
//			
//			solvingLayoutController.loadFlowchart(content);
			
//			solvingLayoutController.showScriptArea();
			rootLayout.setCenter(popMainLayout);
			
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
		solvingLayoutController.addVariable(name, type, "");
	}
}
