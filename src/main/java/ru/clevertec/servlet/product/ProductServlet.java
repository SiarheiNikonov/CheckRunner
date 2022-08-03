package ru.clevertec.servlet.product;

import com.google.gson.Gson;
import ru.clevertec.data.model.Product;
import ru.clevertec.data.model.state.Fail;
import ru.clevertec.data.model.state.Result;
import ru.clevertec.data.model.state.Success;
import ru.clevertec.service.product.ProductService;
import ru.clevertec.util.ServiceLocator;
import ru.clevertec.util.exceptions.RepositoryInitializationException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.regex.Pattern;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {

    private static final String PAGING_REGEX = "^last_index=[1-9]+\\d*&page_size=[1-9]+\\d*$";
    private final ProductService service = (ProductService) ServiceLocator
            .getInstance()
            .getDependency(ProductService.class);

    public ProductServlet() throws RepositoryInitializationException {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String query = req.getQueryString();
        if (Pattern.matches(PAGING_REGEX, query)) {
            String[] params = query.split("&");
            String lastIndex = params[0].split("=")[1];
            String pageSize = params[1].split("=")[1];

            Result<List<Product>> result = service.findAll(pageSize, lastIndex);
            if (result.getClass() == Success.class) {
                List<Product> products = ((Success<List<Product>>) result).getData();
                try (Writer writer = resp.getWriter()) {
                    resp.setContentType("text/json");
                    String res = new Gson().toJson(products);
                    writer.write(res);
                }
            } else {
                setError(resp, (Fail) result);
            }

        } else {

            Result<Product> result = service.getById(req.getParameter("id"));
            if (result.getClass() == Success.class) {
                Product product = ((Success<Product>) result).getData();
                String productJson = new Gson().toJson(product);
                try (Writer writer = resp.getWriter()) {
                    resp.setContentType("text/json");
                    writer.write(productJson);
                }
            } else {
                setError(resp, (Fail) result);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Product product = new Gson().fromJson(req.getReader(), Product.class);
        Result<Boolean> result = service.add(product);
        if (result.getClass() == Success.class) {
            try (Writer writer = resp.getWriter()) {
                writer.write("Product has been saved");
            }
        } else {
            setError(resp, (Fail) result);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Result<Boolean> result = service.removeById(req.getParameter("id"));
        if (result.getClass() == Success.class) {
            try (Writer writer = resp.getWriter()) {
                String message = ((Success<Boolean>) result).getData()
                        ? "Product has been deleted"
                        : "Product with such id not found";
                writer.write(message);
            }
        } else {
            setError(resp, (Fail) result);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Product product = new Gson().fromJson(req.getReader(), Product.class);
        Result<Boolean> result = service.update(product);
        if (result.getClass() == Success.class) {
            try (Writer writer = resp.getWriter()) {
                writer.write("Product has been updated");
            }
        } else {
            setError(resp, (Fail) result);
        }
    }

    private void setError(HttpServletResponse resp, Fail fail) throws IOException {
        String message = fail.getMessage();
        int errorCode = fail.getErrorCode();
        resp.sendError(errorCode, message);
    }
}
