package com.pucmm.isc415.Routes;

import com.pucmm.isc415.Helpers.UrlHelper;
import com.pucmm.isc415.Models.Url;
import com.pucmm.isc415.Models.Visit;
import com.pucmm.isc415.Services.LoginServices;
import com.pucmm.isc415.Services.URLServices;
import com.pucmm.isc415.Models.User;
import com.pucmm.isc415.Services.UserServices;
import com.pucmm.isc415.Services.VisitServices;
import com.pucmm.isc415.Sessions.TempSession;
import com.pucmm.isc415.Utils.Constants;
import com.pucmm.isc415.Utils.MyParser;
import kong.unirest.json.JSONObject;
import spark.ModelAndView;
import spark.Session;
import spark.template.freemarker.FreeMarkerEngine;
import ua_parser.Client;
import ua_parser.Parser;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static spark.Spark.*;

public class IndexRoute {

    public static void start() {

        get("/", (request, response) -> {

            Map<String, Object> responseData = new HashMap<>();

            User currentUser = request.session().attribute("currentUser");
            TempSession tempSession = request.session().attribute("tempSession");
            responseData.put("domain", Constants.DOMAIN);

            if (currentUser != null) {

                responseData.put("currentUser", currentUser);

                int pageNumber = (request.queryParams("page") != null) ? Integer.parseInt(request.queryParams("page")) : 1;
                int urls = (int) URLServices.getInstance().getCount();
                int totalPages = (int) (Math.ceil((double) urls / 10)) + 1;

                if( pageNumber < 0 || pageNumber > totalPages ) {
                    pageNumber = 1;
                }

                List<Url> urlList = URLServices.getInstance().lazyFind(pageNumber, currentUser);

                responseData.put("urls", urlList != null ? urlList : new ArrayList<>());
                responseData.put("pages", totalPages);

            } else {

                responseData.put("urls", tempSession.getUrlList());
            }

            return new FreeMarkerEngine().render(new ModelAndView(responseData, "index.ftl"));
        });

        post("/login", (request, response) -> {

            String username = request.queryParams("username");
            String password = request.queryParams("password");
            boolean remember = request.queryParams("remember") != null;

            if (LoginServices.login(username, password)) {

                User user = UserServices.getInstance().get(username);

                request.session().attribute("currentUser", user);

                if (remember) {
                    response.cookie("USERID", String.valueOf(user.getUsername()), 604800);
                }
            }

            response.redirect("/");
            return "";
        });

        get("/logout", (request, response) -> {

            response.cookie("USERID", null, 0);
            Session session = request.session(true);
            session.attribute("currentUser", null);
            session.invalidate();
            response.redirect("/");
            return "";
        });

        post("/create", (request, response) -> {

            String originalUrl = request.queryParams("url");
            User currentUser = request.session().attribute("currentUser");
            TempSession tempSession = request.session().attribute("tempSession");

            if (originalUrl != null && !originalUrl.isEmpty() && tempSession.getMyUrls().size() < 10) {

                String shortUrl = Constants.DOMAIN + "/r/"
                        + MyParser.base62Encode(URLServices.getInstance().getCount() + 1);

                Map<String,String> preview_result = UrlHelper.getPrevia(originalUrl);

                Url url = new Url(originalUrl, shortUrl, currentUser, new Timestamp(new Date().getTime()),
                        preview_result.get("image"), preview_result.get("description"));

                URLServices.getInstance().create(url);

                if (currentUser == null) {

                    tempSession.getMyUrls().put(originalUrl, shortUrl);

                } else {

                    currentUser.getMyUrls().add(url);
                    UserServices.getInstance().update(currentUser);
                }
            }

            response.redirect("/");
            return "";

        });

        post("/delete", (request, response) -> {

            String originalUrl = request.queryParams("url");

            if (originalUrl != null && !originalUrl.isEmpty())
                URLServices.getInstance().delete(originalUrl);

            response.redirect("/");
            return "";
        });

        get("/r/:shortUrl", (request, response) -> {

            String originalUrl;
            String shortUrl = String.format("%s/r/%s", Constants.DOMAIN, request.params("shortUrl"));

            originalUrl = URLServices.getInstance().getUrlByShort(shortUrl);

            if (originalUrl != null) {

                Parser uaParser = new Parser();
                Client c = uaParser.parse(request.userAgent());
                String os = c.os.family + " " + c.os.major;
                String browser = c.userAgent.family;
                String ip = request.ip();
                String id = UUID.randomUUID().toString();
                long hour = LocalTime.now().getHour();
                String day = LocalDate.now().getDayOfWeek().toString();
                Visit visit = new Visit(URLServices.getInstance().get(originalUrl), browser, os, ip, hour, day);
                visit.setUuid(id);
                VisitServices.getInstance().create(visit);

                response.redirect(originalUrl);

            } else {

                response.redirect("/");
            }
            return "";
        });

        get("/stats/:id", (request, response) -> {

            String urlShort = String.format("%s/r/%s", Constants.DOMAIN, request.params("id"));

            //Browsers
            Map<String, Object> attributes = UrlHelper.getAttributes(urlShort);

            return new FreeMarkerEngine().render(new ModelAndView(attributes, "stats.ftl"));

        });

    }
}