package com.medisphere.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MedisphereNotificationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedisphereNotificationServiceApplication.class, args);
	}

}
