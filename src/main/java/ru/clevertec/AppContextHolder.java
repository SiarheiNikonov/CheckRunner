package ru.clevertec;

import org.springframework.context.ApplicationContext;

public class AppContextHolder {

    private static volatile AppContextHolder instance;
    private final ApplicationContext context;

    private AppContextHolder(ApplicationContext context) {
        this.context = context;
    }

    public ApplicationContext getContext(){
        return context;
    }

    public static void init(ApplicationContext context) {
        if(instance == null) {
            synchronized (AppContextHolder.class){
                if(instance == null) {
                    instance = new AppContextHolder(context);
                }
            }
        }
    }

    public static AppContextHolder getInstance(){
        return instance;
    }
}
