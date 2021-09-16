package lk.ijse.dep7.servletposapp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBConnectionPool {

    private static final int MAX_CONNECTIONS = 4;

    private static List<Connection> connectionPool = new ArrayList<>();
    private static List<Connection> consumerPool = new ArrayList<>();

    static{
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            for (int i = 0; i < MAX_CONNECTIONS; i++) {
                connectionPool.add(DriverManager.getConnection("jdbc:mysql://localhost:3306/dep7_backup_pos", "root", "mysql"));
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

}
