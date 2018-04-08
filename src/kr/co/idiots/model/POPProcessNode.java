package kr.co.idiots.model;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;

public class POPProcessNode extends POPSymbolNode {
	
	public POPProcessNode(POPScriptArea scriptArea) {
		super(scriptArea, POPNodeType.Process);

//		dataInput = new POPProcessDataInput();
//		component.getChildren().add(dataInput);
		//component.setTranslateX(50d);
		//component.setTranslateY(50d);
		
		outFlowLine = new POPFlowLine();
		outFlowLine.setPrevNode(this);
		
//		setOnBoundChangeListener();
//		flowLine.setPrevNode(this);
	}
	
	@Override
	public void initialize() {
		
		POPProcessDataInput dataInput = new POPProcessDataInput(this);
//    	getComponent().getChildren().add(dataInput);
    	setDataInput(dataInput);
    	StackPane.setAlignment(dataInput, Pos.CENTER);
    	component.getChildren().add(dataInput);
    	
//    	setOnDataInputBoundChangeListener();
//		System.out.println("dataInput : " + dataInput.getBoundsInLocal());
//		imgView.setFitWidth(dataInput.getBoundsInLocal().getWidth() + 0.1);
//		imgView.setFitHeight(dataInput.getBoundsInLocal().getHeight() + 0.5);
		this.moveCenter();
    	
    	
//    	System.out.println("DataInputY :" + dataInput.getLayoutY());
//    	component.setAlignment(dataInput, Pos.CENTER);
	}

//	@Override
//	public void setNextNode(POPNode nextNode) {
//		// TODO Auto-generated method stub
//		this.nextNode = nextNode;
//		flowLine.setNextNode(nextNode);
//	}
	
}
