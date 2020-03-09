package com.pucmm.isc415.Utils;

import com.pucmm.isc415.Models.User;
import spark.Session;

import static spark.Spark.before;

public final class Filters {

    public static void filterAdmin(String subdomain) {

        before(subdomain, (request, response) -> {

            Session session = request.session(true);
            User currentUser = session.attribute("currentUser");

            if (currentUser == null || !currentUser.isAdmin()) {

                response.redirect("/");
            }
        });
    }

}