package kr.co.idiots.util;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class POPCaptureHelper {

    public static File doSave(Stage stage, Node node) {
//        FileChooser fileChooser = new FileChooser();
        File file = null;
        try {
            file = File.createTempFile("temp_", ".png", new File(POPCaptureHelper.class.getResource("").getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        file.deleteOnExit();
        if(file != null) {
            return saveFile(file, node);
        } else {
            return null;
        }
    }

    private static File saveFile(File file, Node node) {
        WritableImage image = new WritableImage((int) node.getBoundsInLocal().getWidth() + 10, (int) node.getBoundsInLocal().getHeight());
        node.snapshot(null, image);
        File imageFile = file;
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", imageFile);
            return imageFile;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
