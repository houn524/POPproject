package kr.co.idiots.util;

import javax.script.Compilable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Calculator {
	private static ScriptEngineManager mgr = new ScriptEngineManager();
	
	private static ScriptEngine engine = mgr.getEngineByName("JavaScript");
	
	public static String eval(String value) throws ScriptException {
		return engine.eval(value).toString();
	}
	
	public static boolean compare(String value) {
		boolean result = false;
		
		try {
			engine.eval("var test = " + value + ";");
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		result = Boolean.parseBoolean(engine.get("test").toString());
		
		return result;
	}
}
