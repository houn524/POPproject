package kr.co.idiots.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class POPProblem {
	
	private final IntegerProperty number;
	private final StringProperty title;
	private final StringProperty content;
	private final StringProperty input_example;
	private final StringProperty output_example;
	private final StringProperty input_case;
	private final StringProperty output_case;
	private final StringProperty difficulty;
	
	public POPProblem() {
		this(null, null, null, null, null, null, null, null);
	}
	
	public POPProblem(Integer number, String title, String content, String input_example, String output_example, String input_case, String output_case, String difficulty) {
		this.number = new SimpleIntegerProperty(number);
		this.title = new SimpleStringProperty(title);
		this.content = new SimpleStringProperty(content);
		this.input_example = new SimpleStringProperty(input_example);
		this.output_example = new SimpleStringProperty(output_example);
		this.input_case = new SimpleStringProperty(input_case);
		this.output_case = new SimpleStringProperty(output_case);
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
	
	public String getInputExample() {
		return input_example.get();
	}
	
	public void setInputExample(String inputExample) {
		this.input_example.set(inputExample);
	}
	
	public StringProperty inputEampleProperty() {
		return input_example;
	}
	
	public String getOutputExample() {
		return output_example.get();
	}
	
	public void setOutputExample(String outputExample) {
		this.output_example.set(outputExample);
	}
	
	public StringProperty outputExampleProperty() {
		return output_example;
	}
	
	public String getInputCase() {
		return input_case.get();
	}
	
	public void setInputCase(String inputCase) {
		this.input_case.set(inputCase);
	}
	
	public StringProperty inputCaseProperty() {
		return input_case;
	}
	
	public String getOutputCase() {
		return output_case.get();
	}
	
	public void setOutputCase(String outputCase) {
		this.output_case.set(outputCase);
	}
	
	public StringProperty outputCaseProperty() {
		return output_case;
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
