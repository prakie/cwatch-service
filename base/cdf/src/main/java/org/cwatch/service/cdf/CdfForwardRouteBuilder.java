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
		.to("cdfJms:" + configuration.getCdfWeblogicPositionQueue());
		
		from("activemq:topic:"+configuration.getCdfVoyageTopicName()+"?clientId=cwatchCdfVoyageForward")
		.id("cdfVoyageForward")
		.to("cdfJms:" + configuration.getCdfWeblogicVoyageQueue());
	}
	
	@Bean
	JndiTemplate cdfWeblogicJndiTemplate(CwatchServiceProperties configuration) {
		Properties props = new Properties();
		props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		props.setProperty(Context.PROVIDER_URL, configuration.getCdfWeblogicNamingProviderUrl());
		props.setProperty(Context.SECURITY_PRINCIPAL, configuration.getCdfWeblogicUsername());
		props.setProperty(Context.SECURITY_CREDENTIALS, configuration.getCdfWeblogicPassword());
		return new JndiTemplate(props);
	}
	
	@Bean
	ConnectionFactory cdfWeblogicConnectionFactory(JndiTemplate cdfWeblogicJndiTemplate) throws NamingException {
		return cdfWeblogicJndiTemplate.lookup("weblogic.jms.ConnectionFactory", ConnectionFactory.class);
	}

	@Bean
	JndiDestinationResolver cdfWeblogicJmsDestinationResolver(JndiTemplate cdfWeblogicJndiTemplate) {
		JndiDestinationResolver resolver = new JndiDestinationResolver();
		resolver.setJndiTemplate(cdfWeblogicJndiTemplate);
		return resolver;
	}
	
	@Bean
	JmsComponent cdfJms(ConnectionFactory cdfWeblogicConnectionFactory, DestinationResolver cdfWeblogicJmsDestinationResolver) {
		JmsComponent jms = new JmsComponent();
		jms.setConnectionFactory(cdfWeblogicConnectionFactory);
		jms.setDestinationResolver(cdfWeblogicJmsDestinationResolver);
		return jms;
	}

	
}
