package com.sapient.retail.stock.service.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;
import com.sapient.retail.stock.common.model.impl.RetailStock;
import com.sapient.retail.stock.model.StockResponse;
import com.sapient.retail.stock.service.HelperService;
import com.sapient.retail.stock.service.StockStreamService;

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
public class StockStreamServiceImpl implements StockStreamService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final MongoTemplate template;
    private final MongoCollection<Document> streams;
    private final HelperService helper;

    /**
     * Constructor for dependency injection.
     *
     * @param template the Mongo Template
     * @param helper   instance of the {@link HelperServiceImpl}
     */
    public StockStreamServiceImpl(final MongoTemplate template,
                              final HelperService helper) {
        this.template = template;
        this.helper = helper;
        String collectionName = RetailStock.class.getName().toLowerCase();
        streams = this.template.collectionExists(collectionName)
                ? this.template.getCollection(collectionName)
                : this.template.createCollection(collectionName);
    }

    /* (non-Javadoc)
	 * @see com.sapient.retail.stock.service.StockStreamService#stockStream(java.lang.String)
	 */
    @Override
	public Flux<StockResponse> stockStream(final String productId) {
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
                    RetailStock updates = template.getConverter().read(RetailStock.class,
                            document.getFullDocument());
                    LOGGER.debug("Full Document: " + updates);
                    LOGGER.debug("Request ProductId:" + productId +
                            ", Current Event ProductId:" + updates.getProductId());
                    stream.next(helper.buildFromStock(updates));
                }));
    }

    /* (non-Javadoc)
	 * @see com.sapient.retail.stock.service.StockStreamService#skuStockStream(java.lang.Long)
	 */
    @Override
	public Flux<StockResponse> skuStockStream(final Long upc) {
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
                    RetailStock updates = template.getConverter().read(RetailStock.class,
                            document.getFullDocument());
                    LOGGER.debug("Full Document: " + updates);
                    LOGGER.debug("Request UPC:" + upc +
                            ", Current Event UPC:" + updates.getUpc());
                    stream.next(helper.buildFromStock(updates));
                }));
    }

    /* (non-Javadoc)
	 * @see com.sapient.retail.stock.service.StockStreamService#allStockStream()
	 */
    @Override
	public Flux<RetailStock> allStockStream() {
        LOGGER.info("Registering MongoStream for All products");
        return Flux.create(stream -> streams
                .watch(Collections.singletonList(
                        Aggregates.match(
                                and(in("operationType", asList("update", "replace"))))))
                .fullDocument(FullDocument.UPDATE_LOOKUP)
                .maxAwaitTime(10, TimeUnit.MINUTES)
                .forEach((Consumer<ChangeStreamDocument<Document>>) document -> {
                    LOGGER.debug("Operation Type: " + document.getOperationType());
                    RetailStock updates = template.getConverter().read(RetailStock.class,
                            document.getFullDocument());
                    LOGGER.debug("Full Document: " + updates);
                    LOGGER.debug("Current Event ProductId:" + updates.getProductId());
                    stream.next(updates);
                }));
    }
}
