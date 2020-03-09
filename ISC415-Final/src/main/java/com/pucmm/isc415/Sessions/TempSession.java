package com.pucmm.isc415.Sessions;

import com.pucmm.isc415.Helpers.UrlHelper;
import com.pucmm.isc415.Models.Url;
import kong.unirest.json.JSONObject;

import java.io.UnsupportedEncodingException;
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

    public List<Url> getUrlList() throws UnsupportedEncodingException {

        List<Url> urls = new ArrayList<>();

        for (Map.Entry<String, String> entry : myUrls.entrySet()) {
            Map<String, String> preview_result = UrlHelper.getPrevia(entry.getKey());

            urls.add(new Url(entry.getKey(), entry.getValue(), null, new Timestamp(new Date().getTime()),
                    preview_result.get("image"), preview_result.get("description")));
        }

        Collections.reverse(urls);
        return urls;
    }
}
