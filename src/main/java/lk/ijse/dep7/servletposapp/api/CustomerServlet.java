package lk.ijse.dep7.servletposapp.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.dep7.servletposapp.dto.CustomerDTO;
import lk.ijse.dep7.servletposapp.util.DBConnection;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/customers")
public class CustomerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try (Connection connection = DBConnection.getConnection()) {

            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM customer");
            List<CustomerDTO> customers = new ArrayList<>();

            while (rst.next()) {
                customers.add(new CustomerDTO(rst.getString("id"),
                        rst.getString("name"),
                        rst.getString("address")));
            }

            Jsonb jsonb = JsonbBuilder.create();
            String json = jsonb.toJson(customers);

            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            out.println(json);

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (!req.getContentType().equals("application/json")) {
            /* Todo: Handle this */
            return;
        }

        /* Todo: handle invalid json */
        Jsonb jsonb = JsonbBuilder.create();
        CustomerDTO customer = jsonb.fromJson(req.getReader(), CustomerDTO.class);

        try (Connection connection = DBConnection.getConnection()) {
            /* 4. Save the customer */
            PreparedStatement stm = connection.prepareStatement("INSERT INTO customer (id, name, address) VALUES (?,?,?)");
            stm.setString(1, customer.getId());
            stm.setString(2, customer.getName());
            stm.setString(3, customer.getAddress());

            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            if (stm.executeUpdate() == 1){
                out.println(jsonb.toJson(true));
            }else{
                out.println(jsonb.toJson(false));
            }

        } catch (SQLException exp) {
            throw new RuntimeException(exp);
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("PUT Request");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("DELETE Request");
    }
}