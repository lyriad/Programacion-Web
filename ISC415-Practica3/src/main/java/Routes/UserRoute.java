package Routes;

import Handlers.ArticleHandler;
import Handlers.UserHandler;
import Models.User;
import Utils.Filters;
import Utils.Parser;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class UserRoute {

    public void start() {

        Map<String, Object> responseData = new HashMap<>();

        Filters.filterAdmin("/users/register");
        get("/users/register", (request, response) -> {

            responseData.put("currentUser", request.session().attribute("currentUser"));
            responseData.put("user", new User("", "", "", "", false, false));
            responseData.put("action", "Register user");

            return new FreeMarkerEngine().render(new ModelAndView(responseData, "input_user.ftl"));
        });

        post("/users/register", (request, response) -> {

            responseData.clear();

            String username = request.queryParams("username");
            String name = request.queryParams("name");
            String password = request.queryParams("password");
            String passwordConfirm = request.queryParams("confirm-password");
            boolean admin = request.queryParams("admin") != null;
            boolean author = request.queryParams("author") != null;

            if ((username == null || username.trim().isEmpty())
                || (name == null || name.trim().isEmpty())
                || (password == null || password.isEmpty())
                || (passwordConfirm == null || passwordConfirm.isEmpty())) {

                responseData.put("error", "Fields can't be empty");
                response.redirect("/users/register");

            } else if (!password.equals(passwordConfirm)) {

                responseData.put("error", "Passwords do not match");
                response.redirect("/users/register");

            } else {

                User newUser = new User("", username, name, Parser.getHashedPassword(password), admin, author);

                if (UserHandler.registerUser(newUser)) {

                    responseData.put("success", "User registered successfully");
                    response.redirect("/users/register");

                } else {

                    responseData.put("error", "Error while registering user");
                    response.redirect("/users/register");
                }
            }

            return "";
        });

        get("/profile/:username", (request, response) -> {

            responseData.put("currentUser", request.session().attribute("currentUser"));
            User user = UserHandler.getUser("username", request.params("username"));
            responseData.put("articles", ArticleHandler.getArticlesFromUser(user.getId()));
            responseData.put("user", user);

            return new FreeMarkerEngine().render(new ModelAndView(responseData, "view_user.ftl"));
        });

        Filters.filterLoggedIn("/profile/edit/:username");
        get("/profile/edit/:username", (request, response) -> {

            User user = UserHandler.getUser("username", request.params("username"));

            responseData.put("currentUser", request.session().attribute("currentUser"));
            responseData.put("user", user);
            responseData.put("action", "Edit profile");

            return new FreeMarkerEngine().render(new ModelAndView(responseData, "input_user.ftl"));
        });

        post("/profile/edit/:username", (request, response) -> {

            responseData.clear();

            String username = request.params("username");

            String newUsername = request.queryParams("username");
            String newName = request.queryParams("name");

            if ((newUsername != null && newUsername.isEmpty()) || (newName != null && newName.isEmpty())) {

                responseData.put("error", "Fields can't be empty");
                response.redirect(String.format("/profile/edit/%s", username));

            } else {

                User oldUser = UserHandler.getUser("username", username);

                if (oldUser != null) {
                    User editUser = new User(oldUser.getId(), newUsername, newName, oldUser.getPassword(), oldUser.isAdmin(), oldUser.isAuthor());

                    if (UserHandler.updateUser(editUser)) {

                        if (((User) request.session().attribute("currentUser")).getId().equals(editUser.getId())) {
                            request.session().attribute("currentUser", editUser);
                        }
                        responseData.put("success", "User edited successfully");
                        response.redirect(String.format("/profile/%s", newUsername));
                    } else {
                        responseData.put("error", "Error while updating user");
                        response.redirect(String.format("/profile/edit/%s", username));
                    }
                } else {
                    responseData.put("error", String.format("We couldn't find any user with the username: %s", username));
                }
            }

            return "";
        });
    }
}
