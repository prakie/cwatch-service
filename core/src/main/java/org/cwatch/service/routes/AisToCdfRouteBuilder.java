package org.cwatch.service.routes;

import java.lang.reflect.Type;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.gson.GsonDataFormat;
import org.apache.camel.spring.SpringRouteBuilder;
import org.cwatch.service.CwatchServiceProperties;
import org.cwatch.vdm.cdf.AisMessageToCdfConversionException;
import org.cwatch.vdm.cdf.AisMessageToCdfConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import ssn.spm.domain.ais.AisMessage;
import ssn.spm.domain.ais.AisMessageContainer;
import ssn.spm.domain.vdm.commentblock.CbInfoCb;
import ssn.vdm.support.util.CbInfoCbGsonAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Component
@Import(AisMessageToCdfConverter.class)
@EnableConfigurationProperties(CwatchServiceProperties.class)
public class AisToCdfRouteBuilder extends SpringRouteBuilder {

	@Autowired
	CwatchServiceProperties configuration;
	
	@Autowired
	AisMessageToCdfConverter ais2cdf;
	
	@Override
	public void configure() throws Exception {
		CbInfoCbGsonAdapter<CbInfoCb> adapter = new CbInfoCbGsonAdapter<CbInfoCb>(CbInfoCb.class);
		GsonBuilder gsonBilder = new GsonBuilder();
		gsonBilder.registerTypeAdapter((Type)adapter.getAdapterClass(), adapter);
		Gson gson = gsonBilder.setPrettyPrinting().create();
		GsonDataFormat aisMessageContainerGson = new GsonDataFormat(gson, AisMessageContainer.class);
		GsonDataFormat aisMessageGson = new GsonDataFormat(gson, AisMessage.class);
		
		
		from("direct:ais2cdf")
		.id("ais2cdf")
		.unmarshal().gzip()
		.unmarshal(aisMessageContainerGson)
		.setProperty("aisMessageId").simple("${body.messageId}")
		.split(simple("${body.aisMessages}"))
			.doTry()
				.setProperty("aisMessageType").ognl("@java.lang.Integer@valueOf(request.body.positionReport.aisMessageType)")
				.choice()
					.when(property("aisMessageType").isEqualTo(5))
						.to("direct:ais2cdfVoyage")
					.when(property("aisMessageType").in(1,2,3,18,19))
						.to("direct:ais2cdfPosition")
					.otherwise()
						.to("direct:ais2cdfOther")
				.endDoTry()
			.doCatch(AisMessageToCdfConversionException.class)
				.to("log:ais2cdf.invalid?level=DEBUG&showBody=false&showCaughtException=true&showStackTrace=false")
				.to("direct:ais2cdfInvalidOut")
			.doCatch(Exception.class)
				.multicast().to("direct:ais2cdfError", "direct:ais2cdfErrorOut")
			.end()
		;
		
		from("direct:ais2cdfPosition")
		.errorHandler(noErrorHandler())
		.bean(ais2cdf, "convertPosition")
		.to("direct:ais2cdfPositionOut")
		;
		
		
		from("direct:ais2cdfVoyage")
		.errorHandler(noErrorHandler())
		.bean(ais2cdf, "convertVoyage")
		.to("direct:ais2cdfVoyageOut")
		;
		
		from("direct:ais2cdfOther")
		.errorHandler(noErrorHandler())
		.to("log:ais2cdf.unknown?level=DEBUG");
		
		from("direct:ais2cdfError")
		.marshal(aisMessageGson)
		.to("log:ais2cdf.error?level=WARN&showAll=true&multiline=true&skipBodyLineSeparator=false")
		;
		
		
	}
	
}