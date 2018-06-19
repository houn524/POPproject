package kr.co.idiots;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import kr.co.idiots.view.POPLoginLayoutController;
import kr.co.idiots.view.RootLayoutController;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainApp extends Application {

	private RootLayoutController rootController;
	private Stage primaryStage;
	private Stage popup;
	
	private AnchorPane loginLayout;
	private POPLoginLayoutController loginController;
	
	private Font f;
	
	private double xOffset = 0;
	private double yOffset = 0;
	
	private POPDatabaseConnector connector;
	
	public MainApp() {
		connector = new POPDatabaseConnector();
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("PoP");
		
		try {
			Font.loadFont(new FileInputStream(new File("src/Fonts/NanumGothic.ttf")), 14);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		showLoginLayout();
		primaryStage.show();
	}

	
	public void showLoginLayout() {
		
		try {
			if(loginController == null) 
				loginController = new POPLoginLayoutController(this);
				
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/POPLoginLayout.fxml"));
			loader.setControllerFactory(c -> {
				return loginController;
			});
			
			loginLayout = (AnchorPane) loader.load();
			
			Scene scene = new Scene(loginLayout);
			
			primaryStage.setScene(scene);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void initRootLayout() {
		try {
			RootLayoutController controller = new RootLayoutController(this);
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			loader.setControllerFactory(c -> {
				return controller;
			});
			BorderPane rootLayout = (BorderPane)loader.load();
			
			controller.setRootLayout(rootLayout);
			controller.setMainApp(this);
			

			this.rootController = controller;
			
			Scene scene = new Scene(rootLayout);
			controller.showPOPHomeLayout();

			primaryStage.setScene(scene);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}
			
	public static void main(String[] args) {
		launch(args);
	}
}
