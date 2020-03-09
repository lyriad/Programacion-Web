package Handlers;

import Models.Tag;
import Services.Database;
import Utils.Parser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TagHandler {

    public static List<Tag> getTags() {

        List<Tag> tags = new ArrayList<>();

        Connection conn = null;
        try {
            conn = Database.getInstance().getConnection();

            PreparedStatement prepareStatement = conn.prepareStatement("SELECT * FROM TAG");
            ResultSet dbResult = prepareStatement.executeQuery();

            while(dbResult.next()) {

                tags.add(new Tag(
                        dbResult.getString("id"),
                        dbResult.getString("name")
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

        return tags;
    }

    public static List<Tag> getTagsFromArticle (String id) {

        List<Tag> tags = new ArrayList<>();

        Connection conn = null;
        try {
            conn = Database.getInstance().getConnection();

            PreparedStatement prepareStatement = conn.prepareStatement("SELECT * FROM TAG INNER JOIN ARTICLE_TAG ON (id_tag = id AND id_article = ?)");
            prepareStatement.setString(1, id);
            ResultSet dbResult = prepareStatement.executeQuery();

            while(dbResult.next()) {

                tags.add(new Tag(
                        dbResult.getString("id"),
                        dbResult.getString("name")
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

        return tags;
    }

    public static Tag getTag(String column, String value) {

        Tag tag = null;
        Connection conn = null;
        try {
            String query = String.format("SELECT * FROM TAG WHERE %s = '%s'", column, value);
            conn = Database.getInstance().getConnection();
            PreparedStatement prepareStatement = conn.prepareStatement(query);
            ResultSet result = prepareStatement.executeQuery();

            while(result.next()){
                tag = new Tag();
                tag.setId(result.getString("id"));
                tag.setName(result.getString("name"));
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

        return tag;
    }

    public static boolean registerTag(Tag tag) {

        boolean result = false;

        Connection conn = null;
        try {

            String query = "INSERT INTO TAG VALUES (?, ?)";
            conn = Database.getInstance().getConnection();
            PreparedStatement prepareStatement = conn.prepareStatement(query);

            prepareStatement.setString(1, Parser.generateUUID("TAG"));
            prepareStatement.setString(2, tag.getName());

            result = prepareStatement.executeUpdate() > 0;
            if (result) {
                conn.commit();
            } else {
                conn.rollback();
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

    public static boolean deleteTag(String id) {

        boolean result = false;

        Connection conn = null;
        try {

            String query = "DELETE FROM TAG WHERE id = ?";
            conn = Database.getInstance().getConnection();

            PreparedStatement prepareStatement = conn.prepareStatement(query);
            prepareStatement.setString(1, id);

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
