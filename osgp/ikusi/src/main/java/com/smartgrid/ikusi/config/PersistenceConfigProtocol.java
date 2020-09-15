package com.smartgrid.ikusi.config;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "protocolEntityManagerFactory", basePackages = {
		"com.smartgrid.ikusi.protocol.repositories" })
public class PersistenceConfigProtocol {

	@Bean(name = "protocolDataSource")
	@ConfigurationProperties(prefix = "spring.protocoldatasource")
	public DataSource primaryDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "protocolEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier("protocolDataSource") DataSource dataSource) {
		
		Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.implicit_naming_strategy", "org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy");
        properties.put("hibernate.physical_naming_strategy", "org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy");
        properties.put("hibernate.show_sql", "false");
        
		return builder
				.dataSource(dataSource)
				.packages("com.smartgrid.ikusi.protocol.entities")
				.persistenceUnit("IKUSI_ORM_PROTOCOL")
				.properties(properties)
				.build();
	}

	@Bean(name = "protocolTransactionManager")
	public PlatformTransactionManager transactionManager(
			@Qualifier("protocolEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}
}
