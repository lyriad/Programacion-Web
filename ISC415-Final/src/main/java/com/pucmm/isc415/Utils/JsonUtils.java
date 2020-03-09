package com.pucmm.isc415.Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pucmm.isc415.Helpers.UrlHelper;
import com.pucmm.isc415.Models.User;
import com.pucmm.isc415.Models.Visit;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import spark.ResponseTransformer;

import java.util.Map;

public class JsonUtils {

    private static Gson gson = new GsonBuilder().serializeNulls().create();
    private static Gson gsonExpose = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public static String toJsonNonExpose(Object object) {
        return gson.toJson(object);
    }

    public static String toJson(Object object) {
        return gsonExpose.toJson(object);
    }

    public static ResponseTransformer json() {
        return JsonUtils::toJson;
    }

    public static String jsonUrls(User user) throws InterruptedException {

        String userString = toJson(user);
        JSONObject obj = new JSONObject(userString);
        JSONArray urlArr = obj.getJSONArray("myUrls");
        JSONArray urlArrnew = new JSONArray();
        String urlog = "";
        String urlBase62 = "";


        for(Object val : urlArr){
            JSONObject ob = (JSONObject) val;
            urlog = ob.getString("originalUrl");
            urlBase62 = ob.getString("shortenedUrl");
            Map<String, Object> attributes = UrlHelper.getAttributes(urlBase62);
            attributes.put("originalUrl", urlog);
            attributes.put("shortenedUrl", urlBase62);
            System.out.println(attributes);
            urlArrnew.put(attributes);

        }

        obj.put("myUrls",urlArrnew);
        return obj.toString();

    }

}
