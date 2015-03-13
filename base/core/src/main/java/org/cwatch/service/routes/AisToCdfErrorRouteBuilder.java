package org.cwatch.service.routes;

import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class AisToCdfErrorRouteBuilder extends SpringRouteBuilder {

	@Override
	public void configure() throws Exception {
		errorHandler(
				defaultErrorHandler()
				.logExhaustedMessageHistory(false)
		);
		
		from("direct:ais2cdfInvalidOut")
		.id("cdfInvalidOut")
		.to("log:ais2cdf.invalid?level=DEBUG&showBody=false&showCaughtException=true&showStackTrace=false")
		.setBody(exchangeProperty(AisToCdfRouteBuilder.AIS_MESSAGE))
		.marshal("aisMessageGsonDataFormat")
		.setHeader("ais2cdfInvalidReason", exceptionMessage())
		.to("direct:ais2cdfInvalidLetter");
		
		from("direct:ais2cdfErrorOut")
		.id("cdfErrorOut")
		.setBody(exchangeProperty(AisToCdfRouteBuilder.AIS_MESSAGE))
		.marshal("aisMessageGsonDataFormat")
		.to("log:ais2cdf.error?level=WARN&showAll=true&multiline=true&skipBodyLineSeparator=false")
		.setHeader("ais2cdfErrorReason", exceptionMessage())
		.to("direct:ais2cdfErrorLetter");
		
	}
	
}