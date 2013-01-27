package org.springsprout.hobbyholic.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;

/**
 * @author Keesun Baik
 */
@Configuration
@ComponentScan(basePackages = "org.springsprout.hobbyholic",
        excludeFilters = {@ComponentScan.Filter(Controller.class), @ComponentScan.Filter(Configuration.class)})
@PropertySource(value = "/hobbyholic.properties")
public class AppConfig {

    @Autowired org.springframework.core.env.Environment environment;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(){
        PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
        Resource[] resourceLocations = new Resource[] {
            new ClassPathResource("/hobbyholic.properties"),
        };
        pspc.setLocations(resourceLocations);
        return pspc;
    }

    @Bean
    public DataSource dataSource() {
        ComboPooledDataSource dataSource = null;
        try {
            dataSource = new ComboPooledDataSource();
            dataSource.setUser(environment.getProperty("db.user"));
            dataSource.setPassword(environment.getProperty("db.password"));
            dataSource.setDriverClass(environment.getProperty("db.driver"));
            dataSource.setJdbcUrl(environment.getProperty("db.url"));
            dataSource.setAcquireIncrement(Integer.parseInt(environment.getProperty("db.pool.acquireIncrement")));
            dataSource.setMinPoolSize(Integer.parseInt(environment.getProperty("db.pool.minPoolSize")));
            dataSource.setMaxPoolSize(Integer.parseInt(environment.getProperty("db.pool.maxPoolSize")));
            dataSource.setMaxIdleTime(Integer.parseInt(environment.getProperty("db.pool.maxIdleTime")));
            dataSource.setInitialPoolSize(Integer.parseInt(environment.getProperty("db.pool.initialPoolSize")));
        } catch (PropertyVetoException e) {
            throw new IllegalArgumentException(e);
        }
        return dataSource;
    }

    @Bean
    public SessionFactory sessionFactory(){
//        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
//        ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
//
//        return new LocalSessionFactoryBuilder(dataSource())
//                .scanPackages("org.springsprout.hobbyholic.domain")
//                .addProperties(hibernateProperties())
//                .buildSessionFactory(serviceRegistry);

        LocalSessionFactoryBean lsfb = new LocalSessionFactoryBean();
        lsfb.setDataSource(dataSource());
        lsfb.setHibernateProperties(hibernateProperties());
        lsfb.setPackagesToScan("org.springsprout.hobbyholic.domain");
        return lsfb.getObject();
    }

    @Bean
    public Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put(Environment.DIALECT, environment.getProperty(Environment.DIALECT));
        properties.put(Environment.SHOW_SQL, environment.getProperty(Environment.SHOW_SQL));
        properties.put(Environment.HBM2DDL_AUTO, environment.getProperty(Environment.HBM2DDL_AUTO));
        properties.put(Environment.FORMAT_SQL, environment.getProperty(Environment.FORMAT_SQL));
        return properties;
    }

}
