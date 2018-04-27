package kr.co.idiots.view;

import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import kr.co.idiots.MainApp;
import kr.co.idiots.POPFlowchartPlayer;
import kr.co.idiots.POPVariableManager;
import kr.co.idiots.model.POPNodeType;
import kr.co.idiots.model.POPScriptArea;
import kr.co.idiots.model.POPVariableNode;
import kr.co.idiots.model.compare.POPIsEqualSymbol;
import kr.co.idiots.model.compare.POPLessThanEqualSymbol;
import kr.co.idiots.model.compare.POPLessThanSymbol;
import kr.co.idiots.model.compare.POPNotEqualSymbol;
import kr.co.idiots.model.operation.POPDivideSymbol;
import kr.co.idiots.model.operation.POPMinusSymbol;
import kr.co.idiots.model.operation.POPMultiplySymbol;
import kr.co.idiots.model.operation.POPPlusSymbol;
import kr.co.idiots.model.operation.POPRemainderSymbol;
import kr.co.idiots.model.symbol.POPDecisionNode;
import kr.co.idiots.model.symbol.POPDocumentNode;
import kr.co.idiots.model.symbol.POPLoopNode;
import kr.co.idiots.model.symbol.POPProcessNode;
import kr.co.idiots.model.symbol.POPStartNode;
import kr.co.idiots.model.symbol.POPStopNode;
import kr.co.idiots.model.symbol.POPSymbolNode;
import kr.co.idiots.util.DragManager;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPSolvingLayoutController {
	Point2D lastXY = null;
	
	@FXML
	private ImageView node;
//	@FXML
//	private ScrollPane scrollPane;
	@FXML
	private AnchorPane symbolArea;
	
	@FXML
	private AnchorPane operationArea;
	
	@FXML
	private GridPane variableArea;
//	@FXML
//	private ListView variableListView;
	@FXML
	public AnchorPane rootPane;
	
	@FXML
	private TabPane tabPane;
	
	@FXML
	private AnchorPane scriptPane;
	
	@FXML
	private ScrollPane scriptScrollPane;
	
	@FXML
	private SplitPane splitPane;
	
	@FXML
	private BorderPane consoleFrame;
	
	@FXML
	private Button btnCreateVariable;
	
	private POPFlowchartPlayer flowchartPlayer;
	
	private Label lbConsole;
	
	private Button btnStart;
	
//	private ObservableList<StackPane> variableItems;
	
	private POPProcessNode processSymbol;
	private POPDocumentNode documentSymbol;
	private POPDecisionNode decisionSymbol;
	private POPLoopNode loopSymbol;
	
	private POPPlusSymbol plusSymbol;
	private POPMinusSymbol minusSymbol;
	private POPMultiplySymbol multiplySymbol;
	private POPDivideSymbol divideSymbol;
	private POPRemainderSymbol remainderSymbol;
	
	private POPIsEqualSymbol isEqualSymbol;
	private POPLessThanSymbol lessThanSymbol;
	private POPLessThanEqualSymbol lessThanEqualSymbol;
	private POPNotEqualSymbol notEqualSymbol;
	
	private Stage popup;
	
	private MainApp mainApp;
	
	private Bounds frameBounds;
	
	public static POPScriptArea scriptArea;
	
	private RootLayoutController rootController;
	
	private POPCreateVariableLayoutController createVariableController;

	
	
	public POPSolvingLayoutController() {
		
	}
	
	@FXML
	private void initialize() {
		flowchartPlayer = new POPFlowchartPlayer();
		
		lbConsole = new Label("출력값 : ");
		lbConsole.setFont(new Font(20));
		consoleFrame.setLeft(lbConsole);
		
//		variableItems = FXCollections.observableArrayList();
//		variableListView.setItems(variableItems);
		
		scriptArea = new POPScriptArea(scriptPane, scriptScrollPane);
		
		scriptScrollPane.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
			@Override
			public void changed(ObservableValue<? extends Bounds> arg0, Bounds arg1, Bounds arg2) {
				// TODO Auto-generated method stub
				Node content = scriptScrollPane.getContent();
				scriptScrollPane.setFitToWidth(content.prefWidth(-1) < arg2.getWidth());
				scriptScrollPane.setFitToWidth(content.prefHeight(-1) < arg2.getHeight());
			}
		});
		
		
		btnStart = new Button("실행");
		btnStart.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				lbConsole.setText(scriptArea.play());
