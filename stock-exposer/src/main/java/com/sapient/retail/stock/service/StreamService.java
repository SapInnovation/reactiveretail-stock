package com.sapient.retail.stock.service;

import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;
import com.sapient.retail.stock.common.model.Response;
import com.sapient.retail.stock.common.model.Stock;
import com.sapient.retail.stock.common.model.impl.StockResponse;
import com.sapient.retail.stock.service.impl.RetailHelperService;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Filters.in;
import static java.util.Arrays.asList;

/**
 * Service class for creating Flux Streams of {@link StockResponse}. These streams will be
 * pushed to clients as SSEs (Server Sent Events).
 */
@Service
public class StreamService<T extends Stock, R extends Response> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MongoTemplate template;
    private final HelperService<T, R> helper;
    private final StockService<T, R> stockService;
    private final MessageService messageService;

    @Value(value = "${custom.collectionName}")
    private String collectionName;

    @Value(value = "${custom.streamDuration}")
    private int streamDuration;

    /**
     * Constructor for dependency injection.
     *
     * @param template the Mongo Template
     * @param helper   instance of the {@link RetailHelperService}
     */
    public StreamService(final MongoTemplate template,
                         final HelperService<T, R> helper,
                         final StockService<T, R> stockService,
                         final MessageService messageService) {
        this.template = template;
        this.helper = helper;
        this.stockService = stockService;
        this.messageService = messageService;
    }

    /**
     * Method to open stream for requested product with any SKU/location/stock getting updated in DB collection.
     *
     * @param productId the product ID
     * @return Flux stream of {@link StockResponse} for all updates to requested product
     */
    public Flux<List<R>> stockStream(final String productId) {
        logger.info("Registering MongoStream for Product: " + productId);
        return Flux.concat(stockService.productStock(productId),
                Flux.push(stream -> {
                    MessageHandler handler = msg -> {
                        logger.info("Stock update received. ProductId: " + productId
                                + "updated product: " + msg);
                        if (productId.equals(Stock.class.cast(msg.getPayload())
                                .getProductId())) {
                            stockService.productStock(productId)
                                    .subscribe(stream::next);
                        }
                    };
                    registerHandler(stream, handler);
                })).take(Duration.ofMinutes(streamDuration));
    }

    private void registerHandler(final FluxSink<?> stream,
                                 final MessageHandler handler) {
        SubscribableChannel stockChannel = messageService.getProductStockChannel();
        stockChannel.subscribe(handler);
        stream.onCancel(() -> stockChannel.unsubscribe(handler));
        stream.onDispose(() -> stockChannel.unsubscribe(handler));
    }

    /**
     * Method to open stream for requested UPC with any location/stock getting updated in DB collection.
     *
     * @param upc the upc for the SKU
     * @return Flux stream of {@link StockResponse} for all updates to requested UPC
     */
    public Flux<R> skuStockStream(final Long upc) {
        logger.info("Registering MongoStream for UPC: " + upc);
        return Flux.concat(stockService.skuStock(upc),
                Flux.push(stream -> {
                    MessageHandler handler = msg -> {
                        if (upc.equals(Stock.class.cast(msg.getPayload())
                                .getUpc())) {
                            stockService.skuStock(upc)
                                    .subscribe(stream::next);
                        }
                    };
                    registerHandler(stream, handler);
                })).take(Duration.ofMinutes(streamDuration));
    }

    /**
     * Method to open a stream and respond back with stock information for all products
     */
    @Async
    public void registerStream() {
        logger.info("Initializing MongoStreams");
        template.getCollection(collectionName)
                .watch(Collections.singletonList(match(in("operationType", asList("insert", "update", "replace", "delete")))))
                .fullDocument(FullDocument.UPDATE_LOOKUP)
                .forEach((Consumer<ChangeStreamDocument<Document>>) document -> {
                    logger.debug("Operation Type: " + document.getOperationType());
                    Stock updates = template.getConverter().read(helper.getTargetClass(),
                            document.getFullDocument());
                    logger.debug("Full Document: " + updates);
                    logger.debug("Current Event ProductId: " + updates.getProductId());
                    messageService.publishMessage(updates);
                });
    }
}
