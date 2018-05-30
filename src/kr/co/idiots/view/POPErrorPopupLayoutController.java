package kr.co.idiots.view;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import kr.co.idiots.MainApp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPErrorPopupLayoutController {
	@FXML
	private Label label;
	
	private final BooleanProperty isCommit = new SimpleBooleanProperty();
	public BooleanProperty isCommitProperty() {
        return isCommit;
    }
	
	public final boolean getIsCommit() {
        return isCommitProperty().get();
    }

    public final void setIsCommit(boolean isCommit) {
        isCommitProperty().set(isCommit);
    }
	
	private MainApp mainApp;
	//private POPSolvingLayoutController solvingController;
	private RootLayoutController rootController;
	
	public POPErrorPopupLayoutController() {
	}
	
	public POPErrorPopupLayoutController setText(String text) {
		label.setText(text);
		
		return this;
	}
	
	@FXML
	public void commit() {
		setIsCommit(true);
	}
}
