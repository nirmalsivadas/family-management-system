package com.family_management_system.config_server;

import com.family_management_system.config_server.config.EnvConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {

	public static void main(String[] args) {
		EnvConfig.load();
		SpringApplication.run(ConfigServerApplication.class, args);
	}

}
