package org.cwatch.service.routes;

import java.lang.reflect.Type;

import org.apache.camel.component.gson.GsonDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.spring.SpringRouteBuilder;
import org.cwatch.service.CwatchServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import ssn.spm.domain.ais.AisMessageContainer;
import ssn.spm.domain.vdm.commentblock.CbInfoCb;
import ssn.vdm.support.util.CbInfoCbGsonAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Component
@EnableConfigurationProperties(CwatchServiceProperties.class)
public class AisToCdfRouteBuilder extends SpringRouteBuilder {

	@Autowired
	CwatchServiceProperties configuration;
	
	@Override
	public void configure() throws Exception {
		CbInfoCbGsonAdapter<CbInfoCb> adapter = new CbInfoCbGsonAdapter<CbInfoCb>(CbInfoCb.class);
		GsonBuilder gsonBilder = new GsonBuilder();
		gsonBilder.registerTypeAdapter((Type)adapter.getAdapterClass(), adapter);
		Gson gson = gsonBilder.setPrettyPrinting().create();
		GsonDataFormat aisgson = new GsonDataFormat(gson, AisMessageContainer.class);
		
		
		from("direct:ais2cdf")
		.id("ais2cdf")
		.unmarshal().gzip()
		.unmarshal(aisgson)
		.split(simple("${body.aisMessages}"))
			.setHeader("aisMessageType", simple("${body.positionReport.aisMessageType}"))
		.choice()
			.when(header("aisMessageType").isEqualTo(5))
				.to("direct:ais2cdfVoyage")
			.when(header("aisMessageType").in(1,2,3,18.19))
				.to("direct:ais2cdfPosition")
			.otherwise()
				.stop()
		.endChoice()
		.to("log:ais2cdf?showAll=false&showBody=false");
		
		from("direct:ais2cdfVoyage")
		.to("log:ais2cdfVoyage?showAll=false&showBody=false");
		
		from("direct:ais2cdfPosition")
		.to("log:ais2cdfPosition?showAll=false&showBody=false");
	}
	
}