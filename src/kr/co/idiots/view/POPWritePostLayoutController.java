package kr.co.idiots.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import kr.co.idiots.MainApp;
import kr.co.idiots.model.POPLoggedInMember;
import kr.co.idiots.model.POPPost;
import kr.co.idiots.util.POPPopupManager;
import kr.co.idiots.util.TypeChecker;
import lombok.Getter;
import lombok.Setter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Getter
@Setter
public class POPWritePostLayoutController {

    @FXML private TextField txtTitle;
    @FXML private TextField txtProbleNumber;
    @FXML private TextArea txtContent;

    @FXML ImageView imgView;

    @FXML private Button btnSelect;
    @FXML private Button btnWrite;

    private int flowchartId = 0;

    private MainApp mainApp;

    private RootLayoutController rootController;

    public POPWritePostLayoutController(MainApp mainApp) { this.mainApp = mainApp; }

    @FXML private void initialize() {
        btnWrite.setOnAction(event -> {
            if(!TypeChecker.isInteger(txtProbleNumber.getText()) || !mainApp.getConnector().checkProblemNumber(Integer.parseInt(txtProbleNumber.getText()))) {
                POPPopupManager.showAlertPopup("게시글 작성 오류", "게시글 작성 오류", "존재하지 않는 문제번호입니다.", Alert.AlertType.ERROR);
            } else {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                System.out.println(flowchartId);
                POPPost post = new POPPost(0, txtTitle.getText(), txtContent.getText(), POPLoggedInMember.getInstance().getMember().getId(),
                        flowchartId, dateFormat.format(new java.util.Date()), flowchartId, Integer.parseInt(txtProbleNumber.getText()), imgView.getImage());
                mainApp.getConnector().insertPost(post);
                rootController.showPOPBoardLayout();
            }
        });

        btnSelect.setOnAction(event -> {
            rootController.showPOPSelectImageLayout();
        });
    }
}
