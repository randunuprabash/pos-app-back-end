package lk.ijse.dep7.servletposapp.api;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.dep7.servletposapp.util.DBConnectionPool;


import java.io.IOException;

@WebServlet(name = "TempServlet", value = "/release-all")
public class TempServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBConnectionPool.releaseAllConnections();
    }

}
