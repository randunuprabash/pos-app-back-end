package lk.ijse.dep7.servletposapp.service;

import lk.ijse.dep7.servletposapp.dto.OrderDTO;
import lk.ijse.dep7.servletposapp.exception.FailedOperationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

class OrderServiceTest {

    private OrderService orderService;
    private Connection connection;

    @BeforeEach
    void setUp() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dep7_backup_pos", "root", "mysql");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        orderService = new OrderService(connection);
    }

    @AfterEach
    void tearDown() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @ValueSource(strings =  {"","Kasun", "Di", "Di 2021-08", "2021-09 K"})
    void searchOrders(String query) throws FailedOperationException {
        List<OrderDTO> orderDTOS = orderService.searchOrders(query, 1, 5);
        orderDTOS.forEach(System.out::println);
        Assertions.assertTrue(orderDTOS.size() > 0);
    }

    @Test
    void searchOrders() throws FailedOperationException {
        List<OrderDTO> orderDTOS = orderService.searchOrders("", 2, 2);
        orderDTOS.forEach(System.out::println);
        Assertions.assertTrue(orderDTOS.size() > 0);
    }
}