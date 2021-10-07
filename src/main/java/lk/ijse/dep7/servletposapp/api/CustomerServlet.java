package lk.ijse.dep7.servletposapp.api;


import jakarta.annotation.Resource;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.dep7.servletposapp.dto.CustomerDTO;
import lk.ijse.dep7.servletposapp.exception.DuplicateIdentifierException;
import lk.ijse.dep7.servletposapp.exception.FailedOperationException;
import lk.ijse.dep7.servletposapp.exception.NotFoundException;
import lk.ijse.dep7.servletposapp.service.CustomerService;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/customers")
public class CustomerServlet extends HttpServlet {

    private final Jsonb jsonb = JsonbBuilder.create();

    @Resource(name = "java:comp/env/jdbc/posCP")
    public DataSource connectionPool;

    @Override
    public void init() throws ServletException {

    }

//    @Override
//    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        resp.setHeader("Access-Control-Allow-Origin", "*");
//        resp.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, DELETE");
//        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
//    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        /* GET http://localhost:8080/pos/customers          - All Customers */
        /* GET http://localhost:8080/pos/customers?id=C001  - C001 Customer */
        /* GET http://localhost:8080/pos/customers?id=C100  - 404 */

//        resp.setHeader("Access-Control-Allow-Origin", "*");

        System.out.println("Do GET()");

        try (Connection connection = connectionPool.getConnection()) {

            CustomerService customerService = new CustomerService(connection);
            String id = req.getParameter("id");
            String page = req.getParameter("page");
            String size = req.getParameter("size");

            List<CustomerDTO> customers = new ArrayList<>();
            if (id == null) {

                if (page != null && size != null) {

                    if (!(page.matches("[-]?\\d+") && size.matches("[-]?\\d+"))){
                        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid page or size");
                        return;
                    }

                    int p = Integer.parseInt(page);
                    int s = Integer.parseInt(size);

                    if (p <= 0 || s <= 0) {
                        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid page or size");
                        return;
                    }

                    resp.setHeader("X-Total-Count", customerService.getCustomersCount() + "");
//                    resp.setHeader("Access-Control-Expose-Headers", "X-Total-Count");
                    customers = customerService.findAllCustomers(p, s);
                } else {
                    customers = customerService.findAllCustomers();
                }
            } else {
                customers.add(customerService.findCustomer(id));
            }

            String json = jsonb.toJson(id == null ? customers : customers.get(0));
            resp.setHeader("X-Something", "To understand");
            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            out.println(json);
            out.println("From Servlet");
            out.close();

        } catch (SQLException | FailedOperationException ex) {
            throw new RuntimeException(ex);
        } catch (NotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

//        resp.setHeader("Access-Control-Allow-Origin", "*");

        if (req.getContentType() == null || !req.getContentType().equals("application/json")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try (Connection connection = connectionPool.getConnection()) {

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

            CustomerService customerService = new CustomerService(connection);
            customerService.saveCustomer(customer);

            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_CREATED);
            PrintWriter out = resp.getWriter();
            out.println(jsonb.toJson(customer.getId()));

        } catch (JsonbException exp) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (SQLException | RuntimeException | FailedOperationException exp) {
            throw new RuntimeException(exp);
        } catch (DuplicateIdentifierException e) {
            throw new RuntimeException("Customer already exits", e);
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

//        resp.setHeader("Access-Control-Allow-Origin", "*");

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

            try (Connection connection = connectionPool.getConnection()) {

                CustomerService customerService = new CustomerService(connection);
                customerService.updateCustomer(customer);
                resp.setContentType("application/json");
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                resp.getWriter().println(jsonb.toJson("OK"));

            } catch (SQLException | FailedOperationException ex) {
                throw new RuntimeException(ex);
            } catch (NotFoundException e) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }

        } catch (JsonbException ex) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        /* DELETE http://localhost:8080/pos/customers?id=C001 */
        resp.setHeader("Access-Control-Allow-Origin", "*");

        String id = req.getParameter("id");

        if (id == null || !id.matches("C\\d{3}")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try (Connection connection = connectionPool.getConnection()) {

            CustomerService customerService = new CustomerService(connection);
            customerService.deleteCustomer(id);
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            resp.getWriter().println(jsonb.toJson("OK"));

        } catch (SQLException | FailedOperationException exp) {
            throw new RuntimeException(exp);
        } catch (NotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

    }
}