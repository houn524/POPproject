package kr.co.idiots.view;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import kr.co.idiots.MainApp;
import kr.co.idiots.model.POPLoggedInMember;
import kr.co.idiots.model.POPMember;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPLoginLayoutController {
	
	@FXML private TextField emailField;
	@FXML private TextField pwField;
	@FXML private Button btnLogin;
	
	private MainApp mainApp;
	
	public POPLoginLayoutController(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	@FXML
	private void initialize() {
		btnLogin.setOnAction(event -> {
			if(verifyMember(emailField.getText(), pwField.getText())) {
				POPLoggedInMember.getInstance().setMember(new POPMember(emailField.getText(), pwField.getText()));
				mainApp.initRootLayout();
			}
		});
		
		pwField.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
	        if (ev.getCode() == KeyCode.ENTER) {
	           btnLogin.fire();
	           ev.consume(); 
	        }
	    });
		
		emailField.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
	        if (ev.getCode() == KeyCode.ENTER) {
	           btnLogin.fire();
	           ev.consume(); 
	        }
	    });
	}
	
	public boolean verifyMember(String id, String pw) {
//		String inputId = emailField.getText();
//		String inputPw = pwField.getText();
		
		PreparedStatement st = null;
		
		String sql = "select * from member where id=?";
		
		boolean result = false;
		
		try {
            st = mainApp.getConnector().getConnection().prepareStatement(sql);//mainApp.getConnector().getConnection().createStatement();
            st.setString(1, id);
            
            ResultSet rs = st.executeQuery();
 
            if(!rs.next()) {
            	result = false;
            } else if(pw.equals(rs.getString("pw"))) {
            	result = true;
            } else {
            	result = false;
            }
 
            rs.close();
            st.close();
            
            
        } catch (SQLException se1) {
            se1.printStackTrace();
        }
		
		return result;
	}
}
