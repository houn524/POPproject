package kr.co.idiots.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPMember {
	private String id;
	private String pw;
	
	public POPMember() {
		this(null, null);
	}
	
	public POPMember(String id, String pw) {
		this.id = id;
		this.pw = pw;
	}
}
