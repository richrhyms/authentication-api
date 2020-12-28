package com.richotaru.authenticationapi.configuration;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Properties;

/**
 * @author Otaru Richard richotaru@gmail.com
 */
@Configuration
@ComponentScan
public class ServiceLayerConfiguration {
    @Profile({"dev"})
    @Bean("entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryDev(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource);
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setShowSql(true);
        hibernateJpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.PostgreSQL95Dialect");
        factoryBean.setJpaVendorAdapter(hibernateJpaVendorAdapter);
        Properties props = new Properties();
        props.put("showSql", true);
        props.put("formatSql", true);
        props.put("databasePlatform", Database.POSTGRESQL);
        props.put("hibernate.hbm2ddl.auto", "create");
        factoryBean.setJpaProperties(props);
        factoryBean.setPackagesToScan("com.richotaru.authenticationapi.domain.entity");

        return factoryBean;
    }

    @Profile({"test"})
    @Bean("entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryTest(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource);
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setShowSql(true);
        hibernateJpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.H2Dialect");
        factoryBean.setJpaVendorAdapter(hibernateJpaVendorAdapter);
        Properties props = new Properties();
        props.put("showSql", false);
        props.put("formatSql", true);
        props.put("databasePlatform", Database.H2);
        props.put("hibernate.hbm2ddl.auto", "create");
        factoryBean.setJpaProperties(props);
        factoryBean.setPackagesToScan("com.richotaru.authenticationapi.domain.entity");

        return factoryBean;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource);
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setShowSql(true);
        hibernateJpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.PostgreSQL95Dialect");
        factoryBean.setJpaVendorAdapter(hibernateJpaVendorAdapter);
        Properties props = new Properties();
        props.put("showSql", false);
        props.put("formatSql", true);
        props.put("databasePlatform", Database.POSTGRESQL);
        props.put("hibernate.hbm2ddl.auto", "create");
        factoryBean.setJpaProperties(props);
        factoryBean.setPackagesToScan("com.richotaru.authenticationapi.domain.entity");
        return factoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    @Bean
    public ConstraintValidatorFactory constraintValidatorFactory(final AutowireCapableBeanFactory beanFactory) {
        return new ConstraintValidatorFactory() {

            @Override
            public void releaseInstance(
                    ConstraintValidator<?, ?> arg0) {
                beanFactory.destroyBean(arg0);
            }

            @Override
            public <T extends ConstraintValidator<?, ?>> T getInstance(
                    Class<T> arg0) {
                try {
                    return beanFactory.getBean(arg0);
                } catch (NoSuchBeanDefinitionException e) {
                    if (arg0.isInterface()) {
                        throw e;
                    }
                    return beanFactory.createBean(arg0);
                }
            }
        };
    }

    @Bean
    public Validator validator(ConstraintValidatorFactory factory) {
        return Validation.byDefaultProvider()
                .configure()
                .constraintValidatorFactory(factory)
                .buildValidatorFactory()
                .getValidator();
    }
}
