package com.example.buildMart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
public class BuildMartApplication {

	public static void main(String[] args) {
		SpringApplication.run(BuildMartApplication.class, args);
	}
}