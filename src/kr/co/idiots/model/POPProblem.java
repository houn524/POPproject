package kr.co.idiots.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class POPProblem {
	
	private final IntegerProperty number;
	private final StringProperty title;
	
	public POPProblem() {
		this(null, null);
	}
	
	public POPProblem(Integer number, String title) {
		this.number = new SimpleIntegerProperty(number);
		this.title = new SimpleStringProperty(title);
	}
	
	public int getNumber() {
		return number.get();
	}
	
	public void setNumber(int number) {
		this.number.set(number);
	}
	
	public IntegerProperty numberProperty() {
		return number;
	}
	
	public String getTitle() {
		return title.get();
	}
	
	public void setTitle(String title) {
		this.title.set(title);
	}
	
	public StringProperty titleProperty() {
		return title;
	}
	
}
