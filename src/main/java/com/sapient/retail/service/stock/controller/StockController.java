package com.sapient.retail.service.stock.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sapient.retail.service.stock.db.beans.ProductStock;
import com.sapient.retail.service.stock.db.repository.ProductStockRepository;
import com.sapient.retail.service.stock.exception.StockNotFoundException;
import com.sapient.retail.service.stock.payload.ErrorResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class StockController {

    @Autowired
    private ProductStockRepository productStockRepository;

    @GetMapping("/stock")
    public Flux<ProductStock> getAllProductStock() {
        return productStockRepository.findAll();
    }

    @PostMapping("/stock")
    public Mono<ProductStock> createStockForProduct(@Valid @RequestBody ProductStock productStock) {
        return productStockRepository.save(productStock);
    }

    @GetMapping("/stock/{productId}")
    public Mono<ResponseEntity<ProductStock>> getStockByProductId(@PathVariable(value = "productId") String productId) {
        return productStockRepository.findById(productId)
                .map(availableStock -> ResponseEntity.ok(availableStock))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/stock/{productId}")
    public Mono<ResponseEntity<ProductStock>> updateProductStock(@PathVariable(value = "productId") String productId,
                                                   @Valid @RequestBody ProductStock productStock) {
        return productStockRepository.findById(productId)
                .flatMap(existingStock -> {
                    existingStock.setAvailableStock(productStock.getAvailableStock());
                    existingStock.setProductId(productStock.getProductId());
                    existingStock.setLocation(productStock.getLocation());
                    //TODO: Add more fields here
                    return productStockRepository.save(existingStock);
                })
                .map(updateStock -> new ResponseEntity<>(updateStock, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    // Stock for all products are Sent to the client as Server Sent Events
    @GetMapping(value = "/stream/stock", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProductStock> streamAllProductStock() {
        return productStockRepository.findAll();
    }


    /*
        Exception Handling
    */

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity handleDuplicateKeyException(DuplicateKeyException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("A ProductStock with the same stock-id already exists"));
    }

    @ExceptionHandler(StockNotFoundException.class)
    public ResponseEntity handleStockNotFoundException(StockNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

}
