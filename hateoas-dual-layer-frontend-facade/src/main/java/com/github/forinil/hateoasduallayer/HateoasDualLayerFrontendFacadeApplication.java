package com.github.forinil.hateoasduallayer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HateoasDualLayerFrontendFacadeApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(HateoasDualLayerFrontendFacadeApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(new Class[]{HateoasDualLayerFrontendFacadeApplication.class}, args);
	}
}
