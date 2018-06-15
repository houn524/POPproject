package kr.co.idiots.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import kr.co.idiots.MainApp;
import kr.co.idiots.model.POPLoggedInMember;
import kr.co.idiots.model.POPPost;
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

    @FXML private Button btnSelect;
    @FXML private Button btnWrite;

    private MainApp mainApp;

    private RootLayoutController rootController;

    public POPWritePostLayoutController(MainApp mainApp) { this.mainApp = mainApp; }

    @FXML private void initialize() {
        btnWrite.setOnAction(event -> {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            POPPost post = new POPPost(0, txtTitle.getText(), txtContent.getText(), POPLoggedInMember.getInstance().getMember().getId(),
                    0, dateFormat.format(new java.util.Date()), 0, Integer.parseInt(txtProbleNumber.getText()));
            mainApp.getConnector().insertPost(post);
        });
    }
}
