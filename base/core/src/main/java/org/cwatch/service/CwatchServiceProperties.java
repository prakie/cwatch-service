package org.cwatch.service;

import java.util.concurrent.TimeUnit;

import org.cwatch.env.ConnectionFactoryProperties;
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
	
	private ConnectionFactoryProperties cdfWeblogicConnectionFactory = new ConnectionFactoryProperties("weblogic.jms.ConnectionFactory", null);
	
	private String cdfWeblogicPositionQueue;
	
	private String cdfWeblogicVoyageQueue;
	
	private long timestampFutureThreshold = 1;
	
	private TimeUnit timestampFutureThresholdUnit = TimeUnit.HOURS;
	
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

	public InitialContextProperties getCdfInitialContext() {
		return cdfInitialContext;
	}

	public void setCdfInitialContext(InitialContextProperties cdfInitialContext) {
		this.cdfInitialContext = cdfInitialContext;
	}

	public ConnectionFactoryProperties getCdfWeblogicConnectionFactory() {
		return cdfWeblogicConnectionFactory;
	}

	public void setCdfWeblogicConnectionFactory(
			ConnectionFactoryProperties cdfWeblogicConnectionFactory) {
		this.cdfWeblogicConnectionFactory = cdfWeblogicConnectionFactory;
	}

	public long getTimestampFutureThreshold() {
		return timestampFutureThreshold;
	}

	public void setTimestampFutureThreshold(long timestampFutureThreshold) {
		this.timestampFutureThreshold = timestampFutureThreshold;
	}

	public TimeUnit getTimestampFutureThresholdUnit() {
		return timestampFutureThresholdUnit;
	}

	public void setTimestampFutureThresholdUnit(
			TimeUnit timestampFutureThresholdUnit) {
		this.timestampFutureThresholdUnit = timestampFutureThresholdUnit;
	}

		
}
