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
import kr.co.idiots.SubNodeIF;
import kr.co.idiots.model.POPFlowLine;
import kr.co.idiots.model.POPNodeType;
import kr.co.idiots.model.POPScriptArea;
import kr.co.idiots.model.POPSideArrowFlowLine;
import kr.co.idiots.model.POPSideFlowLine;
import kr.co.idiots.model.compare.POPCompareRootSymbol;
import kr.co.idiots.util.POPBoundManager;
import kr.co.idiots.util.PlatformHelper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPLoopNode extends POPSymbolNode implements SubNodeIF {

	private ArrayList<Node> subNodes;
	
	private POPSideFlowLine leftOutFlowLine;
	private POPSideFlowLine leftDownFlowLine;
	private POPSideFlowLine leftInFlowLine;
	
	private Label leftLabel;
	private Label rightLabel;
	
	private POPLoopStartNode loopStartNode;
	private POPLoopEndNode loopEndNode;
	
	private POPFlowLine internalOutFlowLine;
	private POPSideFlowLine rightOutFlowLine;
	
	private POPSideFlowLine rightUpFlowLine;
	private POPSideArrowFlowLine rightInFlowLine;
	
	private double initMaxLoopWidth = 200;
	private DoubleProperty maxLoopWidth = new SimpleDoubleProperty(initMaxLoopWidth);
	
	public double getLoopWidth() {
		return rightInFlowLine.getStartX() - leftOutFlowLine.getEndX();
	}
	
	public void init() {
		subNodes = new ArrayList<>();
		
		Bounds bound = imgView.getBoundsInParent();
		Point2D pos = Point2D.ZERO;
		
		initMaxLoopWidth = bound.getWidth();
		maxLoopWidth = new SimpleDoubleProperty(initMaxLoopWidth);
		
		leftOutFlowLine = new POPSideFlowLine(this);
		pos = POPBoundManager.getLeftCenter(bound);
		
		leftDownFlowLine = new POPSideFlowLine(this);
		
		leftInFlowLine = new POPSideFlowLine(this);
		leftInFlowLine.setEndY(leftDownFlowLine.getEndY());
		leftInFlowLine.setEndX(POPBoundManager.getBottomCenter(bound).getX());
		
		leftLabel = new Label("아니오");
		rightLabel = new Label("예");
		
		loopStartNode = new POPLoopStartNode(scriptArea);
		loopEndNode = new POPLoopEndNode(scriptArea);
		loopStartNode.getOutFlowLine().setLoopNode(this);
		loopStartNode.setParentNode(this);
		loopStartNode.getOutFlowLine().setNextNode(loopEndNode);
		loopEndNode.setInFlowLine(loopStartNode.getOutFlowLine());
		loopEndNode.setParentNode(this);
		loopEndNode.setLoopNode(this);
		
		rightOutFlowLine = new POPSideFlowLine(this);
		rightUpFlowLine = new POPSideFlowLine(this);
		rightInFlowLine = new POPSideArrowFlowLine();
		
		outFlowLine = new POPFlowLine();
		outFlowLine.setPrevNode(this);
		outFlowLine.setRootNode(this);
		
		subNodes.add(leftOutFlowLine);
		subNodes.add(leftDownFlowLine);
		subNodes.add(leftInFlowLine);
		subNodes.add(leftLabel);
		subNodes.add(rightLabel);
		subNodes.add(loopStartNode);
		subNodes.add(loopEndNode);
		subNodes.add(rightOutFlowLine);
		subNodes.add(rightUpFlowLine);
		subNodes.add(rightInFlowLine);
		
		invisibleSubNodes();
	}
	
	public void visibleSubNodes() {
		for(Node subNode : subNodes) {
			subNode.setVisible(true);
		}
		loopStartNode.getOutFlowLine().setVisible(true);
		outFlowLine.setVisible(true);
	}
	
	public void invisibleSubNodes() {
		for(Node subNode : subNodes) {
			subNode.setVisible(false);
		}
		loopStartNode.getOutFlowLine().setVisible(false);
		outFlowLine.setVisible(false);
	}
	
	
	public void bindSubNodes() {
		Bounds bound = imgView.getBoundsInParent();

		rightOutFlowLine.startXProperty().bindBidirectional(loopEndNode.layoutXProperty());
		rightOutFlowLine.startYProperty().bind(loopEndNode.layoutYProperty());
		rightOutFlowLine.endYProperty().bind(loopEndNode.layoutYProperty());
		rightOutFlowLine.endXProperty().bind(Bindings.add(Bindings.add(component.layoutXProperty(), component.widthProperty()),
				Bindings.add(Bindings.divide(Bindings.subtract(maxLoopWidth, component.widthProperty()), 2), 30)));
		
		rightUpFlowLine.startXProperty().bind(rightOutFlowLine.endXProperty());
		rightUpFlowLine.startYProperty().bind(rightOutFlowLine.endYProperty());
		rightUpFlowLine.endXProperty().bind(rightUpFlowLine.startXProperty());
		rightUpFlowLine.endYProperty().bind(Bindings.add(component.layoutYProperty(), Bindings.divide(component.heightProperty(), 2)));
		
		rightInFlowLine.startXProperty().bind(rightUpFlowLine.endXProperty());
		rightInFlowLine.startYProperty().bind(rightUpFlowLine.endYProperty());
		rightInFlowLine.endXProperty().bind(Bindings.add(component.layoutXProperty(), component.widthProperty()));
		rightInFlowLine.endYProperty().bind(rightInFlowLine.startYProperty());
		
		leftOutFlowLine.startXProperty().bind(component.layoutXProperty());
		leftOutFlowLine.startYProperty().bind(Bindings.add(component.layoutYProperty(), Bindings.divide(component.heightProperty(), 2)));
		leftOutFlowLine.endXProperty().bind(Bindings.subtract(component.layoutXProperty(), 
				Bindings.add(Bindings.divide(Bindings.subtract(maxLoopWidth, component.widthProperty()), 2), 30)));
		leftOutFlowLine.endYProperty().bind(leftOutFlowLine.startYProperty());
		
		leftDownFlowLine.startXProperty().bind(leftOutFlowLine.endXProperty());
		leftDownFlowLine.startYProperty().bind(leftOutFlowLine.endYProperty());
		leftDownFlowLine.endXProperty().bind(leftDownFlowLine.startXProperty());
		leftDownFlowLine.endYProperty().bind(Bindings.add(loopEndNode.layoutYProperty(), 20));
		
		leftInFlowLine.startXProperty().bind(leftDownFlowLine.endXProperty());
		leftInFlowLine.startYProperty().bind(leftDownFlowLine.endYProperty());
		leftInFlowLine.endXProperty().bind(Bindings.add(component.layoutXProperty(), Bindings.divide(component.widthProperty(), 2)));
		leftInFlowLine.endYProperty().bind(leftDownFlowLine.endYProperty());
		
		leftLabel.layoutXProperty().bind(Bindings.subtract(leftOutFlowLine.endXProperty(), Bindings.divide(leftLabel.widthProperty(), 2)));
		leftLabel.layoutYProperty().bind(Bindings.subtract(leftOutFlowLine.endYProperty(), 30));
		rightLabel.layoutXProperty().bind(Bindings.add(loopStartNode.layoutXProperty(), 30));
		rightLabel.layoutYProperty().bind(loopStartNode.layoutYProperty());
		
		loopStartNode.layoutXProperty().bind(Bindings.add(component.layoutXProperty(), Bindings.divide(component.widthProperty(), 2)));
		loopStartNode.layoutYProperty().bind(Bindings.add(component.layoutYProperty(), component.heightProperty()));
		
	}
	
	public POPLoopNode(POPScriptArea scriptArea) {
		super(scriptArea, POPNodeType.Loop);
		init();
		bindSubNodes();
		imgView.setStyle("-fx-effect: dropshadow(three-pass-box, black, 2, 0, 0, 1);");
		setOnBoundChangeListener();
	}
	
	public void adjustPosition() {
		loopEndNode.setLayoutX(loopStartNode.getLayoutX());
    	
    	resizeLoopNode();
    	
    	Node subNode = loopStartNode;
    	while(true) {
			if(subNode instanceof POPSymbolNode) {
				((POPSymbolNode) subNode).moveCenter();
			}
			
			if(subNode instanceof POPLoopEndNode) {
				break;
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
		Thread thread = new Thread() {
            @Override
            public void run() {
                PlatformHelper.run(() -> {
                	adjustPosition();
                	loopStartNode.getOutFlowLine().pullNodes();
                });
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
		
	}
	
	@Override
	protected void setOnBoundChangeListener() {
		component.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {

			@Override
			public void changed(ObservableValue<? extends Bounds> arg0, Bounds oldBound, Bounds newBound) {
				initMaxLoopWidth = newBound.getWidth();
				adjustPositionThread();
			}
		});
	}
	
	@Override
	public void moveCenter() {
		if(inFlowLine != null && isAllocated) {
			Bounds prevBound = null;
			prevBound = inFlowLine.getPrevNode().getBoundsInParent();
			component.setLayoutX((prevBound.getMinX() + (prevBound.getWidth() / 2)) - (imgView.getFitWidth() / 2));
		}
	}
	
	public void resizeLoopNode() {
		maxLoopWidth.set(initMaxLoopWidth);
		for(Node subNode : subNodes) {
			if(subNode instanceof POPDecisionNode) {
				maxLoopWidth.set(Math.max(((POPDecisionNode) subNode).getDecisionWidth(), maxLoopWidth.get()));
			} else if(subNode instanceof POPLoopNode) {
				maxLoopWidth.set(Math.max(((POPLoopNode) subNode).getLoopWidth(), maxLoopWidth.get()));
			} else if(!(subNode instanceof POPLoopStartNode) && !(subNode instanceof POPLoopEndNode) && subNode instanceof POPSymbolNode) {
				maxLoopWidth.set(Math.max(((POPSymbolNode) subNode).getComponent().getWidth(), maxLoopWidth.get()));
			}
		}
	}
}
