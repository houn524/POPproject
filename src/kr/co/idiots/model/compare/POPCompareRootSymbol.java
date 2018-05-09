package kr.co.idiots.model.compare;

import kr.co.idiots.model.POPBlank;
import kr.co.idiots.model.POPNodeType;
import kr.co.idiots.model.operation.POPOperationSymbol;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPCompareRootSymbol extends POPOperationSymbol {
	private POPBlank compareBlank;
	private String value;
	
	public POPCompareRootSymbol() {
		super();
		this.type = POPNodeType.Compare;
		
		compareBlank = new POPBlank(this);
		contents.getChildren().add(compareBlank);
		
		setInitWidth();
	}
}


