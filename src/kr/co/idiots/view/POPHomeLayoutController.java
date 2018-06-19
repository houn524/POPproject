package kr.co.idiots.view;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class POPHomeLayoutController {

    private final double IMG_WIDTH = 1040;
    private final double IMG_HEIGHT = 560;

    private final int NUM_OF_IMGS = 9;
    private final int SLIDE_FREQ = 4; // in secs

    private ArrayList<ImageView> imgViewList;

    @FXML private Pagination pagination;

    @FXML private void initialize() {
        imgViewList = new ArrayList<>();

        for(int i = 0; i < NUM_OF_IMGS; i++) {
            InputStream stream = getClass().getResourceAsStream("/images/Manual"+ (i + 1) + ".png");
            Image img = new Image(stream);
            ImageView imgView = new ImageView(img);

            imgViewList.add(imgView);
        }

        pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                return createPage(pageIndex);
            }
        });
    }

    private ImageView createPage(int pageIndex) {
        return imgViewList.get(pageIndex);
    }
}
