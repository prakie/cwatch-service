package org.cwatch.service.test;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.camel.component.ActiveMQConfiguration;
import org.apache.camel.model.ContextScanDefinition;
import org.apache.camel.spring.CamelContextFactoryBean;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
public class ListenTstarback1Configuration {

	
	@Bean
	CamelContextFactoryBean camelContext() {
		CamelContextFactoryBean factory = new CamelContextFactoryBean();
		factory.setId("cwatchServiceListenContext");
		factory.setContextScan(new ContextScanDefinition());
		return factory;
	}
	
	@Bean
	ActiveMQComponent amqremote() {
		ActiveMQConfiguration cfg = new ActiveMQConfiguration();
		cfg.setBrokerURL("tcp://tstarback1:62626");
		cfg.setErrorHandlerLogStackTrace(false);
		
		ActiveMQComponent cmp = new ActiveMQComponent();
		cmp.setConfiguration(cfg);
		return cmp;
	}
	
	@Bean
	ActiveMQComponent amqlocal() {
		ActiveMQConfiguration cfg = new ActiveMQConfiguration();
		cfg.setBrokerURL("vm:(broker:(tcp://0.0.0.0:61616)?brokerName=cwatchListenBroker&persistent=false&useShutdownHook=false)");
		cfg.setErrorHandlerLogStackTrace(false);
		
		ActiveMQComponent cmp = new ActiveMQComponent();
		cmp.setConfiguration(cfg);
		return cmp;
	}
	
}
