package kr.co.idiots.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPLoggedInMember {
	
	private POPMember member;
	private static POPLoggedInMember instance = new POPLoggedInMember();
	
	public static POPLoggedInMember getInstance() {
		return instance;
	}
}
