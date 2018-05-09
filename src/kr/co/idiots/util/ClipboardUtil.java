package kr.co.idiots.util;

import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class ClipboardUtil {
	
	public static ClipboardContent makeClipboardContent(MouseEvent event, Node child, String text) {
		ClipboardContent cb = new ClipboardContent();
		if (text != null) {
            cb.put(DataFormat.PLAIN_TEXT, text);
        }
		if(!event.isShiftDown()) {
			SnapshotParameters params = new SnapshotParameters();
			params.setFill(Color.TRANSPARENT);
			Bounds b = child.getBoundsInParent();
			double f = 0;
			params.setViewport(new Rectangle2D(b.getMinX() - f, b.getMinY() - f, b.getWidth() + f + f, b.getHeight() + f + f));
			WritableImage image = child.snapshot(params, null);
			cb.put(DataFormat.IMAGE, image);
		}
		return cb;
	}
	
	public static Image makeSnapShot(Node node) {
		SnapshotParameters params = new SnapshotParameters();
		Bounds b = node.getBoundsInParent();
		double f = 0;
		params.setViewport(new Rectangle2D(b.getMinX() - f, b.getMinY() - f, b.getWidth() + f + f, b.getHeight() + f + f));
		WritableImage image = node.snapshot(params, null);
		return image;
	}
}
