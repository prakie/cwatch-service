package org.cwatch.service.routes;

import org.apache.camel.spring.SpringRouteBuilder;
import org.cwatch.service.CwatchServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties({CwatchServiceProperties.class})
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
		
		from("direct:ais2cdfInvalidOut")
		.id("cdfInvalidOut")
		.to("log:ais2cdf.invalid?level=DEBUG&showBody=false&showCaughtException=true&showStackTrace=false")
		.setBody(property(AisToCdfRouteBuilder.AIS_MESSAGE))
		.marshal("aisMessageGsonDataFormat")
		.setHeader("ais2cdfInvalidReason", exceptionMessage())
		.to("activemq:topic:"+configuration.getCdfInvalidTopicName() + "?jmsMessageType=Text");
		
		from("direct:ais2cdfErrorOut")
		.id("cdfErrorOut")
		.setBody(property(AisToCdfRouteBuilder.AIS_MESSAGE))
		.marshal("aisMessageGsonDataFormat")
		.to("log:ais2cdf.error?level=WARN&showAll=true&multiline=true&skipBodyLineSeparator=false")
		.to("activemq:topic:"+configuration.getCdfErrorTopicName() + "?jmsMessageType=Text");
		
	}
	
}