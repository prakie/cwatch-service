package org.cwatch.service.cdf;

import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.spring.SpringRouteBuilder;
import org.cwatch.service.CwatchServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.jms.support.destination.JndiDestinationResolver;
import org.springframework.jndi.JndiTemplate;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties(CwatchServiceProperties.class)
public class CdfForwardRouteBuilder extends SpringRouteBuilder {

	@Autowired
	CwatchServiceProperties configuration;
	
	@Override
	public void configure() throws Exception {
		from("activemq:topic:"+configuration.getCdfPositionTopicName()+"?clientId=cwatchCdfPositionForward")
		.id("cdfPositionForward")
		.to("imdateJms:queue:imdate.l0.queue");
		
		from("activemq:topic:"+configuration.getCdfVoyageTopicName()+"?clientId=cwatchCdfVoyageForward")
		.id("cdfVoyageForward")
		.to("imdateJms:queue:imdate.ovr.queue");
	}
	
	@Bean
	JndiTemplate imdateJndiTemplate(CwatchServiceProperties configuration) {
		Properties props = new Properties();
		props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		props.setProperty(Context.PROVIDER_URL, configuration.getImdateNamingProviderUrl());
		props.setProperty(Context.SECURITY_PRINCIPAL, configuration.getImdateUsername());
		props.setProperty(Context.SECURITY_CREDENTIALS, configuration.getImdatePassword());
		return new JndiTemplate(props);
	}
	
	@Bean
	ConnectionFactory imdateConnectionFactory(JndiTemplate imdateJndiTemplate) throws NamingException {
		return imdateJndiTemplate.lookup("weblogic.jms.ConnectionFactory", ConnectionFactory.class);
	}

	@Bean
	JndiDestinationResolver imdateJmsDestinationResolver(JndiTemplate imdateJndiTemplate) {
		JndiDestinationResolver resolver = new JndiDestinationResolver();
		resolver.setJndiTemplate(imdateJndiTemplate);
		return resolver;
	}
	
	@Bean
	JmsComponent imdateJms(ConnectionFactory imdateConnectionFactory, DestinationResolver imdateJmsDestinationResolver) {
		JmsComponent jms = new JmsComponent();
		jms.setConnectionFactory(imdateConnectionFactory);
		jms.setDestinationResolver(imdateJmsDestinationResolver);
		return jms;
	}

	
}
