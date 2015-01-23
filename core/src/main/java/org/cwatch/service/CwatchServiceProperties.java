package org.cwatch.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cwatch-service")
public class CwatchServiceProperties {
	
	private String batchTopicName;

	
	public String getBatchTopicName() {
		return batchTopicName;
	}

	public void setBatchTopicName(String batchTopicName) {
		this.batchTopicName = batchTopicName;
	}

	
	
	
}
