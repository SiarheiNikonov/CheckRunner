package ru.clevertec.servlet.filter;

import javax.servlet.*;
import java.io.IOException;

//@WebFilter(urlPatterns = "/*", servletNames = {"ProductServlet"})
public class JsonContentTypeFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        response.setContentType("text/json");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
