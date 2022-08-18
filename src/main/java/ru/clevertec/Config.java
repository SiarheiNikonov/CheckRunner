package ru.clevertec;

import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.util.jdbc.ConnectionPool;
import ru.clevertec.util.jdbc.PropsKt;

@Configuration
@ComponentScan("ru.clevertec")
public class Config {

    @Bean
    public ConnectionPool connectionPool() {
        return new ConnectionPool(PropsKt.DEFAULT_POOL_SIZE);
    }

    @Bean
    public Gson gson() {
        return new Gson();
    }
}
