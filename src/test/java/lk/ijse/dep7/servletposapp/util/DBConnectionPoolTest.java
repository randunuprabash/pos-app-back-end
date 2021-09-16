package lk.ijse.dep7.servletposapp.util;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DBConnectionPoolTest {

    @Test
    void getConnection() throws SQLException {
        assertNotNull(DBConnectionPool.getConnection());
    }
}