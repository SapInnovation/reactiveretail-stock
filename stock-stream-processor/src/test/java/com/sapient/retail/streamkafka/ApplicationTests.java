package com.sapient.retail.streamkafka;

import com.sapient.retail.stock.common.builder.GenericBuilder;
import com.sapient.retail.stock.common.model.Stock;
import com.sapient.retail.stock.common.model.StockInfo;
import com.sapient.retail.stock.common.model.impl.RetailStock;
import com.sapient.retail.stock.common.repository.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@EnableKafka
@SpringJUnitConfig(Application.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(partitions = 1, controlledShutdown = true)
@EnableAutoConfiguration
@ActiveProfiles("test")
class ApplicationTests {
    @Autowired
    private StockRepository<RetailStock> repository;

    @Autowired
    private WebTestClient client;

    @Value(value = "${custom.supplyInfoProvider}")
    private String supplyProvider;

    @Value(value = "${custom.demandInfoProvider}")
    private String demandProvider;

    @BeforeEach
    void setUp() throws InterruptedException {
        client.post()
                .uri("/stockdata")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(supplyStockDummy()), RetailStock.class)
                .exchange();
        Thread.sleep(2000);
        repository.deleteAll().block();
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll().block();
    }

    @Test
    void api_StockByProduct_NullStock_ShouldSend400() {
        client.post()
                .uri("/stockdata")
                .contentType(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void api_StockByProduct_IncompleteStock_ShouldSend400() {
        client.post()
                .uri("/stockdata")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(incompleteStock()), RetailStock.class)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    @Tag(value = "test_with_wait")
    void api_StockByProduct_ValidSupplyStock_ShouldComplete() throws InterruptedException {
        RetailStock supplyStock = supplyStock();
        client.post()
                .uri("/stockdata")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(supplyStock), RetailStock.class)
                .exchange()
                .expectStatus().isAccepted();
        Thread.sleep(2000);
        RetailStock dbStock = repository.findByUpc(supplyStock.getUpc())
                .block();
        assertNotNull(dbStock);
        assertEquals(supplyStock.getUpc(), dbStock.getUpc());
        assertEquals(supplyStock.getProductId(), dbStock.getProductId());
        assertEquals(supplyStock.getInformationSource(), dbStock.getInformationSource());
        assertEquals(supplyStock().getStock().get(10001L).getSupply(),
                dbStock.getStock().get(10001L).getSupply());
        assertEquals(supplyStock().getStock().get(10004L).getSupply(),
                dbStock.getStock().get(10004L).getAvailableStock());

    }

    @Test
    @Tag(value = "test_with_wait")
    void api_StockByProduct_ValidDemandStock_ShouldComplete() throws InterruptedException {
        RetailStock supplStock = supplyStock();
        repository.save(supplStock).block();
        RetailStock demandStock = demandStock();
        client.post()
                .uri("/stockdata")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(demandStock), RetailStock.class)
                .exchange()
                .expectStatus().isAccepted();
        Thread.sleep(2000);
        RetailStock dbStock = repository.findByUpc(demandStock.getUpc())
                .block();
        assertNotNull(dbStock);
        assertEquals(demandStock.getUpc(), dbStock.getUpc());
        assertEquals(supplStock.getProductId(), dbStock.getProductId());
        assertEquals(demandStock.getInformationSource(), dbStock.getInformationSource());
        assertEquals(supplyStock().getStock().get(10001L).getSupply(),
                dbStock.getStock().get(10001L).getSupply());
        assertEquals(demandStock().getStock().get(10001L).getDemand(),
                dbStock.getStock().get(10001L).getDemand());
        assertEquals((Long) (supplyStock().getStock().get(10001L).getSupply()
                        - demandStock().getStock().get(10001L).getDemand()),
                dbStock.getStock().get(10001L).getAvailableStock());

    }

    private RetailStock incompleteStock() {
        return GenericBuilder.of(RetailStock::new)
                .with(Stock::setUpc, 10L)
                .build();
    }

    private RetailStock supplyStockDummy() {
        return supplyStockWithUpc(100L);
    }

    private RetailStock supplyStock() {
        return supplyStockWithUpc(10001L);
    }

    private RetailStock supplyStockWithUpc(final long upc) {
        Map<Long, StockInfo> stocks = new HashMap<>();
        stocks.put(10001L, getStockInfo("online", 519L, 0L));
        stocks.put(10002L, getStockInfo("Edgware Road", 175L, 0L));
        stocks.put(10003L, getStockInfo("Paddington", -89L, 0L));
        stocks.put(10004L, getStockInfo("Uxbridge", 100L, 50L));
        return GenericBuilder.of(RetailStock::new)
                .with(Stock::setUpc, upc)
                .with(Stock::setProductId, "P_10001")
                .with(Stock::setPartNumber, "UPC10001")
                .with(RetailStock::setInformationSource, supplyProvider)
                .with(RetailStock::setStock, stocks)
                .build();
    }

    private RetailStock demandStock() {
        Map<Long, StockInfo> stocks = new HashMap<>();
        stocks.put(10001L, getStockInfo("online", 230L, 20L));
        return GenericBuilder.of(RetailStock::new)
                .with(Stock::setUpc, 10001L)
                .with(Stock::setProductId, "P_10002")
                .with(Stock::setPartNumber, "UPC10002")
                .with(RetailStock::setInformationSource, demandProvider)
                .with(RetailStock::setStock, stocks)
                .build();
    }

    private StockInfo getStockInfo(final String locationName,
                                   final long supply,
                                   final long demand) {
        return GenericBuilder.of(StockInfo::new)
                .with(StockInfo::setLocationName, locationName)
                .with(StockInfo::setSupply, supply)
                .with(StockInfo::setDemand, demand)
                .build();
    }
}
