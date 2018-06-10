package kr.co.idiots.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import kr.co.idiots.MainApp;
import kr.co.idiots.POPDatabaseConnector;
import kr.co.idiots.util.POPPopupManager;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPJoinLayoutController {

	@FXML private TextField txtId;
	@FXML private PasswordField txtPw;
	@FXML private PasswordField txtPwConfirm;
	
	@FXML private Button btnJoin;
	
	private MainApp mainApp;
	private POPLoginLayoutController loginController;
	
	public POPJoinLayoutController(MainApp mainApp, POPLoginLayoutController loginController) {
		this.mainApp = mainApp;
		this.loginController = loginController;
	}
	
	@FXML
	private void initialize() {
		
		btnJoin.setOnAction(event -> {
			if(checkJoin()) {
				mainApp.getConnector().insertMember(txtId.getText(), txtPw.getText());
				POPPopupManager.showAlertPopup("회원가입", "회원가입", "회원가입이 완료되었습니다.", AlertType.INFORMATION);
				loginController.getJoinStage().close();
			}
		});
	}
	
	private boolean checkJoin() {
		POPDatabaseConnector connector = mainApp.getConnector();
		
		String id = txtId.getText();
		String pw = txtPw.getText();
		String pwConfirm = txtPwConfirm.getText();
		
		if(id.isEmpty()) {
			POPPopupManager.showAlertPopup("회원가입", "회원가입", "아이디를 입력해주세요.", AlertType.ERROR);
			return false;
		} else if(pw.isEmpty()) {
			POPPopupManager.showAlertPopup("회원가입", "회원가입", "비밀번호를 입력해주세요.", AlertType.ERROR);
			return false;
		} else if(pwConfirm.isEmpty()) {
			POPPopupManager.showAlertPopup("회원가입", "회원가입", "비밀번호 확인을 입력해주세요.", AlertType.ERROR);
			return false;
		}
		
		if(connector.checkOverlapID(id)) {
			POPPopupManager.showAlertPopup("회원가입", "회원가입", "이미 존재하는 아이디입니다.", AlertType.ERROR);
			return false;
		} else if(!pw.equals(pwConfirm)) {
			POPPopupManager.showAlertPopup("회원가입", "회원가입", "비밀번호가 일치하지 않습니다.", AlertType.ERROR);
			return false;
		}
		
		return true;
	}
}
