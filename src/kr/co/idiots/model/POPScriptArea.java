package kr.co.idiots.model;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
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
import kr.co.idiots.model.symbol.POPDecisionNode;
import kr.co.idiots.model.symbol.POPSymbolNode;
import kr.co.idiots.util.DragManager;
import kr.co.idiots.util.POPNodeDataFormat;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPScriptArea {
	private AnchorPane pane;
	private ScrollPane scrollPane;
	private POPSymbolNode startNode;
	private double centerXOfStartNode = 50;
	private POPNode nodePointer;
	private CodeGenerator generator;
	private POPFlowchartPlayer flowchartPlayer;
	private Group component;
	private boolean isSynchronized = false;
	
	private double lastMouseX;
	private double lastMouseY;
	
	private DoubleProperty zoomScale = new SimpleDoubleProperty(1.0);
	
	public POPScriptArea(AnchorPane pane, ScrollPane scrollPane) {
		this.pane = pane;
		this.scrollPane = scrollPane;
		
		component = new Group();
		pane.getChildren().add(component);
		
		flowchartPlayer = new POPFlowchartPlayer();
		
		generator = new CodeGenerator();
		
		pane.scaleXProperty().bind(zoomScale);
		pane.scaleYProperty().bind(zoomScale);
		
		component.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {

			@Override
			public void changed(ObservableValue<? extends Bounds> arg0, Bounds arg1, Bounds newBound) {
				// TODO Auto-generated method stub
				if(isSynchronized) {
					return;
				}
				
				if(newBound.getMinX() < 50) {
//					moveFlowchart(component, 5);
					component.setTranslateX(component.getTranslateX() + 5);
					centerXOfStartNode += 5;
					
				} else if(newBound.getMinX() >= 55){
					component.setTranslateX(component.getTranslateX() - 5);
					centerXOfStartNode -= 5;
				}
				
			}
			
		});
		
		scrollPane.addEventFilter(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>() {

			@Override
			public void handle(ScrollEvent event) {
				// TODO Auto-generated method stub
				if(MainApp.pressedKeys.contains(KeyCode.CONTROL)) {
					if(event.getDeltaY() > 0) {
						zoomScale.set(zoomScale.get() + 0.1);
					} else if(event.getDeltaY() < 0) {
						zoomScale.set(zoomScale.get() - 0.1);
					}
					event.consume();
				}
					
			}

		});
		
		
		scrollPane.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {

			@Override
			public void changed(ObservableValue<? extends Bounds> arg0, Bounds oldBound, Bounds newBound) {
				// TODO Auto-generated method stub
				pane.setPrefSize(scrollPane.getWidth(), scrollPane.getHeight());
				
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
			DragManager.lastCenterXOfStartNode = centerXOfStartNode;
//			System.out.println(DragManager.lastCenterXOfStartNode);
			event.consume();
		});
		
		scrollPane.setOnDragDropped(event -> {
//			DragManager.isSynchronized = true;
			Dragboard db = event.getDragboard();
			Node node = null;
			
			boolean success = false;
			
			if(DragManager.dragMoving) {
				if(!(DragManager.draggedNode instanceof POPDecisionNode)) {
					DragManager.isSynchronized = true;
				}
					
				node = DragManager.draggedNode;
				
				if(DragManager.isAllocatedNode) {
					
					DragManager.isAllocatedNode = false;
				}
				
//				if(node instanceof POPDecisionNode) {
//					component.getChildren().add(((POPDecisionNode) node).getContents());
//					
//					Group contents;
//					contents = ((POPDecisionNode) node).getContents();
//					
//					if((event.getX() - ((contents.getBoundsInLocal().getWidth() * pane.getScaleX()) / 2)) / pane.getScaleX() < 0) {
//						contents.setLayoutX(0);
//					} else {
//						contents.setLayoutX((event.getX() - ((contents.getBoundsInLocal().getWidth() * pane.getScaleX()) / 2)) / pane.getScaleX() - (centerXOfStartNode - 50));
//					}
//					contents.setLayoutY((event.getY() - ((contents.getBoundsInLocal().getHeight() * pane.getScaleY()) / 2)) / pane.getScaleY());
//				} else {
				
				if(node instanceof POPDecisionNode) {
					component.getChildren().add(node);
					for(Node subNode : ((POPDecisionNode) node).getSubNodes()) {
						component.getChildren().add(subNode);
						if(subNode instanceof POPSymbolNode) {
							component.getChildren().add(((POPSymbolNode) subNode).getOutFlowLine());
						}
					}
				} else {
					component.getChildren().add(node);
				}
				
				
				
				if((event.getX() - ((node.getBoundsInLocal().getWidth() * pane.getScaleX()) / 2)) / pane.getScaleX() < 0) {
					node.setLayoutX(0);
				} else {
					
					node.setLayoutX((event.getX() - ((node.getBoundsInLocal().getWidth() * pane.getScaleX()) / 2)) / pane.getScaleX() - (DragManager.lastCenterXOfStartNode - 50));
				}
				node.setLayoutY((event.getY() - ((node.getBoundsInLocal().getHeight() * pane.getScaleY()) / 2)) / pane.getScaleY());
//				}
				
				if(DragManager.draggedNode instanceof POPOperationSymbol) {
					((POPOperationSymbol) DragManager.draggedNode).setParentNode(null);
					((POPOperationSymbol) DragManager.draggedNode).setParentSymbol(null);
				}
				
				if(DragManager.draggedNode instanceof POPVariableNode) {
					((POPVariableNode) DragManager.draggedNode).setParentSymbol(null);
				}
				
//				if(DragManager.draggedNode instanceof POPDecisionNode) {
//					((POPDecisionNode) node).adjustPosition();
//				}
				
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
				
//				if(node instanceof POPDecisionNode) {
//					Group contents;
//					contents = ((POPDecisionNode) node).getContents();
//					
//					if((event.getX() - ((contents.getBoundsInLocal().getWidth() * pane.getScaleX()) / 2)) / pane.getScaleX() < 0) {
//						contents.setLayoutX(0);
//					} else {
//						contents.setLayoutX((event.getX() - ((contents.getBoundsInLocal().getWidth() * pane.getScaleX()) / 2)) / pane.getScaleX() - (centerXOfStartNode - 50));
//					}
//					contents.setLayoutY((event.getY() - ((contents.getBoundsInLocal().getHeight() * pane.getScaleY()) / 2)) / pane.getScaleY());
//				} else {
					if((event.getX() - ((node.getBoundsInLocal().getWidth() * pane.getScaleX()) / 2)) / pane.getScaleX() < 0) {
						node.setLayoutX(0);
					} else {
						node.setLayoutX((event.getX() - ((node.getBoundsInLocal().getWidth() * pane.getScaleX()) / 2)) / pane.getScaleX() - (DragManager.lastCenterXOfStartNode - 50));
					}
					node.setLayoutY((event.getY() - ((node.getBoundsInLocal().getHeight() * pane.getScaleY()) / 2)) / pane.getScaleY());
//				}
				
				
				
				((POPSymbolNode) node).initialize();
				
				
				if(node instanceof POPDecisionNode) {
					
					component.getChildren().add(node);
					for(Node subNode : ((POPDecisionNode) node).getSubNodes()) {
						component.getChildren().add(subNode);
						if(subNode instanceof POPSymbolNode) {
							component.getChildren().add(((POPSymbolNode) subNode).getOutFlowLine());
							
						}
					}
				} else {
					component.getChildren().add(node);
				}
				
//				if(node instanceof POPDecisionNode) {
//					component.getChildren().add(((POPDecisionNode) node).getLeftFlowLine());
//					component.getChildren().add(((POPDecisionNode) node).getRightFlowLine());
//					component.getChildren().add(((POPDecisionNode) node).getLeftSubNode());
//					component.getChildren().add(((POPDecisionNode) node).getRightSubNode());
//				}
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
				
				if((event.getX() - ((node.getBoundsInLocal().getWidth() * pane.getScaleX()) / 2)) / pane.getScaleX() < 0) {
					node.setLayoutX(0);
				} else {
					node.setLayoutX((event.getX() - ((node.getBoundsInLocal().getWidth() * pane.getScaleX()) / 2)) / pane.getScaleX() - (DragManager.lastCenterXOfStartNode - 50));
				}
				node.setLayoutY((event.getY() - ((node.getBoundsInLocal().getHeight() * pane.getScaleY()) / 2)) / pane.getScaleY());
				
				component.getChildren().add(node);
				success = true;
			} else if(POPNodeType.variableGroup.contains(Enum.valueOf(POPNodeType.class, db.getString()))) {
				node = new POPVariableNode(this, 
						(String) db.getContent(POPNodeDataFormat.variableNameFormat),
						(Enum.valueOf(POPNodeType.class, (String) db.getContent(POPNodeDataFormat.variableTypeFormat))));
				((POPVariableNode) node).initialize(null);
				
				if((event.getX() - ((node.getBoundsInLocal().getWidth() * pane.getScaleX()) / 2)) / pane.getScaleX() < 0) {
					node.setLayoutX(0);
				} else {
					node.setLayoutX((event.getX() - ((node.getBoundsInLocal().getWidth() * pane.getScaleX()) / 2)) / pane.getScaleX() - (DragManager.lastCenterXOfStartNode - 50));
				}
				node.setLayoutY((event.getY() - ((node.getBoundsInLocal().getHeight() * pane.getScaleY()) / 2)) / pane.getScaleY());
				
				component.getChildren().add(node);
				success = true;
			}
			
			event.setDropCompleted(success);
			event.consume();
		});
		
	}
			
	public void add(POPSymbolNode node) {
		if(node instanceof POPDecisionNode) {
			component.getChildren().add(node);
			for(Node subNode : ((POPDecisionNode) node).getSubNodes()) {
				component.getChildren().add(subNode);
				if(subNode instanceof POPSymbolNode) {
					component.getChildren().add(((POPSymbolNode) subNode).getOutFlowLine());
				}
			}
		} else {
			component.getChildren().add(node);
		}
		
		if(node.getOutFlowLine() != null)
			component.getChildren().add(node.getOutFlowLine());
		
	}
	
	public void remove(POPSymbolNode node) {
		if(node instanceof POPDecisionNode) {
			component.getChildren().remove(node);
			for(Node subNode : ((POPDecisionNode) node).getSubNodes()) {
				component.getChildren().remove(subNode);
				if(subNode instanceof POPSymbolNode) {
					component.getChildren().remove(((POPSymbolNode) subNode).getOutFlowLine());
				}
			}
		} else {
			component.getChildren().remove(node);
		}
		
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
