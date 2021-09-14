package lk.ijse.dep7.servletposapp.api;

import java.io.*;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import lk.ijse.dep7.servletposapp.dto.CustomerDTO;

@WebServlet(urlPatterns = "/customers")
public class CustomerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /* 1. DB Connect */
        /* 2. Fetch Customers */
        /* 3. Convert to JSON Array */
        /* 4. Send back to the client */
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
        CustomerDTO customerDTO = jsonb.fromJson(body.toString(), CustomerDTO.class);
        System.out.println(customerDTO.getId());
        System.out.println(customerDTO.getName());
        System.out.println(customerDTO.getAddress());

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