package com.sapient.retail.stock.service;

import com.sapient.retail.stock.common.model.Response;
import com.sapient.retail.stock.common.model.Stock;
import reactor.core.publisher.Mono;

public interface HelperService<T extends Stock, R extends Response> {
    Mono<T> stockNotFound();

    R buildFromStock(T stock);

    R buildFromStock(T stock, Long locationId);
}
