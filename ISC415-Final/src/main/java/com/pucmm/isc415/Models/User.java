package com.pucmm.isc415.Models;

import com.google.gson.annotations.Expose;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Entity
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class User implements Serializable {

    @Id
    @Expose
    private String username;

    @Column(nullable = false)
    @Expose
    private String name;

    @Column (unique = true, nullable = false)
    @Expose
    private String email;

    @Column(nullable = false)
    @Expose
    private boolean admin;

    @Column(nullable = false)
    @Expose
    private Calendar registerDate;

    @Column(nullable = false)
    private String password;

    @OneToMany ( mappedBy = "owner", fetch = FetchType.EAGER)
    @Expose
    private List<Url> myUrls;

    public User() {

    }

    public User(String username, String name, String email, boolean admin, Calendar registerDate, String password) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.admin = admin;
        this.registerDate = registerDate;
        this.password = password;
        this.myUrls = new ArrayList<>();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public Calendar getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Calendar registerDate) {
        this.registerDate = registerDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Url> getMyUrls() {
        return myUrls;
    }

    public void setMyUrls(List<Url> myUrls) {
        this.myUrls = myUrls;
    }

    @Override
    public String toString() {

        return String.format("USER - Username: %s, Name: %s, Email: %s, Admin: %b",
                this.username,
                this.name,
                this.email,
                this.admin);
    }
}
