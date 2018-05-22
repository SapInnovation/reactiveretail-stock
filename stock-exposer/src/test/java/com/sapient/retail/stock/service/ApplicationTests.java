package com.sapient.retail.stock.service;

import com.sapient.retail.stock.Application;
import com.sapient.retail.stock.common.builder.GenericBuilder;
import com.sapient.retail.stock.common.model.impl.RetailStock;
import com.sapient.retail.stock.common.model.impl.RetailStockInfo;
import com.sapient.retail.stock.common.repository.StockRepository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.*;

/**
 * @author ragarora
 */
@SpringJUnitConfig(Application.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    StockRepository stockRepository;

    @BeforeEach
    void cleanDB() {
        stockRepository.deleteAll().block();
    }

    @Test
    void testGetStockForProduct() {
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
    void testGetStockForUPC() {
        saveStockInDB(10002, "P_10002", 1);

        webTestClient.get()
                .uri("/stock/upc/{upc}",
                        Collections.singletonMap("upc", 10002))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response ->
                        Assertions.assertThat(response.getResponseBody()).isNotNull())
                .jsonPath("$.upc").isEqualTo(10002L)
                .jsonPath("$.locationId").isEqualTo(10001L)
                .jsonPath("$.availableStock").isEqualTo(519L);
    }

    @Test
    void testGetSkuStockForLocation() {
        saveStockInDB(10001, "P_10001", 1);
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

    private void saveStockInDB(final long upc,
                               final String productId,
                               final int length) {
        List<RetailStock> retailStock = new ArrayList<>();
        for (int i = 0; i < length; i++)
        	retailStock.add(getStock(upc + i, productId));
        stockRepository.saveAll(retailStock).count().block();
    }

    private RetailStock getStock(long upc, String productId) {
        Map<Long, RetailStockInfo> stocks = new HashMap<>();
        stocks.put(10001L, getStockInfo("online", 519L));
        stocks.put(10002L, getStockInfo("Edgware Road", 175L));
        stocks.put(10003L, getStockInfo("Paddington", 59L));
        return GenericBuilder.of(RetailStock::new)
                .with(RetailStock::setUpc, upc)
                .with(RetailStock::setProductId, productId)
                .with(RetailStock::setStock, stocks)
                .build();
    }

    private RetailStockInfo getStockInfo(final String locationName,
                                   final long available) {
        return GenericBuilder.of(RetailStockInfo::new)
                .with(RetailStockInfo::setLocationName, locationName)
                .with(RetailStockInfo::setAvailableStock, available)
                .build();
    }
}
