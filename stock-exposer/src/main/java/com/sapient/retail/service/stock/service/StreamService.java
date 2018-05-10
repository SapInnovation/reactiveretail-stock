package com.sapient.retail.service.stock.service;

import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;
import com.sapient.retail.service.stock.db.beans.Stock;
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

    public StreamService(final MongoTemplate template) {
        this.template = template;
        streams = this.template.collectionExists("stock")
                ? this.template.getCollection("stock")
                : this.template.createCollection("stock");
    }

    private ChangeStreamIterable<Document> createMongoStream(final long skuId) {
        ChangeStreamIterable<Document> documentChanges = streams.watch(Collections.singletonList(
                Aggregates.match(
                        and(
                                in("operationType", asList("update", "replace")),
                                eq("fullDocument._id", skuId)))
        )).fullDocument(FullDocument.UPDATE_LOOKUP);
        LOGGER.info("Registering MongoStream for skuId: " + skuId);
        return documentChanges;
    }

    public Flux<Stock> registerStream(final int skuId) {
        return Flux.create(stream -> createMongoStream(skuId)
                .maxAwaitTime(10, TimeUnit.MINUTES)
                .forEach((Consumer<ChangeStreamDocument<Document>>) document -> {
                    LOGGER.info("Operation Type: " + document.getOperationType());
                    Stock stockUpdate = template.getConverter().read(Stock.class,
                            document.getFullDocument());
                    LOGGER.info("Full Document: " + stockUpdate);
                    LOGGER.info("Request skuId:" + skuId +
                            ", Current Event skuId:" + stockUpdate.getSkuId());
                    stream.next(stockUpdate);
                }));
    }
}
