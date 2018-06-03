//package kr.co.idiots.view;
//
//import java.io.IOException;
//
//import javafx.fxml.FXMLLoader;
//import javafx.scene.layout.StackPane;
//import kr.co.idiots.MainApp;
//import lombok.Getter;
//import lombok.Setter;
//
//@Getter
//@Setter
//public class POPProblemCard {
//	private POPProblemCardController controller;
//	private StackPane component;
//	
//	private MainApp mainApp;
//	
//	public POPProblemCard(MainApp mainApp) {
//		this.mainApp = mainApp;
//		
//		controller = new POPProblemCardController(mainApp);
//		FXMLLoader loader = new FXMLLoader();
//		loader.setLocation(MainApp.class.getResource("view/POPProblemCard.fxml"));
////		loader.setRoot(this);
////		loader.setController(this);
//		loader.setControllerFactory(c -> {
//			return controller;
//		});
//		
//		try {
//            component = (StackPane) loader.load();
//        } catch (IOException exception) {
//            throw new RuntimeException(exception);
//        }
//	}
//	
//	public POPProblemCard setText(String text) {
//		controller.getProblemTitle().setText(text);
//		
//		return this;
//	}
//}
