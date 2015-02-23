package org.cwatch.service.test;

import org.cwatch.service.routes.AisToCdfRouteBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
//@EnableConfigurationProperties(CwatchSplitProperties.class)
@Import(AisToCdfRouteBuilder.class)
public class ConvertAis2CdfTool implements CommandLineRunner {

//	@Autowired
//	CwatchSplitProperties cwatchSplitProperties;
//	
//	@Bean
//	CamelContextFactoryBean camelContext() {
//		CamelContextFactoryBean factory = new CamelContextFactoryBean();
//		factory.setId("cwatchAis2CdfContext");
//		factory.setContextScan(new ContextScanDefinition());
//		return factory;
//	}
//	
//	@Bean
//	SpringRouteBuilder routeBuilder() {
//		return new SpringRouteBuilder() {
//			@Override
//			public void configure() throws Exception {
//
//				new GsonDataFormat();
//				
//				from("amqremote:topic:"+cwatchSplitProperties.getBatchTopicName()+"?clientId=cwatchListenServiceBatch")
//				.id("batchReceiver")
//				.to("direct:ais2cdf")
////				.to("direct:logmsg")
//				;
//				
//				from("direct:logmsg")
//				.unmarshal().gzip()
//				.convertBodyTo(String.class)
//				.to("log:batch?showBody=true")
//				.process(new Processor() {
//					@Override
//					public void process(Exchange exchange) throws Exception {
//						Gson gson = new GsonBuilder().setPrettyPrinting().create();
//						JsonParser jp = new JsonParser();
//						JsonElement je = jp.parse(exchange.getIn().getBody(String.class));
//						exchange.getIn().setBody(gson.toJson(je));					}
//				})
//				.to("amqlocal:topic:vdm.batch.unzip")
//				.to("file:.")
//				;
//			}
//		};
//	}
//	
//	@Bean
//	ActiveMQComponent amqremote() {
//		ActiveMQConfiguration cfg = new ActiveMQConfiguration();
//		cfg.setBrokerURL("tcp://tstarback1:62626");
//		cfg.setErrorHandlerLogStackTrace(false);
//		
//		ActiveMQComponent cmp = new ActiveMQComponent();
//		cmp.setConfiguration(cfg);
//		return cmp;
//	}
//	
//	@Bean
//	ActiveMQComponent amqlocal() {
//		ActiveMQConfiguration cfg = new ActiveMQConfiguration();
//		cfg.setBrokerURL("vm:(broker:(tcp://0.0.0.0:61616)?brokerName=cwatchListenBroker&persistent=false&useShutdownHook=false)");
//		cfg.setErrorHandlerLogStackTrace(false);
//		
//		ActiveMQComponent cmp = new ActiveMQComponent();
//		cmp.setConfiguration(cfg);
//		return cmp;
//	}
//	
	@Override
	public void run(String... args) throws Exception {
	}
//
//	public static void main(String[] args) {
//		SpringApplication.run(ConvertAis2CdfTool.class, args);
//	}
	
}
