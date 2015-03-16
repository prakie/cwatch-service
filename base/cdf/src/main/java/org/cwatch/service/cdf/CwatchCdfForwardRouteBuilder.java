package org.cwatch.service.cdf;

import org.apache.camel.spring.SpringRouteBuilder;
import org.cwatch.service.CwatchServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties(CwatchServiceProperties.class)
@Import(CdfForwardRouteBuilder.class)
public class CwatchCdfForwardRouteBuilder extends SpringRouteBuilder {

	@Autowired
	CwatchServiceProperties configuration;
	
	@Override
	public void configure() throws Exception {
		from("activemq:topic:"+configuration.getCdfPositionTopicName()+"?clientId=cwatchCdfPositionForward")
		.id("cdfPositionForward")
		.to("direct:cdfPositionForward");
		
		from("activemq:topic:"+configuration.getCdfVoyageTopicName()+"?clientId=cwatchCdfVoyageForward")
		.id("cdfVoyageForward")
		.to("direct:cdfVoyageForward");
	}

}
