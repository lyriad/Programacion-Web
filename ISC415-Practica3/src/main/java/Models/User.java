package Models;

public class User {

    private String id;
    private String username;
    private String name;
    private String password;
    private boolean admin;
    private boolean author;

    public User(){

    }

    public User(String id, String username, String name, String password, boolean admin, boolean author) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.password = password;
        this.admin = admin;
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isAuthor() {
        return author;
    }

    public void setAuthor(boolean author) {
        this.author = author;
    }
}
