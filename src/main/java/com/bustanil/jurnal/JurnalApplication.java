package com.bustanil.jurnal;

import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@SpringBootApplication
@EnableTransactionManagement
public class JurnalApplication {

//	@Bean
//	DataSourceConnectionProvider connectionProvider(DataSource dataSource) {
//		return new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource));
//	}
//
//	@Bean
//	public DefaultDSLContext dsl(DataSource dataSource) {
//		return new DefaultDSLContext(configuration(dataSource));
//	}
//
//	@Bean
//	public DefaultConfiguration configuration(DataSource dataSource) {
//		DefaultConfiguration jooqConfiguration = new DefaultConfiguration();
//		jooqConfiguration.set(connectionProvider(dataSource));
//		jooqConfiguration.setSQLDialect(SQLDialect.POSTGRES);
////		jooqConfiguration.set(new DefaultExecuteListenerProvider(exceptionTranslator));
//
//		return jooqConfiguration;
//	}

	public static void main(String[] args) {
		SpringApplication.run(JurnalApplication.class, args);
	}
}
