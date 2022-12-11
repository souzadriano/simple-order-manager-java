package com.souzadriano.som;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.souzadriano.som.repositories")
public class SimpleOrderManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleOrderManagerApplication.class, args);
	}

}
