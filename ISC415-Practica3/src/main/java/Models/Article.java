package Models;

import java.sql.Timestamp;
import java.util.List;

public class Article {

    private String id;
    private String title;
    private String body;
    private User author;
    private Timestamp date;
    private List<Comment> comments;
    private List<Tag> tags;

    public Article() {

    }

    public Article(String id, String title, String body, User author, Timestamp date, List<Comment> comments, List<Tag> tags) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.author = author;
        this.date = date;
        this.comments = comments;
        this.tags = tags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
