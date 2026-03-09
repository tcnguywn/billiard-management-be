package com.backend.billiards_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class BilliardManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(BilliardManagementApplication.class, args);
	}

}
