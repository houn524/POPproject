package kr.co.idiots.model;

import java.io.IOException;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import kr.co.idiots.CodeGenerator;
import kr.co.idiots.MainApp;
import kr.co.idiots.POPFlowchartPlayer;
import kr.co.idiots.POPNodeFactory;
import kr.co.idiots.SubNodeIF;
import kr.co.idiots.model.operation.POPOperationSymbol;
import kr.co.idiots.model.symbol.POPDecisionEndNode;
import kr.co.idiots.model.symbol.POPDecisionNode;
import kr.co.idiots.model.symbol.POPLoopEndNode;
import kr.co.idiots.model.symbol.POPLoopNode;
import kr.co.idiots.model.symbol.POPSymbolNode;
import kr.co.idiots.util.DragManager;
import kr.co.idiots.util.POPNodeDataFormat;
import kr.co.idiots.util.PlatformHelper;
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
				pane.setPrefSize(scrollPane.getWidth() - 2, scrollPane.getHeight() - 2);
				
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
			
			DragManager.lastCenterXOfStartNode = centerXOfStartNode;
			event.consume();
		});
		
		scrollPane.setOnDragDropped(event -> {
			
//			DragManager.isSynchronized = true;
			Dragboard db = event.getDragboard();
//			
			String clsName = db.getString();
			String varName = null;
			String varTypeName = null;
			if(db.hasContent(POPNodeDataFormat.variableNameFormat))
				varName = db.getContent(POPNodeDataFormat.variableNameFormat).toString();
			if(db.hasContent(POPNodeDataFormat.variableTypeFormat))
				varTypeName = db.getContent(POPNodeDataFormat.variableTypeFormat).toString();
			
			
			Node node = null;
			boolean success = false;
			
			if(DragManager.dragMoving) {
				if(!(DragManager.draggedNode instanceof POPDecisionNode) && !(DragManager.draggedNode instanceof POPLoopNode)) {
					DragManager.isSynchronized = true;
				}
					
				node = DragManager.draggedNode;
				
				if(DragManager.isAllocatedNode) {
					
					DragManager.isAllocatedNode = false;
				}
				
//				locateNode(event, node);
//				add(node);
				locateAndAddNode(event, node);
				
				if(DragManager.draggedNode instanceof POPOperationSymbol) {
					((POPOperationSymbol) DragManager.draggedNode).setParentNode(null);
					((POPOperationSymbol) DragManager.draggedNode).setParentSymbol(null);
				}
				
				if(DragManager.draggedNode instanceof POPVariableNode) {
					((POPVariableNode) DragManager.draggedNode).setParentSymbol(null);
				}
				
				
				DragManager.dragMoving = false;
				DragManager.draggedNode = null;
				
			} else {
				node = (Node) POPNodeFactory.createNode(clsName, varName, varTypeName);
				
				if(node instanceof POPOperationSymbol) {
					((POPOperationSymbol) node).initialize(null);
				} else if(node instanceof POPVariableNode) {
					((POPVariableNode) node).initialize(null);
				} else {
					((POPSymbolNode) node).initialize();
				}
				
				locateAndAddNode(event, node);
//				locateNode(event, node);
//				
//				if(node instanceof POPSymbolNode) {
//					add(node);
//				} else {
//					component.getChildren().add(node);
//				}
			}
			
			success = true;
			event.setDropCompleted(success);
			event.consume();
			
			
		});
		
	}
	
	private void locateNode(DragEvent event, Node node) {
		double x = -1, y = -1;
		
		if((event.getX() - ((node.getBoundsInLocal().getWidth() * pane.getScaleX()) / 2)) / pane.getScaleX() < 0 ||
				(event.getX() - ((node.getBoundsInLocal().getHeight() * pane.getScaleY()) / 2)) / pane.getScaleY() < 0) {
			
		}
		
		if((event.getX() - ((node.getBoundsInLocal().getWidth() * pane.getScaleX()) / 2)) / pane.getScaleX() < 0) {
			x = 0;
		}
		
		if((event.getX() - ((node.getBoundsInLocal().getHeight() * pane.getScaleY()) / 2)) / pane.getScaleY() < 0) {
			y = 0;
		} 
		
		if(x != 0) {
			x = (event.getX() - ((node.getBoundsInLocal().getWidth() * pane.getScaleX()) / 2)) / pane.getScaleX() - (DragManager.lastCenterXOfStartNode - 50) + (scrollPane.getHvalue() * ( pane.getBoundsInLocal().getWidth() - scrollPane.getBoundsInLocal().getWidth() / zoomScale.get()));
		}
		
		if(y != 0) {
			y = (event.getY() - ((node.getBoundsInLocal().getHeight() * pane.getScaleY()) / 2)) / pane.getScaleY() + (scrollPane.getVvalue() * (pane.getBoundsInLocal().getHeight() - scrollPane.getBoundsInLocal().getHeight() / zoomScale.get()));
		}
		
		node.setLayoutX(x);
		node.setLayoutY(y);
	}
	
	private void locateAndAddNode(DragEvent event, Node node) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				PlatformHelper.run(() -> {
					locateNode(event, node);
					if(node instanceof POPSymbolNode) {
						add(node);
					} else {
						component.getChildren().add(node);
					}
				});
			}
		};
		
		thread.setDaemon(true);
		thread.start();
	}
			
	public void addWithOutFlowLine(POPSymbolNode node) {
		add(node);
		
		if(node.getOutFlowLine() != null)
			component.getChildren().add(node.getOutFlowLine());	
	}
	
	public void add(Node node) {
		if(node instanceof SubNodeIF) {
			component.getChildren().add(node);
			
			for(Node subNode : ((SubNodeIF) node).getSubNodes()) {
				if(subNode instanceof SubNodeIF) {
					add((POPSymbolNode) subNode);
				} else {
					component.getChildren().add(subNode);
				}
				if(!(subNode instanceof POPDecisionEndNode) && !(subNode instanceof POPLoopEndNode) && subNode instanceof POPSymbolNode) {
					component.getChildren().add(((POPSymbolNode) subNode).getOutFlowLine());
				}
			}
		} else {
			component.getChildren().add(node);
		}
	}
	
	public void remove(POPSymbolNode node) {
		if(node instanceof SubNodeIF) {
			component.getChildren().remove(node);
			for(Node subNode : ((SubNodeIF) node).getSubNodes()) {
				
				if(subNode instanceof SubNodeIF) {
					remove((POPSymbolNode) subNode);
				} else {
					component.getChildren().remove(subNode);
				}
				if(!(subNode instanceof POPDecisionEndNode) && subNode instanceof POPSymbolNode) {
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
		flowchartPlayer.playFlowChart(startNode);
		return flowchartPlayer.getOutput().toString();
	}
}
