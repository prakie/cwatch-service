package org.cwatch.service.testing;

import org.apache.camel.Exchange;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.ContextScanDefinition;
import org.apache.camel.spring.CamelContextFactoryBean;
import org.cwatch.boot.camel.MessageIdSuffixExpression;
import org.cwatch.service.routes.AisToCdfRouteBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(AisToCdfRouteBuilder.class)
public class VdmHttp2CdfFile implements CommandLineRunner {

	private static NotifyBuilder finished;

	@Bean
	CamelContextFactoryBean camelContext() {
		CamelContextFactoryBean factory = new CamelContextFactoryBean();
		factory.setId(VdmHttp2CdfFile.class.getSimpleName());
		factory.setContextScan(new ContextScanDefinition());
		return factory;
	}
	
	@Bean
	public RouteBuilder route(final VdmHttpTestDataSet vdmHttpTestDataSet) {
		return new RouteBuilder() {
			public void configure() {
				from("dataset:vdmHttpTestDataSet")
				.id("testData")
				.to("direct:ais2cdf")
				;

				from("direct:ais2cdfPositionOut")
				.to("log:pos?showBody=false&showExchangeId=true")
				.setHeader(Exchange.FILE_NAME, MessageIdSuffixExpression.of(".xml"))
				.to("file:target/cdfPos");
				
				from("direct:ais2cdfVoyageOut")
				.to("log:voyage?showBody=false&showExchangeId=true")
				.setHeader(Exchange.FILE_NAME, MessageIdSuffixExpression.of(".xml"))
				.to("file:target/cdfVoyage");
				
				from("direct:ais2cdfErrorOut")
				.to("log:error?showAll=true")
				;
				
				from("direct:ais2cdfInvalidOut")
				.to("log:invalid?showBody=false")
				;
				
				
				finished = new NotifyBuilder(getContext()).fromRoute("testData").whenExactlyDone((int) vdmHttpTestDataSet.getSize()).create();
			}
		};
	}
	
	@Override
	public void run(String... args) throws Exception {
	}

	public static void main(String[] args) {
		SpringApplication sa = new SpringApplication(VdmHttp2CdfFile.class);
		sa.setWebEnvironment(false);
		ConfigurableApplicationContext app = sa.run(args);
		finished.matchesMockWaitTime();
	}
	
}
