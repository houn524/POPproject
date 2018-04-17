package kr.co.idiots.model;

import javafx.scene.shape.Line;
import kr.co.idiots.model.symbol.POPSymbolNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPSideFlowLine extends Line {
	private POPSymbolNode prevNode;
	private POPSymbolNode nextNode;
		
	public POPSideFlowLine(POPSymbolNode prevNode) {
		this.prevNode = prevNode;
		setStrokeWidth(5.0f);
	}
	
	public void setStartPos(double x, double y) {
		setStartX(x);
		setStartY(y);
	}
	
	public void setEndPos(double x, double y) {
		setEndX(x);
		setEndY(y);
	}
}
