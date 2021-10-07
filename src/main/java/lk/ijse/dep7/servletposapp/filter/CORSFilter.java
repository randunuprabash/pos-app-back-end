package lk.ijse.dep7.servletposapp.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

@WebFilter(urlPatterns = "/customers/*")
public class CORSFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        System.out.println("Incoming Request URL: "  + req.getRequestURL());
        System.out.println("Incoming Request URI: "  + req.getRequestURI());
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("Outgoing?");
    }
}
