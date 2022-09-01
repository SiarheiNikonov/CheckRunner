package ru.clevertec;

import com.google.gson.Gson;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import ru.clevertec.data.model.Company;
import ru.clevertec.data.model.DiscountCard;
import ru.clevertec.data.model.DiscountCardType;
import ru.clevertec.data.model.Product;
import ru.clevertec.util.jdbc.ConnectionPool;
import ru.clevertec.util.jdbc.PropsKt;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories("ru.clevertec.data.repository")
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

    @Bean
    public SessionFactory sessionFactory(@Autowired org.hibernate.cfg.Configuration config){
        return config.buildSessionFactory();
    }

    @Bean
    public org.hibernate.cfg.Configuration configuration(){
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
        configuration.addAnnotatedClass(Company.class);
        configuration.addAnnotatedClass(Product.class);
        configuration.addAnnotatedClass(DiscountCardType.class);
        configuration.addAnnotatedClass(DiscountCard.class);
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        return configuration.configure();
    }
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(PropsKt.DRIVER);
        dataSource.setUrl(String.format(PropsKt.URL, PropsKt.DATABASE_NAME));
        dataSource.setUsername(PropsKt.USER);
        dataSource.setPassword(PropsKt.PASSWORD);

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(@Autowired EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public EntityManagerFactory entityManagerFactory(@Autowired SessionFactory sessionFactory) {
        return sessionFactory;
//        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//        vendorAdapter.setGenerateDdl(Boolean.TRUE);
//        vendorAdapter.setShowSql(Boolean.TRUE);
//        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
//        factory.setJpaVendorAdapter(vendorAdapter);
//        factory.setPackagesToScan("ru.clevertec.data.model");
//        factory.setDataSource(dataSource());
//        factory.afterPropertiesSet();
//        factory.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());
//        return factory.getObject();
    }
}
