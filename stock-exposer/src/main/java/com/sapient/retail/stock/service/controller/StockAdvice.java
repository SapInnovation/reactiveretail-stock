package com.sapient.retail.stock.service.controller;

import com.sapient.retail.stock.service.exception.StockNotFoundException;
import com.sapient.retail.stock.service.model.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class StockAdvice {

    @ExceptionHandler(StockNotFoundException.class)
    public ResponseEntity<Error> handleStockNotFound() {
        return new ResponseEntity<>(Error.stockNotFound(), HttpStatus.OK);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Error> handleGenericError() {
        return new ResponseEntity<>(Error.genericError(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
