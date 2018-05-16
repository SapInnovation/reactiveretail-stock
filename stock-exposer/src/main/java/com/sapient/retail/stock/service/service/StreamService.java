package com.sapient.retail.stock.service.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;
import com.sapient.retail.stock.common.model.Stock;
import com.sapient.retail.stock.common.repository.StockRepository;
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

@Service
public class StreamService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final MongoTemplate template;
    private final MongoCollection<Document> streams;
    private final StockRepository repository;

    public StreamService(final MongoTemplate template,
                         final StockRepository repository) {
        this.template = template;
        String collectionName = Stock.class.getName().toLowerCase();
        streams = this.template.collectionExists(collectionName)
                ? this.template.getCollection(collectionName)
                : this.template.createCollection(collectionName);
        this.repository = repository;
    }

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

    public Flux<Stock> productStock(final String productId) {
        LOGGER.debug("Fetching stock information for product: " + productId);
        return repository.findStocksByProductIdOrderByUpc(productId);
    }
}
