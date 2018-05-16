package com.sapient.retail.stock.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

/**
 * @author ragarora
 */
@SpringBootApplication
@EnableReactiveMongoRepositories (basePackages = {"com.sapient.retail.stock.common.repository"})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
