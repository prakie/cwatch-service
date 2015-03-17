package org.cwatch.service;

import org.cwatch.service.routes.AisToCdfRouteBuilder;
import org.cwatch.service.routes.CwatchRouteBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
	CwatchRouteBuilder.class, 
	AisToCdfRouteBuilder.class, 
	CwatchServicePropertiesConfiguration.class
})
public class CwatchServiceConfiguration {

}
