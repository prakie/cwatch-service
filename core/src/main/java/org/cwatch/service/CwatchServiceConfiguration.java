package org.cwatch.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources(@PropertySource("classpath:/cwatch-service.properties"))
public class CwatchServiceConfiguration {

}
