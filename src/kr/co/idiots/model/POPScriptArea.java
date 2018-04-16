package kr.co.idiots.model;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import kr.co.idiots.CodeGenerator;
import kr.co.idiots.MainApp;
import kr.co.idiots.POPFlowchartPlayer;
import kr.co.idiots.model.operation.POPOperationSymbol;
import kr.co.idiots.model.symbol.POPSymbolNode;
import kr.co.idiots.util.DragManager;
import kr.co.idiots.util.POPNodeDataFormat;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPScriptArea {
	private AnchorPane component;
	private ScrollPane scrollPane;
	private POPSymbolNode startNode;
	private POPNode nodePointer;
	private CodeGenerator generator;
	private POPFlowchartPlayer flowchartPlayer;
	
	
	private DoubleProperty zoomScale = new SimpleDoubleProperty(1.0);
	
	public POPScriptArea(AnchorPane component, ScrollPane scrollPane) {
		this.component = component;
		this.scrollPane = scrollPane;
		
		flowchartPlayer = new POPFlowchartPlayer();
		
		generator = new CodeGenerator();
		
		component.scaleXProperty().bind(zoomScale);
		component.scaleYProperty().bind(zoomScale);
		
		scrollPane.addEventFilter(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>() {

			@Override
			public void handle(ScrollEvent event) {
				// TODO Auto-generated method stub
				if(MainApp.pressedKeys.contains(KeyCode.CONTROL)) {
					zoomScale.set(zoomScale.get() + event.getDeltaY() * 0.001);
				}
					
			}

		});
		
		setOnDrag();
	}
	
	private void setOnDrag() {
		scrollPane.setOnDragOver(event -> {
			Dragboard db = event.getDragboard();
			if(db.hasImage()) {
				event.acceptTransferModes(TransferMode.MOVE);
			}
			
			if(scrollPane.getViewportBounds().getWidth() - event.getSceneX() <= 20) {
				scrollPane.setHvalue(scrollPane.getHvalue() + 0.03);
			}
			event.consume();
		});
		
		scrollPane.setOnDragDropped(event -> {
			DragManager.isSynchronized = true;
			Dragboard db = event.getDragboard();
			Node node = null;
			
			boolean success = false;
			
			if(DragManager.dragMoving) {
				node = DragManager.draggedNode;
				component.getChildren().add(node);
				if(DragManager.isAllocatedNode) {
					
					DragManager.isAllocatedNode = false;
				}
				node.setLayoutX((event.getX() - (node.getBoundsInLocal().getWidth() / 2)) / component.getScaleX());
				node.setLayoutY((event.getY() - (node.getBoundsInLocal().getHeight() / 2)) / component.getScaleY());
				
				if(DragManager.draggedNode instanceof POPOperationSymbol) {
					((POPOperationSymbol) DragManager.draggedNode).setParentNode(null);
					((POPOperationSymbol) DragManager.draggedNode).setParentSymbol(null);
				}
				
				if(DragManager.draggedNode instanceof POPVariableNode) {
					((POPVariableNode) DragManager.draggedNode).setParentSymbol(null);
				}
				
				DragManager.dragMoving = false;
				DragManager.draggedNode = null;
				
				success = true;
			} else if(POPNodeType.symbolGroup.contains(Enum.valueOf(POPNodeType.class, db.getString()))) {
				Class<? extends POPSymbolNode> nodeClass = null;
				try {
					nodeClass = (Class<? extends POPSymbolNode>) Class
							.forName("kr.co.idiots.model.symbol.POP" + db.getString() + "Node");
					node = nodeClass.getDeclaredConstructor(POPScriptArea.class).newInstance(this);
				} catch (ClassNotFoundException | IllegalAccessException | InstantiationException | 
						IllegalArgumentException | InvocationTargetException | NoSuchMethodException | 
						SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				((POPSymbolNode) node).initialize();
				node.setLayoutX(event.getX() - (node.getBoundsInLocal().getWidth() / 2));
				node.setLayoutY(event.getY() - (node.getBoundsInLocal().getHeight() / 2));
				component.getChildren().add(node);
				success = true;
			} else if(POPNodeType.operationGroup.contains(Enum.valueOf(POPNodeType.class, db.getString()))) {
				Class<? extends POPOperationSymbol> nodeClass = null;
				try {
					nodeClass = (Class<? extends POPOperationSymbol>) Class
							.forName("kr.co.idiots.model.operation.POP" + db.getString() + "Symbol");
					node = nodeClass.newInstance();
				} catch (ClassNotFoundException | IllegalAccessException | InstantiationException | 
						IllegalArgumentException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				((POPOperationSymbol) node).initialize(null);
				node.setLayoutX(event.getX() - (node.getBoundsInLocal().getWidth() / 2));
				node.setLayoutY(event.getY() - (node.getBoundsInLocal().getHeight() / 2));
				component.getChildren().add(node);
				success = true;
			} else if(POPNodeType.variableGroup.contains(Enum.valueOf(POPNodeType.class, db.getString()))) {
				node = new POPVariableNode(this, 
						(String) db.getContent(POPNodeDataFormat.variableNameFormat),
						(Enum.valueOf(POPNodeType.class, (String) db.getContent(POPNodeDataFormat.variableTypeFormat))));
				((POPVariableNode) node).initialize(null);
				node.setLayoutX(event.getX() - (node.getBoundsInLocal().getWidth() / 2));
				node.setLayoutY(event.getY() - (node.getBoundsInLocal().getHeight() / 2));
				component.getChildren().add(node);
				success = true;
			}
			
			event.setDropCompleted(success);
			event.consume();
		});
		
	}
			
	public void add(POPSymbolNode node) {
		component.getChildren().add(node.getComponent());
		if(node.getOutFlowLine() != null)
			component.getChildren().add(node.getOutFlowLine());
	}
	
	public void remove(POPSymbolNode node) {
		component.getChildren().remove(node);
		if(node.getOutFlowLine() != null)
			component.getChildren().remove(node.getOutFlowLine());
	}

	public void setStartNode(POPSymbolNode startNode) {
		this.startNode = startNode;
		nodePointer = startNode;
	}
	
	public String generate() throws IOException, NoSuchFieldException {
		
		return generator.generate(startNode);
	}
	
	public String play() {
		flowchartPlayer.playNode(startNode);
		return flowchartPlayer.getOutput().toString();
	}
}
