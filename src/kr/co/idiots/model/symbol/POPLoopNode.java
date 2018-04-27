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
	
	private POPLoopStartNode loopStartNode;
	private POPLoopEndNode loopEndNode;
	
	private POPFlowLine internalOutFlowLine;
	private POPSideFlowLine rightOutFlowLine;
	
	private POPSideFlowLine rightUpFlowLine;
	private POPSideArrowFlowLine rightInFlowLine;
	
	
	private double initMaxLoopWidth = 200;
	private DoubleProperty maxLoopWidth = new SimpleDoubleProperty(initMaxLoopWidth);
	
	public double getLoopWidth() {
		return maxLoopWidth.get() + 60;
	}
	
	public POPLoopNode(POPScriptArea scriptArea) {
		super(scriptArea, POPNodeType.Loop);
		// TODO Auto-generated constructor stub
		
		subNodes = new ArrayList<>();
		
		Bounds bound = imgView.getBoundsInParent();
		Point2D pos = Point2D.ZERO;
		
		leftOutFlowLine = new POPSideFlowLine(this);
		pos = POPBoundManager.getLeftCenter(bound);
		leftOutFlowLine.setStartPos(pos.getX(), pos.getY());
		leftOutFlowLine.setEndPos(pos.getX() - 10, pos.getY());
		
		leftDownFlowLine = new POPSideFlowLine(this);
		leftDownFlowLine.setStartPos(leftOutFlowLine.getEndX(), leftOutFlowLine.getEndY());
		leftDownFlowLine.setEndPos(pos.getX() - 10, pos.getY() + 100);
		
		leftInFlowLine = new POPSideFlowLine(this);
		leftInFlowLine.setStartPos(leftDownFlowLine.getEndX(), leftDownFlowLine.getEndY());
		leftInFlowLine.setEndY(leftDownFlowLine.getEndY());
		leftInFlowLine.setEndX(POPBoundManager.getBottomCenter(bound).getX());
		
		loopStartNode = new POPLoopStartNode(scriptArea);
		loopEndNode = new POPLoopEndNode(scriptArea);
		loopStartNode.getOutFlowLine().setLoopNode(this);
		loopStartNode.getOutFlowLine().setNextNode(loopEndNode);
		loopEndNode.setInFlowLine(loopStartNode.getOutFlowLine());
		loopEndNode.setLoopNode(this);
		
		rightOutFlowLine = new POPSideFlowLine(this);
		rightUpFlowLine = new POPSideFlowLine(this);
		rightInFlowLine = new POPSideArrowFlowLine();
		
		outFlowLine = new POPFlowLine();
		outFlowLine.setPrevNode(this);
		outFlowLine.setRootNode(this);
		
		rightOutFlowLine.startXProperty().bind(loopEndNode.layoutXProperty());
		rightOutFlowLine.startYProperty().bind(loopEndNode.layoutYProperty());
		rightOutFlowLine.endYProperty().bind(loopEndNode.layoutYProperty());
		pos = POPBoundManager.getRightCenter(bound);
		rightOutFlowLine.endXProperty().bind(Bindings.add(Bindings.add(component.layoutXProperty(), component.widthProperty()), 
				Bindings.add(Bindings.divide(Bindings.subtract(maxLoopWidth, component.getBoundsInLocal().getWidth()), 2), 30)));
		
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
				Bindings.add(Bindings.divide(Bindings.subtract(maxLoopWidth, component.getBoundsInLocal().getWidth()), 2), 30)));
		leftOutFlowLine.endYProperty().bind(leftOutFlowLine.startYProperty());
		
		leftDownFlowLine.startXProperty().bind(leftOutFlowLine.endXProperty());
		leftDownFlowLine.startYProperty().bind(leftOutFlowLine.endYProperty());
		leftDownFlowLine.endXProperty().bind(leftDownFlowLine.startXProperty());
		leftDownFlowLine.endYProperty().bind(Bindings.add(loopEndNode.layoutYProperty(), 20));
		
		leftInFlowLine.startXProperty().bind(leftDownFlowLine.endXProperty());
		leftInFlowLine.startYProperty().bind(leftDownFlowLine.endYProperty());
		leftInFlowLine.endXProperty().bind(Bindings.add(component.layoutXProperty(), Bindings.divide(component.widthProperty(), 2)));
		leftInFlowLine.endYProperty().bind(leftDownFlowLine.endYProperty());
		
		loopStartNode.layoutXProperty().bind(Bindings.add(component.layoutXProperty(), Bindings.divide(component.widthProperty(), 2)));
		loopStartNode.layoutYProperty().bind(Bindings.add(component.layoutYProperty(), component.heightProperty()));
		
		
		
		setOnBoundChangeListener();
	}
	
	public void adjustPosition() {
		resizeLoopNode();
		System.out.println("---------------------");
		Bounds bound = component.getBoundsInParent();
		Bounds imgBound = imgView.getBoundsInParent();
		Point2D pos = Point2D.ZERO;
		
		pos = POPBoundManager.getBottomCenter(bound);
//		loopStartNode.setLayoutX(pos.getX());
//		loopStartNode.setLayoutY(pos.getY());
		
		loopEndNode.setLayoutX(loopStartNode.getLayoutX());
		
//		pos = POPBoundManager.getRightCenter(bound);
//		rightOutFlowLine.setStartPos(loopEndNode.getLayoutX(), loopEndNode.getLayoutY());
//		rightOutFlowLine.setEndPos(pos.getX() + (((maxLoopWidth - bound.getWidth()) / 2) + 30), loopEndNode.getLayoutY());
//		
//		rightUpFlowLine.setStartPos(rightOutFlowLine.getEndX(), rightOutFlowLine.getEndY());
//		rightUpFlowLine.setEndPos(rightUpFlowLine.getStartX(), pos.getY());
//		
//		rightInFlowLine.setStartPos(rightUpFlowLine.getEndX(), rightUpFlowLine.getEndY());
//		rightInFlowLine.setEndPos(pos.getX(), pos.getY());
//		
//		pos = POPBoundManager.getLeftCenter(bound);
//		leftOutFlowLine.setStartPos(pos.getX(), pos.getY());
//		leftOutFlowLine.setEndPos(pos.getX() - (((maxLoopWidth - bound.getWidth()) / 2) + 30), pos.getY());
//		
//		leftDownFlowLine.setStartPos(leftOutFlowLine.getEndX(), leftOutFlowLine.getEndY());
//		leftDownFlowLine.setEndPos(leftOutFlowLine.getEndX(), loopEndNode.getLayoutY() + 20);
//		
//		leftInFlowLine.setStartPos(leftDownFlowLine.getEndX(), leftDownFlowLine.getEndY());
//		leftInFlowLine.setEndY(leftDownFlowLine.getEndY());
//		leftInFlowLine.setEndX(POPBoundManager.getBottomCenter(bound).getX());
//		
//		resizeLoopNode();
		
//		outFlowLine.setStartX(leftInFlowLine.getEndX());
//		outFlowLine.setStartY(leftInFlowLine.getEndY());
		
//		if(inFlowLine != null && inFlowLine.getLoopNode() != null) {
//			inFlowLine.getLoopNode().adjustPosition();
//			System.out.println("2");
//		} else if(inFlowLine != null && inFlowLine.getDecisionNode() != null) {
//			inFlowLine.getDecisionNode().adjustPosition();
//			System.out.println("1-2");
//		}
		
		Thread thread = new Thread() {
            @Override
            public void run() {
                PlatformHelper.run(() -> {
                	Node subNode = loopStartNode;
                	while(true) {
    					
    					
    					if(subNode instanceof POPSymbolNode) {
    						((POPSymbolNode) subNode).moveCenter();
    					}
    					
    					if(subNode instanceof POPLoopEndNode) {
    						break;
    					}
    					System.out.println("************************");
    					subNode = ((POPSymbolNode) subNode).getOutFlowLine().getNextNode();
    				}
                });
//                try { Thread.sleep(100); } catch (InterruptedException e) {}
            }
        };
        
        thread.setDaemon(true);
        thread.start();
        
//		DragManager.isDecisionSync = true;
		
//		moveCenter();
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
		
		subNodes.add(leftOutFlowLine);
		subNodes.add(leftDownFlowLine);
		subNodes.add(leftInFlowLine);
		subNodes.add(loopStartNode);
		subNodes.add(loopEndNode);
		subNodes.add(rightOutFlowLine);
		subNodes.add(rightUpFlowLine);
		subNodes.add(rightInFlowLine);
	}
	
	@Override
	protected void setOnBoundChangeListener() {
		component.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {

			@Override
			public void changed(ObservableValue<? extends Bounds> arg0, Bounds oldBound, Bounds newBound) {
				// TODO Auto-generated method stub
				
				adjustPosition();
//				System.out.println("5");
//				if(outFlowLine != null) {
//					outFlowLine.setStartX(newBound.getMinX() + (newBound.getWidth() / 2));
//					outFlowLine.setStartY(leftInFlowLine.getEndY());
//				}
////				
//				if(inFlowLine != null) {
//					inFlowLine.setEndX(newBound.getMinX() + (newBound.getWidth() / 2));
//					inFlowLine.setEndY(newBound.getMinY());
//				}
				
				resizeLoopNode();
				
				loopStartNode.getOutFlowLine().lengthChanging(0, 0);
				
				initMaxLoopWidth = newBound.getWidth();
				
		        
				
				
//				outFlowLine.setStartX(leftInFlowLine.getEndX());
//				outFlowLine.setStartY(leftInFlowLine.getEndY());
//				moveCenter();
				
				
			}
		});
	}
	
	@Override
	public void moveCenter() {
		if(inFlowLine != null && isAllocated) {
			
			Bounds newBound = null;
			Bounds prevBound = null;
			
			newBound = component.getBoundsInParent();
			prevBound = inFlowLine.getPrevNode().getBoundsInParent();
			
		
//			if(outFlowLine != null) {
//				outFlowLine.setStartX(newBound.getMinX() + (newBound.getWidth() / 2));				
//				outFlowLine.setStartY(leftInFlowLine.getEndY());
//			}
//			
//			if(inFlowLine != null) {
//				inFlowLine.setEndX(newBound.getMinX() + (newBound.getWidth() / 2));
//				inFlowLine.setEndY(newBound.getMinY());
//			}
			
			component.setLayoutX((prevBound.getMinX() + (prevBound.getWidth() / 2)) - (newBound.getWidth() / 2));
			
		}
	}
	
	private void resizeLoopNode() {
		maxLoopWidth.set(initMaxLoopWidth);// = initMaxLoopWidth;
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

	public void calMaxLength() {
		
	}
}
