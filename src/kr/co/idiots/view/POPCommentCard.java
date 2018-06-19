package kr.co.idiots.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import kr.co.idiots.MainApp;
import kr.co.idiots.model.POPComment;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@Getter
@Setter
public class POPCommentCard {

    private VBox rootBox;
    private POPCommentCardController controller;

    private MainApp mainApp;
    private POPComment comment;
    private POPPostLayoutController postController;

    public POPCommentCard(MainApp mainApp, POPPostLayoutController postController, POPComment comment) {
        this.mainApp = mainApp;
        this.postController = postController;
        this.comment = comment;
        try {
            controller = new POPCommentCardController(mainApp, this, comment);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/POPCommentCard.fxml"));
            loader.setControllerFactory(c -> {
                return controller;
            });
            rootBox = (VBox)loader.load();

        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
