package org.cwatch.service;

import org.cwatch.env.InitialContextProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cwatch-service")
public class CwatchServiceProperties {
	
	private String vdmBatchTopicName;
	
	private String cdfPositionTopicName = "cdf.pos";
	
	private String cdfVoyageTopicName = "cdf.voyage";
	
	private String cdfErrorQueueName = "cdf.error";
	
	private String cdfInvalidQueueName = "cdf.invalid";
	
	private InitialContextProperties cdfInitialContext = new InitialContextProperties();
	
	private String cdfWeblogicConnectionFactory = "weblogic.jms.ConnectionFactory";
	
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

	public String getCdfWeblogicConnectionFactory() {
		return cdfWeblogicConnectionFactory;
	}

	public void setCdfWeblogicConnectionFactory(String cdfWeblogicConnectionFactory) {
		this.cdfWeblogicConnectionFactory = cdfWeblogicConnectionFactory;
	}

	public InitialContextProperties getCdfInitialContext() {
		return cdfInitialContext;
	}

	public void setCdfInitialContext(InitialContextProperties cdfInitialContext) {
		this.cdfInitialContext = cdfInitialContext;
	}

		
}
