package kr.co.idiots.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class POPPopupManager {
	
	public static void showAlertPopup(String title, String header, String content, AlertType type) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);

		alert.showAndWait();
	}
	
}
