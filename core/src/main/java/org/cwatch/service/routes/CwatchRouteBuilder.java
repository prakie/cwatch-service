package org.cwatch.service.routes;

import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.camel.component.ActiveMQConfiguration;
import org.apache.camel.ExchangePattern;
import org.apache.camel.spring.SpringRouteBuilder;
import org.cwatch.vdm.StiresSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Component
public class CwatchRouteBuilder extends SpringRouteBuilder {
	
	@Value("${cwatch-service.stires-vdm-core.batch-service.path:stiresVdmBatchService}")
	String batchServicePath;
	
	@Value("${cwatch-service.stires-vdm-core.monitoring-service.path:stiresMonitoringService}")
	String monitoringServicePath;
	
	@Override
	public void configure() throws Exception {
		errorHandler(
				defaultErrorHandler()
				.logStackTrace(false)
				.logRetryStackTrace(false)
				.logExhaustedMessageHistory(false)
		);
		
		from("servlet:///" + batchServicePath + "?servletName=stiresVdmCoreServlet")
		.id("batchServlet")
		.startupOrder(3)
		.to(ExchangePattern.InOnly, "activemq:topic:stires.vdm.batch");
		
		from("servlet:///" + monitoringServicePath+ "?servletName=stiresVdmCoreServlet")
		.id("monitoringServlet")
		.startupOrder(4)
		.to(ExchangePattern.InOnly, "activemq:topic:stires.vdm.monitoring");
		
		from("activemq:topic:stires.vdm.batch?clientId=cwatchServiceStiresSenderBatch&asyncConsumer=true")
		.id("batchSender")
		.startupOrder(1)
		.threads()
		.to("bean:stiresSender?method=sendMessages(${in.header.SSNPRoxy}, ${body})");
		
		from("activemq:topic:stires.vdm.monitoring?clientId=cwatchServiceStiresSenderMonitoring&asyncConsumer=true")
		.id("monitoringSender")
		.startupOrder(2)
		.threads()
		.to("bean:stiresSender?method=sendMonitoring(${in.header.SSNPRoxy}, ${body})");
	}
	
	@Bean
	BrokerService cwatchServiceBroker() throws Exception {
		return BrokerFactory.createBroker("broker:(tcp://0.0.0.0:61616)?brokerName=cwatchServiceBroker&persistent=false&useShutdownHook=false");
	}
	
	@Bean
	@DependsOn("cwatchServiceBroker")
	ActiveMQComponent activemq() {
		ActiveMQConfiguration cfg = new ActiveMQConfiguration();
		cfg.setBrokerURL("vm://cwatchServiceBroker?create=false");
		
		ActiveMQComponent cmp = new ActiveMQComponent();
		cmp.setConfiguration(cfg);
		return cmp;
	}
	
	@Bean
	StiresSender stiresSender(@Value("${stires.baseUrl:http://localhost:8081}") String stiresBaseUrl) {
		StiresSender stiresSender = new StiresSender();
		stiresSender.setStiresBaseUrl(stiresBaseUrl);
		return stiresSender;
	}
}