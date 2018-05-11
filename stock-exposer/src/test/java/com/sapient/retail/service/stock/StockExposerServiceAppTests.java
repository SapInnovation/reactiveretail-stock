package com.sapient.retail.service.stock;

import com.sapient.retail.service.stock.db.beans.ProductStock;
import com.sapient.retail.service.stock.db.repository.ProductStockRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

/**
 * @author ragarora
 */
@SpringJUnitConfig(StockExposerServiceApp.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StockExposerServiceAppTests {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    ProductStockRepository productStockRepository;

    @Test
    public void testCreateProductStock() {
        ProductStock productStock = new ProductStock("1001", 5000, "London Waterloo");

        webTestClient.post().uri("/stock")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(productStock), ProductStock.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.skuId").isNotEmpty()
                .jsonPath("$.location").isEqualTo("London Paddington")
                .jsonPath("$.availableStock").isEqualTo("1001");
    }

    @Test
    public void testGetStockForAllProducts() {
        webTestClient.get().uri("/stock")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(ProductStock.class);
    }

    @Test
    public void testGetStockForProduct() {
        ProductStock productStock = productStockRepository.save(
                new ProductStock("1001", 500, "London Waterloo")).block();

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
        ProductStock productStock = productStockRepository.save(
                new ProductStock("1002", 501, "London Waterloo")).block();

        ProductStock newProductStock = new ProductStock("1002", 51, "London Waterloo");

        webTestClient.put()
                .uri("/stock/{productId}", Collections.singletonMap("productId", productStock.getProductId()))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(newProductStock), ProductStock.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.availableStock").isEqualTo(51)
                .jsonPath("$.productId").isEqualTo("1002")
                .jsonPath("$.location").isEqualTo("London Waterloo");
    }
}
