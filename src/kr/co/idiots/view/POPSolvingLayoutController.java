package kr.co.idiots.view;

import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import kr.co.idiots.MainApp;
import kr.co.idiots.model.POPDocumentNode;
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
	
	private Button btnStart;
	
	private ObservableList<StackPane> variableItems;
	
	private POPProcessNode processSymbol;
	private POPDocumentNode documentSymbol;
	
	private MainApp mainApp;
	
	private Bounds frameBounds;
	
	public static POPScriptArea scriptArea;
	
	public POPSolvingLayoutController() {
		
	}
	
	@FXML
	private void initialize() {
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
		
		btnStart = new Button("실행");
		btnStart.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					scriptArea.generate();
				} catch (IOException | NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		
		processSymbol = new POPProcessNode(scriptArea);
		symbolArea.getChildren().add(processSymbol.getComponent());
		documentSymbol = new POPDocumentNode(scriptArea);
		symbolArea.getChildren().add(documentSymbol.getComponent());
		documentSymbol.getComponent().setLayoutY(100);
		
		POPSymbolNode startNode = new POPStartNode(scriptArea);
		POPSymbolNode stopNode = new POPStopNode(scriptArea);
		
		startNode.getOutFlowLine().setNextNode(stopNode);
		stopNode.setInFlowLine(startNode.getOutFlowLine());
		
		scriptArea.setStartNode(startNode);
		scriptArea.add(startNode);
		scriptArea.add(stopNode);
		scriptArea.getComponent().getChildren().add(btnStart);
	}
	
	@FXML
	private void createVariable() {
		char ch = (char) ((Math.random() * 26) + 65);
		String name = String.valueOf(ch);
		Object value = (int) Math.random() * 100;
		
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
