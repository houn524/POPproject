package kr.co.idiots.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPComment {

    private final int number;
    private final String content;
    private final String author;
    private final String date;
    private final int flowchartId;
    private final int postNumber;

    public POPComment() {
        this(0, null, null, null, 0, 0);
    }

    public POPComment(Integer number, String content, String author, String date, Integer flowchartId, Integer postNumber) {
        this.number = number;
        this.content = content;
        this.author = author;
        this.date = date;
        this.flowchartId = flowchartId == null ? 0 : flowchartId;
        this.postNumber = postNumber;
    }
}
