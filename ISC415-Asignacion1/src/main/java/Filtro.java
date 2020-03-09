import spark.Session;

import static spark.Spark.after;

class Filtro {

    private boolean checked = false;

    void aplicarFiltros() {
        after((request, response) -> {
            Session session  = request.session(true);

            if (session.attribute("usuario") == null && !checked) {
                response.redirect("/formularioLogIn/");
                checked = true;
            } else {
                checked = false;
            }
        });
    }
}
