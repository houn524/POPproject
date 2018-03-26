package kr.co.idiots.view;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import kr.co.idiots.MainApp;
import kr.co.idiots.model.POPNode;
import kr.co.idiots.model.POPScriptArea;
import kr.co.idiots.model.POPStartNode;
import kr.co.idiots.model.POPStopNode;

public class POPMainLayoutController {
	Point2D lastXY = null;
	
	@FXML
	private ImageView node;
	@FXML
	private AnchorPane frame;
	@FXML
	private Pane emptyFrame;
	
	private MainApp mainApp;
	
	private Bounds frameBounds;
	
	private POPScriptArea scriptArea;
	
	public POPMainLayoutController() {
		
	}
	
	@FXML
	private void initialize() {
		scriptArea = new POPScriptArea(emptyFrame);
		
		node.setOnMouseDragged(event -> {
//			frameBounds = frame.getBoundsInParent();
			System.out.println("Move");
//			event.setDragDetect(false);
//			Node on = (Node)event.getTarget();
//			if(lastXY == null) {
//				lastXY = new Point2D(event.getSceneX(), event.getSceneY());
//			}
//			double dx = event.getSceneX() - lastXY.getX();
//			double dy = event.getSceneY() - lastXY.getY();
//			on.setTranslateX(on.getTranslateX() + dx);
//			on.setTranslateY(on.getTranslateY() + dy);
//			lastXY = new Point2D(event.getSceneX(), event.getSceneY());
			
//			System.out.println(frameBounds);
//			System.out.println(event.getSceneX());
//			System.out.println(event.getSceneY());
//			if(!frameBounds.intersects(event.getSceneX(), event.getSceneY(), 1, 1)) event.setDragDetect(true);
			event.setDragDetect(true);
			event.consume();
		});
		
		node.setOnDragDetected(event -> {
			System.out.println("Drag: " + event);
			Node on = (Node)event.getTarget();
			Dragboard db = on.startDragAndDrop(TransferMode.COPY);
			ClipboardContent content = new ClipboardContent();
			content.putImage(node.getImage());
			db.setContent(content);
//			db.setContent(makeClipboardContent(event, on, null));
			event.consume();
		});
		
		scriptArea.getComponent().setOnDragOver(event -> {
			Dragboard db = event.getDragboard();
			if(db.hasImage()) {
				event.acceptTransferModes(TransferMode.COPY);
			}
		});
		
		scriptArea.getComponent().setOnDragDropped(event -> {
			Dragboard db = event.getDragboard();
			boolean success = false;
			if(db.hasImage()) {
				ImageView imgView = new ImageView();
				imgView.setImage(db.getImage());
				emptyFrame.getChildren().add(imgView);
				
				event.consume();
			}
		});
		
		node.setOnDragDropped(event -> {
			Dragboard db = event.getDragboard();
			boolean success = false;
			if(db.hasImage()) {
				ImageView imgView = new ImageView();
				imgView.setImage(db.getImage());
				emptyFrame.getChildren().add(imgView);
			}
			
			event.consume();
		});
		
		node.setOnMouseReleased(d -> lastXY = null);
		
		/*
			Start, Stop 노드 생성
		*/
		POPNode startNode = new POPStartNode(scriptArea);
		POPNode stopNode = new POPStopNode(scriptArea);
		
		startNode.setNextNode(stopNode);
		
		scriptArea.add(startNode);
		scriptArea.add(stopNode);
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
