package com.pucmm.isc415.Routes;

import com.google.gson.JsonObject;
import com.pucmm.isc415.Helpers.UrlHelper;
import com.pucmm.isc415.Models.Url;
import com.pucmm.isc415.Models.User;
import com.pucmm.isc415.Services.URLServices;
import com.pucmm.isc415.Services.UserServices;
import com.pucmm.isc415.Utils.Constants;
import com.pucmm.isc415.Utils.JWTUtils;
import com.pucmm.isc415.Utils.JsonUtils;
import com.pucmm.isc415.Utils.MyParser;
import kong.unirest.json.JSONObject;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static spark.Spark.*;

public class ApiRoute {

    public static void start() {

        post("/token", (request, response) -> {
            JsonObject json = new JsonObject();
            json.addProperty("token", JWTUtils.createJWT(UUID.randomUUID().toString(), Constants.DOMAIN, "Access Token", 0));
            return json;
        });

        path("/api", () -> {
            before("/*", (request, response) -> {

                String token = request.headers("token") != null ? request.headers("token") : request.headers("TOKEN");

                if (token == null || token.isEmpty() || !JWTUtils.decodeJWT(token)) {
                    halt(401);
                }
            });

            afterAfter("/*", (request, response) -> {
                if(request.headers("Accept") != null && request.headers("Accept").equalsIgnoreCase(Constants.ACCEPT_TYPE_XML)) {

                    response.header("Content-Type", Constants.ACCEPT_TYPE_XML);

                } else {
                    response.header("Content-Type", Constants.ACCEPT_TYPE_JSON);
                }
            });

            get("/urls/:username", (request, response) -> {
                String username = request.params("username");
                String test = JsonUtils.jsonUrls(UserServices.getInstance().get(username));
                return test;
            });

            post("/urls/create", (request, response) -> {

                String originalUrl = request.queryParams("url");
                String username = request.queryParams("username");

                if (originalUrl == null || originalUrl.isEmpty()) {
                    halt(400, "Url is required.");
                }

                User mUser = null;

                if (username != null && !username.isEmpty()) {
                    mUser = UserServices.getInstance().get(username);

                    if (mUser == null) {
                        halt(400, "User does not exist");
                    }
                }

                String shortUrl = Constants.DOMAIN + "/r/"
                        + MyParser.base62Encode(URLServices.getInstance().getCount() + 1);

                //If mUser is null, register URL as visitor
                Map<String, String> preview_result = UrlHelper.getPrevia(originalUrl);

                Url url = new Url(originalUrl, shortUrl, mUser, new Timestamp(new Date().getTime()),
                        preview_result.get("image"), preview_result.get("description"));

                URLServices.getInstance().create(url);

                //If mUser is not null, add url to their shortened urls
                if (mUser != null) {
                    mUser.getMyUrls().add(url);
                    UserServices.getInstance().update(mUser);
                }

                return url.toJson();
            });
        });
    }
}
