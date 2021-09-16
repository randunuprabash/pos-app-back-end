package lk.ijse.dep7.servletposapp.api;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import lk.ijse.dep7.servletposapp.util.DBConnectionPool;

import java.io.IOException;

@WebServlet(name = "TempServlet", value = "/release-all")
public class TempServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBConnectionPool.releaseAllConnections();
    }

}
