package lk.ijse.dep7.servletposapp.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.IOException;
import java.io.PrintWriter;

@WebFilter(urlPatterns = "/*")
public class CORSFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        resp.setHeader("Access-Control-Allow-Origin", "*");

        if (req.getMethod().equalsIgnoreCase("OPTIONS")){
            resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
            resp.setHeader("Access-Control-Allow-Methods", "POST, PUT, DELETE, GET");
        }

        if (req.getMethod().equalsIgnoreCase("GET")){
            resp.setHeader("Access-Control-Expose-Headers", "X-Total-Count");
        }

        filterChain.doFilter(servletRequest, servletResponse);

//        System.out.println("We can manipulate both request and response of the servlet");
//
//        FakeResponse fakeResponse = new FakeResponse((HttpServletResponse) servletResponse);
//        filterChain.doFilter(servletRequest, fakeResponse);
//        PrintWriter writer = fakeResponse.getWriter();
//        writer.println("Hello I am from the Filter");
//        fakeResponse.setHeader("X-Filter", "Something Crazy Here");
//        fakeResponse.setFlushMode(true);
//        writer.close();

    }

    static class FakeResponse extends HttpServletResponseWrapper {

        private boolean flushMode;
        private final PrintWriter fakeWriter;

        public FakeResponse(HttpServletResponse response) throws IOException {
            super(response);

            fakeWriter = new PrintWriter(super.getOutputStream()) {

                @Override
                public void flush() {
                    if (flushMode) super.flush();
                }

                @Override
                public void close() {
                    if (flushMode) super.close();
                }
            };
        }

        public boolean isFlushMode() {
            return flushMode;
        }

        public void setFlushMode(boolean flushMode) {
            this.flushMode = flushMode;
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            return fakeWriter;
        }
    }

}
