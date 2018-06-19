package kr.co.idiots.view;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import kr.co.idiots.MainApp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPPreviewImageLayoutController {

    @FXML private ImageView imgView;

    private MainApp mainApp;
    private ImageView selectedImage;
    private int flowchartId;

    private ArrayList<HBox> imgViewList;
    private ArrayList<Integer> flowchartIdList;

    private RootLayoutController rootController;

    public POPPreviewImageLayoutController(MainApp mainApp, int flowchartId) {
        this.mainApp = mainApp;
        this.flowchartId = flowchartId;
    }

    @FXML private void initialize() {
        Image image = mainApp.getConnector().loadImage(flowchartId);
        if(image == null) {
            return;
        }
        imgView.setImage(mainApp.getConnector().loadImage(flowchartId));
        imgView.setFitWidth(imgView.getImage().getWidth() / 2);
        imgView.setFitHeight(imgView.getImage().getHeight() / 2);
    }
}
