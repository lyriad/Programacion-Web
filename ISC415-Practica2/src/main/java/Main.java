import Models.Estudiante;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {

        port(getHerokuAssignedPort());
        staticFiles.location("/public");

        get("/", (request, response) -> {

            return new FreeMarkerEngine().render(new ModelAndView(null, "index.ftl"));
        });

        get("/listar", (request, response) -> {

            Map<String, List<Estudiante>> datos = new HashMap<>();
            datos.put("estudiantes", Singleton.getInstance().getEstudiantes());

            return new FreeMarkerEngine().render(new ModelAndView(datos, "list.ftl"));
        });

        get("/registrar", (request, response) -> {

            Map<String, Object> datos = new HashMap<>();
            datos.put("action", "Registrar");
            datos.put("estudiante", new Estudiante(0, "", "", ""));
            return new FreeMarkerEngine().render(new ModelAndView(datos, "input.ftl"));
        });

        post("/registrar", (request, response) -> {

            int matricula = Integer.parseInt(request.queryParams("matricula"));
            String nombre = request.queryParams("nombre");
            String apellido = request.queryParams("apellido");
            String telefono = request.queryParams("telefono");

            Estudiante newEstudiante = new Estudiante(matricula, nombre, apellido, telefono);
            Singleton.getInstance().insertEstudiante(newEstudiante);

            response.redirect("/registrar");
            return "";
        });

        get("/estudiante/:matricula", (request, response) -> {

            int matricula = Integer.parseInt(request.params("matricula"));
            Map<String, Estudiante> datos = new HashMap<>();
            datos.put("estudiante", Singleton.getInstance().getEstudiante(matricula));

            return new FreeMarkerEngine().render(new ModelAndView(datos, "view.ftl"));
        });

        post("/eliminar/:matricula", (request, response) -> {

            int matricula = Integer.parseInt(request.params("matricula"));

            if (!Singleton.getInstance().deleteEstudiante(matricula)) {
                response.redirect(String.format("/estudiante/%d", matricula));
            }

            response.redirect("/listar");
            return "";
        });

        get("/editar/:matricula", (request, response) -> {

            int matricula = Integer.parseInt(request.params("matricula"));
            Map<String, Object> datos = new HashMap<>();

            datos.put("action", "Editar");
            datos.put("estudiante", Singleton.getInstance().getEstudiante(matricula));

            return new FreeMarkerEngine().render(new ModelAndView(datos, "input.ftl"));
        });

        post("/editar/:matricula", (request, response) -> {

            int matricula = Integer.parseInt(request.queryParams("matricula"));
            String nombre = request.queryParams("nombre");
            String apellido = request.queryParams("apellido");
            String telefono = request.queryParams("telefono");

            Estudiante newEstudiante = new Estudiante(matricula, nombre, apellido, telefono);
            Singleton.getInstance().updateEstudiante(newEstudiante);

            response.redirect("/listar");
            return "";
        });

        before((request, response) -> {

            for (String param : request.queryParams()) {
                if (request.queryParams(param) == null || request.queryParams(param).length() == 0) {
                    response.redirect(request.headers("referer"));
                }
            }
        });
    }

    private static int getHerokuAssignedPort() {

        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 5000;
    }
}