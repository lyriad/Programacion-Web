package com.pucmm.isc415;

import com.pucmm.isc415.Models.User;
import com.pucmm.isc415.Routes.ApiRoute;
import com.pucmm.isc415.Routes.IndexRoute;
import com.pucmm.isc415.Routes.UserRoute;
import com.pucmm.isc415.SOAP.SoapService;
import com.pucmm.isc415.Services.Database;
import com.pucmm.isc415.Services.DatabaseHelper;
import com.pucmm.isc415.Services.UserServices;
import com.pucmm.isc415.Sessions.TempSession;
import spark.Session;
import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {

        port(getHerokuAssignedPort());
        staticFiles.location("/public");

        try {

            DatabaseHelper.startDatabase();
            Database.getInstance().testConnection();
            DatabaseHelper.createAdmin();
            SoapService.init();
        } catch (Exception e) {

            e.printStackTrace();
            stop();
        }

        ApiRoute.start();
        IndexRoute.start();
        UserRoute.start();

        before((request, response) -> {

            Session session = request.session(true);

            if (request.cookie("USERID") != null) {

                String username = request.cookie("USERID");
                User user = UserServices.getInstance().get(username);

                session.attribute("currentUser", user);
            }

            if (session.attribute("tempSession") == null) {

                session.attribute("tempSession", new TempSession());
            }
        });
    }

    private static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 9325;
    }
}
