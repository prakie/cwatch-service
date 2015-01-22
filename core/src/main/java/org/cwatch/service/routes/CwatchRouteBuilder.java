package org.cwatch.service.routes;

import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class CwatchRouteBuilder extends SpringRouteBuilder {
	
	@Override
	public void configure() throws Exception {
		errorHandler(
				defaultErrorHandler()
				.logStackTrace(false)
				.logRetryStackTrace(false)
				.logExhaustedMessageHistory(false)
		);
		
		from("activemq:topic:stires.vdm.batch?clientId=cwatchServiceBatch")
		.id("batchReceiver")
		.to("log:batch?showBody=false");
	}
	
}