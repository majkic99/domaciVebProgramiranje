package rs.raf.demo.models;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Quote {
    private int id;
    private String author;
    private String title;
    private String text;
    private LocalDate date;
    private List<Subject> comments;

    public Quote() {

    }

    public Quote(int id, String author, String title, String text, List<Subject> comments, LocalDate date) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.text = text;
        this.comments = comments;
        this.date = date;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Subject> getComments() {
        return comments;
    }

    public void setComments(List<Subject> comments) {
        this.comments = comments;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
