package org.cwatch.service.routes;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.camel.component.ActiveMQConfiguration;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
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
	
	
	@Bean
	ActiveMQComponent activemq(@Value("${cwatch-service.split.brokerUrl:tcp://tstarback1.emsa.local:61616}") String brokerUrl) {
		ActiveMQConfiguration cfg = new ActiveMQConfiguration();
		cfg.setBrokerURL(brokerUrl);
		
		ActiveMQComponent cmp = new ActiveMQComponent();
		cmp.setConfiguration(cfg);
		return cmp;
	}
	
}