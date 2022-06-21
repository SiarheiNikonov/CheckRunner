package ru.clevertec.service.handler;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.clevertec.service.CheckService;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;


public class CheckServiceHandler implements InvocationHandler {

    private CheckService service;

    private Gson gson = new Gson();

    public CheckServiceHandler(CheckService service) {
        this.service = service;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Logger logger = LogManager.getLogger(this.getClass().getSimpleName());
        logger.debug("Method: " + method.getName());
        logger.debug("Arguments:");
        Arrays.stream(args).forEach(
                arg -> {
                    if (Arrays.stream(arg.getClass().getInterfaces())
                            .filter(interf -> interf == Map.class)
                            .count() == 1) {
                        logger.debug("\t map:");
                        ((Map) arg).forEach((key, value) ->
                                logger.debug("\t\t- " + key.getClass().getSimpleName() + ": " + gson.toJson(key) + ", count: " + value));

                    } else logger.debug("\t- " + gson.toJson(arg));

                }
        );

        Object result = method.invoke(service, args);
        if (result == null) logger.debug("Result is null or method returned nothing (void). \n");
        else logger.debug("Result: " + gson.toJson(result) + "\n");

        return result;
    }
}
