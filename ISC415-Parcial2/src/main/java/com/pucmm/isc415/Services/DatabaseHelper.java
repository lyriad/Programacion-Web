package com.pucmm.isc415.Services;

import com.pucmm.isc415.Models.User;
import com.pucmm.isc415.Utils.MyParser;
import org.h2.tools.Server;
import java.sql.SQLException;
import java.util.Calendar;

public class DatabaseHelper {

    public static void startDatabase() throws SQLException {
        Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers").start();
    }

    public static void stopDatabase() throws SQLException {
        Server.shutdownTcpServer("tcp://localhost:9092", "", true, true);
    }

    public static void createAdmin() {

        UserServices.getInstance().create(new User(
                "admin",
                "Admin",
                "admin@domain.com",
                true,
                Calendar.getInstance(),
                MyParser.getHashedPassword("admin")
        ));
    }
}
