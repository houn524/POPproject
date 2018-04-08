package kr.co.idiots.util;

public class TypeChecker {
	
	public static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}
	
	public static boolean isDouble(String value) {
		try {
			Double.parseDouble(value);
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}
}
