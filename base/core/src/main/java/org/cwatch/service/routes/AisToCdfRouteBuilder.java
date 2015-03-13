package org.cwatch.service.routes;

import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;

import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;
import javax.xml.bind.Marshaller;

import org.apache.camel.Exchange;
import org.apache.camel.component.gson.GsonDataFormat;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.spring.SpringRouteBuilder;
import org.apache.camel.support.ExpressionAdapter;
import org.cwatch.imdate.cdf.v_1_0.ImdateCdfTools;
import org.cwatch.vdm.ais.AisMessage;
import org.cwatch.vdm.ais.AisMessageContainer;
import org.cwatch.vdm.ais.AisPositionReport;
import org.cwatch.vdm.ais.gson.CbInfoCbGsonAdapter;
import org.cwatch.vdm.cdf.AisMessageToCdfConversionException;
import org.cwatch.vdm.cdf.AisMessageToCdfConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import ssn.spm.domain.vdm.commentblock.CbInfoCb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Component
@Import(AisMessageToCdfConverter.class)
public class AisToCdfRouteBuilder extends SpringRouteBuilder {

	public static final String AIS_MESSAGE_CONTAINER_JSON = "AIS_MESSAGE_CONTAINER_JSON";

	public static final String AIS_MESSAGE = "AIS_MESSAGE";

	@Autowired
	AisMessageToCdfConverter ais2cdf;
	
	@Override
	public void configure() throws Exception {
		
		from("direct:ais2cdf")
		.id("ais2cdf")
		.doTry()
			.unmarshal("maybegzip")
			.setProperty(AIS_MESSAGE_CONTAINER_JSON, body())
			.unmarshal("aisMessageContainerGsonDataFormat")
			.setProperty("aisMessageId").simple("${body.messageId}")
			.split(simple("${body.aisMessages}"))
			.doTry()
				.setProperty(AIS_MESSAGE, body())
				.setProperty("aisMessageType", new ExpressionAdapter() {
					@Override
					public Object evaluate(Exchange exchange) {
						AisMessage msg = exchange.getIn().getBody(AisMessage.class);
						AisPositionReport positionReport = msg.getPositionReport();
						if (positionReport==null) {
							throw new AisMessageToCdfConversionException("positionReport missing");
						}
						try {
							return Integer.valueOf(positionReport.getAisMessageType());
						} catch (NumberFormatException e) {
							return null;
						}
					}
				})
				.choice()
					.when(exchangeProperty("aisMessageType").isEqualTo(5))
						.to("direct:ais2cdfVoyage")
					.when(exchangeProperty("aisMessageType").in(1,2,3,18,19))
						.to("direct:ais2cdfPosition")
					.otherwise()
						.to("direct:ais2cdfOther")
			.endDoTry()
			.doCatch(AisMessageToCdfConversionException.class)
				.to("direct:ais2cdfInvalidOut")
			.doCatch(MarshalException.class)
				.to("direct:ais2cdfInvalidOut")
			.doCatch(Exception.class)
				.to("direct:ais2cdfErrorOut")
			.end()
		.endDoTry()
		.doCatch(Exception.class)
			.to("direct:ais2cdfErrorOut")
		.end()
		;
		
		from("direct:ais2cdfPosition")
		.id("ais2cdfPosition")
		.errorHandler(noErrorHandler())
		.bean(ais2cdf, "convertPosition")
		.marshal("positionDataFormat")
		.to("direct:ais2cdfPositionOut")
		;
		
		
		from("direct:ais2cdfVoyage")
		.id("ais2cdfVoyage")
		.errorHandler(noErrorHandler())
		.bean(ais2cdf, "convertVoyage")
		.marshal("voyageDataFormat")
		.to("direct:ais2cdfVoyageOut")
		;
		
		from("direct:ais2cdfOther")
		.id("ais2cdfOther")
		.errorHandler(noErrorHandler())
		.to("log:ais2cdf.unknown?level=DEBUG");
		
		
		
	}

	
	
	@Bean
	private JaxbDataFormat positionDataFormat() {
		return new JaxbDataFormat(ImdateCdfTools.createContext()) {
			@Override
			protected Marshaller createMarshaller() throws JAXBException,
					SAXException, FileNotFoundException, MalformedURLException {
				Marshaller m = super.createMarshaller();
				m.setSchema(ImdateCdfTools.getPositionSchema());
				return m;
			}
		};
	}
	
	@Bean
	private JaxbDataFormat voyageDataFormat() {
		return new JaxbDataFormat(ImdateCdfTools.createContext()) {
			@Override
			protected Marshaller createMarshaller() throws JAXBException,
					SAXException, FileNotFoundException, MalformedURLException {
				Marshaller m = super.createMarshaller();
				m.setSchema(ImdateCdfTools.getVoyageSchema());
				return m;
			}
		};
	}
	
	@Bean
	private GsonDataFormat aisMessageGsonDataFormat(Gson aisGgson) {
		GsonDataFormat aisMessageGson = new GsonDataFormat(aisGgson, AisMessage.class);
		return aisMessageGson;
	}

	@Bean
	private GsonDataFormat aisMessageContainerGsonDataFormat(Gson aisGgson) {
		GsonDataFormat aisMessageContainerGson = new GsonDataFormat(aisGgson, AisMessageContainer.class);
		return aisMessageContainerGson;
	}

	@Bean
	private Gson aisGson() {
		CbInfoCbGsonAdapter<CbInfoCb> adapter = new CbInfoCbGsonAdapter<CbInfoCb>(CbInfoCb.class);
		GsonBuilder gsonBilder = new GsonBuilder();
		gsonBilder.registerTypeAdapter((Type)adapter.getAdapterClass(), adapter);
		Gson gson = gsonBilder.setPrettyPrinting().create();
		return gson;
	}

}