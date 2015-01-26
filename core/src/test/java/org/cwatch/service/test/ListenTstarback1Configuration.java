package org.cwatch.service.test;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.camel.component.ActiveMQConfiguration;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.model.ContextScanDefinition;
import org.apache.camel.spring.CamelContextFactoryBean;
import org.apache.camel.spring.SpringRouteBuilder;
import org.cwatch.split.CwatchSplitProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties(CwatchSplitProperties.class)
public class ListenTstarback1Configuration {

	@Autowired
	CwatchSplitProperties cwatchSplitProperties;
	
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
