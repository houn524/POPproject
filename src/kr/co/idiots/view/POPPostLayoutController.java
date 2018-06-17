package kr.co.idiots.view;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import kr.co.idiots.MainApp;
import kr.co.idiots.model.POPComment;
import kr.co.idiots.model.POPLoggedInMember;
import kr.co.idiots.model.POPPost;
import kr.co.idiots.model.POPProblem;
import lombok.Getter;
import lombok.Setter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

@Getter
@Setter
public class POPPostLayoutController {

    @FXML private Label lbTitle;
    @FXML private Label lbProblem;
    @FXML private Label lbAuthor;
    @FXML private Label lbDate;
    @FXML private Text content;

    @FXML private VBox vBox;
    @FXML private VBox commentBox;
    @FXML private Button btnDelete;

    @FXML private Button btnWriteComment;
    @FXML private TextArea commentContent;
    @FXML private Button btnPreview;
    @FXML private CheckBox checkFlowchart;
    @FXML private Button btnSaveComment;

    private POPPost post;

    private MainApp mainApp;

    private RootLayoutController rootController;

    private int commentFlowchartId = 0;

    private ArrayList<POPComment> commentList;

    public POPPostLayoutController(MainApp mainApp, POPPost post) {
        this.mainApp = mainApp;
        this.post = post;
    }

    public void updateComments() {
        commentBox.getChildren().clear();

        commentList = mainApp.getConnector().loadComments(post.getNumber());
        Iterator<POPComment> it = commentList.iterator();
        while(it.hasNext()) {
            POPCommentCard commentCard = new POPCommentCard(mainApp, this, it.next());
            commentBox.getChildren().add(commentCard.getRootBox());
        }
    }

    @FXML private void initialize() {
        lbTitle.setText(post.getTitle());
        lbProblem.setText(String.valueOf(post.getProblemNumber()) + " : " + mainApp.getConnector().loadProblemTitle(post.getProblemNumber()));
        lbAuthor.setText(post.getAuthor());
        lbDate.setText(post.getDate());
        content.setText(post.getContent());

        updateComments();

        if(post.getImage() != null) {
            HBox box = new HBox();
            ImageView imgView = new ImageView(post.getImage());

            box.getChildren().add(imgView);

            box.setPadding(new Insets(30, 30, 30, 30));

            vBox.getChildren().add(box);
        }

        if(post.getAuthor().equals(POPLoggedInMember.getInstance().getMember().getId())) {
            btnDelete.setVisible(true);

            btnDelete.setOnAction(event -> {
                mainApp.getConnector().deletePost(post.getNumber());
                rootController.showPOPBoardLayout();
            });
        }

        lbProblem.setOnMouseEntered(event -> {
            mainApp.getPrimaryStage().getScene().setCursor(Cursor.HAND);
            lbProblem.setUnderline(true);
        });

        lbProblem.setOnMouseExited(event -> {
            mainApp.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
            lbProblem.setUnderline(false);
        });

        lbProblem.setOnMouseClicked(event -> {
            POPProblem problem = mainApp.getConnector().loadProblemByNumber(post.getProblemNumber());
            mainApp.getRootController().showPOPMainLayout(problem);
        });

        btnWriteComment.setOnAction(event -> {
            commentContent.setVisible(!commentContent.isVisible());
            commentContent.setText("");
            btnPreview.setVisible(!btnPreview.isVisible());
            checkFlowchart.setVisible(!checkFlowchart.isVisible());
            checkFlowchart.setSelected(false);
            btnSaveComment.setVisible(!btnSaveComment.isVisible());
        });

        btnPreview.setOnAction(event -> {
            int id = mainApp.getConnector().loadFlowchartId(
                    POPLoggedInMember.getInstance().getMember().getId(),
                    post.getProblemNumber()
            );
            rootController.showPOPPreviewImageLayout(mainApp.getConnector().loadFlowchartId(
                    POPLoggedInMember.getInstance().getMember().getId(),
                    post.getProblemNumber()
            ));
        });

        btnSaveComment.setOnAction(event -> {
            if(checkFlowchart.isSelected()) {
                commentFlowchartId = mainApp.getConnector().loadFlowchartId(
                        POPLoggedInMember.getInstance().getMember().getId(),
                        post.getProblemNumber()
                );
            } else {
                commentFlowchartId = 0;
            }

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            POPComment comment = new POPComment(
                    0,
                    commentContent.getText(),
                    POPLoggedInMember.getInstance().getMember().getId(),
                    dateFormat.format(new java.util.Date()),
                    commentFlowchartId,
                    post.getNumber()
            );
            mainApp.getConnector().insertComment(comment);

            commentContent.setVisible(!commentContent.isVisible());
            commentContent.setText("");
            btnPreview.setVisible(!btnPreview.isVisible());
            checkFlowchart.setVisible(!checkFlowchart.isVisible());
            checkFlowchart.setSelected(false);
            btnSaveComment.setVisible(!btnSaveComment.isVisible());
            updateComments();
        });
    }
}
