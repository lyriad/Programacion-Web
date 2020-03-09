package com.pucmm.isc415.Routes;

import com.pucmm.isc415.Models.URL;
import com.pucmm.isc415.Models.Visit;
import com.pucmm.isc415.Services.LoginServices;
import com.pucmm.isc415.Services.URLServices;
import com.pucmm.isc415.Models.User;
import com.pucmm.isc415.Services.UserServices;
import com.pucmm.isc415.Services.VisitServices;
import com.pucmm.isc415.Sessions.TempSession;
import com.pucmm.isc415.Utils.Constants;
import com.pucmm.isc415.Utils.MyParser;
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

                List<URL> urlList = URLServices.getInstance().lazyFind(pageNumber, currentUser);

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

                URL url = new URL(originalUrl, shortUrl, currentUser, new Timestamp(new Date().getTime()));
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
            long chromeVisits = VisitServices.getInstance().getSizeVisitByShortUrlBrowser(urlShort, "Chrome");
            long operaVisits = VisitServices.getInstance().getSizeVisitByShortUrlBrowser(urlShort, "Opera");
            long firefoxVisits = VisitServices.getInstance().getSizeVisitByShortUrlBrowser(urlShort, "Firefox");
            long edgeVisits = VisitServices.getInstance().getSizeVisitByShortUrlBrowser(urlShort, "Edge");
            long safariVisits = VisitServices.getInstance().getSizeVisitByShortUrlBrowser(urlShort, "Safari");

            //Days
            long mondayVisits = VisitServices.getInstance().getSizeByShortUrlDay(urlShort, "MONDAY");
            long tuesdayVisits = VisitServices.getInstance().getSizeByShortUrlDay(urlShort, "TUESDAY");
            long wednesdayVisits = VisitServices.getInstance().getSizeByShortUrlDay(urlShort, "WEDNESDAY");
            long thursdayVisits = VisitServices.getInstance().getSizeByShortUrlDay(urlShort, "THURSDAY");
            long fridayVisits = VisitServices.getInstance().getSizeByShortUrlDay(urlShort, "FRIDAY");
            long saturdayVisits = VisitServices.getInstance().getSizeByShortUrlDay(urlShort, "SATURDAY");
            long sundayVisits = VisitServices.getInstance().getSizeByShortUrlDay(urlShort, "SUNDAY");

            //times
            long zero = VisitServices.getInstance().getSizeByShortUrltime(urlShort, 0);
            long one = VisitServices.getInstance().getSizeByShortUrltime(urlShort, 1);
            long two = VisitServices.getInstance().getSizeByShortUrltime(urlShort, 2);
            long three = VisitServices.getInstance().getSizeByShortUrltime(urlShort, 3);
            long four = VisitServices.getInstance().getSizeByShortUrltime(urlShort, 4);
            long five = VisitServices.getInstance().getSizeByShortUrltime(urlShort, 5);
            long six = VisitServices.getInstance().getSizeByShortUrltime(urlShort, 6);
            long seven = VisitServices.getInstance().getSizeByShortUrltime(urlShort, 7);
            long eight = VisitServices.getInstance().getSizeByShortUrltime(urlShort, 8);
            long nine = VisitServices.getInstance().getSizeByShortUrltime(urlShort, 9);
            long ten = VisitServices.getInstance().getSizeByShortUrltime(urlShort, 10);
            long eleven = VisitServices.getInstance().getSizeByShortUrltime(urlShort, 11);
            long twelve = VisitServices.getInstance().getSizeByShortUrltime(urlShort, 12);
            long thirteen = VisitServices.getInstance().getSizeByShortUrltime(urlShort, 13);
            long fourteen = VisitServices.getInstance().getSizeByShortUrltime(urlShort, 14);
            long fifteen = VisitServices.getInstance().getSizeByShortUrltime(urlShort, 15);
            long sixteen = VisitServices.getInstance().getSizeByShortUrltime(urlShort, 16);
            long seventeen = VisitServices.getInstance().getSizeByShortUrltime(urlShort, 17);
            long eighteen = VisitServices.getInstance().getSizeByShortUrltime(urlShort, 18);
            long nineteen = VisitServices.getInstance().getSizeByShortUrltime(urlShort, 19);
            long twenty = VisitServices.getInstance().getSizeByShortUrltime(urlShort, 20);
            long twenty_one = VisitServices.getInstance().getSizeByShortUrltime(urlShort, 21);
            long twenty_two = VisitServices.getInstance().getSizeByShortUrltime(urlShort, 22);
            long twenty_three = VisitServices.getInstance().getSizeByShortUrltime(urlShort, 23);

            //OS
            long windows10 = VisitServices.getInstance().getSizeVisitByShortUrlOs(urlShort, "Windows 10");
            long windows7 = VisitServices.getInstance().getSizeVisitByShortUrlOs(urlShort, "Windows 7");
            long ubuntu1604 = VisitServices.getInstance().getSizeVisitByShortUrlOs(urlShort, "Ubuntu 16.04");
            long ubuntu1804 = VisitServices.getInstance().getSizeVisitByShortUrlOs(urlShort, "Ubuntu 18.04");
            long android9 = VisitServices.getInstance().getSizeVisitByShortUrlOs(urlShort, "Android 9");
            long android8 = VisitServices.getInstance().getSizeVisitByShortUrlOs(urlShort, "Android 8");

            Map<String, Object> attributes = new HashMap<>();
            User loggedUser = request.session().attribute("currentUser");
            attributes.put("loggedUser", loggedUser);
            attributes.put("chromeVisits", chromeVisits);
            attributes.put("operaVisits", operaVisits);
            attributes.put("firefoxVisits", firefoxVisits);
            attributes.put("edgeVisits", edgeVisits);
            attributes.put("safariVisits", safariVisits);

            attributes.put("mondayVisits", mondayVisits);
            attributes.put("tuesdayVisits", tuesdayVisits);
            attributes.put("wednesdayVisits", wednesdayVisits);
            attributes.put("thursdayVisits", thursdayVisits);
            attributes.put("fridayVisits", fridayVisits);
            attributes.put("saturdayVisits", saturdayVisits);
            attributes.put("sundayVisits", sundayVisits);

            attributes.put("zero", zero);
            attributes.put("one", one);
            attributes.put("two", two);
            attributes.put("three", three);
            attributes.put("four", four);
            attributes.put("five", five);
            attributes.put("six", six);
            attributes.put("seven", seven);
            attributes.put("eight", eight);
            attributes.put("nine", nine);
            attributes.put("ten", ten);
            attributes.put("eleven", eleven);
            attributes.put("twelve", twelve);
            attributes.put("thirteen", thirteen);
            attributes.put("fourteen", fourteen);
            attributes.put("fifteen", fifteen);
            attributes.put("sixteen", sixteen);
            attributes.put("seventeen", seventeen);
            attributes.put("eighteen", eighteen);
            attributes.put("nineteen", nineteen);
            attributes.put("twenty", twenty);
            attributes.put("twenty_one", twenty_one);
            attributes.put("twenty_two", twenty_two);
            attributes.put("twenty_three", twenty_three);

            attributes.put("windows10",windows10);
            attributes.put("windows7",windows7);
            attributes.put("ubuntu1604",ubuntu1604);
            attributes.put("ubuntu1804",ubuntu1804);
            attributes.put("android8",android8);
            attributes.put("android9",android9);

            return new FreeMarkerEngine().render(new ModelAndView(attributes, "stats.ftl"));
        });

    }
}