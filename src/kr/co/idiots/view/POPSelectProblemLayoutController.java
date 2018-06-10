package kr.co.idiots.view;

import com.sun.javafx.scene.control.skin.TableHeaderRow;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import kr.co.idiots.MainApp;
import kr.co.idiots.model.POPProblem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPSelectProblemLayoutController {
//	@FXML
//	private Pane cardPane;
	
	@FXML
	private FlowPane flowPane;
	
	@FXML
	private TableView tableView;
	
	@FXML
	private TableColumn<POPProblem, Integer> numberColumn;
	
	@FXML
	private TableColumn<POPProblem, String> titleColumn;
	
	@FXML
	private HBox easyBox;
	
	@FXML
	private HBox mediumBox;
	
	@FXML
	private HBox hardBox;
	
	private MainApp mainApp;
	
	private RootLayoutController rootController;
	
	private ObservableList<POPProblem> problemData = FXCollections.observableArrayList();
	
	public POPSelectProblemLayoutController(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	@FXML
	private void initialize() {
		numberColumn.setCellValueFactory(cellData -> cellData.getValue().numberProperty().asObject());
		titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
		
		tableView.setRowFactory(tv -> {
			TableRow<POPProblem> row = new TableRow<>();
			row.setOnMouseEntered(event -> {
				if(!row.isEmpty()) {
					mainApp.getPrimaryStage().getScene().setCursor(Cursor.HAND);
					row.setStyle("-fx-text-background-color: #37a5e5;"
							+ "-fx-effect:  dropshadow( gaussian, rgba( 0, 0, 0, 0.5 ), 10, 0, 2, 2 );");
				}
					
			});
			
			row.setOnMouseExited(event -> {
				mainApp.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
				row.setStyle("");
			});
			
			row.setOnMouseClicked(event -> {
				POPProblem clickedRow = row.getItem();
				mainApp.getRootController().showPOPMainLayout(clickedRow);
			});
			return row;
		});
		
		problemData.addAll(mainApp.getConnector().loadProblems("초급"));
		
		tableView.setItems(problemData);
		
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
		
		easyBox.setOnMouseEntered(event -> {
			mainApp.getPrimaryStage().getScene().setCursor(Cursor.HAND);
			easyBox.setStyle("-fx-background-color: lightblue; -fx-background-insets: 0 20 0 20;");
		});
		
		easyBox.setOnMouseClicked(event -> {
			problemData.clear();
			problemData.addAll(mainApp.getConnector().loadProblems("초급"));
		});
		
		easyBox.setOnMouseExited(event -> {
			mainApp.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
			easyBox.setStyle("");
		});
		
		mediumBox.setOnMouseEntered(event -> {
			mainApp.getPrimaryStage().getScene().setCursor(Cursor.HAND);
			mediumBox.setStyle("-fx-background-color: lightblue; -fx-background-insets: 0 20 0 20;");
		});
		
		mediumBox.setOnMouseClicked(event -> {
			problemData.clear();
			problemData.addAll(mainApp.getConnector().loadProblems("중급"));
		});
		
		mediumBox.setOnMouseExited(event -> {
			mainApp.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
			mediumBox.setStyle("");
		});
		
		hardBox.setOnMouseEntered(event -> {
			mainApp.getPrimaryStage().getScene().setCursor(Cursor.HAND);
			hardBox.setStyle("-fx-background-color: lightblue; -fx-background-insets: 0 20 0 20;");
		});
		
		hardBox.setOnMouseClicked(event -> {
			problemData.clear();
			problemData.addAll(mainApp.getConnector().loadProblems("고급"));
		});
		
		hardBox.setOnMouseExited(event -> {
			mainApp.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
			hardBox.setStyle("");
		});
	}
}
