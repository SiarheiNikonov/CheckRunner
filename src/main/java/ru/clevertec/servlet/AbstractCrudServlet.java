package ru.clevertec.servlet;

import ru.clevertec.service.RequestMethod;
import ru.clevertec.service.Service;
import ru.clevertec.service.model.state.Fail;
import ru.clevertec.service.model.state.Result;
import ru.clevertec.service.model.state.Success;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;

public class AbstractCrudServlet extends HttpServlet {
    protected Service service;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String query = req.getQueryString();
        Result<String> result = service.handleRequest(query, RequestMethod.GET);
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
