package com.sapient.retail.streamkafka;

import com.sapient.retail.streamkafka.stream.StockDataStreams;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories (basePackages = {"com.sapient.retail.stock.common.repository"})
@EnableBinding(StockDataStreams.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
