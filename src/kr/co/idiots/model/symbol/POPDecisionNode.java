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
//	private Group contents;
	private Rectangle boundChecker;
	private ArrayList<Node> subNodes;
	
	private POPSideFlowLine leftFlowLine;
	private POPSideFlowLine rightFlowLine;
	
	private POPDecisionStartNode leftStartNode;
	private POPDecisionStartNode rightStartNode;
	
	private POPDecisionEndNode leftEndNode;
	private POPDecisionEndNode rightEndNode;
	
	private POPDecisionNode parentDecisionNode;
	
	private DoubleProperty maxLength = new SimpleDoubleProperty(100);
	
	private double initMaxWidth = 250;
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
	
	public POPDecisionNode(POPScriptArea scriptArea) {
		super(scriptArea, POPNodeType.Decision);
		// TODO Auto-generated constructor stub
		
		subNodes = new ArrayList<>();
		
		Bounds bound = imgView.getBoundsInParent();
		Point2D pos = Point2D.ZERO;
		
		leftFlowLine = new POPSideFlowLine(this);
		pos = POPBoundManager.getLeftCenter(bound);
		leftFlowLine.setStartPos(pos.getX(), pos.getY());
		leftFlowLine.setEndPos(pos.getX() - 10, pos.getY());
		
		rightFlowLine = new POPSideFlowLine(this);
		pos = POPBoundManager.getRightCenter(bound);
		rightFlowLine.setStartPos(pos.getX(), pos.getY());
		rightFlowLine.setEndPos(pos.getX() + 10, pos.getY());
		
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
		
		leftFlowLine.startXProperty().bind(component.layoutXProperty());
		leftFlowLine.startYProperty().bind(Bindings.add(component.layoutYProperty(), Bindings.divide(component.heightProperty(), 2)));
		leftFlowLine.endXProperty().bind(Bindings.subtract(component.layoutXProperty(), Bindings.divide(Bindings.subtract(leftMaxWidth, component.widthProperty()), 2)));
		leftFlowLine.endYProperty().bind(leftFlowLine.startYProperty());
		
		rightFlowLine.startXProperty().bind(Bindings.add(component.layoutXProperty(), component.widthProperty()));
		rightFlowLine.startYProperty().bind(Bindings.add(component.layoutYProperty(), Bindings.divide(component.heightProperty(), 2)));
		rightFlowLine.endXProperty().bind(Bindings.add(Bindings.add(component.layoutXProperty(), component.widthProperty()), Bindings.divide(Bindings.subtract(rightMaxWidth, component.widthProperty()), 2)));
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
//		leftEndNode.getSideFlowLine().startYProperty().bind(leftEndNode.layoutYProperty());
		leftEndNode.getSideFlowLine().endXProperty().bind(rightEndNode.layoutXProperty());
		leftEndNode.getSideFlowLine().endYProperty().bind(Bindings.add(component.layoutYProperty(), maxLength));
//		leftEndNode.getSideFlowLine().endYProperty().bind(rightEndNode.layoutYProperty());
		
		rightEndNode.layoutYProperty().bind(leftEndNode.getSideFlowLine().endYProperty());
//		rightEndNode.layoutYProperty().bind(leftEndNode.layoutYProperty());
//		if(leftFlowLine != null) {
//			pos = POPBoundManager.getLeftCenter(bound);
//			leftFlowLine.setStartPos(pos.getX(), pos.getY());
//			leftFlowLine.setEndPos(pos.getX() - ((leftMaxWidth - (bound.getWidth())) / 2), pos.getY());
//		}
//		
//		if(rightFlowLine != null) {
//			pos = POPBoundManager.getRightCenter(bound);
//			rightFlowLine.setStartPos(pos.getX(), pos.getY());
//			rightFlowLine.setEndPos(pos.getX() + ((rightMaxWidth - (bound.getWidth())) / 2), pos.getY());
//		}
//		
//		if(leftStartNode != null) {
//			leftStartNode.setLayoutX(leftFlowLine.getEndX());
//			leftStartNode.setLayoutY(leftFlowLine.getEndY());
//		}
//		
//		if(rightStartNode != null) {
//			rightStartNode.setLayoutX(rightFlowLine.getEndX());
//			rightStartNode.setLayoutY(rightFlowLine.getEndY());
//		}
//		
//		if(leftEndNode != null) {
//			leftEndNode.setLayoutX(leftStartNode.getLayoutX());
//		}
//		
//		if(rightEndNode != null) {
//			rightEndNode.setLayoutX(rightStartNode.getLayoutX());
//		}
//		
//		leftEndNode.getSideFlowLine().setStartX(leftEndNode.getLayoutX());
//		leftEndNode.getSideFlowLine().setStartY(leftEndNode.getLayoutY());
//		leftEndNode.getSideFlowLine().setEndX(rightEndNode.getLayoutX());
//		leftEndNode.getSideFlowLine().setEndY(rightEndNode.getLayoutY());
//		
//		rightEndNode.setLayoutY(leftEndNode.getLayoutY());
				
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
			
			if(subNode instanceof POPLoopNode) {
				leftMaxWidth.set(Math.max(((POPLoopNode) subNode).getLoopWidth(), leftMaxWidth.get()));
			} else if(subNode instanceof POPDecisionNode) {
				leftMaxWidth.set(Math.max(((POPDecisionNode) subNode).getDecisionWidth(), leftMaxWidth.get()));
			} else if(subNode instanceof POPSymbolNode) {
				leftMaxWidth.set(Math.max(((POPSymbolNode) subNode).getComponent().getWidth(), leftMaxWidth.get()));
			}
			
			subNode = ((POPSymbolNode) subNode).getOutFlowLine().getNextNode();
		}
		
		subNode = rightStartNode.getOutFlowLine().getNextNode();
		while(true) {
			if(subNode instanceof POPDecisionEndNode)
				break;
			
			if(subNode instanceof POPLoopNode) {
				rightMaxWidth.set(Math.max(((POPLoopNode) subNode).getLoopWidth(), rightMaxWidth.get()));
			} else if(subNode instanceof POPDecisionNode) {
				rightMaxWidth.set(Math.max(((POPDecisionNode) subNode).getDecisionWidth(), rightMaxWidth.get()));
			} else if(subNode instanceof POPSymbolNode) {
				rightMaxWidth.set(Math.max(((POPSymbolNode) subNode).getComponent().getWidth(), rightMaxWidth.get()));
			}
			
			subNode = ((POPSymbolNode) subNode).getOutFlowLine().getNextNode();
		}
		
	}
	
	public void adjustPosition() {
		resizeDecisionNode();
    	
    	Node subNode = leftStartNode;
		
		while(true) {
			if(subNode instanceof POPDecisionEndNode) {
				break;
			}
			
			if(subNode instanceof POPSymbolNode) {
				((POPSymbolNode) subNode).moveCenter();
			}
			subNode = ((POPSymbolNode) subNode).getOutFlowLine().getNextNode();
		}
		
		subNode = rightStartNode;
		while(true) {
			if(subNode instanceof POPDecisionEndNode) {
				break;
			}
			
			if(subNode instanceof POPSymbolNode) {
				((POPSymbolNode) subNode).moveCenter();
			}
			subNode = ((POPSymbolNode) subNode).getOutFlowLine().getNextNode();
		}
	}
	
	public void adjustPositionThread() {
		Bounds bound = component.getBoundsInParent();
		Bounds imgBound = imgView.getBoundsInLocal();
		Point2D pos = Point2D.ZERO;
		
//		if(leftFlowLine != null) {
//			pos = POPBoundManager.getLeftCenter(bound);
//			leftFlowLine.setStartPos(pos.getX(), pos.getY());
//			leftFlowLine.setEndPos(pos.getX() - ((leftMaxWidth - (bound.getWidth())) / 2), pos.getY());
//		}
//		
//		if(rightFlowLine != null) {
//			pos = POPBoundManager.getRightCenter(bound);
//			rightFlowLine.setStartPos(pos.getX(), pos.getY());
//			rightFlowLine.setEndPos(pos.getX() + ((rightMaxWidth - (bound.getWidth())) / 2), pos.getY());
//		}
//		
//		if(leftStartNode != null) {
//			leftStartNode.setLayoutX(leftFlowLine.getEndX());
//			leftStartNode.setLayoutY(leftFlowLine.getEndY());
//		}
//		
//		if(rightStartNode != null) {
//			rightStartNode.setLayoutX(rightFlowLine.getEndX());
//			rightStartNode.setLayoutY(rightFlowLine.getEndY());
//		}
//		
//		if(leftEndNode != null) {
//			leftEndNode.setLayoutX(leftStartNode.getLayoutX());
//		}
//		
//		if(rightEndNode != null) {
//			rightEndNode.setLayoutX(rightStartNode.getLayoutX());
//		}
//		
//		leftEndNode.getSideFlowLine().setStartX(leftEndNode.getLayoutX());
//		leftEndNode.getSideFlowLine().setStartY(leftEndNode.getLayoutY());
//		leftEndNode.getSideFlowLine().setEndX(rightEndNode.getLayoutX());
//		leftEndNode.getSideFlowLine().setEndY(rightEndNode.getLayoutY());
//		
//		rightEndNode.setLayoutY(leftEndNode.getLayoutY());
//		DragManager.isDecisionSync = true;
//		
//		System.out.println(DragManager.isAdjustPosSync);
//		if(outFlowLine != null && outFlowLine.getLoopNode() != null && DragManager.isAdjustPosSync) {
//			outFlowLine.getLoopNode().adjustPosition();
//			System.out.println(this);
//			System.out.println("1");
//		} else if(outFlowLine != null && outFlowLine.getDecisionNode() != null) {
//			outFlowLine.getDecisionNode().adjustPosition();
//			System.out.println("1-1");
//		}
		
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
		
		subNodes.add(leftFlowLine);
		subNodes.add(rightFlowLine);
		subNodes.add(leftStartNode);
		subNodes.add(rightStartNode);
		subNodes.add(leftEndNode);
		subNodes.add(rightEndNode);
		subNodes.add(leftEndNode.getSideFlowLine());
		
//		scriptArea.getComponent().getChildren().add(leftFlowLine);
//		scriptArea.getComponent().getChildren().add(rightFlowLine);
//		scriptArea.getComponent().getChildren().add(leftSubNode);
//		scriptArea.getComponent().getChildren().add(rightSubNode);
		
		
//		contents.getChildren().add(leftFlowLine);
//		contents.getChildren().add(rightFlowLine);
//		contents.getChildren().add(leftSubNode);
//		contents.getChildren().add(rightSubNode);
//		contents.getChildren().add(boundChecker);
//		scriptArea.getComponent().getChildren().add(boundChecker);
		
//		Bounds bound = component.getBoundsInParent();
//		Bounds imgBound = imgView.getBoundsInLocal();
//		Point2D pos = Point2D.ZERO;
//		
//		pos = POPBoundManager.getLeftCenter(bound);
//		leftFlowLine.setStartPos(pos.getX(), pos.getY());
//		leftFlowLine.setEndPos(pos.getX() - 10, pos.getY());
//		
//		pos = POPBoundManager.getRightCenter(bound);
//		rightFlowLine.setStartPos(pos.getX(), pos.getY());
//		rightFlowLine.setEndPos(pos.getX() + 10, pos.getY());
//		adjustPosition();
//		this.moveCenter();
		
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
//				outFlowLine.setStartY(leftEndNode.getLayoutY());
//			}
			
//			if(inFlowLine != null) {
//				inFlowLine.setEndX(newBound.getMinX() + (newBound.getWidth() / 2));
//				inFlowLine.setEndY(newBound.getMinY());
//			}
			
			component.setLayoutX((prevBound.getMinX() + (prevBound.getWidth() / 2)) - (newBound.getWidth() / 2));
			
			
			
		}
	}
	
	@Override
	protected void setOnBoundChangeListener() {
		component.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {

			@Override
			public void changed(ObservableValue<? extends Bounds> arg0, Bounds oldBound, Bounds newBound) {
				// TODO Auto-generated method stub
				
				adjustPositionThread();
				
//				if(newBound.getHeight() > imgView.getBoundsInLocal().getHeight()) {
//					System.out.println("Gg");
//					return;
//				}
					
				
//				Bounds bound = component.getBoundsInParent();
//				Bounds imgBound = imgView.getBoundsInLocal();
//				Point2D pos = Point2D.ZERO;
//				System.out.println("changed");
//				if(outFlowLine != null) {
//					outFlowLine.setStartX(newBound.getMinX() + (newBound.getWidth() / 2));
//					outFlowLine.setStartY(leftEndNode.getLayoutY());
//				}
//				
//				if(inFlowLine != null) {
//					inFlowLine.setEndX(newBound.getMinX() + (newBound.getWidth() / 2));
//					inFlowLine.setEndY(newBound.getMinY());
//				}
				
//				if(outFlowLine != null && outFlowLine.getLoopNode() != null) {
//					outFlowLine.getLoopNode().adjustPosition();
//					System.out.println("3");
//				}
				outFlowLine.pullNodesThread();
				
				
				initMaxWidth += newBound.getWidth() - oldBound.getWidth();
				
				
				
				
				
//				moveCenter();
			}
		});
	}
}
