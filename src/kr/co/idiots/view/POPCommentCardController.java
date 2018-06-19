package kr.co.idiots.view;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import kr.co.idiots.MainApp;
import kr.co.idiots.model.POPComment;
import kr.co.idiots.model.POPLoggedInMember;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPCommentCardController {

    @FXML private Label lbAuthor;
    @FXML private Label lbDate;
    @FXML private Text txtContent;
    @FXML private VBox vBox;
    @FXML private Button btnDelete;

    private MainApp mainApp;
    private POPComment comment;
    private POPCommentCard card;

    public POPCommentCardController(MainApp mainApp, POPCommentCard card, POPComment comment) {
        this.mainApp = mainApp;
        this.card = card;
        this.comment = comment;
    }

    @FXML private void initialize() {
        lbAuthor.setText(comment.getAuthor());
        lbDate.setText(comment.getDate());
        txtContent.setText(comment.getContent());

        if(comment.getInputStream() != null) {
            HBox box = new HBox();
            ImageView imgView = new ImageView(new Image(comment.getInputStream()));
            box.getChildren().add(imgView);
            box.setPadding(new Insets(30, 30, 30, 30));

            vBox.getChildren().add(box);
        }

        if(comment.getAuthor().equals(POPLoggedInMember.getInstance().getMember().getId()))
            btnDelete.setVisible(true);
        else
            btnDelete.setVisible(false);

        btnDelete.setOnAction(event -> {
            mainApp.getConnector().deleteComment(comment.getNumber());
            card.getPostController().updateComments();
        });
    }
}
