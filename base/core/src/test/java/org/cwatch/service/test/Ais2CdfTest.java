package org.cwatch.service.test;

import java.net.URL;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.dataset.DataSet;
import org.apache.camel.component.dataset.DataSetSupport;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultCamelBeanPostProcessor;
import org.apache.camel.model.ContextScanDefinition;
import org.apache.camel.spring.CamelContextFactoryBean;
import org.cwatch.service.routes.AisToCdfRouteBuilder;
import org.junit.Assert;
import org.junit.Before;
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
	
	@Produce(uri="direct:ais2cdf")
	ProducerTemplate ais2cdf; 
	
	@EndpointInject(uri = "mock:ais2cdfErrorOut")
	protected MockEndpoint ais2cdfErrorOut;
	
	@Test
	public void testZippedValidSet() throws Exception {
		NotifyBuilder notify = new NotifyBuilder(camelContext).whenCompleted(1).and().not().whenFailed(1).create();
		ais2cdfErrorOut.expectedMessageCount(0);
		
		camelContext.addRoutes(new RouteBuilder() {
			public void configure() {
				from("dataset:aisData?preloadSize=5")
				.id("testData")
				.to("direct:ais2cdf")
				;
			}
		});
		
		Assert.assertTrue(notify.matches());
		ais2cdfErrorOut.assertIsSatisfied();
	}

	@Before
	public void setup() throws Exception {
		new DefaultCamelBeanPostProcessor(camelContext).postProcessBeforeInitialization(this, null);
	}
	
	private static URL getResource(String res) {
		return Ais2CdfTest.class.getResource(res);
	}
	
	@Test
	public void testInvalidContainer() throws Exception {
		ais2cdfErrorOut.reset();
		ais2cdfErrorOut.expectedMessageCount(1);
		ais2cdf.sendBody(getResource("/vdmHttp-invalidContainer.json"));
		ais2cdfErrorOut.assertIsSatisfied();
	}

	@Test
	public void testValid() throws Exception {
		ais2cdfErrorOut.expectedMessageCount(0);
		ais2cdf.sendBody(getResource("/vdmHttp-valid.json"));
		ais2cdfErrorOut.assertIsSatisfied();
	}
	
	@Test
	public void testNoMonitoring() throws Exception {
		ais2cdfErrorOut.expectedMessageCount(0);
		ais2cdf.sendBody(getResource("/vdmHttp-withoutMonitoringMessage.json"));
		ais2cdfErrorOut.assertIsSatisfied();
	}
	
	@Test
	public void testOnlyMessages() throws Exception {
		ais2cdfErrorOut.expectedMessageCount(0);
		ais2cdf.sendBody(getResource("/vdmHttp-onlyMessages.json"));
		ais2cdfErrorOut.assertIsSatisfied();
	}
	
	@Test
	public void testCase01() throws Exception {
		ais2cdfErrorOut.expectedMessageCount(0);
		ais2cdf.sendBody(getResource("/vdmHttp-case01.json"));
		ais2cdfErrorOut.assertIsSatisfied();
	}

	@Test
	/**
	 * source value in json is invalid
	 * @throws Exception
	 */
	public void testCase02() throws Exception {
		ais2cdfErrorOut.expectedMessageCount(1);
		ais2cdf.sendBody(getResource("/vdmHttp-case02.json"));
		ais2cdfErrorOut.assertIsSatisfied();
	}
	
	@Test
	/**
	 * source value in json is invalid
	 * @throws Exception
	 */
	public void testCase03() throws Exception {
		ais2cdfErrorOut.expectedMessageCount(1);
		ais2cdf.sendBody(getResource("/vdmHttp-case03.json"));
		ais2cdfErrorOut.assertIsSatisfied();
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
		DefaultCamelBeanPostProcessor camelProc() {
			return new DefaultCamelBeanPostProcessor();
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
		public RouteBuilder logRouteBuilder() {
			return new RouteBuilder() {
				public void configure() {
					from("direct:ais2cdfPositionOut")
					.to("log:pos?showBody=false");
					
					from("direct:ais2cdfVoyageOut")
					.to("log:voyage?showBody=false")
					;
					
					from("direct:ais2cdfErrorOut")
					.to("log:error?showAll=true")
					.to("mock:ais2cdfErrorOut")
					;
					
					from("direct:ais2cdfInvalidOut")
					.to("log:invalid?showAll=true")
					;
					
				}
			};
		}
	}
}
