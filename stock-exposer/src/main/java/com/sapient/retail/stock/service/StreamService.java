package com.sapient.retail.stock.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;
import com.sapient.retail.stock.common.model.Response;
import com.sapient.retail.stock.common.model.Stock;
import com.sapient.retail.stock.common.model.impl.RetailStock;
import com.sapient.retail.stock.common.model.impl.StockResponse;

import com.sapient.retail.stock.service.impl.RetailHelperService;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.*;
import static java.util.Arrays.asList;

/**
 * Service class for creating Flux Streams of {@link StockResponse}. These streams will be
 * pushed to clients as SSEs (Server Sent Events).
 */
@Service
public class StreamService<T extends Stock, R extends Response> {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final MongoTemplate template;
    private final MongoCollection<Document> streams;
    private final HelperService<T, R> helper;

    /**
     * Constructor for dependency injection.
     *
     * @param template the Mongo Template
     * @param helper   instance of the {@link RetailHelperService}
     */
    public StreamService(final MongoTemplate template,
                         final HelperService<T, R> helper) {
        this.template = template;
        this.helper = helper;
        String collectionName = RetailStock.class.getName().toLowerCase();
        streams = this.template.collectionExists(collectionName)
                ? this.template.getCollection(collectionName)
                : this.template.createCollection(collectionName);
    }

    /**
     * Method to open stream for requested product with any SKU/location/stock getting updated in DB collection.
     *
     * @param productId the product ID
     * @return Flux stream of {@link StockResponse} for all updates to requested product
     */
    public Flux<R> stockStream(final String productId, Class<T> clazz) {
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
                    T updates = template.getConverter().read(clazz,
                            document.getFullDocument());
                    LOGGER.debug("Full Document: " + updates);
                    LOGGER.debug("Request ProductId:" + productId +
                            ", Current Event ProductId:" + updates.getProductId());
                    stream.next(helper.buildFromStock(updates));
                }));
    }

    /**
     * Method to open stream for requested UPC with any location/stock getting updated in DB collection.
     *
     * @param upc the upc for the SKU
     * @return Flux stream of {@link StockResponse} for all updates to requested UPC
     */
    public Flux<R> skuStockStream(final Long upc, Class<T> clazz) {
        LOGGER.info("Registering MongoStream for UPC: " + upc);
        return Flux.create(stream -> streams
                .watch(Collections.singletonList(
                        Aggregates.match(
                                and(in("operationType", asList("update", "replace")),
                                        eq("fullDocument._id", upc)))))
                .fullDocument(FullDocument.UPDATE_LOOKUP)
                .maxAwaitTime(10, TimeUnit.MINUTES)
                .forEach((Consumer<ChangeStreamDocument<Document>>) document -> {
                    LOGGER.debug("Operation Type: " + document.getOperationType());
                    T updates = template.getConverter().read(clazz,
                            document.getFullDocument());
                    LOGGER.debug("Full Document: " + updates);
                    LOGGER.debug("Request UPC:" + upc +
                            ", Current Event UPC:" + updates.getUpc());
                    stream.next(helper.buildFromStock(updates));
                }));
    }

    /**
     * Method to open a stream and respond back with stock information for all products
     *
     * @return Flux<Stock> for all available products in DB collection
     */
    public Flux<Stock> allStockStream(Class<T> clazz) {
        LOGGER.info("Registering MongoStream for All products");
        return Flux.create(stream -> streams
                .watch(Collections.singletonList(
                        Aggregates.match(
                                and(in("operationType", asList("update", "replace"))))))
                .fullDocument(FullDocument.UPDATE_LOOKUP)
                .maxAwaitTime(10, TimeUnit.MINUTES)
                .forEach((Consumer<ChangeStreamDocument<Document>>) document -> {
                    LOGGER.debug("Operation Type: " + document.getOperationType());
                    Stock updates = template.getConverter().read(clazz,
                            document.getFullDocument());
                    LOGGER.debug("Full Document: " + updates);
                    LOGGER.debug("Current Event ProductId:" + updates.getProductId());
                    stream.next(updates);
                }));
    }
}
