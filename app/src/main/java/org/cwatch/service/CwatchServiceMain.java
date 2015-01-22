package org.cwatch.service;

import javax.jms.ConnectionFactory;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.camel.component.ActiveMQConfiguration;
import org.apache.camel.model.ContextScanDefinition;
import org.apache.camel.spring.CamelContextFactoryBean;
import org.cwatch.split.CwatchSplitConfiguration;
import org.cwatch.split.routes.CwatchSplitRouteBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({CwatchSplitConfiguration.class, CwatchSplitRouteBuilder.class})
public class CwatchServiceMain implements CommandLineRunner {

	@Bean
	CamelContextFactoryBean camelContext() {
		CamelContextFactoryBean factory = new CamelContextFactoryBean();
		factory.setId("cwatchService");
		factory.setContextScan(new ContextScanDefinition());
		return factory;
	}
	
	@Bean
	ActiveMQComponent activemq(ConnectionFactory connectionFactory) {
		ActiveMQConfiguration cfg = new ActiveMQConfiguration();
		cfg.setConnectionFactory(connectionFactory);
		cfg.setErrorHandlerLogStackTrace(false);
		cfg.setUsePooledConnection(false);
		
		ActiveMQComponent cmp = new ActiveMQComponent();
		cmp.setConfiguration(cfg);
		return cmp;
	}
	
	@Override
	public void run(String... args) throws Exception {
	}

	public static void main(String[] args) {
		SpringApplication.run(CwatchServiceMain.class, args);
	}

}
