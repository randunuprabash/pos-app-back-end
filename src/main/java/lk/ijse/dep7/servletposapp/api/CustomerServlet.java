package lk.ijse.dep7.servletposapp.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.dep7.servletposapp.dto.CustomerDTO;
import lk.ijse.dep7.servletposapp.util.DBConnection;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/customers")
public class CustomerServlet extends HttpServlet {

    private final Jsonb jsonb = JsonbBuilder.create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        /* GET http://localhost:8080/pos/customers          - All Customers */
        /* GET http://localhost:8080/pos/customers?id=C001  - C001 Customer */
        /* GET http://localhost:8080/pos/customers?id=C100  - 404 */

        try (Connection connection = DBConnection.getConnection()) {

            String id = req.getParameter("id");
            String sql = (id == null) ? "SELECT * FROM customer" : "SELECT * FROM customer WHERE id=?";
            PreparedStatement stm = connection.prepareStatement(sql);

            if (id != null) {
                stm.setString(1, id);
            }

            ResultSet rst = stm.executeQuery();
            List<CustomerDTO> customers = new ArrayList<>();

            while (rst.next()) {
                customers.add(new CustomerDTO(rst.getString("id"),
                        rst.getString("name"),
                        rst.getString("address")));
            }

            if (id != null && customers.isEmpty()){
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            String json = jsonb.toJson(id == null ? customers : customers.get(0));
            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            out.println(json);

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (req.getContentType() == null || !req.getContentType().equals("application/json")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try (Connection connection = DBConnection.getConnection()) {

            CustomerDTO customer = jsonb.fromJson(req.getReader(), CustomerDTO.class);

            if (customer.getId() == null || !customer.getId().matches("C\\d{3}")) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Customer id can't be empty");
                return;
            } else if (customer.getName() == null || customer.getName().trim().isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Customer name can't be empty");
                return;
            } else if (customer.getAddress() == null || customer.getAddress().trim().isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Customer address can't be empty");
                return;
            }

            PreparedStatement stm = connection.prepareStatement("INSERT INTO customer (id, name, address) VALUES (?,?,?)");

            stm.setString(1, customer.getId());
            stm.setString(2, customer.getName());
            stm.setString(3, customer.getAddress());

            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            if (stm.executeUpdate() == 1) {
                out.println(jsonb.toJson(customer.getId()));
            } else {
                throw new RuntimeException("Failed to save the customer, try again");
            }

        } catch (JsonbException exp) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (SQLException | RuntimeException exp) {
            throw new RuntimeException(exp);
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (req.getContentType() == null || !req.getContentType().equals("application/json")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            CustomerDTO customer = jsonb.fromJson(req.getReader(), CustomerDTO.class);

            if (customer.getId() == null || !customer.getId().matches("C\\d{3}")) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Customer id can't be empty");
                return;
            } else if (customer.getName() == null || customer.getName().trim().isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Customer name can't be empty");
                return;
            } else if (customer.getAddress() == null || customer.getAddress().trim().isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Customer address can't be empty");
                return;
            }

            try (Connection connection = DBConnection.getConnection()) {

                PreparedStatement stm = connection.prepareStatement("UPDATE customer SET name=?, address=? WHERE id=?");
                stm.setString(1, customer.getName());
                stm.setString(2, customer.getAddress());
                stm.setString(3, customer.getId());

                if (stm.executeUpdate() == 1) {
                    resp.setContentType("application/json");
                    resp.getWriter().println(jsonb.toJson("OK"));
                } else {
                    throw new RuntimeException("Failed to update the customer");
                }

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        } catch (JsonbException ex) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        /* DELETE http://localhost:8080/pos/customers?id=C001 */

        String id = req.getParameter("id");

        if (id == null || !id.matches("C\\d{3}")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try (Connection connection = DBConnection.getConnection()) {

            PreparedStatement stm = connection.prepareStatement("DELETE FROM customer WHERE id=?");
            stm.setString(1, id);

            if (stm.executeUpdate() == 1) {
                resp.setContentType("application/json");
                resp.getWriter().println(jsonb.toJson("OK"));
            } else {
                throw new RuntimeException("Failed to delete the customer");
            }

        } catch (SQLException exp) {
            throw new RuntimeException(exp);
        }

    }
}