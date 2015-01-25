package org.cwatch.service.test;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.dataset.DataSet;
import org.apache.camel.component.dataset.DataSetSupport;
import org.apache.camel.model.ContextScanDefinition;
import org.apache.camel.spring.CamelContextFactoryBean;
import org.cwatch.service.routes.AisToCdfRouteBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class Ais2CdfTest {

	@Autowired
	CamelContext camelContext;
	
	@Test
	public void test() {
		Assert.assertTrue(new NotifyBuilder(camelContext).whenDone(5).create().matchesMockWaitTime());
	}

	@Configuration
	@Import(AisToCdfRouteBuilder.class)
	public static class ContextConfig  {
		@Bean
		CamelContextFactoryBean camelContext() {
			CamelContextFactoryBean factory = new CamelContextFactoryBean();
			factory.setId("testContext");
			factory.setContextScan(new ContextScanDefinition());
			return factory;
		}
		
		@Bean
		DataSet aisData() {
			return new DataSetSupport(5) {
				@Override
				protected Object createMessageBody(long messageIndex) {
					return Ais2CdfTest.class.getResource(String.format("/ais%02d.json.gz", messageIndex+1));
				}
			};
			
		}
		
		@Bean
		public RouteBuilder route() {
			return new RouteBuilder() {
				public void configure() {
					from("dataset:aisData")
					.to("direct:ais2cdf")
					;

					from("direct:ais2cdfPositionOut")
					.to("log:pos");
					
					from("direct:ais2cdfVoyageOut")
					.to("log:voyage")
					;
					
					from("direct:ais2cdfErrorOut")
					.to("log:error")
					;
					
					from("direct:ais2cdfInvalidOut")
					.to("log:invalid")
					;
					
				}
			};
		}
	}
}
