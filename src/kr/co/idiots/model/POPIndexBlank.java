package kr.co.idiots.model;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import kr.co.idiots.util.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPIndexBlank extends TextField {

	private POPArrayNode parentNode;
	
	public POPIndexBlank(POPArrayNode parentNode) {
		this.parentNode = parentNode;
		this.setPrefSize(10, 20);
		this.setEditable(false);
		
		setOnIndexBlankDrag();
		setOnIndexBlankChange();
	}

	private void setOnIndexBlankChange() {
		textProperty().addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue arg0, Object oldValue, Object newValue) {
				// TODO Auto-generated method stub
				
				if(getText().isEmpty())
					setPrefWidth(0);
				else
					setPrefWidth(TextUtils.computeTextWidth(getFont(), getText(), 0.0D) + 20);
				
				parentNode.resizeContents();
//				parentNode.getParentSymbol().setContentsAutoSize();
			}
		});
	}

	private void setOnIndexBlankDrag() {
		
	}
}
