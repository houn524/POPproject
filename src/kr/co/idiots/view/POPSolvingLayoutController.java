package kr.co.idiots.view;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import kr.co.idiots.MainApp;
import kr.co.idiots.POPFlowchartPlayer;
import kr.co.idiots.POPVariableManager;
import kr.co.idiots.SubNodeIF;
import kr.co.idiots.model.POPArrayNode;
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
import kr.co.idiots.model.operation.POPStringPlusSymbol;
import kr.co.idiots.model.symbol.POPDecisionEndNode;
import kr.co.idiots.model.symbol.POPDecisionNode;
import kr.co.idiots.model.symbol.POPDocumentNode;
import kr.co.idiots.model.symbol.POPLoopEndNode;
import kr.co.idiots.model.symbol.POPLoopNode;
import kr.co.idiots.model.symbol.POPProcessNode;
import kr.co.idiots.model.symbol.POPStartNode;
import kr.co.idiots.model.symbol.POPStopNode;
import kr.co.idiots.model.symbol.POPSymbolNode;
import kr.co.idiots.util.DragManager;
import kr.co.idiots.util.PlatformHelper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPSolvingLayoutController {
	Point2D lastXY = null;
	
	@FXML
	private ImageView node;
	
//	@FXML
//	private AnchorPane symbolArea;
	
	@FXML
	private Group symbolGroup;
	
	@FXML
	private AnchorPane operationArea;
	
	@FXML
	private FlowPane variableArea;
	
	@FXML
	public AnchorPane rootPane;
	
	@FXML
	private TabPane tabPane;
	
	@FXML
	private AnchorPane scriptPane;
	
	@FXML
	private ScrollPane scriptScrollPane;
	
	@FXML
	private SplitPane nodeSplitPane;
	
	@FXML
	private SplitPane scriptSplitPane;
	
	@FXML
	private AnchorPane consoleFrame;
	
	@FXML
	private Button btnCreateVariable;
	
	@FXML
	private Button btnCreateArray;
	
	private POPFlowchartPlayer flowchartPlayer;
	
	private Label lbConsole;
	
	@FXML
	private Button btnStart;
	
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
	
	private POPStringPlusSymbol stringPlusSymbol;
	
	private static Stage popup;
	
	private static MainApp mainApp;
	
	private Bounds frameBounds;
	
	public static POPScriptArea scriptArea;
	
	private RootLayoutController rootController;
	
	private POPCreateVariableLayoutController createVariableController;
	private POPCreateArrayLayoutController createArrayController;
	private static POPErrorPopupLayoutController errorPopupController;

	public static final Set<KeyCode> pressedKeys = new HashSet<>();
	
	public POPSolvingLayoutController(MainApp mainApp) {
		this.mainApp = mainApp;
		
		mainApp.getPrimaryStage().getScene().setOnKeyPressed(e -> {
			pressedKeys.add(e.getCode());
		});
		mainApp.getPrimaryStage().getScene().setOnKeyReleased(e -> pressedKeys.remove(e.getCode()));
		
		loopSymbol = new POPLoopNode(scriptArea);
		loopSymbol.invisibleSubNodes();
		decisionSymbol = new POPDecisionNode(scriptArea);
		decisionSymbol.invisibleSubNodes();
//		loopSymbol.invisibleSubNodes();
	}
	
	@FXML
	private void initialize() {
		flowchartPlayer = new POPFlowchartPlayer(this);
		
		lbConsole = new Label("출력값 : ");
		lbConsole.setFont(new Font(20));
		consoleFrame.getChildren().add(lbConsole);
		
		scriptArea = new POPScriptArea(scriptPane, scriptScrollPane, this);
		
		scriptScrollPane.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
			@Override
			public void changed(ObservableValue<? extends Bounds> arg0, Bounds arg1, Bounds arg2) {
				Node content = scriptScrollPane.getContent();
				scriptScrollPane.setFitToWidth(content.prefWidth(-1) < arg2.getWidth());
				scriptScrollPane.setFitToWidth(content.prefHeight(-1) < arg2.getHeight());
			}
		});
		
		btnStart.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Thread thread = new Thread() {
					@Override
					public void run() {
						PlatformHelper.run(() -> {
							lbConsole.setText(scriptArea.play());
						});
					}
				};
				
				thread.setDaemon(true);
				thread.start();
				
			}
			
		});
		
		btnCreateVariable.setOnAction(event-> {
			showCreateVariablePopup();
		});
		
		btnCreateArray.setOnAction(event -> {
			showCreateArrayPopup();
		});
		
		processSymbol = new POPProcessNode(scriptArea);
		symbolGroup.getChildren().add(processSymbol.getComponent());
		processSymbol.setLayoutX(processSymbol.getLayoutX() + 20);
		documentSymbol = new POPDocumentNode(scriptArea);
		symbolGroup.getChildren().add(documentSymbol.getComponent());
		documentSymbol.setLayoutX(documentSymbol.getLayoutX() + 20);
		documentSymbol.getComponent().setLayoutY(70);
		
		this.add(symbolGroup, decisionSymbol);
		decisionSymbol.setLayoutX(decisionSymbol.getLayoutX() + 10);
		decisionSymbol.getComponent().setLayoutY(140);
		
		this.add(symbolGroup, loopSymbol);
		
		loopSymbol.setLayoutX(loopSymbol.getLayoutX() + 10);
		loopSymbol.getComponent().setLayoutY(250);
		
		plusSymbol = new POPPlusSymbol();
		operationArea.getChildren().add(plusSymbol);
		minusSymbol = new POPMinusSymbol();
		operationArea.getChildren().add(minusSymbol);
		minusSymbol.setTranslateY(40);
		multiplySymbol = new POPMultiplySymbol();
		operationArea.getChildren().add(multiplySymbol);
		multiplySymbol.setTranslateY(80);
		divideSymbol = new POPDivideSymbol();
		operationArea.getChildren().add(divideSymbol);
		divideSymbol.setTranslateY(120);
		remainderSymbol = new POPRemainderSymbol();
		operationArea.getChildren().add(remainderSymbol);
		remainderSymbol.setTranslateY(160);
		
		isEqualSymbol = new POPIsEqualSymbol();
		operationArea.getChildren().add(isEqualSymbol);
		isEqualSymbol.setTranslateY(240);
		lessThanSymbol = new POPLessThanSymbol();
		operationArea.getChildren().add(lessThanSymbol);
		lessThanSymbol.setTranslateY(280);
		lessThanEqualSymbol = new POPLessThanEqualSymbol();
		operationArea.getChildren().add(lessThanEqualSymbol);
		lessThanEqualSymbol.setTranslateY(320);
		notEqualSymbol = new POPNotEqualSymbol();
		operationArea.getChildren().add(notEqualSymbol);
		notEqualSymbol.setTranslateY(360);
		
		stringPlusSymbol = new POPStringPlusSymbol();
		operationArea.getChildren().add(stringPlusSymbol);
		stringPlusSymbol.setLayoutY(440);
		
		
		POPSymbolNode startNode = new POPStartNode(scriptArea);
		POPSymbolNode stopNode = new POPStopNode(scriptArea);
		
		startNode.getOutFlowLine().setNextNode(stopNode);
		startNode.getOutFlowLine().setStartNode((POPStartNode) startNode); 
		stopNode.setParentNode(startNode);
		startNode.getOutFlowLine().pullNodesThread();
		
		scriptArea.setStartNode(startNode);
		scriptArea.addWithOutFlowLine(startNode);
		scriptArea.addWithOutFlowLine(stopNode);
		
		DragManager.dragRootPane = rootPane;
		DragManager.tabPane = tabPane;
		
		mainApp.getPrimaryStage().widthProperty().addListener((obs, oldVal, newVal) -> {
			if(Double.isNaN(oldVal.doubleValue())) {
				return;
			}
			double absNodeDividerPos = nodeSplitPane.getDividerPositions()[0] * oldVal.doubleValue();
			nodeSplitPane.setDividerPosition(0, absNodeDividerPos / newVal.doubleValue());
		});
		
		mainApp.getPrimaryStage().heightProperty().addListener((obs, oldVal, newVal) -> {
			if(Double.isNaN(oldVal.doubleValue())) {
				return;
			}
			double absConsoleDividerPos = oldVal.doubleValue() - (scriptSplitPane.getDividerPositions()[1] * oldVal.doubleValue());
			scriptSplitPane.setDividerPosition(1, (newVal.doubleValue() - absConsoleDividerPos) / newVal.doubleValue());
			
			double absProblemDividerPos = scriptSplitPane.getDividerPositions()[0] * oldVal.doubleValue();
			scriptSplitPane.setDividerPosition(0, absProblemDividerPos / newVal.doubleValue());
		});
		
		Thread thread = new Thread() {
			@Override
			public void run() {
				PlatformHelper.run(() -> {
//					while(true) {
//						ThreadMXBean bean = ManagementFactory.getThreadMXBean();
//						if(bean.getThreadCount() <= 23) {
							loopSymbol.visibleSubNodes();
							decisionSymbol.visibleSubNodes();
//							break;
//						}
//						System.out.println(bean.getThreadCount());
//					}
				});
			}
		};
		
		thread.setDaemon(true);
		thread.start();
		
