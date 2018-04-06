package kr.co.idiots.model;

public class POPDocumentDataInput extends POPDataInput {
	
	public POPDocumentDataInput(POPNode parentNode) {
		super(parentNode);
		
		this.add(new POPOutputSymbol(this));
	}

}
