package org.cwatch.service.routes;

import org.apache.camel.spring.SpringRouteBuilder;
import org.cwatch.service.CwatchServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties({CwatchServiceProperties.class})
@Import(AisToCdfErrorRouteBuilder.class)
public class CwatchRouteBuilder extends SpringRouteBuilder {

	@Autowired
	CwatchServiceProperties configuration;
	
	@Override
	public void configure() throws Exception {
		errorHandler(
				defaultErrorHandler()
				.logExhaustedMessageHistory(false)
		);
		
		from("activemq:topic:"+configuration.getVdmBatchTopicName()+"?clientId=cwatchCdfBatch")
		.id("cdfBatchReceiver")
		.to("direct:ais2cdf");

		from("direct:ais2cdfPositionOut")
		.id("cdfPositionOut")
		.to("activemq:topic:"+configuration.getCdfPositionTopicName() + "?jmsMessageType=Text");
		
		from("direct:ais2cdfVoyageOut")
		.id("cdfVoyageOut")
		.to("activemq:topic:"+configuration.getCdfVoyageTopicName() + "?jmsMessageType=Text");
		
		from("direct:ais2cdfInvalidLetter")
		.to("activemq:topic:"+configuration.getCdfInvalidTopicName() + "?jmsMessageType=Text");
		
		from("direct:ais2cdfErrorLetter")
		.to("activemq:topic:"+configuration.getCdfErrorTopicName() + "?jmsMessageType=Text");
		
	}
	
}