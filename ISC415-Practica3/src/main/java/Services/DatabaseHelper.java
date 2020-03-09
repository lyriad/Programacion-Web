package Services;

import Utils.Parser;
import org.h2.jdbc.JdbcSQLException;
import org.h2.tools.Server;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {

    public static void startDatabase() throws SQLException {
        Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers").start();
    }

    public static void stopDatabase() throws SQLException {
        Server.shutdownTcpServer("tcp://localhost:9092", "", true, true);
    }

    public static void setupTables() throws  SQLException {

        String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS USER (\n" +
                " id VARCHAR(36) PRIMARY KEY,\n" +
                " username VARCHAR(32) NOT NULL UNIQUE,\n" +
                " name VARCHAR(128) NOT NULL,\n" +
                " password VARCHAR(128) NOT NULL,\n" +
                " admin BOOLEAN NOT NULL,\n" +
                " author BOOLEAN NOT NULL);";

        String CREATE_TABLE_TAG = "CREATE TABLE IF NOT EXISTS TAG (\n" +
                " id VARCHAR(36) PRIMARY KEY,\n" +
                " name VARCHAR(32) NOT NULL UNIQUE);";

        String CREATE_TABLE_ARTICLE = "CREATE TABLE IF NOT EXISTS ARTICLE (\n" +
                " id VARCHAR(36) PRIMARY KEY,\n" +
                " title VARCHAR(96) NOT NULL UNIQUE,\n" +
                " body VARCHAR(3072) NOT NULL,\n" +
                " author VARCHAR(36) NOT NULL,\n" +
                " date TIMESTAMP NOT NULL, " +
                " FOREIGN KEY (author) REFERENCES USER(id));";

        String CREATE_TABLE_COMMENT = "CREATE TABLE IF NOT EXISTS COMMENT (\n" +
                " id VARCHAR(36) PRIMARY KEY,\n" +
                " comment VARCHAR(200) NOT NULL,\n" +
                " author VARCHAR(36) NOT NULL,\n" +
                " article VARCHAR(36) NOT NULL,\n" +
                " FOREIGN KEY (author) REFERENCES USER(id)," +
                " FOREIGN KEY (article) REFERENCES ARTICLE(id));";

        String CREATE_TABLE_ARTICLE_TAG = "CREATE TABLE IF NOT EXISTS ARTICLE_TAG (\n" +
                " id_article VARCHAR(36) NOT NULL,\n" +
                " id_tag VARCHAR(36) NOT NULL,\n" +
                " FOREIGN KEY (id_article) REFERENCES ARTICLE(id)," +
                " FOREIGN KEY (id_tag) REFERENCES TAG(id));";

        String CREATE_DEFAULT_USER = String.format("INSERT INTO USER VALUES '%s', 'lyriad', 'Manuel Espinal', '%s', true, true;",
                Parser.generateUUID("USER"),
                Parser.getHashedPassword("manuel"));

        Connection conn = Database.getInstance().getConnection();
        Statement query = conn.createStatement();
        query.execute(CREATE_TABLE_USER);
        query.execute(CREATE_TABLE_TAG);
        query.execute(CREATE_TABLE_ARTICLE);
        query.execute(CREATE_TABLE_COMMENT);
        query.execute(CREATE_TABLE_ARTICLE_TAG);
        try {
            query.execute(CREATE_DEFAULT_USER);
        } catch(JdbcSQLException e) {
            System.out.println("WARNING: DEFAULT USER ALREADY EXISTS");
        }
        query.close();
        conn.close();
    }
}
