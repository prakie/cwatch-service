package org.cwatch.service.routes;

import org.apache.camel.spring.SpringRouteBuilder;
import org.cwatch.service.CwatchServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties(CwatchServiceProperties.class)
public class CwatchRouteBuilder extends SpringRouteBuilder {

	@Autowired
	CwatchServiceProperties configuration;
	
	@Override
	public void configure() throws Exception {
		errorHandler(
				defaultErrorHandler()
				.logStackTrace(false)
				.logRetryStackTrace(false)
				.logExhaustedMessageHistory(false)
		);
		
		from("activemq:topic:"+configuration.getBatchTopicName()+"?clientId=cwatchServiceBatch")
		.id("batchReceiver")
		.to("log:batch?showBody=false");
	}
	
}