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
public class POPSelectImageLayoutController {

    @FXML private FlowPane flowPane;
    @FXML private Button btnSelect;

    private MainApp mainApp;
    private ImageView selectedImage;
    private int selectedId;

    private ArrayList<HBox> imgViewList;
    private ArrayList<Integer> flowchartIdList;

    private RootLayoutController rootController;

    public POPSelectImageLayoutController(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML private void initialize() {
        ArrayList<Image> imgList = null;
        imgViewList = new ArrayList<>();
        flowchartIdList = new ArrayList<>();
        imgList = mainApp.getConnector().loadImages(POPLoggedInMember.getInstance().getMember().getId());
        flowchartIdList = mainApp.getConnector().loadFlowchartIds(POPLoggedInMember.getInstance().getMember().getId());

        Iterator<Image> it = imgList.iterator();

        while(it.hasNext()) {
            ImageView imgView = new ImageView(it.next());
            imgView.setFitWidth(250);
            imgView.setFitHeight(250);

            HBox box = new HBox();
            box.setPrefWidth(250);
            box.setPrefHeight(250);
            box.getChildren().add(imgView);
            imgViewList.add(box);
            box.setOnMousePressed(event -> {
                unselectAll();
                box.setStyle("-fx-border-color: blue;");
                selectedImage = (ImageView) box.getChildren().get(0);
                selectedId = flowchartIdList.get(imgViewList.indexOf(box));
            });
            flowPane.getChildren().add(box);
        }

        btnSelect.setOnAction(event -> {
            rootController.getWritePostController().getImgView().setImage(selectedImage.getImage());
            rootController.getWritePostController().getImgView().setFitWidth(200);
            rootController.getWritePostController().getImgView().setFitHeight(200);
            rootController.getWritePostController().setFlowchartId(selectedId);
            rootController.setSelectedImageView(selectedImage);
            rootController.getSelectImageStage().close();
        });
    }

    private void unselectAll() {
        Iterator<HBox> it = imgViewList.iterator();
        while(it.hasNext()) {
            HBox box = it.next();
            box.setStyle("");
        }
    }

}
