package ru.clevertec.servlet;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.clevertec.AppContextHolder;
import ru.clevertec.Config;
import ru.clevertec.util.jdbc.DatabaseInitializer;

import javax.servlet.*;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            DatabaseInitializer.main(null);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        ApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(Config.class);
        AppContextHolder.init(annotationConfigApplicationContext);
    }
}
