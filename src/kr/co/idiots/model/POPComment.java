package kr.co.idiots.model;

import java.io.InputStream;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPComment {

    private final int number;
    private final String content;
    private final String author;
    private final String date;
    private final int postNumber;
    private InputStream inputStream;

    public POPComment() {
        this(0, null, null, null, 0, null);
    }

    public POPComment(Integer number, String content, String author, String date, Integer postNumber, InputStream inputStream) {
        this.number = number;
        this.content = content;
        this.author = author;
        this.date = date;
        this.postNumber = postNumber;
        this.inputStream = inputStream;
    }
}
