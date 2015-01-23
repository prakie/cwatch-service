package org.cwatch.service;

import org.cwatch.split.CwatchSplitConfiguration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({CwatchSplitConfiguration.class})
public class CwatchServiceMain implements CommandLineRunner {
	
	@Override
	public void run(String... args) throws Exception {
	}

	public static void main(String[] args) {
		SpringApplication.run(CwatchServiceMain.class, args);
	}

}
