package com.pucmm.isc415.Models;

import javax.persistence.*;

@Entity
public class Visit {

    @Id
    private String uuid;

    @OneToOne
    private URL url;

    @Column (nullable = false)
    private String browser;

    @Column (nullable = false)
    private String os;

    @Column (nullable = false)
    private String ip;

    @Column (nullable = false)
    private long time;

    @Column (nullable = false)
    private String day;

    public Visit() {
    }

    public Visit(URL url, String browser, String os, String ip, long time, String day) {

        this.url = url;
        this.browser = browser;
        this.os = os;
        this.ip = ip;
        this.time = time;
        this.day = day;
    }

    public Visit(String uuid, URL url, String browser, String os, String ip, long time, String day) {

        this.uuid = uuid;
        this.url = url;
        this.browser = browser;
        this.os = os;
        this.ip = ip;
        this.time = time;
        this.day = day;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
