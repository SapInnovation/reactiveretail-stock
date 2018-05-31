package com.sapient.retail.stock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author ragarora
 */
@SpringBootApplication
@EnableReactiveMongoRepositories (basePackages = {"com.sapient.retail.stock.common.repository"})
@EnableAsync
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
