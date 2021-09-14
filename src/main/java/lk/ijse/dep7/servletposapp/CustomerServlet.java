package lk.ijse.dep7.servletposapp;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
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
        /*
            {
               "id": "C001",
               "name": 10.25,
               "status": true,
               "profilePicture": null,
               "address": {
                            "city": "Rathanpura",
                            "province": "Sabaragmuwa"
                          }
            }

            [
              "IJSE", "ESOFT", 10, 25.25, true, false, null, {"id": "C001"}, [10,20]
            ]

            {} JSON Object
            [] JSON Array

        * */
        System.out.println("GET Request");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!req.getContentType().equals("application/json")){
            /* Todo: send an error message */
            return;
        }

//        BufferedReader reader = req.getReader();
//        String line = null;
//        String body = "";
//
//        while ((line = reader.readLine()) != null){
//            body += line;
//        }
//
//        System.out.println(body);

        StringBuilder body = new StringBuilder();
        req.getReader().lines().forEach(l -> body.append(l + "\n"));
        System.out.println(body.toString());

        Jsonb jsonb = JsonbBuilder.create();
        Customer customer = jsonb.fromJson(body.toString(), Customer.class);
        System.out.println(customer.getId());
        System.out.println(customer.getName());
        System.out.println(customer.getAddress());

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