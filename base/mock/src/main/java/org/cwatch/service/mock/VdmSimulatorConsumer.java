package org.cwatch.service.mock;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.cwatch.sim.vdm.JoinSentenceVdmCallback;
import org.cwatch.sim.vdm.VdmSimulator;
import org.cwatch.vdm.AisGsonProxy;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import ssn.ais.processor.SsnAisProcessor;

import com.google.common.base.Throwables;
import com.google.common.collect.Maps;

public class VdmSimulatorConsumer extends DefaultConsumer {

	private VdmSimulator sim;
	private ClassPathXmlApplicationContext ctx;
	
	public VdmSimulatorConsumer(Endpoint endpoint, Processor processor) {
		super(endpoint, processor);
	}
	
	
	@Override
	protected void doStart() throws Exception {
		DefaultListableBeanFactory parentBeanFactory = new DefaultListableBeanFactory();
		parentBeanFactory.registerSingleton("aisgsonproxy", new AisGsonProxy() {
			@Override
			public void sendMonitoring(String sourceProxyName, byte[] zippedStream) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public void sendMessages(String sourceProxyName, byte[] zippedStream) {
				try {
					Exchange exchange = getEndpoint().createExchange();
					exchange.getIn().setBody(zippedStream);
					exchange.getIn().setHeader("SSNPRoxy", sourceProxyName);
					getProcessor().process(exchange);
				} catch (Exception e) {
					throw Throwables.propagate(e);
				}
			}
		});
		GenericApplicationContext parentContext = new GenericApplicationContext(parentBeanFactory);
		parentContext.refresh();
		
		ctx = new ClassPathXmlApplicationContext(new String[] {"classpath:test-stires-sender-context.xml"}, parentContext);
		ctx.refresh();
		
		final SsnAisProcessor ssnAisProcessor = ctx.getBean(SsnAisProcessor.class);

		
		sim = new VdmSimulator();
		sim.setCallback(new JoinSentenceVdmCallback() {
			@Override
			protected void joined(String sentences) {
				Map<String, Object> messageMap = Maps.newHashMap();
				messageMap.put("message", sentences);
				ssnAisProcessor.processIncomingMessage(messageMap);
			}
		});
		sim.setNumberOfVessels(40000);
		
		sim.start();
		
		super.doStart();
	}
	
	@Override
	protected void doStop() throws Exception {
		super.doStop();
		sim.stop();
		ctx.close();
	}

}
