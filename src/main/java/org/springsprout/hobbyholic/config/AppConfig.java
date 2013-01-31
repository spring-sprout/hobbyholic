package org.springsprout.hobbyholic.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.stereotype.Controller;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;

/**
 * @author Keesun Baik
 * @author Yongkwon Park
 */
@Configuration
@ComponentScan(basePackages="org.springsprout.hobbyholic"
             , excludeFilters={@ComponentScan.Filter(Controller.class), @ComponentScan.Filter(Configuration.class)})
@PropertySource(name="hobbyholicProperties", value="/hobbyholic.properties")
public class AppConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(){
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public DataSource dataSource(@Value("${db.driver}") String driverClass
                               , @Value("${db.url}") String jdbcUrl
                               , @Value("${db.user}") String user
                               , @Value("${db.password}") String password
                               , @Value("${db.pool.acquireIncrement}") Integer acquireIncrement
                               , @Value("${db.pool.minPoolSize}") Integer minPoolSize
                               , @Value("${db.pool.maxPoolSize}") Integer maxPoolSize
                               , @Value("${db.pool.maxIdleTime}") Integer maxIdleTime
                               , @Value("${db.pool.initialPoolSize}") Integer initialPoolSize) throws PropertyVetoException {

        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(driverClass);
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUser(user);
        dataSource.setPassword(password);
        dataSource.setAcquireIncrement(acquireIncrement);
        dataSource.setMinPoolSize(minPoolSize);
        dataSource.setMaxPoolSize(maxPoolSize);
        dataSource.setMaxIdleTime(maxIdleTime);
        dataSource.setInitialPoolSize(initialPoolSize);

        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource
                                                , @Value("${hibernate.dialect}") String dialect
                                                , @Value("${hibernate.hbm2ddl.auto}") String hbm2ddlAuto
                                                , @Value("${hibernate.show_sql}") String showSql
                                                , @Value("${hibernate.format_sql}") String formatSql){

        Properties hibernateProperties = new Properties();
        hibernateProperties.put(Environment.DIALECT, dialect);
        hibernateProperties.put(Environment.SHOW_SQL, hbm2ddlAuto);
        hibernateProperties.put(Environment.HBM2DDL_AUTO, showSql);
        hibernateProperties.put(Environment.FORMAT_SQL, formatSql);

        LocalSessionFactoryBean lsfb = new LocalSessionFactoryBean();
        lsfb.setDataSource(dataSource);
        lsfb.setHibernateProperties(hibernateProperties);
        lsfb.setPackagesToScan("org.springsprout.hobbyholic.domain");

        return lsfb;
    }

}
