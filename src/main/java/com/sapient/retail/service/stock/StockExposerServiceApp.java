package com.sapient.retail.service.stock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

/**
 * @author ragarora
 */
@SpringBootApplication
@EnableReactiveMongoRepositories
public class StockExposerServiceApp {

	public static void main(String[] args) {
		SpringApplication.run(StockExposerServiceApp.class, args);
	}
}
