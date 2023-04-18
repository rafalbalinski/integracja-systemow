package com.example.system.integration;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class SystemIntegrationApplication {

	public static void main(String[] args) {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(SystemIntegrationApplication.class);
		builder.headless(false);
		builder.run(args);
	}

}
