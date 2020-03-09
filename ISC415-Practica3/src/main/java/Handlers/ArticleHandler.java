package Handlers;

import Models.Article;
import Models.Tag;
import Models.User;
import Services.Database;
import Utils.Parser;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArticleHandler {

    public static Article getArticle (String column, String value) {

        Article article = null;
        Connection conn = null;

        try {
            String query = String.format("SELECT * FROM ARTICLE WHERE %s = '%s'", column, value);
            conn = Database.getInstance().getConnection();
            PreparedStatement prepareStatement = conn.prepareStatement(query);
            ResultSet dbResult = prepareStatement.executeQuery();
            dbResult.next();

            article = new Article(
                    dbResult.getString("id"),
                    dbResult.getString("title"),
                    dbResult.getString("body"),
                    UserHandler.getUser("id", dbResult.getString("author")),
                    dbResult.getTimestamp("date"),
                    CommentHandler.getCommentsFromArticle(dbResult.getString("id")),
                    TagHandler.getTagsFromArticle(dbResult.getString("id"))
            );

        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            try {
                assert conn != null;
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return article;
    }

    public static List<Article> getArticles() {

        List<Article> articles = new ArrayList<>();

        Connection conn = null;
        try {
            conn = Database.getInstance().getConnection();

            PreparedStatement prepareStatement = conn.prepareStatement("SELECT * FROM ARTICLE ORDER BY date DESC");
            ResultSet dbResult = prepareStatement.executeQuery();

            while(dbResult.next()) {

                articles.add(new Article(
                        dbResult.getString("id"),
                        dbResult.getString("title"),
                        dbResult.getString("body"),
                        UserHandler.getUser("id", dbResult.getString("author")),
                        dbResult.getTimestamp("date"),
                        CommentHandler.getCommentsFromArticle(dbResult.getString("id")),
                        TagHandler.getTagsFromArticle(dbResult.getString("id"))
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            try {
                assert conn != null;
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return articles;
    }

    public static List<Article> getArticlesFromUser(String id) {

        List<Article> articles = new ArrayList<>();

        Connection conn = null;
        try {
            conn = Database.getInstance().getConnection();

            PreparedStatement prepareStatement = conn.prepareStatement("SELECT * FROM ARTICLE WHERE author = ? ORDER BY date DESC");
            prepareStatement.setString(1, id);
            ResultSet dbResult = prepareStatement.executeQuery();

            while(dbResult.next()) {

                articles.add(new Article(
                        dbResult.getString("id"),
                        dbResult.getString("title"),
                        dbResult.getString("body"),
                        UserHandler.getUser("id", dbResult.getString("author")),
                        dbResult.getTimestamp("date"),
                        CommentHandler.getCommentsFromArticle(dbResult.getString("id")),
                        TagHandler.getTagsFromArticle(dbResult.getString("id"))
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            try {
                assert conn != null;
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return articles;
    }

    public static boolean registerArticle(Article article) {

        boolean result = false;
        article.setId(Parser.generateUUID("ARTICLE"));

        Connection conn = null;
        Savepoint save;

        try {
            String query = "INSERT INTO ARTICLE VALUES (?, ?, ?, ?, ?)";
            conn = Database.getInstance().getConnection();
            save = conn.setSavepoint("save");

            PreparedStatement prepareStatement = conn.prepareStatement(query);

            prepareStatement.setString(1, article.getId());
            prepareStatement.setString(2, article.getTitle());
            prepareStatement.setString(3, article.getBody());
            prepareStatement.setString(4, article.getAuthor().getId());
            prepareStatement.setTimestamp(5, article.getDate());

            result = prepareStatement.executeUpdate() > 0;

            if (result) {

                PreparedStatement tagStatement;
                String tagQuery = "INSERT INTO ARTICLE_TAG VALUES (?, ?)";

                for (Tag tag : article.getTags()) {
                    tagStatement = conn.prepareStatement(tagQuery);
                    tagStatement.setString(1, article.getId());
                    tagStatement.setString(2, tag.getId());

                    if (tagStatement.executeUpdate() <= 0) {
                        conn.rollback(save);
                        break;
                    }
                }
            } else {
                conn.rollback(save);
                result = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            try {
                assert conn != null;
                conn.commit();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static boolean deleteArticle(Article article) {

        boolean result1 = false, result2 = false, result3 = false;
        Connection conn = null;
        Savepoint save;
        try {
            conn = Database.getInstance().getConnection();
            save = conn.setSavepoint("save");

            PreparedStatement commentStatement = conn.prepareStatement(String.format("DELETE FROM COMMENT WHERE article = '%s';", article.getId()));
            PreparedStatement tagStatement = conn.prepareStatement(String.format("DELETE FROM ARTICLE_TAG WHERE id_article = '%s';", article.getId()));
            PreparedStatement articleStatement = conn.prepareStatement(String.format("DELETE FROM ARTICLE WHERE id = '%s';", article.getId()));

            result1 = commentStatement.executeUpdate() > 0;
            result2 = tagStatement.executeUpdate() > 0;
            result3 = articleStatement.executeUpdate() > 0;

            if (!result1 || !result2 || !result3) {
                conn.rollback(save);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            try {
                assert conn != null;
                conn.commit();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result1 && result2 && result3;
    }

    public static boolean updateArticle(Article editArticle) {

        boolean result = false;

        Connection conn = null;
        try {

            String query = "UPDATE ARTICLE SET title = ?, body = ? WHERE id = ?";
            conn = Database.getInstance().getConnection();
            PreparedStatement prepareStatement = conn.prepareStatement(query);

            prepareStatement.setString(1, editArticle.getTitle());
            prepareStatement.setString(2, editArticle.getBody());
            prepareStatement.setString(3, editArticle.getId());

            result = prepareStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            try {
                assert conn != null;
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
