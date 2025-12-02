package com.nt;

import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableEurekaServer
@Slf4j
public class EurekaServerApplication {

	private static Logger logger=org.slf4j.LoggerFactory.getLogger("EurekaServerApplication");
	public static void main(String[] args) {
		SpringApplication.run(EurekaServerApplication.class, args);
		logger.info("Eureka Server Stared on :: 8761");
		
	}

}
