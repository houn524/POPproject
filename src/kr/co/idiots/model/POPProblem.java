package kr.co.idiots.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class POPProblem {
	
	private final IntegerProperty number;
	private final StringProperty title;
	private final StringProperty content;
	private final StringProperty input;
	private final StringProperty output;
	private final StringProperty difficulty;
	
	public POPProblem() {
		this(null, null, null, null, null, null);
	}
	
	public POPProblem(Integer number, String title, String content, String input, String output, String difficulty) {
		this.number = new SimpleIntegerProperty(number);
		this.title = new SimpleStringProperty(title);
		this.content = new SimpleStringProperty(content);
		this.input = new SimpleStringProperty(input);
		this.output = new SimpleStringProperty(output);
		this.difficulty = new SimpleStringProperty(difficulty);
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
	
	public String getContent() {
		return content.get();
	}
	
	public void setContent(String content) {
		this.content.set(content);
	}
	
	public StringProperty contentProperty() {
		return content;
	}
	
	public String getInput() {
		return input.get();
	}
	
	public void setInput(String input) {
		this.input.set(input);
	}
	
	public StringProperty inputProperty() {
		return input;
	}
	
	public String getOutput() {
		return output.get();
	}
	
	public void setOutput(String output) {
		this.output.set(output);
	}
	
	public StringProperty outputProperty() {
		return output;
	}
	
	public String getDifficulty() {
		return difficulty.get();
	}
	
	public void setDifficulty(String difficulty) {
		this.difficulty.set(difficulty);
	}
	
	public StringProperty difficultyProperty() {
		return difficulty;
	}
	
}
