package org.cwatch.service;

import org.cwatch.service.routes.AisToCdfRouteBuilder;
import org.cwatch.service.routes.CwatchRouteBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources(@PropertySource("classpath:/cwatch-service.properties"))
@Import({CwatchRouteBuilder.class, AisToCdfRouteBuilder.class})
public class CwatchServiceConfiguration {

}
