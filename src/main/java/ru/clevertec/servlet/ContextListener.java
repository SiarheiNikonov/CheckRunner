package ru.clevertec.servlet;

import ru.clevertec.util.jdbc.DatabaseInitializer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        try {
            DatabaseInitializer.main(null);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
