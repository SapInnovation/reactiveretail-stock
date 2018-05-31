package com.sapient.retail.stock.common.repository;

import com.sapient.retail.stock.common.model.Stock;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StockRepository<T extends Stock> {
    Flux<T> findByProductIdOrderByUpc(final String productId);

    Mono<T> findByUpc(final Long upc);

    <S extends T> Mono<S> save(S entity);

    Mono<Void> deleteAll();

    <S extends T> Flux<S> saveAll(Iterable<S> entities);
}
