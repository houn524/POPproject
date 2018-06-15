package kr.co.idiots.view;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import kr.co.idiots.MainApp;
import kr.co.idiots.model.POPPost;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPPostLayoutController {

    @FXML private Label lbTitle;
    @FXML private Label lbProblem;
    @FXML private Label lbAuthor;
    @FXML private Label lbDate;
    @FXML private Text content;

    @FXML private VBox vBox;

    private POPPost post;

    private MainApp mainApp;

    private RootLayoutController rootController;

    public POPPostLayoutController(MainApp mainApp, POPPost post) {
        this.mainApp = mainApp;
        this.post = post;


    }

    @FXML private void initialize() {
        lbTitle.setText(post.getTitle());
        lbProblem.setText(String.valueOf(post.getProblemNumber()) + " : " + mainApp.getConnector().loadProblemTitle(post.getProblemNumber()));
        lbAuthor.setText(post.getAuthor());
        lbDate.setText(post.getDate());
        content.setText(post.getContent());

        if(post.getImage() != null) {
            HBox box = new HBox();
            ImageView imgView = new ImageView(post.getImage());

            box.getChildren().add(imgView);

            box.setPadding(new Insets(30, 30, 30, 30));

            vBox.getChildren().add(box);
        }
    }
}
