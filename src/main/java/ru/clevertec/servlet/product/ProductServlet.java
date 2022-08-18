package ru.clevertec.servlet.product;

import ru.clevertec.AppContextHolder;
import ru.clevertec.service.product.ProductService;
import ru.clevertec.servlet.AbstractCrudServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

@WebServlet("/products")
public class ProductServlet extends AbstractCrudServlet {

    @Override
    public void init() throws ServletException {
        service = AppContextHolder.getInstance().getContext().getBean(ProductService.class);
    }
}
