package kr.co.idiots.model.symbol;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
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
	private Group contents;
	private Rectangle boundChecker;
	
	private POPSideFlowLine leftFlowLine;
	private POPSideFlowLine rightFlowLine;
	
	private POPDecisionSubNode leftSubNode;
	private POPDecisionSubNode rightSubNode;
	
	public POPDecisionNode(POPScriptArea scriptArea) {
		super(scriptArea, POPNodeType.Decision);
		// TODO Auto-generated constructor stub
		
		contents = new Group();
		contents.getChildren().add(component);
		
		Bounds bound = imgView.getBoundsInParent();
		Point2D pos = Point2D.ZERO;
		
		leftFlowLine = new POPSideFlowLine(this);
		pos = POPBoundManager.getLeftCenter(bound);
		leftFlowLine.setStartPos(pos.getX(), pos.getY());
		leftFlowLine.setEndPos(pos.getX() - 10, pos.getY());
//		System.out.println(bound);
//		component.getChildren().add(leftFlowLine);
		
		rightFlowLine = new POPSideFlowLine(this);
		pos = POPBoundManager.getRightCenter(bound);
		rightFlowLine.setStartPos(pos.getX(), pos.getY());
		rightFlowLine.setEndPos(pos.getX() + 10, pos.getY());
//		component.getChildren().add(rightFlowLine);
		
		leftSubNode = new POPDecisionSubNode(scriptArea);
		rightSubNode = new POPDecisionSubNode(scriptArea);
		
		outFlowLine = new POPFlowLine();
		outFlowLine.setPrevNode(this);
		
		boundChecker = new Rectangle(contents.getBoundsInParent().getMinX(), contents.getBoundsInParent().getMinY(),
				contents.getBoundsInParent().getWidth(), contents.getBoundsInParent().getHeight());
		
		setOnBoundChangeListener();
	}
	
	public void adjustPosition() {
		Bounds bound = component.getBoundsInParent();
		Bounds imgBound = imgView.getBoundsInLocal();
		Point2D pos = Point2D.ZERO;
		
		if(leftFlowLine != null) {
			pos = POPBoundManager.getLeftCenter(imgBound);
			leftFlowLine.setStartPos(0, pos.getY());
			leftFlowLine.setEndPos(-10, pos.getY());
//			leftSubNode.setLayoutX(leftFlowLine.getEndX());
//			leftSubNode.setLayoutY(leftFlowLine.getEndY());
		}
		
		if(rightFlowLine != null) {
			pos = POPBoundManager.getRightCenter(imgBound);
			rightFlowLine.setStartPos(pos.getX(), pos.getY());
			rightFlowLine.setEndPos(pos.getX() + 10, pos.getY());
//			rightFlowLine.setStartPos(imgBound.getWidth(), pos.getY());
//			rightFlowLine.setEndPos(imgBound.getWidth() + 10, pos.getY());
//			rightSubNode.setLayoutX(rightFlowLine.getEndX());
//			rightSubNode.setLayoutY(rightFlowLine.getEndY());
		}
		
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
		
		contents.getChildren().add(leftFlowLine);
		contents.getChildren().add(rightFlowLine);
//		contents.getChildren().add(leftSubNode);
//		contents.getChildren().add(rightSubNode);
//		contents.getChildren().add(boundChecker);
		scriptArea.getComponent().getChildren().add(boundChecker);
		
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
		contents.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {

			@Override
			public void changed(ObservableValue<? extends Bounds> arg0, Bounds oldBound, Bounds newBound) {
				// TODO Auto-generated method stub
				
//				adjustPosition();
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
				
				boundChecker.setX(newBound.getMinX());
				boundChecker.setY(newBound.getMinY());
				boundChecker.setWidth(newBound.getWidth());
				boundChecker.setHeight(newBound.getHeight());
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
