package kr.co.idiots.model.symbol;

import java.util.ArrayList;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import kr.co.idiots.SubNodeIF;
import kr.co.idiots.model.POPFlowLine;
import kr.co.idiots.model.POPNodeType;
import kr.co.idiots.model.POPScriptArea;
import kr.co.idiots.model.POPSideFlowLine;
import kr.co.idiots.model.compare.POPCompareRootSymbol;
import kr.co.idiots.util.POPBoundManager;
import kr.co.idiots.util.PlatformHelper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPDecisionNode extends POPSymbolNode implements SubNodeIF {
	private Rectangle boundChecker;
	private ArrayList<Node> subNodes;
	
	private POPSideFlowLine leftFlowLine;
	private POPSideFlowLine rightFlowLine;
	
	private Label leftLabel;
	private Label rightLabel;
	
	private POPDecisionStartNode leftStartNode;
	private POPDecisionStartNode rightStartNode;
	
	private POPDecisionEndNode leftEndNode;
	private POPDecisionEndNode rightEndNode;
	
	private POPDecisionNode parentDecisionNode;
	
	private DoubleProperty maxLength = new SimpleDoubleProperty(100);
	
	private double initMaxWidth = 160;
	private DoubleProperty leftMaxWidth = new SimpleDoubleProperty(initMaxWidth);
	private DoubleProperty rightMaxWidth = new SimpleDoubleProperty(initMaxWidth);
	private DoubleProperty sideFlowLineY = new SimpleDoubleProperty();
	
	public double getDecisionWidth() {
		Bounds bound = component.getBoundsInParent();
		this.resizeDecisionNode();
		
		double width = Math.max(leftMaxWidth.get(), rightMaxWidth.get());
		
		return width * 2;
	}
	
	public void calMaxLength() {
		double leftMaxLength;		
		leftMaxLength = leftEndNode.getInFlowLine().getStartY() + POPFlowLine.nodeMinGap + 30 - getLayoutY();
		
		double rightMaxLength;
		rightMaxLength = rightEndNode.getInFlowLine().getStartY() + POPFlowLine.nodeMinGap + 30 - getLayoutY();
		
		maxLength.set(Math.max(leftMaxLength, rightMaxLength));
	}
	
	public void init() {
		subNodes = new ArrayList<>();
		
		Bounds bound = imgView.getBoundsInParent();
		Point2D pos = Point2D.ZERO;
		
		leftFlowLine = new POPSideFlowLine(this);
		pos = POPBoundManager.getLeftCenter(bound);
		
		rightFlowLine = new POPSideFlowLine(this);
		pos = POPBoundManager.getRightCenter(bound);
		
		leftLabel = new Label("예");
		rightLabel = new Label("아니오");
		
		leftStartNode = new POPDecisionStartNode(scriptArea);
		leftStartNode.getOutFlowLine().setDecisionNode(this);
		leftStartNode.setParentNode(this);
		rightStartNode = new POPDecisionStartNode(scriptArea);
		rightStartNode.getOutFlowLine().setDecisionNode(this);
		rightStartNode.setParentNode(this);
		
		rightEndNode = new POPDecisionEndNode(scriptArea, this, null);
		leftEndNode = new POPDecisionEndNode(scriptArea, this, rightEndNode);
		rightEndNode.setSideNode(leftEndNode);
		leftEndNode = leftEndNode.createSideFlowLine();
		leftEndNode.setParentNode(this);
		rightEndNode.setParentNode(this);
		
		leftStartNode.getOutFlowLine().setNextNode(leftEndNode);
		leftEndNode.setInFlowLine(leftStartNode.getOutFlowLine());
//		leftEndNode.moveCenter();
		rightStartNode.getOutFlowLine().setNextNode(rightEndNode);
		rightEndNode.setInFlowLine(rightStartNode.getOutFlowLine());
//		rightEndNode.moveCenter();
		
		outFlowLine = new POPFlowLine();
		outFlowLine.setRootNode(this);
		outFlowLine.setPrevNode(this);
		
		subNodes.add(leftFlowLine);
		subNodes.add(rightFlowLine);
		subNodes.add(leftLabel);
		subNodes.add(rightLabel);
		subNodes.add(leftStartNode);
		subNodes.add(rightStartNode);
		subNodes.add(leftEndNode);
		subNodes.add(rightEndNode);
		subNodes.add(leftEndNode.getSideFlowLine());
		
//		invisibleSubNodes();
	}
	
	public void bindSubNodes() {
		leftFlowLine.startXProperty().bind(component.layoutXProperty());
		leftFlowLine.startYProperty().bind(Bindings.add(component.layoutYProperty(), Bindings.divide(component.heightProperty(), 2)));
		leftFlowLine.endXProperty().bind(Bindings.subtract(Bindings.subtract(component.layoutXProperty(), Bindings.divide(Bindings.subtract(leftMaxWidth, component.widthProperty()), 2)), 10));
		leftFlowLine.endYProperty().bind(leftFlowLine.startYProperty());
		
		rightFlowLine.startXProperty().bind(Bindings.add(component.layoutXProperty(), component.widthProperty()));
		rightFlowLine.startYProperty().bind(Bindings.add(component.layoutYProperty(), Bindings.divide(component.heightProperty(), 2)));
		rightFlowLine.endXProperty().bind(Bindings.add(Bindings.add(Bindings.add(component.layoutXProperty(), component.widthProperty()), Bindings.divide(Bindings.subtract(rightMaxWidth, component.widthProperty()), 2)), 10));
		rightFlowLine.endYProperty().bind(rightFlowLine.startYProperty());
		
		
		leftStartNode.layoutXProperty().bind(leftFlowLine.endXProperty());
		leftStartNode.layoutYProperty().bind(leftFlowLine.endYProperty());
		
		rightStartNode.layoutXProperty().bind(rightFlowLine.endXProperty());
		rightStartNode.layoutYProperty().bind(rightFlowLine.endYProperty());
		
		leftEndNode.layoutXProperty().bind(leftStartNode.layoutXProperty());
		leftEndNode.layoutYProperty().bind(leftEndNode.getSideFlowLine().startYProperty());
		
		rightEndNode.layoutXProperty().bind(rightStartNode.layoutXProperty());
		rightEndNode.layoutYProperty().bind(leftEndNode.getSideFlowLine().endYProperty());
		
		leftEndNode.getSideFlowLine().startXProperty().bind(leftEndNode.layoutXProperty());
		leftEndNode.getSideFlowLine().startYProperty().bind(Bindings.add(component.layoutYProperty(), maxLength));
		leftEndNode.getSideFlowLine().endXProperty().bind(rightEndNode.layoutXProperty());
		leftEndNode.getSideFlowLine().endYProperty().bind(Bindings.add(component.layoutYProperty(), maxLength));
		
		rightEndNode.layoutYProperty().bind(leftEndNode.getSideFlowLine().endYProperty());
				
		leftLabel.layoutXProperty().bind(Bindings.subtract(leftStartNode.layoutXProperty(), Bindings.divide(leftLabel.widthProperty(), 2)));
		leftLabel.layoutYProperty().bind(Bindings.subtract(leftStartNode.layoutYProperty(), 30));
		rightLabel.layoutXProperty().bind(Bindings.subtract(rightStartNode.layoutXProperty(), Bindings.divide(rightLabel.widthProperty(), 2)));
		rightLabel.layoutYProperty().bind(Bindings.subtract(rightStartNode.layoutYProperty(), 30));
	}
	
	public void visibleSubNodes() {
//		Thread thread = new Thread() {
//			@Override
//			public void run() {
//				PlatformHelper.run(() -> {
//					for(Node subNode : subNodes) {
//						subNode.setVisible(true);
//					}
//					leftStartNode.getOutFlowLine().setVisible(true);
//					rightStartNode.getOutFlowLine().setVisible(true);
//					outFlowLine.setVisible(true);
//				});
//			}
//		};
//		
//		thread.setDaemon(true);
//		thread.start();
		for(Node subNode : subNodes) {
			subNode.setVisible(true);
		}
		leftStartNode.getOutFlowLine().setVisible(true);
		rightStartNode.getOutFlowLine().setVisible(true);
		outFlowLine.setVisible(true);
	}
	
	public void invisibleSubNodes() {
//		Thread thread = new Thread() {
//			@Override
//			public void run() {
//				PlatformHelper.run(() -> {
//					for(Node subNode : subNodes) {
//						subNode.setVisible(false);
//					}
//					leftStartNode.getOutFlowLine().setVisible(false);
//					rightStartNode.getOutFlowLine().setVisible(false);
//					outFlowLine.setVisible(false);
//				});
//			}
//		};
//		
//		thread.setDaemon(true);
//		thread.start();
		for(Node subNode : subNodes) {
			subNode.setVisible(false);
		}
		leftStartNode.getOutFlowLine().setVisible(false);
		rightStartNode.getOutFlowLine().setVisible(false);
		outFlowLine.setVisible(false);
	}
	
	public POPDecisionNode(POPScriptArea scriptArea) {
		super(scriptArea, POPNodeType.Decision);
		// TODO Auto-generated constructor stub
		
//		subNodes = new ArrayList<>();
//		
//		Bounds bound = imgView.getBoundsInParent();
//		Point2D pos = Point2D.ZERO;
//		
//		leftFlowLine = new POPSideFlowLine(this);
//		pos = POPBoundManager.getLeftCenter(bound);
//		
//		rightFlowLine = new POPSideFlowLine(this);
//		pos = POPBoundManager.getRightCenter(bound);
//		
//		leftLabel = new Label("예");
//		rightLabel = new Label("아니오");
//		
//		leftStartNode = new POPDecisionStartNode(scriptArea);
//		leftStartNode.getOutFlowLine().setDecisionNode(this);
//		leftStartNode.setParentNode(this);
//		rightStartNode = new POPDecisionStartNode(scriptArea);
//		rightStartNode.getOutFlowLine().setDecisionNode(this);
//		rightStartNode.setParentNode(this);
//		
//		rightEndNode = new POPDecisionEndNode(scriptArea, this, null);
//		leftEndNode = new POPDecisionEndNode(scriptArea, this, rightEndNode);
//		rightEndNode.setSideNode(leftEndNode);
//		leftEndNode = leftEndNode.createSideFlowLine();
//		leftEndNode.setParentNode(this);
//		rightEndNode.setParentNode(this);
//		
//		leftStartNode.getOutFlowLine().setNextNode(leftEndNode);
//		leftEndNode.setInFlowLine(leftStartNode.getOutFlowLine());
////		leftEndNode.moveCenter();
//		rightStartNode.getOutFlowLine().setNextNode(rightEndNode);
//		rightEndNode.setInFlowLine(rightStartNode.getOutFlowLine());
////		rightEndNode.moveCenter();
//		
//		outFlowLine = new POPFlowLine();
//		outFlowLine.setRootNode(this);
//		outFlowLine.setPrevNode(this);
		init();
		bindSubNodes();
//		leftFlowLine.startXProperty().bind(component.layoutXProperty());
//		leftFlowLine.startYProperty().bind(Bindings.add(component.layoutYProperty(), Bindings.divide(component.heightProperty(), 2)));
//		leftFlowLine.endXProperty().bind(Bindings.subtract(Bindings.subtract(component.layoutXProperty(), Bindings.divide(Bindings.subtract(leftMaxWidth, component.widthProperty()), 2)), 10));
//		leftFlowLine.endYProperty().bind(leftFlowLine.startYProperty());
//		
//		rightFlowLine.startXProperty().bind(Bindings.add(component.layoutXProperty(), component.widthProperty()));
//		rightFlowLine.startYProperty().bind(Bindings.add(component.layoutYProperty(), Bindings.divide(component.heightProperty(), 2)));
//		rightFlowLine.endXProperty().bind(Bindings.add(Bindings.add(Bindings.add(component.layoutXProperty(), component.widthProperty()), Bindings.divide(Bindings.subtract(rightMaxWidth, component.widthProperty()), 2)), 10));
//		rightFlowLine.endYProperty().bind(rightFlowLine.startYProperty());
//		
//		
//		leftStartNode.layoutXProperty().bind(leftFlowLine.endXProperty());
//		leftStartNode.layoutYProperty().bind(leftFlowLine.endYProperty());
//		
//		rightStartNode.layoutXProperty().bind(rightFlowLine.endXProperty());
//		rightStartNode.layoutYProperty().bind(rightFlowLine.endYProperty());
//		
//		leftEndNode.layoutXProperty().bind(leftStartNode.layoutXProperty());
//		leftEndNode.layoutYProperty().bind(leftEndNode.getSideFlowLine().startYProperty());
//		
//		rightEndNode.layoutXProperty().bind(rightStartNode.layoutXProperty());
//		rightEndNode.layoutYProperty().bind(leftEndNode.getSideFlowLine().endYProperty());
//		
//		leftEndNode.getSideFlowLine().startXProperty().bind(leftEndNode.layoutXProperty());
//		leftEndNode.getSideFlowLine().startYProperty().bind(Bindings.add(component.layoutYProperty(), maxLength));
//		leftEndNode.getSideFlowLine().endXProperty().bind(rightEndNode.layoutXProperty());
//		leftEndNode.getSideFlowLine().endYProperty().bind(Bindings.add(component.layoutYProperty(), maxLength));
//		
//		rightEndNode.layoutYProperty().bind(leftEndNode.getSideFlowLine().endYProperty());
//				
//		leftLabel.layoutXProperty().bind(Bindings.subtract(leftStartNode.layoutXProperty(), Bindings.divide(leftLabel.widthProperty(), 2)));
//		leftLabel.layoutYProperty().bind(Bindings.subtract(leftStartNode.layoutYProperty(), 30));
//		rightLabel.layoutXProperty().bind(Bindings.subtract(rightStartNode.layoutXProperty(), Bindings.divide(rightLabel.widthProperty(), 2)));
//		rightLabel.layoutYProperty().bind(Bindings.subtract(rightStartNode.layoutYProperty(), 30));
		
//		subNodes.add(leftFlowLine);
//		subNodes.add(rightFlowLine);
//		subNodes.add(leftLabel);
//		subNodes.add(rightLabel);
//		subNodes.add(leftStartNode);
//		subNodes.add(rightStartNode);
//		subNodes.add(leftEndNode);
//		subNodes.add(rightEndNode);
//		subNodes.add(leftEndNode.getSideFlowLine());
		imgView.setStyle("-fx-effect: dropshadow(three-pass-box, black, 2, 0, 0, 1);");
		setOnBoundChangeListener();
	}
	
	public void resizeDecisionNode() {
		
		leftMaxWidth.set(initMaxWidth);
		rightMaxWidth.set(initMaxWidth);
		
		Node subNode;
		subNode = leftStartNode.getOutFlowLine().getNextNode();
		while(true) {
			if(subNode instanceof POPDecisionEndNode)
				break;
			
			if(subNode instanceof POPDecisionStartNode) {
				leftMaxWidth.set(30);
			} else if(subNode instanceof POPLoopNode) {
				leftMaxWidth.set(Math.max(((POPLoopNode) subNode).getLoopWidth(), leftMaxWidth.get()));
			} else if(subNode instanceof POPDecisionNode) {
				leftMaxWidth.set(Math.max(((POPDecisionNode) subNode).getDecisionWidth(), leftMaxWidth.get()));
			} else if(!(subNode instanceof POPDecisionStartNode) && subNode instanceof POPSymbolNode) {
				leftMaxWidth.set(Math.max(((POPSymbolNode) subNode).getComponent().getWidth(), leftMaxWidth.get()));
			}
			
			subNode = ((POPSymbolNode) subNode).getOutFlowLine().getNextNode();
		}
		
		subNode = rightStartNode.getOutFlowLine().getNextNode();
		while(true) {
			if(subNode instanceof POPDecisionEndNode)
				break;
			
			if(subNode instanceof POPDecisionStartNode) {
				rightMaxWidth.set(30);
			} else if(subNode instanceof POPLoopNode) {
				rightMaxWidth.set(Math.max(((POPLoopNode) subNode).getLoopWidth(), rightMaxWidth.get()));
			} else if(subNode instanceof POPDecisionNode) {
				rightMaxWidth.set(Math.max(((POPDecisionNode) subNode).getDecisionWidth(), rightMaxWidth.get()));
			} else if(!(subNode instanceof POPDecisionStartNode) && subNode instanceof POPSymbolNode) {
				rightMaxWidth.set(Math.max(((POPSymbolNode) subNode).getComponent().getWidth(), rightMaxWidth.get()));
			}
			
			subNode = ((POPSymbolNode) subNode).getOutFlowLine().getNextNode();
		}
		
	}
	
	public void adjustPosition() {
		resizeDecisionNode();
    	
    	Node subNode = leftStartNode.getOutFlowLine().getNextNode();
		
		while(true) {
			if(subNode instanceof POPDecisionEndNode) {
				break;
			}
			
			if(subNode instanceof POPSymbolNode) {
				((POPSymbolNode) subNode).moveCenter();
			}
			subNode = ((POPSymbolNode) subNode).getOutFlowLine().getNextNode();
		}
		
		subNode = rightStartNode.getOutFlowLine().getNextNode();
		while(true) {
			if(subNode instanceof POPDecisionEndNode) {
				break;
			}
			
			if(subNode instanceof POPSymbolNode) {
				((POPSymbolNode) subNode).moveCenter();
			}
			subNode = ((POPSymbolNode) subNode).getOutFlowLine().getNextNode();
		}
		
		if(outFlowLine.getLoopNode() != null) {
    		outFlowLine.getLoopNode().adjustPosition();
    	} else if(outFlowLine.getDecisionNode() != null) {
    		outFlowLine.getDecisionNode().adjustPosition();
    	}
	}
	
	public void adjustPositionThread() {
		Bounds bound = component.getBoundsInParent();
		Bounds imgBound = imgView.getBoundsInLocal();
		Point2D pos = Point2D.ZERO;
		
		Thread thread = new Thread() {
            @Override
            public void run() {
                PlatformHelper.run(() -> {
                	adjustPosition();
                });
                try { Thread.sleep(100); } catch (InterruptedException e) {}
            }
        };
        
        thread.setDaemon(true);
        thread.start();
	}
	
	@Override
	public void initialize() {
		super.initialize();
		
		POPCompareRootSymbol symbol = new POPCompareRootSymbol();
		symbol.initialize(this);
		this.setOperationSymbol(symbol);
		StackPane.setAlignment(symbol, Pos.CENTER);
		component.getChildren().add(symbol);
		symbol.setRootSymbol(true);
		this.setRootSymbol(symbol);
		
		Thread thread = new Thread() {
			@Override
			public void run() {
				PlatformHelper.run(() -> {
					visibleSubNodes();
				});
			}
		};
		
		thread.setDaemon(true);
		thread.start();
//		subNodes.add(leftFlowLine);
//		subNodes.add(rightFlowLine);
//		subNodes.add(leftLabel);
//		subNodes.add(rightLabel);
//		subNodes.add(leftStartNode);
//		subNodes.add(rightStartNode);
//		subNodes.add(leftEndNode);
//		subNodes.add(rightEndNode);
//		subNodes.add(leftEndNode.getSideFlowLine());
	}
	
	@Override
	public void moveCenter() {
		if(inFlowLine != null && isAllocated) {
			Bounds newBound = null;
			Bounds prevBound = null;
			
			newBound = component.getBoundsInParent();
			prevBound = inFlowLine.getPrevNode().getBoundsInParent();
			
			component.setLayoutX((prevBound.getMinX() + (prevBound.getWidth() / 2)) - (newBound.getWidth() / 2) + 2);
			
		}
	}
	
	@Override
	protected void setOnBoundChangeListener() {
		component.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {

			@Override
			public void changed(ObservableValue<? extends Bounds> arg0, Bounds oldBound, Bounds newBound) {
				// TODO Auto-generated method stub
				initMaxWidth = newBound.getWidth();
				adjustPositionThread();
				outFlowLine.pullNodesThread();
				
//				initMaxWidth += (newBound.getWidth() - oldBound.getWidth()) / 2;
				
			}
		});
	}
}
