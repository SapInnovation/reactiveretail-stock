package com.sapient.retail.stock.service;

import java.util.Collections;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.google.gson.Gson;
import com.sapient.retail.stock.common.model.Stock;
import com.sapient.retail.stock.common.repository.StockRepository;

import reactor.core.publisher.Mono;

/**
 * @author ragarora
 */
@SpringJUnitConfig(Application.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    StockRepository stockRepository;

    @Test
    public void testCreateProductStock() {
        String stockInput = "{ \"upc\": \"70002\", \"productId\": \"P70002\", \"stock\": [{ \"locationId\": \"10001\", \"locationName\": \"online\", \"availableValue\": \"519\" }, { \"locationId\": \"11001\", \"locationName\": \"Edgware Road\", \"availableValue\": \"175\" }, { \"locationId\": \"11002\", \"locationName\": \"Paddington\", \"availableValue\": \"57\" }] }";
        Gson gson = new Gson();
        Stock stock = gson.fromJson(stockInput, Stock.class);
        //Stock productStock = new Stock("1001", 5000, "London Waterloo");

        webTestClient.post().uri("/stock")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(stock), Stock.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.upc").isNotEmpty()
                .jsonPath("$.productId").isEqualTo("P70002")
                .jsonPath("$.stock.locationId").isEqualTo(10001);
    }

    @Test
    public void testGetStockForAllProducts() {
        webTestClient.get().uri("/stock")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Stock.class);
    }

    @Test
    public void testGetStockForProduct() {
    	String stockInput = "{ \"upc\": \"70002\", \"productId\": \"P70002\", \"stock\": [{ \"locationId\": \"10001\", \"locationName\": \"online\", \"availableValue\": \"519\" }, { \"locationId\": \"11001\", \"locationName\": \"Edgware Road\", \"availableValue\": \"175\" }, { \"locationId\": \"11002\", \"locationName\": \"Paddington\", \"availableValue\": \"57\" }] }";
        Gson gson = new Gson();
        Stock stock = gson.fromJson(stockInput, Stock.class);
    	Stock productStock = stockRepository.save(stock).block();

        webTestClient.get()
                .uri("/stock/{productId}", Collections.singletonMap("productId", productStock.getProductId()))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response ->
                        Assertions.assertThat(response.getResponseBody()).isNotNull());
    }

    @Test
    public void testUpdateProductStock() {
    	String stockInput = "{ \"upc\": \"70002\", \"productId\": \"P70002\", \"stock\": [{ \"locationId\": \"10001\", \"locationName\": \"online\", \"availableValue\": \"519\" }, { \"locationId\": \"11001\", \"locationName\": \"Edgware Road\", \"availableValue\": \"175\" }, { \"locationId\": \"11002\", \"locationName\": \"Paddington\", \"availableValue\": \"57\" }] }";
        Gson gson = new Gson();
        Stock stock = gson.fromJson(stockInput, Stock.class);
    	stockRepository.save(stock).block();

    	String newStockInput = "{ \"upc\": \"70002\", \"productId\": \"P70002\", \"stock\": [{ \"locationId\": \"10001\", \"locationName\": \"online\", \"availableValue\": \"5\" }, { \"locationId\": \"11001\", \"locationName\": \"Edgware Road\", \"availableValue\": \"175\" }, { \"locationId\": \"11002\", \"locationName\": \"Paddington\", \"availableValue\": \"57\" }] }";
    	Stock newStock = gson.fromJson(newStockInput, Stock.class);

        webTestClient.put()
                .uri("/stock/{productId}", Collections.singletonMap("productId", stock.getProductId()))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(newStock), Stock.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.upc").isEqualTo(70002)
                .jsonPath("$.productId").isEqualTo("P70002")
                .jsonPath("$.stock.locationId").isEqualTo(10001);
    }
}
