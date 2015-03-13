package org.cwatch.service;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources(@PropertySource("classpath:/cwatch-service.properties"))
@EnableConfigurationProperties(CwatchServiceProperties.class)
public class CwatchServicePropertiesConfiguration {

}
