package com.pucmm.isc415.Services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static Database database;

    private  Database() {

        registrarDriver();
    }

    public static Database getInstance() {

        if (database == null) {

            database = new Database();
        }
        return database;
    }

    private void registrarDriver() {
        try {

            Class.forName("org.h2.Driver");

        } catch (ClassNotFoundException e) {

            System.out.println(String.format("ERROR: %s", e));
        }
    }

    public Connection getConnection() {

        Connection conn = null;
        try {

            String DB_URL = "jdbc:h2:tcp://localhost/~/database";
            conn = DriverManager.getConnection(DB_URL, "admin", "admin");

        } catch (SQLException e) {

            System.out.println(String.format("ERROR: %s", e));
        }
        return conn;
    }

    public void testConnection() {

        try {
            getConnection().close();

        } catch (SQLException e) {

            System.out.println(String.format("ERROR: %s", e));
        }
    }
}
