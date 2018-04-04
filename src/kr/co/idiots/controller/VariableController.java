package kr.co.idiots.controller;

import java.util.HashMap;

public class VariableController {
	private HashMap<String, Object> variables;
	
	public VariableController() {
		variables = new HashMap<String, Object>();
	}
	
	public void addVariable(String name, Object value) {
		variables.put(name, value);
	}
	
	public Object getValue(String name) {
		return variables.get(name);
	}
}
