package com.sapient.retail.service.stock.rest.router;

import com.sapient.retail.service.stock.rest.handler.StockHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class StockRouter {
    @Bean
    public RouterFunction<ServerResponse> routes(final StockHandler stockHandler) {
        return route(GET("/listStock"), stockHandler::allStock);
    }
}
