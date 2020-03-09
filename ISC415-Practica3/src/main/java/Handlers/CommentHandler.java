package Handlers;

import Models.Comment;
import Services.Database;
import Utils.Parser;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentHandler {

    public static List<Comment> getCommentsFromArticle (String id) {

        List<Comment> comments = new ArrayList<>();

        Connection conn = null;
        try {
            conn = Database.getInstance().getConnection();

            PreparedStatement prepareStatement = conn.prepareStatement("SELECT * FROM COMMENT WHERE article = ?;");
            prepareStatement.setString(1, id);
            ResultSet dbResult = prepareStatement.executeQuery();

            while(dbResult.next()) {

                comments.add(new Comment(
                        dbResult.getString("id"),
                        dbResult.getString("comment"),
                        UserHandler.getUser("id", dbResult.getString("author"))
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

        return comments;
    }

    public static boolean registerComment(String articleId, Comment comment) {

        boolean result = false;

        comment.setId(Parser.generateUUID("COMMENT"));
        Connection conn = null;
        Savepoint save;
        try {

            conn = Database.getInstance().getConnection();
            save = conn.setSavepoint("save");

            PreparedStatement prepareStatement = conn.prepareStatement("INSERT INTO COMMENT VALUES (?, ?, ?, ?)");

            prepareStatement.setString(1, comment.getId());
            prepareStatement.setString(2, comment.getComment());
            prepareStatement.setString(3, comment.getAuthor().getId());
            prepareStatement.setString(4, articleId);

            result = prepareStatement.executeUpdate() > 0;

            if (!result) {
                conn.rollback(save);
                result = false;
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

        return result;
    }
}
