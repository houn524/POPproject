package kr.co.idiots.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class POPPost {

    private final IntegerProperty number;
    private final StringProperty title;
    private final StringProperty content;
    private final StringProperty author;
    private final IntegerProperty commentCount;
    private final StringProperty date;
    private final IntegerProperty flowchartId;
    private final IntegerProperty problemNumber;

    public POPPost() { this(null, null, null, null, null, null, null, null); }

    public POPPost(Integer number, String title, String content, String author, Integer commentCount,
                   String date, Integer flowchartId, Integer problemNumber) {
        this.number = new SimpleIntegerProperty(number);
        this.title = new SimpleStringProperty(title);
        this.content = new SimpleStringProperty(content);
        this.author = new SimpleStringProperty(author);
        this.commentCount = new SimpleIntegerProperty(commentCount);
        this.date = new SimpleStringProperty(date);
        this.flowchartId = new SimpleIntegerProperty(flowchartId);
        this.problemNumber = new SimpleIntegerProperty(problemNumber);
    }

    public int getNumber() {
        return number.get();
    }

    public IntegerProperty numberProperty() {
        return number;
    }

    public void setNumber(int number) {
        this.number.set(number);
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getContent() {
        return content.get();
    }

    public StringProperty contentProperty() {
        return content;
    }

    public void setContent(String content) {
        this.content.set(content);
    }

    public String getAuthor() {
        return author.get();
    }

    public StringProperty authorProperty() {
        return author;
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public int getCommentCount() {
        return commentCount.get();
    }

    public IntegerProperty commentCountProperty() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount.set(commentCount);
    }

    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public int getFlowchartId() {
        return flowchartId.get();
    }

    public IntegerProperty flowchartIdProperty() {
        return flowchartId;
    }

    public void setFlowchartId(int flowchartId) {
        this.flowchartId.set(flowchartId);
    }

    public int getProblemNumber() {
        return problemNumber.get();
    }

    public IntegerProperty problemNumberProperty() {
        return problemNumber;
    }

    public void setProblemNumber(int problemNumber) {
        this.problemNumber.set(problemNumber);
    }
}
