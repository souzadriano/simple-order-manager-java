package com.souzadriano.som;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

	private ApiInfo apiInfo() {
		return new ApiInfo("Simple Order Manager Rest APIs", "APIs for Simple Order Manager Rest.", "1.0", "Terms of service",
				new Contact("Adriano de Souza", "www.org.com", ""), "License of API", "API license URL",
				Collections.emptyList());
	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.OAS_30).apiInfo(apiInfo()).select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build();
	}
}