package com.family_management_system.family_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FamilyServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FamilyServiceApplication.class, args);
	}

}
