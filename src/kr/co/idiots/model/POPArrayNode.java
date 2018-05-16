package kr.co.idiots.model;

import java.util.ArrayList;

import kr.co.idiots.model.operation.POPOperationSymbol;
import kr.co.idiots.util.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPArrayNode extends POPVariableNode {

	private ArrayList<Object> array;
	private POPIndexBlank indexBlank;
	private final double hgap = 3;
	
	public POPArrayNode(POPScriptArea scriptArea, String name) {
		super(scriptArea, name, POPNodeType.Array);
		
		indexBlank = new POPIndexBlank(this);
		contents.getChildren().add(indexBlank);
		contents.setHgap(hgap);
		
		resizeContents();
	}
	
	public void resizeContents() {
		imgView.setFitWidth(TextUtils.computeTextWidth(lbName.getFont(), lbName.getText(), 0.0D) + 
				TextUtils.computeTextWidth(indexBlank.getFont(), indexBlank.getText(), 0.0D) + 20 +
				20);
		contents.setPrefWrapLength(imgView.getBoundsInLocal().getWidth());
		contents.setMinWidth(imgView.getBoundsInLocal().getWidth());
		
		if(parentSymbol != null) {
			parentSymbol.setContentsAutoSize();
		}
	}
	
	public void initialize(POPOperationSymbol parentSymbol) {
		super.initialize(parentSymbol);
		
		indexBlank.setEditable(true);
	}
}
