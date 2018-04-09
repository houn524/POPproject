package kr.co.idiots.model;

import java.io.IOException;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import kr.co.idiots.CodeGenerator;
import kr.co.idiots.MainApp;

public class POPScriptArea {
	private AnchorPane component;
	private ScrollPane scrollPane;
	private POPSymbolNode startNode;
	private POPNode nodePointer;
	private CodeGenerator generator;
	
	
	private DoubleProperty zoomScale = new SimpleDoubleProperty(1.0);
	
	public POPScriptArea(AnchorPane component, ScrollPane scrollPane) {
		this.component = component;
		this.scrollPane = scrollPane;
		
		generator = new CodeGenerator();
		
		component.scaleXProperty().bind(zoomScale);
		component.scaleYProperty().bind(zoomScale);
		
		scrollPane.addEventFilter(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>() {

			@Override
			public void handle(ScrollEvent event) {
				// TODO Auto-generated method stub
				if(MainApp.pressedKeys.contains(KeyCode.CONTROL))
					zoomScale.set(zoomScale.get() + event.getDeltaY() * 0.001);
			}

		});
	}
		
	public AnchorPane getComponent() { return component; }
	
	public void add(POPSymbolNode node) {
		component.getChildren().add(node.getComponent());
		if(node.getOutFlowLine() != null)
			component.getChildren().add(node.getOutFlowLine());
	}

	public POPNode getStartNode() {
		return startNode;
	}

	public void setStartNode(POPSymbolNode startNode) {
		this.startNode = startNode;
		nodePointer = startNode;
	}
	
	public String generate() throws IOException, NoSuchFieldException {
		
		return generator.generate(startNode);
	}
}
