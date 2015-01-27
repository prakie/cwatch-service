package org.cwatch.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cwatch-service")
public class CwatchServiceProperties {
	
	private String cdfPositionTopicName = "cdf.pos";
	
	private String cdfVoyageTopicName = "cdf.voyage";
	
	private String cdfErrorQueueName = "cdf.error";
	
	private String cdfInvalidQueueName = "cdf.invalid";
	
	private String imdateNamingProviderUrl = "t3://twls55:7030";
	
	private String imdateUsername = "imdate";
	
	private String imdatePassword = "imdate123";
	
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

	public String getCdfErrorTopicName() {
		return cdfErrorQueueName;
	}

	public void setCdfErrorQueueName(String cdfErrorQueueName) {
		this.cdfErrorQueueName = cdfErrorQueueName;
	}

	public String getCdfInvalidTopicName() {
		return cdfInvalidQueueName;
	}

	public void setCdfInvalidQueueName(String cdfInvalidQueueName) {
		this.cdfInvalidQueueName = cdfInvalidQueueName;
	}

	public String getImdateNamingProviderUrl() {
		return imdateNamingProviderUrl;
	}

	public void setImdateNamingProviderUrl(String imdateNamingProviderUrl) {
		this.imdateNamingProviderUrl = imdateNamingProviderUrl;
	}

	public String getImdateUsername() {
		return imdateUsername;
	}

	public void setImdateUsername(String imdateUsername) {
		this.imdateUsername = imdateUsername;
	}

	public String getImdatePassword() {
		return imdatePassword;
	}

	public void setImdatePassword(String imdatePassword) {
		this.imdatePassword = imdatePassword;
	}

	
	
	
}
