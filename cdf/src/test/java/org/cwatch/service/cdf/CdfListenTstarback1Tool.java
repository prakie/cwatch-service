package org.cwatch.service.cdf;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.camel.component.ActiveMQConfiguration;
import org.apache.camel.model.ContextScanDefinition;
import org.apache.camel.spring.CamelContextFactoryBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(CdfForwardRouteBuilder.class)
public class CdfListenTstarback1Tool implements CommandLineRunner {

	@Bean
	CamelContextFactoryBean camelContext() {
		CamelContextFactoryBean factory = new CamelContextFactoryBean();
		factory.setId("cwatchCdfListenContext");
		factory.setContextScan(new ContextScanDefinition());
		return factory;
	}
	
	@Bean
	ActiveMQComponent activemq() {
		ActiveMQConfiguration cfg = new ActiveMQConfiguration();
		cfg.setBrokerURL("tcp://tstarback1:62626");
		cfg.setErrorHandlerLogStackTrace(false);
		
		ActiveMQComponent cmp = new ActiveMQComponent();
		cmp.setConfiguration(cfg);
		return cmp;
	}

	@Override
	public void run(String... args) throws Exception {
	}

	public static void main(String[] args) {
		SpringApplication.run(CdfListenTstarback1Tool.class, args);
	}
	
}
