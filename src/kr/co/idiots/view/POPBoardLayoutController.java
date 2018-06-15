package kr.co.idiots.view;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import kr.co.idiots.MainApp;
import kr.co.idiots.model.POPPost;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPBoardLayoutController {

    @FXML private TableView tableView;
    @FXML private TableColumn<POPPost, String> titleColumn;
    @FXML private TableColumn<POPPost, String> authorColumn;
    @FXML private TableColumn<POPPost, Integer> commentCountColumn;
    @FXML private TableColumn<POPPost, String> dateColumn;

    @FXML private Button btnPost;

    private MainApp mainApp;
    private RootLayoutController rootController;

    private ObservableList<POPPost> postData = FXCollections.observableArrayList();

    public POPBoardLayoutController(MainApp mainApp) { this.mainApp = mainApp; }

    @FXML private  void initialize() {
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        authorColumn.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        commentCountColumn.setCellValueFactory(cellData -> cellData.getValue().commentCountProperty().asObject());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());

        titleColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.4));
        authorColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2));
        commentCountColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2));
        dateColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2));

        postData.addAll(mainApp.getConnector().loadPosts());

        tableView.setItems(postData);

        tableView.setSelectionModel(null);

        tableView.widthProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth)
            {
                TableHeaderRow header = (TableHeaderRow) tableView.lookup("TableHeaderRow");
                header.reorderingProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        header.setReordering(false);
                    }
                });
            }
        });

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        btnPost.setOnAction(event -> {
            rootController.showPOPWritePostLayout();
        });
    }
}
