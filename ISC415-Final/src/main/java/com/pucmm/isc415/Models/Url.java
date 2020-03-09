package com.pucmm.isc415.Models;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.pucmm.isc415.Utils.XmlDateFormat;
import org.apache.commons.codec.binary.Base64;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.*;
import java.net.URL;
import java.sql.Timestamp;

@Entity
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class Url implements Serializable {

    @Id
    @Column (nullable = false, length=500)
    @Expose
    private String originalUrl;

    @Column (nullable = false)
    @Expose
    private String shortenedUrl;

    @ManyToOne
    @Expose(serialize = false)
    @XmlTransient
    private User owner;

    @XmlJavaTypeAdapter(XmlDateFormat.class)
    private Timestamp date;

    @Expose
    private String image_url;

    @Column( length=3000 )
    @Expose
    private String description;

    public Url() {

    }

    public Url(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public Url(String originalUrl, String shortenedUrl, User owner, Timestamp date, String image_url, String description) {
        this.originalUrl = originalUrl;
        this.shortenedUrl = shortenedUrl;
        this.owner = owner;
        this.date = date;
        this.image_url = image_url;
        this.description = description;
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

    public String getImage_url() { return image_url; }

    public void setImage_url(String image_url) { this.image_url = image_url; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {

        return String.format("Original: %s, Short: %s",
                this.originalUrl,
                this.shortenedUrl);
    }

    public JsonObject toJson () throws IOException {

        JsonObject json = new JsonObject();

        json.addProperty("original_url", this.originalUrl);
        json.addProperty("shortened_url", this.shortenedUrl);
        json.addProperty("created", this.date.getTime());
        json.addProperty("user", this.owner != null ? this.owner.getUsername() : "Anonymous");
        json.addProperty("preview_image", getImageBase64(this.image_url));
        json.addProperty("description", this.description);

        return json;
    }

    public String getImageBase64(String image_url) throws IOException {
        URL urlac = new URL(image_url);
        InputStream in = new BufferedInputStream(urlac.openStream());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int n = 0;
        while (-1!=(n=in.read(buf)))
        {
            out.write(buf, 0, n);
        }
        out.close();
        in.close();
        byte[] response = out.toByteArray();

        String encodedURL = new String(Base64.encodeBase64(response));

        return encodedURL;

    }
}
