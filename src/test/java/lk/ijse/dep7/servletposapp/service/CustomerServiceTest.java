package lk.ijse.dep7.servletposapp.service;

import lk.ijse.dep7.servletposapp.dto.CustomerDTO;
import lk.ijse.dep7.servletposapp.exception.FailedOperationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceTest {

    private CustomerService customerService;
    private Connection connection;

    @BeforeEach
    void setUp() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dep7_backup_pos", "root", "mysql");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        customerService = new CustomerService(connection);
    }

    @AfterEach
    void tearDown() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void findAllCustomers() throws FailedOperationException {
        List<CustomerDTO> allCustomers = customerService.findAllCustomers(1, 2);
        assertTrue(allCustomers.size() == 2);
        allCustomers = customerService.findAllCustomers(1, 3);
        assertTrue(allCustomers.size() == 3);
        allCustomers = customerService.findAllCustomers(2, 2);
        assertTrue(allCustomers.size() == 2);
    }
}