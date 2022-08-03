package ru.clevertec.servlet.card;

import com.google.gson.Gson;
import ru.clevertec.data.model.DiscountCard;
import ru.clevertec.data.model.state.Fail;
import ru.clevertec.data.model.state.Result;
import ru.clevertec.data.model.state.Success;
import ru.clevertec.service.card.CardService;
import ru.clevertec.util.ServiceLocator;
import ru.clevertec.util.exceptions.RepositoryInitializationException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.regex.Pattern;

@WebServlet("/cards")
public class CardServlet extends HttpServlet {

    private static final String PAGING_REGEX = "^last_index=[1-9]+\\d*&page_size=[1-9]+\\d*$";
    private final CardService service = (CardService) ServiceLocator
            .getInstance()
            .getDependency(CardService.class);

    public CardServlet() throws RepositoryInitializationException {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String query = req.getQueryString();
        if (Pattern.matches(PAGING_REGEX, query)) {
            String[] params = query.split("&");
            String lastIndex = params[0].split("=")[1];
            String pageSize = params[1].split("=")[1];

            Result<List<DiscountCard>> result = service.findAll(pageSize, lastIndex);
            if (result.getClass() == Success.class) {
                List<DiscountCard> products = ((Success<List<DiscountCard>>) result).getData();
                try (Writer writer = resp.getWriter()) {
                    resp.setContentType("text/json");
                    String res = new Gson().toJson(products);
                    writer.write(res);
                }
            } else {
                setError(resp, (Fail) result);
            }
        }else {
            Result<DiscountCard> result = service.getById(req.getParameter("id"));
            if (result.getClass() == Success.class) {
                DiscountCard card = ((Success<DiscountCard>) result).getData();
                String cardJson = new Gson().toJson(card);
                try (Writer writer = resp.getWriter()) {
                    resp.setContentType("text/json");
                    writer.write(cardJson);
                }
            } else {
                setError(resp, (Fail) result);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        DiscountCard card = new Gson().fromJson(req.getReader(), DiscountCard.class);
        Result<Boolean> result = service.add(card);
        if (result.getClass() == Success.class) {
            try (Writer writer = resp.getWriter()) {
                writer.write("Card has been saved");
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
                        ? "Card has been deleted"
                        : "Card with such id not found";
                writer.write(message);
            }
        } else {
            setError(resp, (Fail) result);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        DiscountCard card = new Gson().fromJson(req.getReader(), DiscountCard.class);
        Result<Boolean> result = service.update(card);
        if (result.getClass() == Success.class) {
            try (Writer writer = resp.getWriter()) {
                writer.write("Card has been updated");
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
