package com.github.forinil.hateoasduallayer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class HateoasDualLayerBackendApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(HateoasDualLayerBackendApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(HateoasDualLayerBackendApplication.class, args);
	}
}
