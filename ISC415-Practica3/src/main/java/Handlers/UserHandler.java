package Handlers;

import Models.User;
import Services.Database;
import Utils.Parser;

import java.sql.*;

public class UserHandler {

    public static User getUser(String column, String value) {

        User user = null;
        Connection conn = null;
        try {
            String query = String.format("SELECT * FROM USER WHERE %s = '%s'", column, value);
            conn = Database.getInstance().getConnection();
            PreparedStatement prepareStatement = conn.prepareStatement(query);
            ResultSet dbResult = prepareStatement.executeQuery();
            dbResult.next();

            user = new User(
                    dbResult.getString("id"),
                    dbResult.getString("username"),
                    dbResult.getString("name"),
                    dbResult.getString("password"),
                    dbResult.getBoolean("admin"),
                    dbResult.getBoolean("author")
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

        return user;
    }

    public static boolean registerUser(User user) {

        boolean result = false;

        Connection conn = null;
        try {

            String query = "INSERT INTO USER VALUES (?, ?, ?, ?, ?, ?)";
            conn = Database.getInstance().getConnection();
            PreparedStatement prepareStatement = conn.prepareStatement(query);

            prepareStatement.setString(1, Parser.generateUUID("USER"));
            prepareStatement.setString(2, user.getUsername());
            prepareStatement.setString(3, user.getName());
            prepareStatement.setString(4, user.getPassword());
            prepareStatement.setBoolean(5, user.isAdmin());
            prepareStatement.setBoolean(6, user.isAuthor());

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

    public static boolean updateUser(User editUser) {

        boolean result = false;

        Connection conn = null;
        try {

            String query = "UPDATE USER SET username = ?, name = ?, password = ? WHERE id = ?";
            conn = Database.getInstance().getConnection();
            PreparedStatement prepareStatement = conn.prepareStatement(query);

            prepareStatement.setString(1, editUser.getUsername());
            prepareStatement.setString(2, editUser.getName());
            prepareStatement.setString(3, editUser.getPassword());
            prepareStatement.setString(4, editUser.getId());

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
