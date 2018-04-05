package kr.co.idiots.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import kr.co.idiots.MainApp;
import kr.co.idiots.controller.VariableController;
import kr.co.idiots.model.POPProcessNode;
import kr.co.idiots.model.POPScriptArea;
import kr.co.idiots.model.POPStartNode;
import kr.co.idiots.model.POPStopNode;
import kr.co.idiots.model.POPSymbolNode;
import kr.co.idiots.model.POPVariableNode;

public class POPSolvingLayoutController {
	Point2D lastXY = null;
	
	@FXML
	private ImageView node;
	@FXML
	private ScrollPane scrollPane;
	@FXML
	private AnchorPane symbolArea;
	
	@FXML
	private AnchorPane variableArea;
	@FXML
	private ListView variableListView;
	
	@FXML
	private AnchorPane emptyFrame;
	
	private VariableController variables;
	private ObservableList<Group> variableItems;
	
	private POPProcessNode processSymbol;
	
	private MainApp mainApp;
	
	private Bounds frameBounds;
	
	public static POPScriptArea scriptArea;
	
	public POPSolvingLayoutController() {
		
	}
	
	@FXML
	private void initialize() {
		variables = new VariableController();
		variableItems = FXCollections.observableArrayList();
		variableListView.setItems(variableItems);
		
		scriptArea = new POPScriptArea(emptyFrame);
		
		scrollPane.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
			@Override
			public void changed(ObservableValue<? extends Bounds> arg0, Bounds arg1, Bounds arg2) {
				// TODO Auto-generated method stub
				Node content = scrollPane.getContent();
				scrollPane.setFitToWidth(content.prefWidth(-1) < arg2.getWidth());
				scrollPane.setFitToWidth(content.prefHeight(-1) < arg2.getHeight());
			}
		});
		
		processSymbol = new POPProcessNode(scriptArea);
		symbolArea.getChildren().add(processSymbol.getComponent());
		
		POPSymbolNode startNode = new POPStartNode(scriptArea);
		POPSymbolNode stopNode = new POPStopNode(scriptArea);
		
		startNode.getOutFlowLine().setNextNode(stopNode);
		stopNode.setInFlowLine(startNode.getOutFlowLine());
		
		scriptArea.add(startNode);
		scriptArea.add(stopNode);
	}
	
	@FXML
	private void createVariable() {
		char ch = (char) ((Math.random() * 26) + 65);
		String name = String.valueOf(ch);
		Object value = (int) Math.random() * 100;
		variables.addVariable(name, value);
		
		System.out.println(name);
		POPVariableNode varNode = new POPVariableNode(scriptArea, name);
		variableItems.add(varNode.getComponent());
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
