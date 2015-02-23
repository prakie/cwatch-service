package org.cwatch.service.mock;

import org.apache.camel.spring.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.AbstractApplicationContext;

@SpringBootApplication
public class CwatchMockMain implements CommandLineRunner {

	@Autowired(required=true)
	AbstractApplicationContext applicationContext;

	@Override
	public void run(String... args) throws Exception {
		Main main = new Main();
		main.setApplicationContext(applicationContext);
		main.run();
	}

	public static void main(String[] args) {
		SpringApplication.run(CwatchMockMain.class, args);
	}

}
