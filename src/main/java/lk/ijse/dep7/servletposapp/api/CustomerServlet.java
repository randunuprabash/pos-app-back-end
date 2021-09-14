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
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        CustomerDTO customerDTO = new CustomerDTO("C001", "Kasun", "Galle");

        Jsonb jsonb = JsonbBuilder.create();
        String json = jsonb.toJson(customerDTO);
        System.out.println(json);

        out.println(json);

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