import spark.ModelAndView;
import spark.Session;

import static spark.Spark.*;
import spark.template.freemarker.FreeMarkerEngine;

public class Main {
    public static void main(String[] args) {

        port(5000);
        //begin GET
        get("/", (request, response) -> {
            return new FreeMarkerEngine().render(new ModelAndView(null, "index.ftl"));
        });

        get("/hola", (request, response) -> {
            return new FreeMarkerEngine().render(new ModelAndView(null, "hola.ftl"));
        });

        get("/otra", (request, response) -> {
            return "otra";
        });

        get("/formularioLogIn/", (request, response) -> {
            return new FreeMarkerEngine().render(new ModelAndView(null, "formularioLogIn.ftl"));
        });
        //begin POST
        post("/procesarLogin/", (request, response) -> {
            String nombre = request.queryParams("usuario");
            String contrasena = request.queryParams("contrasena");

            if (nombre.isEmpty() || contrasena.isEmpty()) {
                response.redirect("/formularioLogIn/");
            } else {
                Session session = request.session(true);

                session.attribute("usuario", nombre);
                response.redirect("/");
            }

            return "";
        });

        new Filtro().aplicarFiltros();
    }
}
