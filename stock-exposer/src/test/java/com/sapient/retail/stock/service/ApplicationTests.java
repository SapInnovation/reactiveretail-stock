package com.sapient.retail.stock.service;

import com.sapient.retail.stock.Application;
import com.sapient.retail.stock.common.builder.GenericBuilder;
import com.sapient.retail.stock.common.model.Stock;
import com.sapient.retail.stock.common.model.StockInfo;
import com.sapient.retail.stock.common.model.impl.RetailStock;
import com.sapient.retail.stock.common.repository.StockRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.*;

@SpringJUnitConfig(Application.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    void cleanDB() {
        saveStockInDB(10001, "P_10001", 1);
    }

    @AfterEach
    void tearDown() {
        stockRepository.deleteAll().block();
    }

    @Test
    void api_StockByProduct_ValidProductId_ShouldComplete() {
        tearDown();
        saveStockInDB(10001, "P_10001", 2);

        webTestClient.get()
                .uri("/stock/product/{productId}",
                        Collections.singletonMap("productId", "P_10001"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response ->
                        Assertions.assertThat(response.getResponseBody()).isNotNull())
                .jsonPath("$.length()").isEqualTo(2)
                .jsonPath("$.[0].productId").isEqualTo("P_10001");
    }

    @Test
    void api_StockByProduct_InvalidProductId_ShouldSend404() {
        webTestClient.get()
                .uri("/stock/product/{productId}",
                        Collections.singletonMap("productId", "P_Invalid"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response ->
                        Assertions.assertThat(response.getResponseBody()).isNotNull())
                .jsonPath("$.code").isEqualTo(404);
    }

    @Test
    void api_StockByUPC_ValidUPC_ShouldComplete() {
        webTestClient.get()
                .uri("/stock/upc/{upc}",
                        Collections.singletonMap("upc", 10001))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response ->
                        Assertions.assertThat(response.getResponseBody()).isNotNull())
                .jsonPath("$.upc").isEqualTo(10001L)
                .jsonPath("$.locationId").isEqualTo(10001L)
                .jsonPath("$.availableStock").isEqualTo(519L);
    }

    @Test
    void api_StockByUPC_InvalidUPC_ShouldSend404() {
        webTestClient.get()
                .uri("/stock/upc/{upc}",
                        Collections.singletonMap("upc", 1))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response ->
                        Assertions.assertThat(response.getResponseBody()).isNotNull())
                .jsonPath("$.code").isEqualTo(404);
    }

    @Test
    void api_StockByUPC_MissingUPC_ShouldSend404() {
        webTestClient.get()
                .uri("/stock/upc/{upc}",
                        Collections.singletonMap("upc", ""))
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void api_StockByUPC_InvalidUPC_ShouldSend400() {
        webTestClient.get()
                .uri("/stock/upc/{upc}",
                        Collections.singletonMap("upc", "abc"))
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .consumeWith(response ->
                        Assertions.assertThat(response.getResponseBody()).isNotNull())
                .jsonPath("$.code").isEqualTo(400);
    }

    @Test
    void api_StockByUPCAndLocation_ShouldComplete() {
        Map<String, Object> uriMap = new HashMap<>();
        uriMap.put("upc", 10001L);
        uriMap.put("locationId", 10002L);

        webTestClient.get()
                .uri("/stock/upc/{upc}/{locationId}", uriMap)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response ->
                        Assertions.assertThat(response.getResponseBody()).isNotNull())
                .jsonPath("$.upc").isEqualTo(10001L)
                .jsonPath("$.locationId").isEqualTo(10002L)
                .jsonPath("$.availableStock").isEqualTo(175L);
    }

    @Test
    void api_StockByUPCAndLocation_InvalidUPC_ShouldSend404() {
        Map<String, Object> uriMap = new HashMap<>();
        uriMap.put("upc", 1L);
        uriMap.put("locationId", 10002L);

        webTestClient.get()
                .uri("/stock/upc/{upc}/{locationId}", uriMap)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response ->
                        Assertions.assertThat(response.getResponseBody()).isNotNull())
                .jsonPath("$.code").isEqualTo(404);
    }

    @Test
    void api_StockByUPCAndLocation_IncorrectUPC_ShouldSend400() {
        Map<String, Object> uriMap = new HashMap<>();
        uriMap.put("upc", "abc");
        uriMap.put("locationId", 10002L);

        webTestClient.get()
                .uri("/stock/upc/{upc}/{locationId}", uriMap)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .consumeWith(response ->
                        Assertions.assertThat(response.getResponseBody()).isNotNull())
                .jsonPath("$.code").isEqualTo(400);
    }

    @Test
    void api_StockByUPCAndLocation_IncorrectLocation_ShouldSend400() {
        Map<String, Object> uriMap = new HashMap<>();
        uriMap.put("upc", 10001L);
        uriMap.put("locationId", "abc");

        webTestClient.get()
                .uri("/stock/upc/{upc}/{locationId}", uriMap)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .consumeWith(response ->
                        Assertions.assertThat(response.getResponseBody()).isNotNull())
                .jsonPath("$.code").isEqualTo(400);
    }

    @Test
    void api_StockByUPCAndLocation_InvalidLocation_ShouldGetNullLocation() {
        Map<String, Object> uriMap = new HashMap<>();
        uriMap.put("upc", 10001L);
        uriMap.put("locationId", 10L);

        webTestClient.get()
                .uri("/stock/upc/{upc}/{locationId}", uriMap)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response ->
                        Assertions.assertThat(response.getResponseBody()).isNotNull())
                .jsonPath("$.upc").isEqualTo(10001L)
                .jsonPath("$.locationName").isEmpty()
                .jsonPath("$.availableStock").isEmpty();
    }

    @Test
    void api_StockByProductAndLocation_ShouldComplete() {
        Map<String, Object> uriMap = new HashMap<>();
        uriMap.put("productId", "P_10001");
        uriMap.put("locationId", 10002L);

        webTestClient.get()
                .uri("/stock/product/{productId}/{locationId}", uriMap)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response ->
                        Assertions.assertThat(response.getResponseBody()).isNotNull())
                .jsonPath("$.[0].upc").isEqualTo(10001L)
                .jsonPath("$.[0].productId").isEqualTo("P_10001")
                .jsonPath("$.[0]locationId").isEqualTo(10002L)
                .jsonPath("$.[0]availableStock").isEqualTo(175L);
    }

    @Test
    void api_StockByProductAndLocation_InvalidProduct_ShouldSend404() {
        Map<String, Object> uriMap = new HashMap<>();
        uriMap.put("productId", "P_1");
        uriMap.put("locationId", 10002L);

        webTestClient.get()
                .uri("/stock/product/{productId}/{locationId}", uriMap)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response ->
                        Assertions.assertThat(response.getResponseBody()).isNotNull())
                .jsonPath("$.code").isEqualTo(404);
    }

    @Test
    void api_StockByProductAndLocation_InvalidLocation_ShouldGetNullLocation() {
        Map<String, Object> uriMap = new HashMap<>();
        uriMap.put("productId", "P_10001");
        uriMap.put("locationId", 1L);

        webTestClient.get()
                .uri("/stock/product/{productId}/{locationId}", uriMap)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response ->
                        Assertions.assertThat(response.getResponseBody()).isNotNull())
                .jsonPath("$.[0].upc").isEqualTo(10001L)
                .jsonPath("$.[0].productId").isEqualTo("P_10001")
                .jsonPath("$.[0]locationId").isEqualTo(1L)
                .jsonPath("$.[0]locationName").isEmpty()
                .jsonPath("$.[0]availableStock").isEmpty();
    }

    @Test
    void api_StockByProductAndLocation_IncorrectLocation_ShouldSend400() {
        Map<String, Object> uriMap = new HashMap<>();
        uriMap.put("productId", "P_10001");
        uriMap.put("locationId", "abc");

        webTestClient.get()
                .uri("/stock/product/{productId}/{locationId}", uriMap)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .consumeWith(response ->
                        Assertions.assertThat(response.getResponseBody()).isNotNull())
                .jsonPath("$.code").isEqualTo(400);
    }


    private void saveStockInDB(final long upc,
                               final String productId,
                               final int length) {
        List<RetailStock> stock = new ArrayList<>();
        for (int i = 0; i < length; i++)
            stock.add((RetailStock)getStock(upc + i, productId));
        stockRepository.saveAll(stock).count().block();
    }

    private Stock getStock(long upc, String productId) {
        Map<Long, StockInfo> stocks = new HashMap<>();
        stocks.put(10001L, getStockInfo("online", 519L));
        stocks.put(10002L, getStockInfo("Edgware Road", 175L));
        stocks.put(10003L, getStockInfo("Paddington", 59L));
        return GenericBuilder.of(RetailStock::new)
                .with(Stock::setUpc, upc)
                .with(Stock::setProductId, productId)
                .with(RetailStock::setStock, stocks)
                .build();
    }

    private StockInfo getStockInfo(final String locationName,
                                   final long available) {
        return GenericBuilder.of(StockInfo::new)
                .with(StockInfo::setLocationName, locationName)
                .with(StockInfo::setAvailableStock, available)
                .build();
    }
}
