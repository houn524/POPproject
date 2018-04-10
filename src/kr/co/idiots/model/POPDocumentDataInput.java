package kr.co.idiots.model;

public class POPDocumentDataInput extends POPDataInput {
	
	public POPDocumentDataInput(POPNode parentNode) {
		super(parentNode);
		
		POPOutputSymbol outputSymbol = new POPOutputSymbol();
		
		this.add(outputSymbol);
		outputSymbol.initialize(this);
	}

}
