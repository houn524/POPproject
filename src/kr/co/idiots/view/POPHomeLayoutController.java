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

//    @FXML private HBox imgContainer;
//    @FXML private Pane clipPane;
    @FXML private Pagination pagination;

    @FXML private void initialize() {
//        clipPane.setMaxSize(IMG_WIDTH, IMG_HEIGHT);
//        clipPane.setClip(new Rectangle(IMG_WIDTH, IMG_HEIGHT));

        imgViewList = new ArrayList<>();

        for(int i = 0; i < NUM_OF_IMGS; i++) {
            InputStream stream = getClass().getResourceAsStream("/images/Manual"+ (i + 1) + ".png");
            Image img = new Image(stream);
            ImageView imgView = new ImageView(img);

            imgViewList.add(imgView);
//            imgContainer.getChildren().add(imgView);
        }

        pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
        pagination.setPageFactory(new Callback<Integer, Node>() {

            @Override
            public Node call(Integer pageIndex) {
//                if(pageIndex >= 9)
//                    return null;
                return createPage(pageIndex);
            }
        });

//        startAnimation(imgContainer);
    }

    private ImageView createPage(int pageIndex) {
        return imgViewList.get(pageIndex);
    }

    //start animation
    private void startAnimation(final HBox hbox) {
        //error occured on (ActionEvent t) line
        //slide action
        EventHandler<ActionEvent> slideAction = (ActionEvent t) -> {
            TranslateTransition trans = new TranslateTransition(Duration.seconds(1.5), hbox);
            trans.setByX(-IMG_WIDTH);
            trans.setInterpolator(Interpolator.EASE_BOTH);
            trans.play();
        };
        //eventHandler
        EventHandler<ActionEvent> resetAction = (ActionEvent t) -> {
            TranslateTransition trans = new TranslateTransition(Duration.seconds(1), hbox);
            trans.setByX((NUM_OF_IMGS - 1) * IMG_WIDTH);
            trans.setInterpolator(Interpolator.EASE_BOTH);
            trans.play();
        };

        List<KeyFrame> keyFrames = new ArrayList<>();
        for (int i = 1; i <= NUM_OF_IMGS; i++) {
            if (i == NUM_OF_IMGS) {
                keyFrames.add(new KeyFrame(Duration.seconds(i * SLIDE_FREQ), resetAction));
            } else {
                keyFrames.add(new KeyFrame(Duration.seconds(i * SLIDE_FREQ), slideAction));
            }
        }
//add timeLine
        Timeline anim = new Timeline(keyFrames.toArray(new KeyFrame[NUM_OF_IMGS]));

        anim.setCycleCount(Timeline.INDEFINITE);
        anim.playFromStart();
    }
}
