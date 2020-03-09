package com.pucmm.isc415.Sessions;

import com.pucmm.isc415.Models.URL;

import java.sql.Timestamp;
import java.util.*;

public class TempSession {

    private Map<String, String> myUrls;

    public TempSession() {

        this.myUrls = new HashMap<>();
    }

    public Map<String, String> getMyUrls() {
        return myUrls;
    }

    public List<URL> getUrlList() {

        List<URL> urls = new ArrayList<>();

        for (Map.Entry<String, String> entry : myUrls.entrySet())
            urls.add(new URL(entry.getKey(), entry.getValue(), null,new Timestamp(new Date().getTime())));

        Collections.reverse(urls);
        return urls;
    }
}
