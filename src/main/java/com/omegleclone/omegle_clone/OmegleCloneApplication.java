package com.omegleclone.omegle_clone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = {"com.omegleclone.controllers", "com.omegleclone.service", "com.omegleclone.security"})
@EnableJpaRepositories("com.omegleclone.repository")
@EntityScan("com.omegleclone.model")
@SpringBootApplication
public class OmegleCloneApplication {

	public static void main(String[] args) {
		SpringApplication.run(OmegleCloneApplication.class, args);
	}

}
