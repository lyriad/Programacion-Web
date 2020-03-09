import Handlers.UserHandler;
import Models.User;
import Routes.ArticleRoute;
import Routes.IndexRoute;
import Routes.TagRoute;
import Routes.UserRoute;
import Services.Database;
import Services.DatabaseHelper;
import spark.Session;

import java.sql.SQLException;
import static spark.Spark.*;

public class Main {

    public static void main(String[] args) throws SQLException {

        port(getHerokuAssignedPort());
        staticFiles.location("/public");

        DatabaseHelper.startDatabase();
        Database.getInstance().testConnection();
        DatabaseHelper.setupTables();

        new IndexRoute().start();
        new UserRoute().start();
        new TagRoute().start();
        new ArticleRoute().start();

        before((request, response) -> {

            if (request.cookie("USERID") != null) {

                String id = request.cookie("USERID");
                User user = UserHandler.getUser("id", id);

                Session session = request.session(true);
                session.attribute("currentUser", user);
            }

        });
    }

    private static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567;
    }
}
