package org.cwatch.service;

import io.hawt.config.ConfigFacade;

import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CwatchServiceMain implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {

		Main main = new Main();

		main.addRouteBuilder(new MyRouteBuilder());

		main.run();

	}

	public static void main(String[] args) {
		SpringApplication.run(CwatchServiceMain.class, args);
	}

	private static class MyRouteBuilder extends RouteBuilder {
		@Override
		public void configure() throws Exception {
			from("timer:foo?delay=2000").process(new Processor() {
				public void process(Exchange exchange) throws Exception {
					System.out.println("Invoked timer at " + new Date());
				}
			});
		}
	}

	@Bean
	public ConfigFacade configFacade() throws Exception {
		ConfigFacade config = new ConfigFacade() {
			public boolean isOffline() {
				return true;
			}
			
		};
		config.init();
		return config;
	}
}
