package lk.ijse.dep7.servletposapp.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Collection;

@WebFilter(urlPatterns = "/*")
public class CORSFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;

        MyServletResponse wrapperResponse = new MyServletResponse((HttpServletResponse) servletResponse);
        wrapperResponse.setFlushMode(false);
        System.out.println("Incoming Request URL: "  + req.getRequestURL());
        System.out.println("Incoming Request URI: "  + req.getRequestURI());

        filterChain.doFilter(servletRequest, wrapperResponse);
        wrapperResponse.setFlushMode(true);
        PrintWriter writer = wrapperResponse.getWriter();
        wrapperResponse.setHeader("Access-Control-Allow-Origin", "test");
        System.out.println(wrapperResponse.getContentType());
        System.out.println(wrapperResponse.getHeader("Content-Type"));
        System.out.println(wrapperResponse.getHeader("X-Something"));
        System.out.println("-----------------");
        wrapperResponse.getHeaderNames().forEach(System.out::println);
        writer.println("Testing");
        writer.close();
        System.out.println("CLosed");
    }

    static class MyServletResponse extends HttpServletResponseWrapper{

        private boolean flushMode = false;
        private PrintWriter writer;

        public MyServletResponse(HttpServletResponse response) throws IOException {
            super(response);
             writer = new PrintWriter(getOutputStream()){

                @Override
                public void close() {
                    if (flushMode){
                        super.close();
                    }
                }

                @Override
                public void flush() {
                    if (flushMode){
                        super.flush();
                    }
                }
            };
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            return writer;
        }

        public boolean isFlushMode() {
            return flushMode;
        }

        public void setFlushMode(boolean flushMode) {
            this.flushMode = flushMode;
        }

        @Override
        public void flushBuffer() throws IOException {
            writer.flush();
            super.flushBuffer();
        }
    }
}
