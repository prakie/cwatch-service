package org.cwatch.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cwatch-service")
public class CwatchServiceProperties {
	
	private String vdmBatchTopicName;
	
	private String cdfPositionTopicName = "cdf.pos";
	
	private String cdfVoyageTopicName = "cdf.voyage";
	
	private String cdfErrorQueueName = "cdf.error";
	
	private String cdfInvalidQueueName = "cdf.invalid";
	
	private String cdfWeblogicNamingProviderUrl;
	
	private String cdfWeblogicUsername;
	
	private String cdfWeblogicPassword;
	
	private String cdfWeblogicPositionQueue;
	
	private String cdfWeblogicVoyageQueue;
	
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

	public String getCdfWeblogicNamingProviderUrl() {
		return cdfWeblogicNamingProviderUrl;
	}

	public void setCdfWeblogicNamingProviderUrl(String cdfWeblogicNamingProviderUrl) {
		this.cdfWeblogicNamingProviderUrl = cdfWeblogicNamingProviderUrl;
	}

	public String getCdfWeblogicUsername() {
		return cdfWeblogicUsername;
	}

	public void setCdfWeblogicUsername(String cdfWeblogicUsername) {
		this.cdfWeblogicUsername = cdfWeblogicUsername;
	}

	public String getCdfWeblogicPassword() {
		return cdfWeblogicPassword;
	}

	public void setCdfWeblogicPassword(String cdfWeblogicPassword) {
		this.cdfWeblogicPassword = cdfWeblogicPassword;
	}

	public String getCdfWeblogicPositionQueue() {
		return cdfWeblogicPositionQueue;
	}

	public void setCdfWeblogicPositionQueue(String cdfWeblogicPositionQueue) {
		this.cdfWeblogicPositionQueue = cdfWeblogicPositionQueue;
	}

	public String getCdfWeblogicVoyageQueue() {
		return cdfWeblogicVoyageQueue;
	}

	public void setCdfWeblogicVoyageQueue(String cdfWeblogicVoyageQueue) {
		this.cdfWeblogicVoyageQueue = cdfWeblogicVoyageQueue;
	}

	public String getVdmBatchTopicName() {
		return vdmBatchTopicName;
	}

	public void setVdmBatchTopicName(String vdmBatchTopicName) {
		this.vdmBatchTopicName = vdmBatchTopicName;
	}

		
}