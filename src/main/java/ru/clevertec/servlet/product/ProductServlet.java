package ru.clevertec.servlet.product;

import ru.clevertec.data.model.state.Fail;
import ru.clevertec.data.model.state.Result;
import ru.clevertec.data.model.state.Success;
import ru.clevertec.service.RequestMethod;
import ru.clevertec.service.product.ProductService;
import ru.clevertec.util.ServiceLocator;
import ru.clevertec.util.exceptions.RepositoryInitializationException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {

    private final ProductService service = (ProductService) ServiceLocator
            .getInstance()
            .getDependency(ProductService.class);

    public ProductServlet() throws RepositoryInitializationException {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Result<String> result = service.handleRequest(req.getQueryString(), RequestMethod.GET);
        sendResponse(resp, result);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuilder builder = new StringBuilder();
        try(BufferedReader reader = req.getReader()) {
            while(reader.ready()) {
                builder.append(reader.readLine());
            }
        }
        Result<String> result = service.handleRequest(builder.toString(), RequestMethod.POST);
        sendResponse(resp, result);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Result<String> result = service.handleRequest(req.getParameter("id"), RequestMethod.DELETE);
        sendResponse(resp, result);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuilder builder = new StringBuilder();
        try(BufferedReader reader = req.getReader()) {
            while(reader.ready()) {
                builder.append(reader.readLine());
            }
        }
        Result<String> result = service.handleRequest(builder.toString(), RequestMethod.PUT);
        sendResponse(resp, result);
    }

    private void setError(HttpServletResponse resp, Fail fail) throws IOException {
        String message = fail.getMessage();
        int errorCode = fail.getErrorCode();
        resp.sendError(errorCode, message);
    }

    private void writeResponse(HttpServletResponse resp, String answer) throws IOException {
        try (Writer writer = resp.getWriter()) {
            writer.write(answer);
        }
    }

    private void sendResponse(HttpServletResponse resp, Result<String> result) throws IOException {
        if(result.getClass() == Success.class) {
            writeResponse(resp, ((Success<String>) result).getData());
        } else {
            setError(resp, (Fail) result);
        }
    }
}
