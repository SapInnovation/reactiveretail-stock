package com.sapient.retail.stock.service.service;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static java.util.Arrays.asList;

import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;
import com.sapient.retail.stock.common.model.Stock;
import com.sapient.retail.stock.common.repository.StockRepository;

import reactor.core.publisher.Flux;

@Service
public class StockStreamService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final MongoTemplate template;
    private final MongoCollection<Document> streams;
    private final StockRepository repository;

    public StockStreamService(final MongoTemplate template,
                         final StockRepository repository) {
        this.template = template;
        String collectionName = Stock.class.getName().toLowerCase();
        streams = this.template.collectionExists(collectionName)
                ? this.template.getCollection(collectionName)
                : this.template.createCollection(collectionName);
        this.repository = repository;
    }

    /**
     * Method to open stream for requested product with any SKU/location/stock getting updated in DB collection.
     * @param productId
     * @return Flux<Stock> for all updates to requested product as stream.
     */
    public Flux<Stock> stockStream(final String productId) {
        LOGGER.info("Registering MongoStream for Product: " + productId);
        return Flux.create(stream -> streams
                .watch(Collections.singletonList(
                        Aggregates.match(
                                and(in("operationType", asList("update", "replace")),
                                        eq("fullDocument.productId", productId)))))
                .fullDocument(FullDocument.UPDATE_LOOKUP)
                .maxAwaitTime(10, TimeUnit.MINUTES)
                .forEach((Consumer<ChangeStreamDocument<Document>>) document -> {
                    LOGGER.debug("Operation Type: " + document.getOperationType());
                    Stock updates = template.getConverter().read(Stock.class,
                            document.getFullDocument());
                    LOGGER.debug("Full Document: " + updates);
                    LOGGER.debug("Request ProductId:" + productId +
                            ", Current Event ProductId:" + updates.getProductId());
                    stream.next(updates);
                }));
    }

    /**
     * Method to open stream for requested UPC with any location/stock getting updated in DB collection.
     * @param upc
     * @return Flux<Stock> for all updates to requested UPC as stream.
     */
    public Flux<Stock> skuStockStream(final Long upc) {
        LOGGER.info("Registering MongoStream for UPC: " + upc);
        return Flux.create(stream -> streams
                .watch(Collections.singletonList(
                        Aggregates.match(
                                and(in("operationType", asList("update", "replace")),
                                        eq("fullDocument.upc", upc)))))
                .fullDocument(FullDocument.UPDATE_LOOKUP)
                .maxAwaitTime(10, TimeUnit.MINUTES)
                .forEach((Consumer<ChangeStreamDocument<Document>>) document -> {
                    LOGGER.debug("Operation Type: " + document.getOperationType());
                    Stock updates = template.getConverter().read(Stock.class,
                            document.getFullDocument());
                    LOGGER.debug("Full Document: " + updates);
                    LOGGER.debug("Request UPC:" + upc +
                            ", Current Event UPC:" + updates.getUpc());
                    stream.next(updates);
                }));
    }

    /**
     * Method to open a stream and respond back with stock information for all products
     * @return Flux<Stock> for all available products in DB collection
     */
    public Flux<Stock> allStockStream() {
        LOGGER.info("Registering MongoStream for All products");
        return Flux.create(stream -> streams
                .watch(Collections.singletonList(
                        Aggregates.match(
                                and(in("operationType", asList("update", "replace"))))))
                .fullDocument(FullDocument.UPDATE_LOOKUP)
                .maxAwaitTime(10, TimeUnit.MINUTES)
                .forEach((Consumer<ChangeStreamDocument<Document>>) document -> {
                    LOGGER.debug("Operation Type: " + document.getOperationType());
                    Stock updates = template.getConverter().read(Stock.class,
                            document.getFullDocument());
                    LOGGER.debug("Full Document: " + updates);
                    LOGGER.debug("Current Event ProductId:" + updates.getProductId());
                    stream.next(updates);
                }));
    }
}
