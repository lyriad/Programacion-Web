import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Scanner;

public class Main {

    private static String url = "";

    public static void main(String[] args) {

        Scanner standardInput = new Scanner(System.in);
        String docString = "";
        Document docHtml = null;
        Connection httpConn;

        // Request url
        System.out.println("Enter an URL: ");
        url = standardInput.nextLine();

        // Append http at the beggining to avoid connection errors
        if (!url.contains("http://")) {
            url = String.format("http://%s", url);
        }

        // Try to open a connection to the URL and get the html resource
        // Raw html to get the number of lines the document has
        // Parsed html to get an object that makes my life easier
        try {
            httpConn = Jsoup.connect(url).timeout(5000);
            docString = httpConn.execute().body();       // Raw
            docHtml = httpConn.get();                    // Parsed
        } catch (Exception e) {
            System.out.println("There was an error getting the HTML document");
            System.exit(0);
        }
        System.out.println(String.format("El documento tiene %d lineas", docString.split("\n").length));
        System.out.println(String.format("El documento tiene %d parrafos", docHtml.getElementsByTag("p").size()));
        System.out.println(String.format("El documento tiene %d imagenes", docHtml.getElementsByTag("img").size()));
        System.out.println(String.format("El documento tiene %d formularios de metodo GET", getAmountOfForms(docHtml, "get")));
        System.out.println(String.format("El documento tiene %d formularios de metodo POST", getAmountOfForms(docHtml, "post")));
        printInputTags(docHtml);
        sendPostRequest(docHtml);
    }

    /**
     * @function: getAmountOfForms
     * @param doc: html document to count forms
     * @param method: html form method (either GET or POST)
     * @return amount of forms of specified method
     */
    private static int getAmountOfForms (Document doc, String method) {
        int cant = 0;
        for (Element form : doc.getElementsByTag("form")) {
            if (form.attr("method").equalsIgnoreCase(method)) {
                cant++;
            }
        }
        return cant;
    }

    /**
     * @function: printInputTags
     * @param doc: html document to print all input tags
     */
    private static void printInputTags (Document doc) {

        for (Element form : doc.getElementsByTag("form")) {
            System.out.println(String.format("form method=%s action=%s", form.attr("method"), form.attr("action")));
            for (Element input : form.getElementsByTag("input")) {
                System.out.println(String.format("\tinput type=%s", input.attr("type")));
            }
        }
    }

    /**
     * @function: sendPostRequest
     * @param doc: html document to get all forms and send the request to each form action
     */
    private static void sendPostRequest (Document doc) {

        for (Element form : doc.getElementsByTag("form")) {
            if (form.attr("method").equalsIgnoreCase("post")) {
                String urlToSend = form.attr("action").contains("http") ? form.attr("action") : String.format("%s%s", url.substring(0, url.substring(8).indexOf("/") + 8), form.attr("action"));
                try {
                    System.out.println(urlToSend);
                    Connection.Response response = Jsoup.connect(urlToSend)
                            .data("asignatura", "practica1")
                            .header("matricula", "20160229")
                            .method(Connection.Method.POST)
                            .execute();

                    System.out.println(String.format("Response:\n%s", response.parse().toString()));
                } catch (Exception e) {
                    System.out.println("There was an error sending the request to the server");
                    System.exit(0);
                }
            }
        }
    }
}
