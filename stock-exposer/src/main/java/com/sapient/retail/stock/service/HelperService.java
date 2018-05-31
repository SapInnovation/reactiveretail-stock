package com.sapient.retail.stock.service;

import com.sapient.retail.stock.common.model.Response;
import com.sapient.retail.stock.common.model.Stock;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface HelperService<T extends Stock, R extends Response> {
    Mono<T> stockNotFound();

    R buildFromStock(T stock);

    R buildFromStock(T stock, Long locationId);

    Class<T> getTargetClass();
}
