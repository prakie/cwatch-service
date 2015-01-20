package org.cwatch.service.mock;

import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.apache.camel.model.ContextScanDefinition;
import org.apache.camel.spring.CamelContextFactoryBean;
import org.apache.camel.spring.SpringRouteBuilder;
import org.cwatch.vdm.StiresSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class CwatchMockRouteBuilder extends SpringRouteBuilder {
	
	@Value("${cwatch-service.stires-vdm-core.batch-service.path:stiresVdmBatchService}")
	String batchServicePath;
	
	@Value("${cwatch-service.stires-vdm-core.monitoring-service.path:stiresMonitoringService}")
	String monitoringServicePath;
	
	@Override
	public void configure() throws Exception {
		from("servlet:///" + batchServicePath + "?servletName=stiresVdmCoreServlet")
		.id("batchServlet")
		.stop();
		
		from("servlet:///" + monitoringServicePath+ "?servletName=stiresVdmCoreServlet")
		.id("monitoringServlet")
		.stop();
		
		VdmSimulatorEndpoint vdmSimulatorEndpoint = new VdmSimulatorEndpoint();
		vdmSimulatorEndpoint.setCamelContext(getContext());
		
		
		from(vdmSimulatorEndpoint)
		.id("batchSender")
		.to("bean:stiresSender?method=sendMessages(${in.header.SSNPRoxy}, ${body})");
		
//		from("activemq:topic:stires.vdm.monitoring")
//		.id("monitoringSender")
//		.to("bean:stiresSender?method=sendMonitoring(${in.header.SSNPRoxy}, ${body})");
	}
	
	@Bean(name="stiresVdmCoreServlet")
	ServletRegistrationBean stiresVdmCoreCamelServlet(@Value("${cwatch-service.stires-vdm-core.path:/stires-vdm-core/stiresServices}/*") String stiresVdmCorePath) {
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new CamelHttpTransportServlet(), stiresVdmCorePath);
		servletRegistrationBean.setName("stiresVdmCoreServlet");
		return servletRegistrationBean;
	}
	
	@Bean
	CamelContextFactoryBean camelContext() {
		CamelContextFactoryBean factory = new CamelContextFactoryBean();
		factory.setId("cwatchMock");
		factory.setContextScan(new ContextScanDefinition());
		return factory;
	}
	
	@Bean
	StiresSender stiresSender(@Value("${stires.baseUrl:http://localhost:8080}") String stiresBaseUrl) {
		StiresSender stiresSender = new StiresSender();
		stiresSender.setStiresBaseUrl(stiresBaseUrl);
		return stiresSender;
	}
	
	
}