//				try {
//					lbConsole.setText("출력값 : " + scriptArea.generate());
//				} catch (IOException | NoSuchFieldException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}
			
		});
		
		btnCreateVariable.setOnAction(event-> {
			showCreateVariablePopup();
		});
		
		
		
		processSymbol = new POPProcessNode(scriptArea);
		symbolArea.getChildren().add(processSymbol.getComponent());
		documentSymbol = new POPDocumentNode(scriptArea);
		symbolArea.getChildren().add(documentSymbol.getComponent());
		documentSymbol.getComponent().setLayoutY(100);
		decisionSymbol = new POPDecisionNode(scriptArea);
		symbolArea.getChildren().add(decisionSymbol.getComponent());
		decisionSymbol.getComponent().setLayoutY(200);
		loopSymbol = new POPLoopNode(scriptArea);
		symbolArea.getChildren().add(loopSymbol.getComponent());
		loopSymbol.getComponent().setLayoutY(300);
		
		plusSymbol = new POPPlusSymbol();
		operationArea.getChildren().add(plusSymbol);
		minusSymbol = new POPMinusSymbol();
		operationArea.getChildren().add(minusSymbol);
		minusSymbol.setTranslateY(50);
		multiplySymbol = new POPMultiplySymbol();
		operationArea.getChildren().add(multiplySymbol);
		multiplySymbol.setTranslateY(100);
		divideSymbol = new POPDivideSymbol();
		operationArea.getChildren().add(divideSymbol);
		divideSymbol.setTranslateY(150);
		remainderSymbol = new POPRemainderSymbol();
		operationArea.getChildren().add(remainderSymbol);
		remainderSymbol.setTranslateY(200);
		
		isEqualSymbol = new POPIsEqualSymbol();
		operationArea.getChildren().add(isEqualSymbol);
		isEqualSymbol.setTranslateY(300);
		lessThanSymbol = new POPLessThanSymbol();
		operationArea.getChildren().add(lessThanSymbol);
		lessThanSymbol.setTranslateY(350);
		lessThanEqualSymbol = new POPLessThanEqualSymbol();
		operationArea.getChildren().add(lessThanEqualSymbol);
		lessThanEqualSymbol.setTranslateY(400);
		notEqualSymbol = new POPNotEqualSymbol();
		operationArea.getChildren().add(notEqualSymbol);
		notEqualSymbol.setTranslateY(450);
		
		
		POPSymbolNode startNode = new POPStartNode(scriptArea);
		POPSymbolNode stopNode = new POPStopNode(scriptArea);
		
		startNode.getOutFlowLine().setNextNode(stopNode);
		
		scriptArea.setStartNode(startNode);
		scriptArea.addWithOutFlowLine(startNode);
		scriptArea.addWithOutFlowLine(stopNode);
		scriptArea.getComponent().getChildren().add(btnStart);
		
		DragManager.dragRootPane = rootPane;
		DragManager.tabPane = tabPane;
	}
	
	public void showCreateVariablePopup() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/POPCreateVariableLayout.fxml"));
			AnchorPane createVariablePopup = (AnchorPane)loader.load();
			
			popup = new Stage();
			popup.setScene(new Scene(createVariablePopup));
			popup.show();
			
			createVariableController = loader.getController();
			createVariableController.setController(this);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addVariable(String name, POPNodeType type) {
		POPVariableNode varNode = new POPVariableNode(scriptArea, name, type);
		variableArea.add(varNode, POPVariableManager.createdVars.size() % 3, POPVariableManager.createdVars.size() / 3);
		POPVariableManager.createdVars.add(varNode.getName());
		popup.close();
	}
	
	@FXML
	private void showVariablePopup() {
		showCreateVariablePopup();
	}
	
	public static ClipboardContent makeClipboardContent(MouseEvent event, Node child, String text) {
		ClipboardContent cb = new ClipboardContent();
		if(text != null) {
			cb.put(DataFormat.PLAIN_TEXT, text);
		}
		if(!event.isShiftDown()) {
			SnapshotParameters params = new SnapshotParameters();
			params.setFill(Color.TRANSPARENT);
			Bounds b = child.getBoundsInParent();
			double f = 10;
			params.setViewport(new Rectangle2D(b.getMinX() - f, b.getMinY() - f, b.getWidth() + f + f, b.getHeight() + f + f));
			
			WritableImage image = child.snapshot(params, null);
			cb.putImage(image);
//			cb.put(DataFormat.IMAGE, image);
			
//			try {
//				File tmpFile = File.createTempFile("snapshot", ".png");
//				LinkedList<File> list = new LinkedList<File>();
//				ImageIO.write(SwingFXUtils.fromFXImage(image,  null), "png", tmpFile);
//				list.add(tmpFile);
//				cb.put(DataFormat.FILES, list);
//			} catch(Exception e) {
//				
//			}
		}
		return cb;
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
}
