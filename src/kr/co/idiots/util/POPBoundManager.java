package kr.co.idiots.util;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;

public class POPBoundManager {
	
	public static Point2D getTopCenter(Bounds bound) {
		Point2D pos = new Point2D(bound.getMinX() + (bound.getWidth() / 2), bound.getMinY());
		
		return pos;
	}
	
	public static Point2D getLeftCenter(Bounds bound) {
		Point2D pos = new Point2D(bound.getMinX(), bound.getMinY() + (bound.getHeight() / 2));
		
		return pos;
	}
	
	public static Point2D getBottomCenter(Bounds bound) {
		Point2D pos = new Point2D(bound.getMinX() + (bound.getWidth() / 2), bound.getMaxY());
		
		return pos;
	}
	
	public static Point2D getRightCenter(Bounds bound) {
		Point2D pos = new Point2D(bound.getMaxX(), bound.getMinY() + (bound.getHeight() / 2));
		
		return pos;
	}
}
