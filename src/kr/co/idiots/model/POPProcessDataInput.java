package kr.co.idiots.model;

public class POPProcessDataInput extends POPDataInput {
	
	public POPProcessDataInput(POPNode parentNode) {
		super(parentNode);
		
		POPEqualSymbol equalSymbol = new POPEqualSymbol();
		
		this.add(equalSymbol);
		equalSymbol.initialize(this);
	}
}
