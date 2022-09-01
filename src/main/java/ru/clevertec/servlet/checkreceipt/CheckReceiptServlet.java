package ru.clevertec.servlet.checkreceipt;

import ru.clevertec.AppContextHolder;
import ru.clevertec.service.checkreceipt.CheckReceiptService;
import ru.clevertec.service.model.state.Fail;
import ru.clevertec.service.model.state.Result;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/receipt")
public class CheckReceiptServlet extends HttpServlet {

    private CheckReceiptService service;

    @Override
    public void init() throws ServletException {
        service = AppContextHolder.getInstance().getContext().getBean(CheckReceiptService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        String query = req.getQueryString();
        Result<Boolean> result = service.handleRequest(query, resp.getOutputStream());
        if(result.getClass() == Fail.class) {
            Fail fail = (Fail) result;
            resp.sendError(fail.getErrorCode(), fail.getMessage());
        }
        resp.getOutputStream().flush();
        resp.getOutputStream().close();

    }
}
