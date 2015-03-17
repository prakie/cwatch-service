package org.cwatch.service.cdf;

import javax.jms.ConnectionFactory;
import javax.naming.NamingException;

import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.spring.SpringRouteBuilder;
import org.cwatch.service.CwatchServiceProperties;
import org.cwatch.service.CwatchServicePropertiesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.jms.support.destination.JndiDestinationResolver;
import org.springframework.jndi.JndiTemplate;
import org.springframework.stereotype.Component;

@Component
@Import(CwatchServicePropertiesConfiguration.class)
public class CdfForwardRouteBuilder extends SpringRouteBuilder {

	@Autowired
	CwatchServiceProperties configuration;

	@Override
	public void configure() throws Exception {
		from("direct:cdfPositionForward")
		.id("cdfJmsPositionForward")
		.to("cdfJms:" + configuration.getCdfWeblogicPositionQueue());
		
		from("direct:cdfVoyageForward")
		.id("cdfJmsVoyageForward")
		.to("cdfJms:" + configuration.getCdfWeblogicVoyageQueue());
	}

	@Bean
	JndiTemplate cdfWeblogicJndiTemplate(CwatchServiceProperties configuration) {
		return new JndiTemplate(configuration.getCdfInitialContext().createProperties());
	}
	
	@Bean
	ConnectionFactory cdfWeblogicConnectionFactory(@Qualifier("cdfWeblogicJndiTemplate") JndiTemplate cdfWeblogicJndiTemplate, CwatchServiceProperties configuration) throws NamingException {
		return cdfWeblogicJndiTemplate.lookup(configuration.getCdfWeblogicConnectionFactory(), ConnectionFactory.class);
	}

	@Bean
	JndiDestinationResolver cdfWeblogicJmsDestinationResolver(@Qualifier("cdfWeblogicJndiTemplate") JndiTemplate cdfWeblogicJndiTemplate) {
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
