package com.leonardom011.socio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SocioApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocioApplication.class, args);
	}

}
