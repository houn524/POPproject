package kr.co.idiots.model.symbol;

import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import kr.co.idiots.model.POPFlowLine;
import kr.co.idiots.model.POPNodeType;
import kr.co.idiots.model.POPScriptArea;
import kr.co.idiots.model.POPSideFlowLine;
import kr.co.idiots.model.operation.POPCompareRootSymbol;
import kr.co.idiots.util.POPBoundManager;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPDecisionNode extends POPSymbolNode {
//	private Group contents;
	private Rectangle boundChecker;
	private ArrayList<Node> subNodes;
	
	private POPSideFlowLine leftFlowLine;
	private POPSideFlowLine rightFlowLine;
	
	private POPDecisionStartNode leftStartNode;
	private POPDecisionStartNode rightStartNode;
	
	private POPDecisionEndNode leftEndNode;
	private POPDecisionEndNode rightEndNode;
	
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
		rightStartNode = new POPDecisionStartNode(scriptArea);
		
		leftEndNode = new POPDecisionEndNode(scriptArea);
		rightEndNode = new POPDecisionEndNode(scriptArea);
		
		outFlowLine = new POPFlowLine();
		outFlowLine.setPrevNode(this);
				
		setOnBoundChangeListener();
	}
	
	public void adjustPosition() {
		Bounds bound = component.getBoundsInParent();
		Bounds imgBound = imgView.getBoundsInLocal();
		Point2D pos = Point2D.ZERO;
		
		if(leftFlowLine != null) {
			pos = POPBoundManager.getLeftCenter(bound);
			leftFlowLine.setStartPos(pos.getX(), pos.getY());
			leftFlowLine.setEndPos(pos.getX() - 10, pos.getY());
		}
		
		if(rightFlowLine != null) {
			pos = POPBoundManager.getRightCenter(bound);
			rightFlowLine.setStartPos(pos.getX(), pos.getY());
			rightFlowLine.setEndPos(pos.getX() + 10, pos.getY());
		}
		
		if(leftStartNode != null) {
			leftStartNode.setLayoutX(leftFlowLine.getEndX());
			leftStartNode.setLayoutY(leftFlowLine.getEndY());
		}
		
		if(rightStartNode != null) {
			rightStartNode.setLayoutX(rightFlowLine.getEndX());
			rightStartNode.setLayoutY(rightFlowLine.getEndY());
		}
		
		leftStartNode.getOutFlowLine().setNextNode(leftEndNode);
		leftEndNode.setInFlowLine(leftStartNode.getOutFlowLine());
		leftEndNode.moveCenter();
		rightStartNode.getOutFlowLine().setNextNode(rightEndNode);
		rightEndNode.setInFlowLine(rightStartNode.getOutFlowLine());
		rightEndNode.moveCenter();
		
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
		adjustPosition();
		this.moveCenter();
		
	}
	
	@Override
	protected void setOnBoundChangeListener() {
		component.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {

			@Override
			public void changed(ObservableValue<? extends Bounds> arg0, Bounds oldBound, Bounds newBound) {
				// TODO Auto-generated method stub
				
				adjustPosition();
//				if(newBound.getHeight() > imgView.getBoundsInLocal().getHeight()) {
//					System.out.println("Gg");
//					return;
//				}
					
				
//				Bounds bound = component.getBoundsInParent();
//				Bounds imgBound = imgView.getBoundsInLocal();
//				Point2D pos = Point2D.ZERO;
//				System.out.println("changed");
				if(outFlowLine != null) {
					outFlowLine.setStartX(newBound.getMinX() + (newBound.getWidth() / 2));
					outFlowLine.setStartY(newBound.getMaxY());
				}
				
				if(inFlowLine != null) {
					inFlowLine.setEndX(newBound.getMinX() + (newBound.getWidth() / 2));
					inFlowLine.setEndY(newBound.getMinY());
				}
				
//				boundChecker.setX(newBound.getMinX());
//				boundChecker.setY(newBound.getMinY());
//				boundChecker.setWidth(newBound.getWidth());
//				boundChecker.setHeight(newBound.getHeight());
//				
//				if(leftFlowLine != null) {
//					pos = POPBoundManager.getLeftCenter(bound);
//					leftFlowLine.setStartPos(pos.getX(), pos.getY());
//					leftFlowLine.setEndPos(pos.getX() - 10, pos.getY());
////					leftSubNode.setLayoutX(leftFlowLine.getEndX());
////					leftSubNode.setLayoutY(leftFlowLine.getEndY());
//					System.out.println("start : " + pos.getX() + ", " + pos.getY());
//					System.out.println("end : " + (pos.getX() - 10) + ", " + pos.getY());
//				}
//				
//				if(rightFlowLine != null) {
//					pos = POPBoundManager.getRightCenter(bound);
//					rightFlowLine.setStartPos(pos.getX(), pos.getY());
//					rightFlowLine.setEndPos(pos.getX() + 10, pos.getY());
////					rightFlowLine.setStartPos(imgBound.getWidth(), pos.getY());
////					rightFlowLine.setEndPos(imgBound.getWidth() + 10, pos.getY());
////					rightSubNode.setLayoutX(rightFlowLine.getEndX());
////					rightSubNode.setLayoutY(rightFlowLine.getEndY());
//				}
				
				moveCenter();
			}
		});
	}


}
