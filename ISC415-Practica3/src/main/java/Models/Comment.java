package Models;

public class Comment {

    private String id;
    private String comment;
    private User author;

    public Comment() {

    }

    public Comment(String id, String comment, User author) {
        this.id = id;
        this.comment = comment;
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

}
