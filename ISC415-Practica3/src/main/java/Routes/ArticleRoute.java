package Routes;

import Handlers.ArticleHandler;
import Handlers.CommentHandler;
import Handlers.TagHandler;
import Models.Article;
import Models.Comment;
import Models.Tag;
import Models.User;
import Utils.Filters;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import java.sql.Timestamp;
import java.util.*;

import static spark.Spark.get;
import static spark.Spark.post;

public class ArticleRoute {

    public void start() {

        Map<String, Object> responseData = new HashMap<>();

        get("/article/view/:id", (request, response) -> {

            String id = request.params("id");

            User currentUser = request.session().attribute("currentUser");
            responseData.put("currentUser", currentUser);
            responseData.put("article", ArticleHandler.getArticle("id", id));

            return new FreeMarkerEngine().render(new ModelAndView(responseData, "view_article.ftl"));
        });

        Filters.filterAuthor("/article/write");
        get("/article/write", (request, response) -> {

            User currentUser = request.session().attribute("currentUser");
            Article article = responseData.containsKey("article") ? (Article) responseData.get("article") : new Article("", "", "", currentUser, new Timestamp(new Date().getTime()), null, null);
            responseData.put("currentUser", currentUser);
            responseData.put("tags", TagHandler.getTags());
            responseData.put("action", "Write");
            responseData.put("article", article);

            return new FreeMarkerEngine().render(new ModelAndView(responseData, "input_article.ftl"));
        });

        post("/article/write", (request, response) -> {

            responseData.clear();

            String title = request.queryParams("title");
            String body = request.queryParams("body");
            String[] tagNames = request.queryParamsValues("tagName");
            String[] tagIds = request.queryParamsValues("tagId");

            User currentUser = request.session().attribute("currentUser");
            List<Tag> tags = new ArrayList<>();

            if (tagNames != null && tagIds != null) {
                for (int i = 0; i < tagIds.length; i++) tags.add(new Tag(tagIds[i], tagNames[i]));
            }

            Article article = new Article("", title, body, currentUser, new Timestamp(new Date().getTime()), new ArrayList<>(), tags);

            if ((title == null || title.isEmpty())
                || (body == null || body.isEmpty())) {
                responseData.put("error", "Fields can't be empty");
                responseData.put("article", article);
                response.redirect("/article/write");
            } else {

                if (ArticleHandler.registerArticle(article)) {

                    responseData.put("success", "Article posted successfully");
                    response.redirect(String.format("/article/view/%s", ArticleHandler.getArticle("title", article.getTitle()).getId()));

                } else {

                    responseData.put("error", "Error while posting article");
                    responseData.put("article", article);
                    response.redirect("/article/write");
                }
            }

            return "";
        });

        Filters.filterLoggedIn("/article/comment/:id");
        post("/article/comment/:id", (request, response) -> {

            responseData.clear();

            User currentUser = request.session().attribute("currentUser");
            String articleId = request.params("id");
            String commentBody = request.queryParams("comment");

            if (commentBody != null && !commentBody.isEmpty()) {

                Comment comment = new Comment();
                comment.setComment(commentBody);
                comment.setAuthor(currentUser);

                if (CommentHandler.registerComment(articleId, comment)) {

                    responseData.put("info", "Comment posted");

                } else {

                    responseData.put("error", "Error posting comment");
                }

            } else {
                responseData.put("error", "The comment is empty");
            }

            response.redirect(String.format("/article/view/%s", articleId));
            return "";
        });

        Filters.filterAuthor("/article/delete/:id");
        Filters.filterAdmin("/article/delete/:id");
        get("/article/delete/:id", (request, response) -> {

            responseData.clear();

            String articleId = request.params("id");
            Article article = ArticleHandler.getArticle("id", articleId);

            if (article != null) {

                if (ArticleHandler.deleteArticle(article)) {

                    responseData.put("info", "Article deleted successfully");

                } else {

                    responseData.put("error", "Error deleting article");
                }

            } else {
                responseData.put("error", "The article was not found");
            }

            response.redirect("/");
            return "";
        });

        Filters.filterAuthor("/article/edit/:id");
        get("/article/edit/:id", (request, response) -> {

            User currentUser = request.session().attribute("currentUser");
            Article article = ArticleHandler.getArticle("id", request.params("id"));
            responseData.put("currentUser", currentUser);
            responseData.put("tags", TagHandler.getTags());
            responseData.put("action", "Edit");
            responseData.put("article", article);

            return new FreeMarkerEngine().render(new ModelAndView(responseData, "input_article.ftl"));
        });//taitel

        post("/article/edit/:id", (request, response) -> {

            responseData.clear();
            String id = request.params("id");

            String title = request.queryParams("title");
            String body = request.queryParams("body");

            Article article = ArticleHandler.getArticle("id", id);
            article.setTitle(title);
            article.setBody(body);

            if ((title == null || title.isEmpty())
                    || (body == null || body.isEmpty())) {
                responseData.put("error", "Fields can't be empty");
                response.redirect(String.format("/article/edit/%s", id));
            } else {

                if (ArticleHandler.updateArticle(article)) {

                    responseData.put("success", "Article posted successfully");
                    response.redirect(String.format("/article/view/%s", ArticleHandler.getArticle("title", article.getTitle()).getId()));

                } else {

                    responseData.put("error", "Error while posting article");
                    response.redirect(String.format("/article/edit/%s", id));
                }
            }

            return "";
        });
    }
}
