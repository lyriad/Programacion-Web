package com.pucmm.isc415.Helpers;

import com.pucmm.isc415.Services.VisitServices;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UrlHelper {

    public static Map<String, String> getPrevia(String shortenedUrl) {
        String previa_url = "http://api.linkpreview.net/?key=5de832cee749df098be317d944735f8592e964b443e93&q=" + shortenedUrl;

        HttpResponse<JsonNode> previa = Unirest.get(previa_url)
                .asJson();

        JSONObject myObj = previa.getBody().getObject();
        Map<String,String> result = new HashMap<>();
        String image_url = myObj.get("image").toString();
        String description = myObj.get("description").toString();
        if( description.contains("Invalid response status code") ) {
            image_url = "https://bigbadwolfbooks.com/wp-content/themes/bbw2019/images/no-preview.jpg";
            description = "no description";
        }

        result.put("image",image_url);
        result.put("description", description);

        return result;
    }

    public static Map<String, Object> getAttributes( String urlShort ) {

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

        return attributes;
    }

}

