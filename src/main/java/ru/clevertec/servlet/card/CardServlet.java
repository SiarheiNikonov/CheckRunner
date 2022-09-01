package ru.clevertec.servlet.card;

import ru.clevertec.AppContextHolder;
import ru.clevertec.service.card.CardService;
import ru.clevertec.servlet.AbstractCrudServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

@WebServlet("/cards")
public class CardServlet extends AbstractCrudServlet {

    @Override
    public void init() throws ServletException {
        service = AppContextHolder.getInstance().getContext().getBean(CardService.class);
    }
}
