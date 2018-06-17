package kr.co.idiots.view;

import com.sun.tools.hat.internal.model.Root;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import kr.co.idiots.MainApp;
import kr.co.idiots.model.POPLoggedInMember;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Iterator;

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

    private void unselectAll() {
        Iterator<HBox> it = imgViewList.iterator();
        while(it.hasNext()) {
            HBox box = it.next();
            box.setStyle("");
        }
    }

}
