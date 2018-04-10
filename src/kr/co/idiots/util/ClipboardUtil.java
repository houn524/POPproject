package kr.co.idiots.util;

import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class ClipboardUtil {
	
	public static ClipboardContent makeClipboardContent(MouseEvent event, Node child) {
		ClipboardContent cb = new ClipboardContent();
		if(!event.isShiftDown()) {
			SnapshotParameters params = new SnapshotParameters();
			params.setFill(Color.TRANSPARENT);
			Bounds b = child.getBoundsInParent();
			double f = 10;
			params.setViewport(new Rectangle2D(b.getMinX() - f, b.getMinY() - f, b.getWidth() + f + f, b.getHeight() + f + f));
			
			WritableImage image = child.snapshot(params, null);
			cb.putImage(image);
//			cb.put(DataFormat.IMAGE, image);
			
//			try {
//				File tmpFile = File.createTempFile("snapshot", ".png");
//				LinkedList<File> list = new LinkedList<File>();
//				ImageIO.write(SwingFXUtils.fromFXImage(image,  null), "png", tmpFile);
//				list.add(tmpFile);
//				cb.put(DataFormat.FILES, list);
//			} catch(Exception e) {
//				
//			}
		}
		return cb;
	}
}
