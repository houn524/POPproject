package kr.co.idiots.model;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.shape.Line;
import kr.co.idiots.model.symbol.POPSymbolNode;

public class POPSideArrowFlowLine extends Group {
	
	private Line line;

	private static final double arrowLength = 10;
    private static final double arrowWidth = 7;
	
	public POPSideArrowFlowLine() {
		this(new Line(), new Line(), new Line());
	}
	
	private POPSideArrowFlowLine(Line line, Line arrow1, Line arrow2) {
		super(line, arrow1, arrow2);
		this.line = line;
		
		line.setStrokeWidth(3.0f);
        arrow1.setStrokeWidth(3.0f);
        arrow2.setStrokeWidth(3.0f);
        
        InvalidationListener updater = new InvalidationListener() {

			@Override
			public void invalidated(Observable arg0) {
				// TODO Auto-generated method stub

				double ex = getEndX();
				double ey = getEndY();
				
	            double sx = getStartX();
	            double sy = getStartY();
	            
	            arrow1.setEndX(ex);
	            arrow1.setEndY(ey);
	            arrow2.setEndX(ex);
	            arrow2.setEndY(ey);

	            if (ex == sx && ey == sy) {
	                arrow1.setStartX(ex);
	                arrow1.setStartY(ey);
	                arrow2.setStartX(ex);
	                arrow2.setStartY(ey);
	            } else {
	                double factor = arrowLength / Math.hypot(sx-ex, sy-ey);
	                double factorO = arrowWidth / Math.hypot(sx-ex, sy-ey);

	                double dx = (sx - ex) * factor;
	                double dy = (sy - ey) * factor;
	                
	                double ox = (sx - ex) * factorO;
	                double oy = (sy - ey) * factorO;

	                arrow1.setStartX(ex + dx - oy);
	                arrow1.setStartY(ey + dy + ox);
	                arrow2.setStartX(ex + dx + oy);
	                arrow2.setStartY(ey + dy - ox);
	            }
			}
        	
        };
        
        startXProperty().addListener(updater);
        startYProperty().addListener(updater);
        endXProperty().addListener(updater);
        endYProperty().addListener(updater);
        updater.invalidated(null);
	}
	
	public final void setStartX(double x) {
		line.setStartX(x);
	}
	
	public final void setStartY(double y) {
		line.setStartY(y);
	}
	
	public final void setEndX(double x) {
		line.setEndX(x);
	}
	
	public final void setEndY(double y) {
		line.setEndY(y);
	}
	
	public final double getStartX() {
        return line.getStartX();
    }
	
	public final double getStartY() {
        return line.getStartY();
    }
	
	public final double getEndX() {
        return line.getEndX();
    }
	
	public final double getEndY() {
        return line.getEndY();
    }
	
	public final DoubleProperty startXProperty() {
        return line.startXProperty();
    }
	
	public final DoubleProperty startYProperty() {
        return line.startYProperty();
    }
	
	public final DoubleProperty endXProperty() {
        return line.endXProperty();
    }
	
	public final DoubleProperty endYProperty() {
        return line.endYProperty();
    }
}
