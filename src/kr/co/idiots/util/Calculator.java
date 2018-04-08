package kr.co.idiots.util;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Calculator {
	private static ScriptEngineManager mgr = new ScriptEngineManager();
	private static ScriptEngine engine = mgr.getEngineByName("JavaScript");
	
	public static String eval(String value) throws ScriptException {
		System.out.println(engine.eval(value));
		return engine.eval(value).toString();
	}
}
