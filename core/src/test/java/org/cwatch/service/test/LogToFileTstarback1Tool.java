package org.cwatch.service.test;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
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
@Import(ListenTstarback1Configuration.class)
public class LogToFileTstarback1Tool implements CommandLineRunner {

	@Autowired
	CwatchSplitProperties cwatchSplitProperties;
	
	@Bean
	SpringRouteBuilder lotRouteBuilder() {
		return new SpringRouteBuilder() {
			@Override
			public void configure() throws Exception {

				from("amqremote:topic:"+cwatchSplitProperties.getBatchTopicName()+"?clientId=cwatchListenServiceBatch")
				.id("batchReceiver")
				.to("direct:logmsg")
				;
				
				
				from("direct:logmsg")
				.to("file:./zip")
				.unmarshal().gzip()
				.convertBodyTo(String.class)
				.to("log:batch?showBody=true")
				.process(JSON_PRETTYPRINTER)
				.to("amqlocal:topic:vdm.batch.unzip")
				.to("file:.")
				;
				
				
			}
		};
	}
	
	
	@Override
	public void run(String... args) throws Exception {
	}

	public static void main(String[] args) {
		SpringApplication.run(LogToFileTstarback1Tool.class, args);
	}
	
	public static final Processor JSON_PRETTYPRINTER = new Processor() {
		private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
		private final JsonParser jp = new JsonParser();
		@Override
		public void process(Exchange exchange) throws Exception {
			JsonElement je = jp.parse(exchange.getIn().getBody(String.class));
			exchange.getIn().setBody(gson.toJson(je));					}
	};
	
}
