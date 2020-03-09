package com.pucmm.isc415.Models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
public class URL implements Serializable {

    @Id
    @Column (nullable = false, length=500)
    private String originalUrl;

    @Column (nullable = false)
    private String shortenedUrl;

    @ManyToOne
    private User owner;

    private Timestamp date;

    public URL() {

    }

    public URL(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public URL(String originalUrl, String shortenedUrl, User owner, Timestamp date) {
        this.originalUrl = originalUrl;
        this.shortenedUrl = shortenedUrl;
        this.owner = owner;
        this.date = date;
    }

    public Timestamp getDate() { return date; }

    public void setDate(Timestamp date) { this.date = date; }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getShortenedUrl() {
        return shortenedUrl;
    }

    public void setShortenedUrl(String shortenedUrl) {
        this.shortenedUrl = shortenedUrl;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {

        return String.format("Original: %s, Short: %s",
                this.originalUrl,
                this.shortenedUrl);
    }
}
