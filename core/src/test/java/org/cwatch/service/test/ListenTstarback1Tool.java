package org.cwatch.service.test;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.camel.component.ActiveMQConfiguration;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.gson.GsonDataFormat;
import org.apache.camel.model.ContextScanDefinition;
import org.apache.camel.spring.CamelContextFactoryBean;
import org.apache.camel.spring.SpringRouteBuilder;
import org.cwatch.service.routes.AisToCdfRouteBuilder;
import org.cwatch.split.CwatchSplitProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties(CwatchSplitProperties.class)
@Import(AisToCdfRouteBuilder.class)
public class ListenTstarback1Tool implements CommandLineRunner {

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
	SpringRouteBuilder routeBuilder() {
		return new SpringRouteBuilder() {
			@Override
			public void configure() throws Exception {

				new GsonDataFormat();
				
				from("amqremote:topic:"+cwatchSplitProperties.getBatchTopicName()+"?clientId=cwatchListenServiceBatch")
				.id("batchReceiver")
//				.to("direct:ais2cdf")
				.to("direct:logmsg")
				;
				
				from("direct:logmsg")
				.to("file:./zip")
				.unmarshal().gzip()
				.convertBodyTo(String.class)
				.to("log:batch?showBody=true")
				.process(new Processor() {
					@Override
					public void process(Exchange exchange) throws Exception {
						Gson gson = new GsonBuilder().setPrettyPrinting().create();
						JsonParser jp = new JsonParser();
						JsonElement je = jp.parse(exchange.getIn().getBody(String.class));
						exchange.getIn().setBody(gson.toJson(je));					}
				})
				.to("amqlocal:topic:vdm.batch.unzip")
				.to("file:.")
				;
			}
		};
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
	
	@Override
	public void run(String... args) throws Exception {
	}

	public static void main(String[] args) {
		SpringApplication.run(ListenTstarback1Tool.class, args);
	}
	
}
