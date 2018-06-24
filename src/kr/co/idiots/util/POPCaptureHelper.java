package kr.co.idiots.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;

public class POPCaptureHelper {

    public InputStream doSave(Stage stage, Node node) {
//        File file = null;
//        try {
//            file = File.createTempFile("temp_", ".png", new File(getClass().getResource("").getPath()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        file.deleteOnExit();
//        if(file != null) {
            return saveFile(node);
//        } else {
//            return null;
//        }
    }

    private InputStream saveFile(Node node) {
        WritableImage image = new WritableImage((int) node.getBoundsInLocal().getWidth() + 10, (int) node.getBoundsInLocal().getHeight());
        node.snapshot(null, image);
//        File imageFile = file;
        BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
		    ImageIO.write(bImage, "png", outputStream);
		    byte[] res  = outputStream.toByteArray();
		    InputStream inputStream = new ByteArrayInputStream(res);
		    
		    return inputStream;
		} catch (IOException e) {
		    e.printStackTrace();
		}
//        	InputStream is = image.
//            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", imageFile);
//            return imageFile;

        return null;
    }
}
