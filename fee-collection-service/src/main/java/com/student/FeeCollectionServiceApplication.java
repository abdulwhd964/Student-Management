package com.student;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@EnableDiscoveryClient
@OpenAPIDefinition(info = @Info(title = "Rak Bank Assessment", version = "1.0", description = "Fee Collection Service"))
public class FeeCollectionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeeCollectionServiceApplication.class, args);
	}

}
