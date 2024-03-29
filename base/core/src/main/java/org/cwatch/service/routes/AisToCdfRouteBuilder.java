package org.cwatch.service.routes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;

import org.apache.camel.Exchange;
import org.apache.camel.component.gson.GsonDataFormat;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.spring.SpringRouteBuilder;
import org.apache.camel.support.ExpressionAdapter;
import org.cwatch.imdate.cdf.v_1_0.ImdateCdfTools;
import org.cwatch.service.CwatchServiceProperties;
import org.cwatch.vdm.ais.AisMessage;
import org.cwatch.vdm.ais.AisMessageContainer;
import org.cwatch.vdm.ais.AisPositionReport;
import org.cwatch.vdm.ais.gson.CbInfoCbGsonAdapter;
import org.cwatch.vdm.cdf.AisMessageToCdfConversionException;
import org.cwatch.vdm.cdf.AisMessageToCdfConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import ssn.spm.domain.vdm.commentblock.CbInfoCb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

@Component
@Import(AisMessageToCdfConverter.class)
@EnableConfigurationProperties(CwatchServiceProperties.class)
public class AisToCdfRouteBuilder extends SpringRouteBuilder {

	public static final String AIS_MESSAGE_CONTAINER_JSON = "AIS_MESSAGE_CONTAINER_JSON";

	public static final String AIS_MESSAGE = "AIS_MESSAGE";

	@Autowired
	AisMessageToCdfConverter ais2cdf;
	
	@Autowired
	CwatchServiceProperties properties;
	
	@Override
	public void configure() throws Exception {
		
		from("direct:ais2cdf")
		.id("ais2cdf")
		.doTry()
			.unmarshal("maybegzip")
			.setProperty(AIS_MESSAGE_CONTAINER_JSON, body())
			.unmarshal("aisMessageContainerGsonDataFormat")
			.setProperty("aisMessageId").simple("${body.messageId}")
			.split(simple("${body.aisMessages}")).parallelProcessing()
			.doTry()
				.setProperty(AIS_MESSAGE, body())
				.setProperty("aisMessageType", new ExpressionAdapter() {
					
					long futureMillisDiff = TimeUnit.MILLISECONDS.convert(properties.getTimestampFutureThreshold(), properties.getTimestampFutureThresholdUnit());
					
					@Override
					public Object evaluate(Exchange exchange) {
						AisMessage msg = exchange.getIn().getBody(AisMessage.class);
						AisPositionReport positionReport = msg.getPositionReport();
						if (positionReport==null) {
							throw new AisMessageToCdfConversionException("positionReport missing");
						}
						if (positionReport.getTimeL() > System.currentTimeMillis() + futureMillisDiff) {
							throw new AisMessageToCdfConversionException(String.format("message timestamp in future beyond threshold (%d msec)", futureMillisDiff));
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
		.setBody(constant("RECEIVED"))
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
			
			private Schema positionSchema;

			private Schema getPositionSchema() {
				if (positionSchema == null) {
					synchronized (this) {
						if (positionSchema == null) {
							positionSchema = ImdateCdfTools.getPositionSchema();
						}
					}
				}
				return positionSchema;
			}
			
			@Override
			protected Marshaller createMarshaller() throws JAXBException,
					SAXException, FileNotFoundException, MalformedURLException {
				Marshaller m = super.createMarshaller();
				m.setSchema(getPositionSchema());
				return m;
			}
		};
	}
	
	@Bean
	private JaxbDataFormat voyageDataFormat() {
		return new JaxbDataFormat(ImdateCdfTools.createContext()) {
			
			private Schema voyageSchema;

			private Schema getVoyageSchema() {
				if (voyageSchema == null) {
					synchronized (this) {
						if (voyageSchema == null) {
							voyageSchema = ImdateCdfTools.getVoyageSchema();
						}
					}
				}
				return voyageSchema;
			}
			
			@Override
			protected Marshaller createMarshaller() throws JAXBException,
					SAXException, FileNotFoundException, MalformedURLException {
				Marshaller m = super.createMarshaller();
				m.setSchema(getVoyageSchema());
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
		gsonBilder.registerTypeAdapterFactory(new TypeAdapterFactory() {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
				Class<? super T> rawType = typeToken.getRawType();
				if (!Enum.class.isAssignableFrom(rawType)
						|| rawType == Enum.class) {
					return null;
				}
				if (!rawType.isEnum()) {
					rawType = rawType.getSuperclass(); // handle anonymous
														// subclasses
				}
				return (TypeAdapter<T>) new ValidEnumTypeAdapter(rawType);
			}
		});
		Gson gson = gsonBilder.setPrettyPrinting().create();
		return gson;
	}
	
	private static final class ValidEnumTypeAdapter<T extends Enum<T>> extends
			TypeAdapter<T> {
		private final Map<String, T> nameToConstant = new HashMap<String, T>();
		private final Map<T, String> constantToName = new HashMap<T, String>();
		private final Class<T> classOfT;

		public ValidEnumTypeAdapter(Class<T> classOfT) {
			this.classOfT = classOfT;
			try {
				for (T constant : classOfT.getEnumConstants()) {
					String name = constant.name();
					SerializedName annotation = classOfT.getField(name)
							.getAnnotation(SerializedName.class);
					if (annotation != null) {
						name = annotation.value();
					}
					nameToConstant.put(name, constant);
					constantToName.put(constant, name);
				}
			} catch (NoSuchFieldException e) {
				throw new AssertionError();
			}
		}

		public T read(JsonReader in) throws IOException {
			if (in.peek() == JsonToken.NULL) {
				in.nextNull();
				return null;
			}
			String enumName = in.nextString();
			if (!nameToConstant.containsKey(enumName)) {
				throw new IllegalArgumentException(String.format("Unknown value '%s' for type %s", enumName,  classOfT));
			}
			return nameToConstant.get(enumName);
		}

		public void write(JsonWriter out, T value) throws IOException {
			out.value(value == null ? null : constantToName.get(value));
		}
	}

}