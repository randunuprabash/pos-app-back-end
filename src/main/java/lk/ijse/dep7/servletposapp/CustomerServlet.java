package lk.ijse.dep7.servletposapp;

import java.io.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

/*
    1. Exact Mapping
       /customers
       http://localhost:8080/pos/customers != http://localhost:8080/pos/customers/
       http://localhost:8080/pos/customers?q=C001           - OK
       http://localhost:8080/pos/customers#C001             - OK

    2. Wildcard Mapping = Path Mapping
       /customers*  <- This is not path mapping (This is an exact mapping)
       /customers/*
       http://localhost:8080/pos/customers                  - OK
       http://localhost:8080/pos/customers?q=C001           - OK
       http://localhost:8080/pos/customers/                 - OK
       http://localhost:8080/pos/customers/C001             - OK
       http://localhost:8080/pos/customers/C001/panadura    - OK

    3. Extension Mapping
       *.php
       http://localhost:8080/pos/abc.php                    - OK
       http://localhost:8080/pos/customers/c001/abc.php     - OK

   4. Empty String Mapping
      ""
      http://localhost:8080/pos                             - OK
      http://localhost:8080/pos/                            - OK
      http://localhost:8080/pos/customers                   - NOT OK

   5. Default Mapping
      /
*/
@WebServlet(urlPatterns = "/customers")
public class CustomerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("GET Request");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("POST Request");
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