package kr.co.idiots.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import kr.co.idiots.MainApp;
import kr.co.idiots.POPFlowchartPlayer;
import kr.co.idiots.POPNodeFactory;
import kr.co.idiots.POPVariableManager;
import kr.co.idiots.SubNodeIF;
import kr.co.idiots.model.POPArrayNode;
import kr.co.idiots.model.POPBlank;
import kr.co.idiots.model.POPLoggedInMember;
import kr.co.idiots.model.POPNodeType;
import kr.co.idiots.model.POPProblem;
import kr.co.idiots.model.POPScriptArea;
import kr.co.idiots.model.POPVariableNode;
import kr.co.idiots.model.compare.POPIsEqualSymbol;
import kr.co.idiots.model.compare.POPLessThanEqualSymbol;
import kr.co.idiots.model.compare.POPLessThanSymbol;
import kr.co.idiots.model.compare.POPNotEqualSymbol;
import kr.co.idiots.model.operation.POPDivideSymbol;
import kr.co.idiots.model.operation.POPEqualSymbol;
import kr.co.idiots.model.operation.POPLineSymbol;
import kr.co.idiots.model.operation.POPMinusSymbol;
import kr.co.idiots.model.operation.POPMultiplySymbol;
import kr.co.idiots.model.operation.POPOperationSymbol;
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
import kr.co.idiots.util.POPCaptureHelper;
import kr.co.idiots.util.POPPopupManager;
import kr.co.idiots.util.PlatformHelper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPSolvingLayoutController {
	Point2D lastXY = null;
	@FXML private ImageView node;
	@FXML private Group symbolGroup;
	@FXML private AnchorPane operationArea;
	@FXML private FlowPane variableArea;
	@FXML public AnchorPane rootPane;
	@FXML private TabPane tabPane;
	@FXML private AnchorPane scriptPane;
	@FXML private ScrollPane scriptScrollPane;
	@FXML private SplitPane nodeSplitPane;
	@FXML private SplitPane scriptSplitPane;
	
	@FXML private Button btnCreateVariable;
	@FXML private Button btnCreateArray;
	@FXML private Button btnProblemDetail;
	@FXML private Label lbTitle;
	
	private POPFlowchartPlayer flowchartPlayer;
	
	@FXML private Button btnStart;
	
	private POPProblem problem;
	
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
	private POPLineSymbol lineSymbol;
	
	private POPSymbolNode startNode;
	
	private static Stage popup;
	
	public static MainApp mainApp;
	
	private Bounds frameBounds;
	
	public static POPScriptArea scriptArea;
	
	private RootLayoutController rootController;
	
	private int sigCount = 10;
	
	private POPCreateVariableLayoutController createVariableController;
	private POPCreateArrayLayoutController createArrayController;
	
	private POPConsoleLayoutController consoleController;
	private Stage consoleStage;
	
	private POPProblemDetailLayoutController problemDetailController;
	private Stage problemDetailStage;
	
	private POPLoadingLayoutController loadingController;
	
	private Pane splashLayout;
	private ProgressBar loadProgress;
	private Label progressText;
	private static final int SPLASH_WIDTH = 676;
    private static final int SPLASH_HEIGHT = 227;
    
    private double progressVal = 0;
    private double progressMax = 0;
    private double progress = 0;
    private boolean isLoading = true;
    
    private ProgressBar progressBar;
    
    private Task<Void> loadTask;

	public static final Set<KeyCode> pressedKeys = new HashSet<>();
	
	public POPSolvingLayoutController(MainApp mainApp, POPProblem problem) {
		this.mainApp = mainApp;
		this.problem = problem;
		
		mainApp.getPrimaryStage().getScene().setOnKeyPressed(e -> {
			pressedKeys.add(e.getCode());
		});
		mainApp.getPrimaryStage().getScene().setOnKeyReleased(e -> pressedKeys.remove(e.getCode()));
	}
	
	@FXML
	private void initialize() {
		flowchartPlayer = new POPFlowchartPlayer(this);
		
		loadProgress = new ProgressBar();
		loadProgress.setPrefWidth(SPLASH_WIDTH - 20);
		progressText = new Label("Loading...");
		splashLayout = new VBox();
		splashLayout.getChildren().addAll(loadProgress, progressText);
		progressText.setAlignment(Pos.CENTER);
        splashLayout.setStyle(
                "-fx-padding: 5; " +
                "-fx-background-color: cornsilk; " +
                "-fx-border-width:5; " +
                "-fx-border-color: " +
                    "linear-gradient(" +
                        "to bottom, " +
                        "chocolate, " +
                        "derive(chocolate, 50%)" +
                    ");"
        );
        splashLayout.setEffect(new DropShadow());
		
		lbTitle.setText(problem.getTitle());
		
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
							showConsolePopup();
							scriptArea.saveFlowchart(POPLoggedInMember.getInstance().getMember().getId(), problem.getNumber());
							mainApp.getConnector().saveImageByUserIdAndProblemNumber(
									POPLoggedInMember.getInstance().getMember().getId(),
									problem.getNumber(),
									POPCaptureHelper.doSave(mainApp.getPrimaryStage(), scriptArea.getComponent())
							);
						});
					}
				};
				
				thread.setDaemon(true);
				thread.start();
			}
		});
		
		btnProblemDetail.setOnAction(event -> {
			showProblemDetailPopup();
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
		Thread thread = new Thread() {
			@Override
			public void run() {
				PlatformHelper.run(() -> {
					loopSymbol = new POPLoopNode(scriptArea);
					loopSymbol.invisibleSubNodes();
					decisionSymbol = new POPDecisionNode(scriptArea);
					decisionSymbol.invisibleSubNodes();
					
					add(symbolGroup, decisionSymbol);
					decisionSymbol.setLayoutX(decisionSymbol.getLayoutX() + 10);
					decisionSymbol.getComponent().setLayoutY(140);
					
					add(symbolGroup, loopSymbol);
					loopSymbol.setLayoutX(loopSymbol.getLayoutX() + 10);
					loopSymbol.getComponent().setLayoutY(250);
				});
			}
		};
		
		thread.setDaemon(true);
		thread.start();

		plusSymbol = new POPPlusSymbol();
		operationArea.getChildren().add(plusSymbol);
		plusSymbol.setTranslateX(40);
		plusSymbol.setTranslateY(40);
		minusSymbol = new POPMinusSymbol();
		operationArea.getChildren().add(minusSymbol);
		minusSymbol.setTranslateX(140);
		minusSymbol.setTranslateY(40);
		multiplySymbol = new POPMultiplySymbol();
		operationArea.getChildren().add(multiplySymbol);
		multiplySymbol.setTranslateX(40);
		multiplySymbol.setTranslateY(80);
		divideSymbol = new POPDivideSymbol();
		operationArea.getChildren().add(divideSymbol);
		divideSymbol.setTranslateX(140);
		divideSymbol.setTranslateY(80);
		remainderSymbol = new POPRemainderSymbol();
		operationArea.getChildren().add(remainderSymbol);
		remainderSymbol.setTranslateX(40);
		remainderSymbol.setTranslateY(120);
		
		isEqualSymbol = new POPIsEqualSymbol();
		operationArea.getChildren().add(isEqualSymbol);
		isEqualSymbol.setTranslateX(40);
		isEqualSymbol.setTranslateY(200);
		lessThanSymbol = new POPLessThanSymbol();
		operationArea.getChildren().add(lessThanSymbol);
		lessThanSymbol.setTranslateX(140);
		lessThanSymbol.setTranslateY(200);
		lessThanEqualSymbol = new POPLessThanEqualSymbol();
		operationArea.getChildren().add(lessThanEqualSymbol);
		lessThanEqualSymbol.setTranslateX(40);
		lessThanEqualSymbol.setTranslateY(240);
		notEqualSymbol = new POPNotEqualSymbol();
		operationArea.getChildren().add(notEqualSymbol);
		notEqualSymbol.setTranslateX(140);
		notEqualSymbol.setTranslateY(240);
		
		stringPlusSymbol = new POPStringPlusSymbol();
		operationArea.getChildren().add(stringPlusSymbol);
		stringPlusSymbol.setTranslateX(40);
		stringPlusSymbol.setLayoutY(320);
		lineSymbol = new POPLineSymbol();
		operationArea.getChildren().add(lineSymbol);
		lineSymbol.setTranslateX(140);
		lineSymbol.setLayoutY(320);
		
		
		startNode = new POPStartNode(scriptArea);
		POPSymbolNode stopNode = new POPStopNode(scriptArea);
		
		startNode.getOutFlowLine().setNextNode(stopNode);
		startNode.getOutFlowLine().setStartNode((POPStartNode) startNode); 
		stopNode.setParentNode(startNode);

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

			double absProblemDividerPos = scriptSplitPane.getDividerPositions()[0] * oldVal.doubleValue();
			scriptSplitPane.setDividerPosition(0, absProblemDividerPos / newVal.doubleValue());
		});
		
		String content = mainApp.getConnector().loadFlowchartByUserIdAndProblemNumber(POPLoggedInMember.getInstance().getMember().getId(), problem.getNumber());

		Thread loadThread = new Thread() {
			@Override
			public void run() {
				PlatformHelper.run(() -> {
					initVariables();
					loadFlowchart(content);
					Thread visibleThread = new Thread() {
						@Override
						public void run() {
							PlatformHelper.run(() -> {
								loopSymbol.visibleSubNodes();
								decisionSymbol.visibleSubNodes();
							});
						}
					};

					visibleThread.setDaemon(true);
					visibleThread.start();
				});
			}
		};
		
		loadThread.setDaemon(true);
		loadThread.start();

		Thread scaleThread = new Thread() {
			@Override
			public void run() {
				PlatformHelper.run(() -> {
					scriptArea.getZoomScale().set(0.8);
				});
			}
		};

		scaleThread.setDaemon(true);
		scaleThread.start();
	}
	
	public interface InitCompletionHandler {
		void complete();
	}
	
	public void loadFlowchart(String content) {
		int count = sigCount;
		
		this.loadFlowchartSymbol(startNode, new StringBuilder(content), new String(new char[count]).replace("\0", ":"));
	}
	
	public String loadFlowchartSymbol(POPSymbolNode currNode, StringBuilder content, String sig) {
		String resContent = content.toString();
		String[] symbolNode = content.toString().split(sig);
		for(String str : symbolNode) {
			
			if(str.equals("Start") || str.equals("DecisionStart") || str.equals("LoopStart")) {
				resContent = resContent.substring(str.length() + sig.length());
			} else if(str.equals("Stop") || str.equals("DecisionEnd") || str.equals("LoopEnd")) {
				if(resContent.contains(sig))
					resContent = resContent.substring(str.length() + sig.length());
				else
					resContent = resContent.substring(str.length());
				break;
			} else {
				if(resContent.contains(sig))
					resContent = resContent.substring(str.length() + sig.length());
				else
					resContent = resContent.substring(str.length());
				
				POPSymbolNode node = null;
				node = (POPSymbolNode) POPNodeFactory.createPOPNode(str.split("\\(")[0]);
				
				loadSymbolNode(currNode, node, str.split("\\(")[0], str);
				currNode = node;
			}
		}
		return resContent;
	}
	
	public String loadSymbolNode(POPSymbolNode prevNode, POPSymbolNode node, String typeName, String content) {
		content = content.substring(typeName.length() + 1);
		
		node.initialize();
		if(!(prevNode instanceof POPStopNode))
			prevNode.getOutFlowLine().insertNode(node, 2);
		if(node instanceof POPLoopNode) {
			((POPLoopNode) node).adjustPosition();
		}
		else if(node instanceof POPDecisionNode)
			((POPDecisionNode) node).adjustPosition();
		scriptArea.addWithOutFlowLine(node);
		
		POPOperationSymbol symbol = node.getRootSymbol();
		
		content = loadOperationSymbol(symbol, 0, content);
		
		if(node instanceof POPDecisionNode) {
			int count = sigCount;
				content = content.substring(count);

			sigCount -= 1;
			count = sigCount;
			content = loadFlowchartSymbol(((POPDecisionNode) node).getLeftStartNode(), new StringBuilder(content), new String(new char[count]).replace("\0", ":"));
			content = loadFlowchartSymbol(((POPDecisionNode) node).getRightStartNode(), new StringBuilder(content), new String(new char[count]).replace("\0", ":"));
		}
		
		if(node instanceof POPLoopNode) {
			int count = sigCount;
			content = content.substring(count);
			
			sigCount -= 1;
			count = sigCount;
			content = loadFlowchartSymbol(((POPLoopNode) node).getLoopStartNode(), new StringBuilder(content), new String(new char[count]).replace("\0", ":"));
		}
		
		return content;
	}
	
	public String loadOperationSymbol(POPOperationSymbol rootSymbol, int index, String content) {
		String type = content.split("\\(")[0];
		content = content.split("\\(", 2)[1];
		
		POPOperationSymbol symbol = null;
		
		if(!type.equals("Equal") && !type.equals("Compare") && !type.equals("Output") && rootSymbol.getParentArrayNode() == null) {
			symbol = (POPOperationSymbol) POPNodeFactory.createNode(type, null, null);
			((POPBlank) rootSymbol.getContents().getChildren().get(index)).insertNode(symbol);
		} else {
			symbol = rootSymbol;
		}
		
		if(content.split("\\(")[0].equals("Blank")) {
			((POPBlank) symbol.getContents().getChildren().get(0)).setText(content.split("\\('")[1].split("'\\)")[0]);
			content = content.split("\\('", 2)[1].split("'\\)", 2)[1];
		} else if(content.split("\\(")[0].equals("Arr"))  {
			content = content.split("\\('", 2)[1];
			String arrName = content.split("', ")[0];
			POPArrayNode array = (POPArrayNode) POPNodeFactory.createNode("Array", arrName, "Array");
			if(symbol.getParentSymbol() instanceof POPEqualSymbol) {
				if(!array.getIndexBlank().getOptions().contains("추가"))
					array.getIndexBlank().getOptions().add("추가");
			} else {
				array.getIndexBlank().getOptions().remove("추가");
			}
			((POPBlank) symbol.getContents().getChildren().get(0)).insertNode(array);
			content = content.split("', ", 2)[1];
			if(content.split("\\(")[0].equals("Blank")) {
				array.getIndexBlank().getEditor().setText(content.split("\\('")[1].split("'\\)")[0]);
				content = content.split("\\('", 2)[1].split("'\\)", 2)[1];
			} else if(content.split("\\(")[0].equals("Var")) {
				content = content.split("\\('", 2)[1];
				String varName = content.split("'\\)")[0];
				content = content.split("'\\)", 2)[1];
				POPVariableNode variable = (POPVariableNode) POPNodeFactory.createNode("IntegerVariable", varName, "IntegerVariable");
				array.getIndexBlank().insertNode(variable);

				if(!POPVariableManager.createdVars.contains(variable.getName())) {
					addVariable(variable.getName(), variable.getType(), "");
				}
			} else {
				String type2 = content.split("\\(")[0];
				POPOperationSymbol symbol2 = (POPOperationSymbol) POPNodeFactory.createNode(type2, null, null);
				array.getIndexBlank().insertNode(symbol2);
				content = loadOperationSymbol(symbol2, 0, content);
				
			}
			content = content.split("\\)", 2)[1];

			if(!POPVariableManager.createdVars.contains(array.getName())) {
				addArray(array.getName());
			}
		} else if(content.split("\\(")[0].equals("Var")) {
			content = content.split("\\('", 2)[1];
			String varName = content.split("'\\)")[0];
			content = content.split("'\\)", 2)[1];
			POPVariableNode variable = null;
			if(varName.contains("의 크기")) {
				variable = new POPVariableNode(scriptArea, varName, POPNodeType.ArraySize);
			} else {
				variable = (POPVariableNode) POPNodeFactory.createNode("IntegerVariable", varName, "IntegerVariable");
			}
			((POPBlank) symbol.getContents().getChildren().get(0)).insertNode(variable);

			if(!POPVariableManager.createdVars.contains(variable.getName())) {
				addVariable(variable.getName(), variable.getType(), "");
			}
		} else if(content.split("\\(")[0].equals("Line")) {
			POPLineSymbol lineSymbol = (POPLineSymbol) POPNodeFactory.createNode("Line", null, null);
			((POPBlank) symbol.getContents().getChildren().get(0)).insertNode(lineSymbol);
			content = content.split("\\)", 2)[1];
		} else {
			content = loadOperationSymbol(symbol, 0, content);
		}
		
		if(content.charAt(0) == ',') {
			content = content.substring(2);
		} else {
			content = content.substring(1);
			return content;
		}
		
		if(content.split("\\(")[0].equals("Blank")) {
			((POPBlank) symbol.getContents().getChildren().get(2)).setText(content.split("\\('")[1].split("'\\)")[0]);
			content = content.split("\\('", 2)[1].split("'\\)", 2)[1].substring(1);
		} else if(content.split("\\(")[0].equals("Arr"))  {
			content = content.split("\\('", 2)[1];
			String arrName = content.split("', ")[0];
			POPArrayNode array = (POPArrayNode) POPNodeFactory.createNode("Array", arrName, "Array");
			if(symbol.getParentSymbol() instanceof POPEqualSymbol) {
				if(!array.getIndexBlank().getOptions().contains("추가"))
					array.getIndexBlank().getOptions().add("추가");
			} else {
				array.getIndexBlank().getOptions().remove("추가");
			}
			((POPBlank) symbol.getContents().getChildren().get(2)).insertNode(array);
			content = content.split("', ", 2)[1];
			if(content.split("\\(")[0].equals("Blank")) {
				array.getIndexBlank().getEditor().setText(content.split("\\('")[1].split("'\\)")[0]);
				content = content.split("\\('", 2)[1].split("'\\)", 2)[1];
			} else if(content.split("\\(")[0].equals("Var")) {
				content = content.split("\\('", 2)[1];
				String varName = content.split("'\\)")[0];
				content = content.split("'\\)", 2)[1];
				POPVariableNode variable = (POPVariableNode) POPNodeFactory.createNode("IntegerVariable", varName, "IntegerVariable");
				array.getIndexBlank().insertNode(variable);
				
				if(!POPVariableManager.createdVars.contains(variable.getName())) {
					addVariable(variable.getName(), variable.getType(), "");
				}
			} else {
				String type2 = content.split("\\(")[0];
				POPOperationSymbol symbol2 = (POPOperationSymbol) POPNodeFactory.createNode(type2, null, null);
				array.getIndexBlank().insertNode(symbol2);
				content = loadOperationSymbol(symbol2, 0, content);
			}
			content = content.split("\\)", 2)[1].substring(1);
			
			if(!POPVariableManager.createdVars.contains(array.getName())) {
				addArray(array.getName());
			}
			
		} else if(content.split("\\(")[0].equals("Var")) {
			content = content.split("\\('", 2)[1];
			String varName = content.split("'\\)")[0];
			content = content.split("'\\)", 2)[1].substring(1);
			POPVariableNode variable = null;
			if(varName.contains("의 크기")) {
				variable = new POPVariableNode(scriptArea, varName, POPNodeType.ArraySize);
			} else {
				variable = (POPVariableNode) POPNodeFactory.createNode("IntegerVariable", varName, "IntegerVariable");
			}
			((POPBlank) symbol.getContents().getChildren().get(2)).insertNode(variable);
			
			if(!POPVariableManager.createdVars.contains(variable.getName())) {
				addVariable(variable.getName(), variable.getType(), "");
			}
		} else if(content.split("\\(")[0].equals("Line")) {
			POPLineSymbol lineSymbol = (POPLineSymbol) POPNodeFactory.createNode("Line", null, null);
			((POPBlank) symbol.getContents().getChildren().get(2)).insertNode(lineSymbol);
			content = content.split("\\)", 2)[1].substring(1);
		} else {
			content = loadOperationSymbol(symbol, 2, content).substring(1);
		}
		
		return content;
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
	
	public void initVariables() {
		POPVariableManager.initVars = new ArrayList<>();
		POPVariableManager.createdVars = new ArrayList<>();
		String content = problem.getInputCase();
		
		content = content.split(";;")[0];
		
		for(String str : content.split("[\\r\\n]+")) {
			if(str.split("\\(")[0].equals("Var")) {
				POPVariableManager.initVars.add(str.split("Var\\(")[1].split("\\(")[0].toString());
				addVariable(str.split("\\(")[1].toString(), POPNodeType.IntegerVariable, str.split("Var\\(")[1].split("\\(")[1].split("\\)\\)")[0].toString());
			} else if(str.split("\\(")[0].equals("Arr")) {
				POPVariableManager.initVars.add(str.split("\\(")[1].toString());
				POPVariableManager.initVars.add(str.split("\\(")[1].toString() + "의 크기");
				addArray(str.split("\\(")[1].toString());
			}
		}
	}
	
	public void initVariableValues(int index) {
		
		String content = problem.getInputCase();
		content = content.split(";;")[index];

		for(Node box : variableArea.getChildren()) {
			if(box instanceof HBox) {
				Node node = ((HBox) box).getChildren().get(0);
				if(node instanceof POPArrayNode) {

					POPArrayNode array = (POPArrayNode) node;
					if(!POPVariableManager.declaredArrs.containsKey(array.getName())) {
						ArrayList<Object> list = new ArrayList<>();
						POPVariableManager.declaredArrs.put(array.getName(), list);
						POPVariableManager.declaredVars.put(array.getName() + "의 크기", "0");
					}
					
					String str = content;
					while(str.contains("(" + array.getName() + "(")) {
						POPVariableManager.declaredArrs.get(array.getName()).add(str.split("\\(" + array.getName() + "\\(")[1].split("\\)")[0]);
						POPVariableManager.declaredVars.put(array.getName() + "의 크기", String.valueOf(Integer.parseInt(POPVariableManager.declaredVars.get(array.getName() + "의 크기")) + 1));
						str = str.split("\\(" + array.getName() + "\\(", 2)[1].split("\\)", 2)[1];
					}
					
				} else if(node instanceof POPVariableNode) {
					if(content.contains("(" + ((POPVariableNode) node).getName() + "(")) {
						POPVariableManager.declaredVars.put(((POPVariableNode) node).getName(), content.split("\\(" + ((POPVariableNode) node).getName() + "\\(")[1].split("\\)")[0]);
					}
				}
			}
		}
	}
	
	public void checkAnswer() {
		
		String answer;
		String output;
		int i = 0;
		answer = problem.getOutputCase();
		
		String[] answers = answer.split(";;");
		
		for(String str : answers) {
			output = scriptArea.play(i);
			
			str = str.replaceAll("(\\r\\n|\\r|\\n|\\n\\r)", " ");
			output = output.replaceAll("(\\r\\n|\\r|\\n|\\n\\r)", " ");
			
			if(!str.trim().equals(output.trim())) {
				
				POPPopupManager.showAlertPopup("정답 확인", "정답 확인", "틀렸습니다!", AlertType.ERROR);
				return;
			}
			i += 1;
		}
		
		POPPopupManager.showAlertPopup("정답 확인", "정답 확인", "정답입니다!", AlertType.INFORMATION);
		mainApp.getConnector().setSolved(POPLoggedInMember.getInstance().getMember().getId(), problem.getNumber(), true);
	}
	
	public void showConsolePopup() {
		try {
			String output = "";
			
			if(consoleController == null)
				consoleController = new POPConsoleLayoutController(this);
			
			if(consoleStage == null || !consoleStage.isShowing()) {
				
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(MainApp.class.getResource("view/POPConsoleLayout.fxml"));
				loader.setControllerFactory(c -> {
					return consoleController;
				});
				AnchorPane consolePane = (AnchorPane)loader.load();
				
				consoleStage = new Stage();
				consoleStage.setTitle("결과");
				consoleStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/favicon.png")));
				consoleStage.setScene(new Scene(consolePane));
				try {
					output = scriptArea.play(0);
				} catch (NullPointerException | NumberFormatException | IndexOutOfBoundsException e) {
					return;
				}
				
				consoleStage.show();
				
				consoleController.setController(this);
				consoleController.setOutput(output);
			} else {
				consoleStage.requestFocus();
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showProblemDetailPopup() {
		try {
			if(problemDetailController == null)
				problemDetailController = new POPProblemDetailLayoutController();
			
			if(problemDetailStage == null || !problemDetailStage.isShowing()) {
				
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(MainApp.class.getResource("view/POPProblemDetailLayout.fxml"));
				loader.setControllerFactory(c -> {
					return problemDetailController;
				});
				AnchorPane problemDetailPane = (AnchorPane)loader.load();
				
				problemDetailStage = new Stage();
				problemDetailStage.setTitle("문제 자세히 보기");
				problemDetailStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/favicon.png")));
				problemDetailStage.setScene(new Scene(problemDetailPane));
				problemDetailStage.show();
				
				problemDetailController.getTxtContent().setText(problem.getContent());
				problemDetailController.getTxtInputExample().setText(problem.getInputExample());
				problemDetailController.getTxtOutputExample().setText(problem.getOutputExample());
			} else {
				problemDetailStage.requestFocus();
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showCreateVariablePopup() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/POPCreateVariableLayout.fxml"));
			AnchorPane createVariablePopup = (AnchorPane)loader.load();
			
			popup = new Stage();
			popup.setTitle("변수 생성");
			popup.getIcons().add(new Image(getClass().getResourceAsStream("/images/favicon.png")));
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
			popup.setTitle("배열 생성");
			popup.getIcons().add(new Image(getClass().getResourceAsStream("/images/favicon.png")));
			popup.setScene(new Scene(createArrayPopup));
			popup.show();
			
			createArrayController = loader.getController();
			createArrayController.setController(this);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addVariable(String name, POPNodeType type, String value) {
		if(!POPVariableManager.createdVars.contains(name)) {
			POPVariableNode varNode = new POPVariableNode(scriptArea, name, type);
			if(POPVariableManager.initVars.contains(name)) {
				HBox box = new HBox();
				
				box.getChildren().add(varNode);
				varNode.getLbDefault().setText(" : " + value);
				box.getChildren().add(varNode.getLbDefault());
				box.setSpacing(10);
				box.setAlignment(Pos.CENTER);
				variableArea.getChildren().add(box);
			} else {
				variableArea.getChildren().add(varNode);
			}
			
			POPVariableManager.createdVars.add(varNode.getName());
		}
		
		if(popup != null)
			popup.close();
	}
	
	public void addArray(String name) {
		if(!POPVariableManager.createdVars.contains(name)) {
			POPArrayNode arrayNode = new POPArrayNode(scriptArea, name);
			POPVariableNode sizeNode = new POPVariableNode(scriptArea, name + "의 크기", POPNodeType.ArraySize);
			if(POPVariableManager.initVars.contains(name)) {
				HBox arrayBox = new HBox();
				HBox sizeBox = new HBox();

				arrayBox.getChildren().add(arrayNode);
				arrayBox.setAlignment(Pos.CENTER);
				sizeBox.getChildren().add(sizeNode);
				sizeBox.setAlignment(Pos.CENTER);
				variableArea.getChildren().add(arrayBox);
				variableArea.getChildren().add(sizeBox);
			} else {
				variableArea.getChildren().add(arrayNode);
				variableArea.getChildren().add(sizeNode);
			}
			POPVariableManager.createdVars.add(arrayNode.getName());
			POPVariableManager.createdVars.add(sizeNode.getName());
		}
		
		if(popup != null)
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
