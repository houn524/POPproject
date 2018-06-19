package kr.co.idiots.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import kr.co.idiots.MainApp;
import kr.co.idiots.model.POPLoggedInMember;
import kr.co.idiots.model.POPPost;
import kr.co.idiots.model.POPProblem;
import kr.co.idiots.util.POPPopupManager;
import kr.co.idiots.util.TypeChecker;
import lombok.Getter;
import lombok.Setter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

@Getter
@Setter
public class POPWritePostLayoutController {

    @FXML private TextField txtTitle;
    @FXML private TextArea txtContent;

    @FXML private ComboBox<String> comboBox;

    @FXML private CheckBox checkBox;

    @FXML private Button btnSelect;
    @FXML private Button btnWrite;

    private int flowchartId = 0;

    private MainApp mainApp;

    private RootLayoutController rootController;

    private ObservableList<String> problemList;

    public POPWritePostLayoutController(MainApp mainApp) { this.mainApp = mainApp; }

    @FXML private void initialize() {
        problemList = FXCollections.observableArrayList();

        ArrayList<POPProblem> problems = mainApp.getConnector().loadProblems("초급");
        problems.addAll(mainApp.getConnector().loadProblems("중급"));
        problems.addAll(mainApp.getConnector().loadProblems("고급"));

        ArrayList<String> comboString = new ArrayList<>();
        Iterator<POPProblem> it = problems.iterator();
        while(it.hasNext()) {
            POPProblem problem = it.next();
            comboString.add(problem.getNumber() + " : " + problem.getTitle());
        }

        comboBox.getItems().addAll(comboString);
        comboBox.getSelectionModel().selectFirst();
        flowchartId = mainApp.getConnector().loadFlowchartId(POPLoggedInMember.getInstance().getMember().getId(), Integer.parseInt(comboBox.getValue().split(" : ")[0]));

        comboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                flowchartId = mainApp.getConnector().loadFlowchartId(POPLoggedInMember.getInstance().getMember().getId(), Integer.parseInt(newValue.split(" : ")[0]));
            }
        });
        btnWrite.setOnAction(event -> {
            if(checkBox.isSelected())
                flowchartId = mainApp.getConnector().loadFlowchartId(POPLoggedInMember.getInstance().getMember().getId(), Integer.parseInt(comboBox.getValue().split(" : ")[0]));
            else
                flowchartId = 0;

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            POPPost post = new POPPost(0, txtTitle.getText(), txtContent.getText(), POPLoggedInMember.getInstance().getMember().getId(),
                    flowchartId, dateFormat.format(new java.util.Date()), flowchartId, Integer.parseInt(comboBox.getValue().split(" : ")[0]), null);
            mainApp.getConnector().insertPost(post);
            rootController.showPOPBoardLayout();
//            }
        });

        btnSelect.setOnAction(event -> {
            rootController.showPOPPreviewImageLayout(flowchartId);
        });
    }
}
