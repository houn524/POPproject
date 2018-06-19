package kr.co.idiots.model.symbol;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import kr.co.idiots.model.POPNodeType;
import kr.co.idiots.model.POPScriptArea;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPLoopEndNode extends POPSymbolNode {

	private POPLoopNode loopNode;
	
	public POPLoopEndNode(POPScriptArea scriptArea) {
		super(scriptArea, POPNodeType.LoopSub);

		setOnBoundChangeListener();
	}

	@Override
	protected void setOnBoundChangeListener() {
		component.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {

			@Override
			public void changed(ObservableValue<? extends Bounds> arg0, Bounds oldBound, Bounds newBound) {
				if(imgView != null && newBound.getHeight() > imgView.getBoundsInLocal().getHeight())
					return;
			}
		});
	}
}
