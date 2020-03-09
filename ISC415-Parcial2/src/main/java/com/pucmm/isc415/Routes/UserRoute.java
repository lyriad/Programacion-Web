package com.pucmm.isc415.Routes;

import com.pucmm.isc415.Services.UserServices;
import com.pucmm.isc415.Models.User;
import com.pucmm.isc415.Utils.Filters;
import com.pucmm.isc415.Utils.MyParser;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class UserRoute {

    public static void start() {

        Map<String, Object> responseData = new HashMap<>();

        Filters.filterAdmin("/users/register");
        post("/users/register", (request, response) -> {

            responseData.clear();

            String username = request.queryParams("username");
            String name = request.queryParams("name");
            String email = request.queryParams("email");
            String password = request.queryParams("password");
            String passwordConfirm = request.queryParams("confirm-password");

            if ((username == null || username.trim().isEmpty())
                    || (name == null || name.trim().isEmpty())
                    || (password == null || password.isEmpty())
                    || (passwordConfirm == null || passwordConfirm.isEmpty())) {

                responseData.put("error", "Fields can't be empty");

            } else if (!password.equals(passwordConfirm)) {

                responseData.put("error", "Passwords do not match");

            } else {

                User newUser = new User(username, name, email, false, Calendar.getInstance(), MyParser.getHashedPassword(password));


                try {
                    UserServices.getInstance().create(newUser);
                    responseData.put("success", "User registered successfully");

                } catch (Exception e) {

                    System.out.println(e.getMessage());
                    responseData.put("error", "Error while registering user");
                }
            }

            response.redirect("/");
            return "";
        });

        Filters.filterAdmin("/admins");
        get("/admins", (request, response) -> {

            List<User> admins = UserServices.getInstance().getAdmins();

            responseData.put("currentUser", request.session().attribute("currentUser"));
            responseData.put("admins", admins);

            return new FreeMarkerEngine().render(new ModelAndView(responseData, "admins.ftl"));
        });

        Filters.filterAdmin("/admins");
        post("/admins", (request, response) -> {

            responseData.clear();

            String username = request.queryParams("username");
            User user = UserServices.getInstance().get(username);

            if (user == null) {

                responseData.put("error", "We couldn't find an user with that username");

            } else if (user.isAdmin()) {

                responseData.put("info", String.format("User %s is already an admin", user.getUsername()));

            } else {
                user.setAdmin(true);
                UserServices.getInstance().update(user);
                responseData.put("success", String.format("User %s is now an admin", user.getUsername()));
            }

            response.redirect("/admins");
            return "";
        });
    }
}
