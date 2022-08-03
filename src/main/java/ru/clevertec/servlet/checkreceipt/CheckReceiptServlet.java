package ru.clevertec.servlet.checkreceipt;

import ru.clevertec.data.model.state.Fail;
import ru.clevertec.data.model.state.Result;
import ru.clevertec.service.checkreceipt.CheckReceiptService;
import ru.clevertec.util.ServiceLocator;
import ru.clevertec.util.exceptions.RepositoryInitializationException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/receipt")
public class CheckReceiptServlet extends HttpServlet {

    private final CheckReceiptService service = (CheckReceiptService) ServiceLocator
            .getInstance()
            .getDependency(CheckReceiptService.class);

    public CheckReceiptServlet() throws RepositoryInitializationException {
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
