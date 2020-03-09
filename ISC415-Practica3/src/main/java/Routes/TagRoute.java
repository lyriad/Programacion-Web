package Routes;

import Handlers.TagHandler;
import Models.Tag;
import Utils.Filters;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;
import java.util.HashMap;
import java.util.Map;
import static spark.Spark.*;

public class TagRoute {

    public void start() {

        Map<String, Object> responseData = new HashMap<>();

        Filters.filterAuthor("/tags");
        get("/tags", (request, response) -> {

            responseData.put("currentUser", request.session().attribute("currentUser"));
            responseData.put("tags", TagHandler.getTags());

            return new FreeMarkerEngine().render(new ModelAndView(responseData, "tags.ftl"));
        });

        Filters.filterAuthor("/tags/register");
        post("/tags/register", (request, response) -> {

            String tagName = request.queryParams("tag");

            if (tagName == null || tagName.isEmpty()) {
                responseData.put("error", "Tag name can't be empty");
                response.redirect("/tags");
            } else if (tagName.length() > 32) {
                responseData.put("error", "Tag name can't have more than 32 characters");
                response.redirect("/tags");
            } else {
                Tag newTag = new Tag("", tagName);

                if (TagHandler.registerTag(newTag)) {

                    responseData.put("success", "Tag registered successfully");
                    response.redirect("/tags");

                } else {

                    responseData.put("error", "Error while registering tag");
                    response.redirect("/tags");
                }
            }

            return "";
        });

        Filters.filterAuthor("/tags/delete/:id");
        get("/tags/delete/:id", (request, response) -> {

            responseData.clear();
            String id = request.params("id");
            Tag deleteTag = TagHandler.getTag("id", String.valueOf(id));

            if (deleteTag != null) {

                if (TagHandler.deleteTag(id)) {
                    responseData.put("success", String.format("Tag deleted: %s", deleteTag.getName()));
                } else {
                    responseData.put("error", String.format("Error deleting tag: %s", deleteTag.getName()));
                }
            } else {
                responseData.put("error", "Tag was not found");
            }

            response.redirect("/tags");
            return "";
        });
    }
}
