package com.github.forinil.hateoasduallayer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.github.forinil.hateoasduallayer.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo("HATEOAS Dual Layer Frontend Facade API",
                "HATEOAS Dual Layer Frontend Facade API",
                "0.0.1-SNAPSHOT",
                "Terms of service",
                new Contact("Konrad Botor", "https://forinil.github.io", "kbotor@gmail.com"),
                "MIT License",
                "https://github.com/Forinil/hateoas-dual-layer/blob/master/LICENSE",
                Collections.emptyList());
    }
}
