package com.prash.spring.quartz;

import java.util.Properties;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("com.fdc.merchantapi.dao.repository")
public class SpringQuartzJPAConfig {

    private static final String PROPERTY_NAME_HIBERNATE_DIALECT = "hibernate.dialect";
    private static final String PROPERTY_NAME_HIBERNATE_SHOW_SQL = "hibernate.show_sql";

    @Resource
    private Environment env;

    @Bean
    public DataSource dataSource() {
	final JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
	dsLookup.setResourceRef(true);
	return dsLookup.getDataSource("java:/datasources/MerchantDS");
    }

    @Bean
    JdbcTemplate jdbcTemplate() {
	return new JdbcTemplate(this.dataSource());
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
	final LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
	entityManagerFactoryBean.setDataSource(this.dataSource());
	entityManagerFactoryBean
		.setPersistenceProviderClass(HibernatePersistenceProvider.class);
	entityManagerFactoryBean
		.setPackagesToScan("com.fdc.merchantapi.dao.entity");
	entityManagerFactoryBean.setJpaProperties(this.hibProperties());
	return entityManagerFactoryBean;
    }

    private Properties hibProperties() {
	final Properties properties = new Properties();
	properties.put(PROPERTY_NAME_HIBERNATE_DIALECT, "org.hibernate.dialect.OracleDialect");
	properties.put(PROPERTY_NAME_HIBERNATE_SHOW_SQL, "true");
	return properties;
    }

    @Bean
    public JpaTransactionManager transactionManager() {
	final JpaTransactionManager transactionManager = new JpaTransactionManager();
	transactionManager.setTransactionSynchronization(JpaTransactionManager.SYNCHRONIZATION_NEVER);
	transactionManager.setEntityManagerFactory(this.entityManagerFactory()
		.getObject());
	return transactionManager;
    }

}