//		loopSymbol.visibleSubNodes();
	}
	
	public void add(Group pane, Node node) {
		if(node instanceof SubNodeIF) {
			pane.getChildren().add(node);
			
			for(Node subNode : ((SubNodeIF) node).getSubNodes()) {
				if(subNode instanceof SubNodeIF) {
					add(pane, (POPSymbolNode) subNode);
				} else {
					pane.getChildren().add(subNode);
				}
				if(!(subNode instanceof POPDecisionEndNode) && !(subNode instanceof POPLoopEndNode) && subNode instanceof POPSymbolNode) {
					pane.getChildren().add(((POPSymbolNode) subNode).getOutFlowLine());
				}
			}
		} else {
			pane.getChildren().add(node);
		}
	}
	
	public static void showErrorPopup(String string) {
//		try {
//			FXMLLoader loader = new FXMLLoader();
//			loader.setLocation(MainApp.class.getResource("view/POPErrorPopupLayout.fxml"));
//			AnchorPane errorPopup = (AnchorPane)loader.load();
//			
//			popup = new Stage();
//			popup.setScene(new Scene(errorPopup));
////			mainApp.getPrimaryStage().setOnHidden(e -> { popup.show(); });
////			popup.show();
//			
//			errorPopupController = loader.getController();
//			errorPopupController = errorPopupController.setText(string);
////			errorPopupController.setSolvingController(this);
//			errorPopupController.isCommitProperty().addListener((obs, wasCommit, isCommit) -> {
//	            if (isCommit) {
//	                popup.close();
//	            }
//	        });
			
//			popup.showAndWait();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("실행 오류");
			alert.setHeaderText("순서도를 실행할 수 없습니다.");
			alert.setContentText(string);

			alert.showAndWait();
//		} catch(IOException e) {
//			e.printStackTrace();
//		}
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
	
	public void showCreateArrayPopup() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/POPCreateArrayLayout.fxml"));
			AnchorPane createArrayPopup = (AnchorPane)loader.load();
			
			popup = new Stage();
			popup.setScene(new Scene(createArrayPopup));
			popup.show();
			
			createArrayController = loader.getController();
			createArrayController.setController(this);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addVariable(String name, POPNodeType type) {
		POPVariableNode varNode = new POPVariableNode(scriptArea, name, type);
		variableArea.getChildren().add(varNode);
		POPVariableManager.createdVars.add(varNode.getName());
		popup.close();
	}
	
	public void addArray(String name) {
		POPArrayNode arrayNode = new POPArrayNode(scriptArea, name);
		POPVariableNode sizeNode = new POPVariableNode(scriptArea, name + "의 크기", POPNodeType.ArraySize);
		variableArea.getChildren().add(arrayNode);
		POPVariableManager.createdVars.add(arrayNode.getName());
		variableArea.getChildren().add(sizeNode);
		POPVariableManager.createdVars.add(sizeNode.getName());
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
		}
		return cb;
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		
		
	}
}
