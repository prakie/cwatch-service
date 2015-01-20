package org.cwatch.service;

import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.apache.camel.model.ContextScanDefinition;
import org.apache.camel.spring.CamelContextFactoryBean;
import org.apache.camel.spring.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.AbstractApplicationContext;

@SpringBootApplication
public class CwatchServiceMain implements CommandLineRunner {

	@Autowired(required=true)
	AbstractApplicationContext applicationContext;

	@Bean(name="stiresVdmCoreServlet")
	ServletRegistrationBean stiresVdmCoreCamelServlet(@Value("${cwatch-service.stires-vdm-core.path:/stires-vdm-core/stiresServices}/*") String stiresVdmCorePath) {
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new CamelHttpTransportServlet(), stiresVdmCorePath);
		servletRegistrationBean.setName("stiresVdmCoreServlet");
		return servletRegistrationBean;
	}
	
	@Bean
	CamelContextFactoryBean camelContext() {
		CamelContextFactoryBean factory = new CamelContextFactoryBean();
		factory.setId("cwatchService");
		factory.setContextScan(new ContextScanDefinition());
		return factory;
	}
	
	@Override
	public void run(String... args) throws Exception {
		Main main = new Main();
		main.setApplicationContext(applicationContext);
		main.run();
	}

	public static void main(String[] args) {
		SpringApplication.run(CwatchServiceMain.class, args);
	}

}
