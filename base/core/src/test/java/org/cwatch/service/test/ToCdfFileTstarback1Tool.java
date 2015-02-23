package org.cwatch.service.test;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
//@EnableConfigurationProperties(CwatchSplitProperties.class)
//@Import({ListenTstarback1Configuration.class, AisToCdfRouteBuilder.class})
public class ToCdfFileTstarback1Tool implements CommandLineRunner {

//	@Autowired
//	CwatchSplitProperties cwatchSplitProperties;
//	
//	@Bean
//	SpringRouteBuilder lotRouteBuilder() {
//		return new SpringRouteBuilder() {
//			@Override
//			public void configure() throws Exception {
//
//				from("amqremote:topic:"+cwatchSplitProperties.getBatchTopicName()+"?clientId=cwatchListenServiceBatch")
//				.id("batchReceiver")
//				.to("direct:ais2cdf")
//				;
//				
//				from("direct:ais2cdfPositionOut")
//				.setHeader(Exchange.FILE_NAME, new MessageIdSuffixExpression(".xml"))
//				.to("file:position");
//				
//				from("direct:ais2cdfVoyageOut")
//				.setHeader(Exchange.FILE_NAME, new MessageIdSuffixExpression(".xml"))
//				.to("file:voyage");
//				
//				from("direct:ais2cdfInvalidOut")
//				.marshal("aisMessageGsonDataFormat")
//				.setHeader(Exchange.FILE_NAME, new MessageIdSuffixExpression(".json"))
//				.to("file:invalid");
//				
//				from("direct:ais2cdfErrorOut")
//				.marshal("aisMessageGsonDataFormat")
//				.setHeader(Exchange.FILE_NAME, new MessageIdSuffixExpression(".json"))
//				.to("file:error")
//				.setHeader(Exchange.FILE_NAME, new MessageIdSuffixExpression(".container.json"))
//				.setBody(property(AisToCdfRouteBuilder.AIS_MESSAGE_CONTAINER_JSON))
//				.process(LogToFileTstarback1Tool.JSON_PRETTYPRINTER)
//				.to("file:error")
//				;
//				
//				
//			}
//		};
//	}
//	
//	
	@Override
	public void run(String... args) throws Exception {
	}
//
//	public static void main(String[] args) {
//		SpringApplication.run(ToCdfFileTstarback1Tool.class, args);
//	}
//	
}
