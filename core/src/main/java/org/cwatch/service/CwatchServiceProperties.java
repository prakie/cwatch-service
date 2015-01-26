package org.cwatch.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cwatch-service")
public class CwatchServiceProperties {
	
	private String batchTopicName;
	
	private String cdfPositionTopicName = "cdf.pos";
	
	private String cdfVoyageTopicName = "cdf.voyage";
	
	private String cdfErrorQueueName = "cdf.error";
	
	private String cdfInvalidQueueName = "cdf.invalid";
	
	public String getBatchTopicName() {
		return batchTopicName;
	}

	public void setBatchTopicName(String batchTopicName) {
		this.batchTopicName = batchTopicName;
	}

	public String getCdfPositionTopicName() {
		return cdfPositionTopicName;
	}

	public void setCdfPositionTopicName(String cdfPositionTopicName) {
		this.cdfPositionTopicName = cdfPositionTopicName;
	}

	public String getCdfVoyageTopicName() {
		return cdfVoyageTopicName;
	}

	public void setCdfVoyageTopicName(String cdfVoyageTopicName) {
		this.cdfVoyageTopicName = cdfVoyageTopicName;
	}

	public String getCdfErrorQueueName() {
		return cdfErrorQueueName;
	}

	public void setCdfErrorQueueName(String cdfErrorQueueName) {
		this.cdfErrorQueueName = cdfErrorQueueName;
	}

	public String getCdfInvalidQueueName() {
		return cdfInvalidQueueName;
	}

	public void setCdfInvalidQueueName(String cdfInvalidQueueName) {
		this.cdfInvalidQueueName = cdfInvalidQueueName;
	}

	
	
	
}
