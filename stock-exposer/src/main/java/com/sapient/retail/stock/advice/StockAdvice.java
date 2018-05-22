package com.sapient.retail.stock.advice;

import com.sapient.retail.stock.exception.StockNotFoundException;
import com.sapient.retail.stock.model.Error;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Stock Advice for handling Global Exceptions.
 */
@ControllerAdvice
public class StockAdvice {
    /**
     * Handler for {@link StockNotFoundException} which is thrown when the stock
     * information is not available for input request.
     *
     * @return ResponseEntity with {@link Error}
     */
    @ExceptionHandler(StockNotFoundException.class)
    public ResponseEntity<Error> handleStockNotFound() {
        return new ResponseEntity<>(Error.stockNotFound(), HttpStatus.OK);
    }

    /**
     * Handler for any {@link Exception} that do not match specific exceptions.
     *
     * @return ResponseEntity with {@link Error}
     */
    //@ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handleGenericError() {
        return new ResponseEntity<>(Error.genericError(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handler for {@link TypeMismatchException} which is client request does not match
     * required server types.
     *
     * @param typeMismatchException {@link TypeMismatchException} instance
     * @return ResponseEntity with {@link Error}
     */
    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<Error> handleTypeMismatch(final TypeMismatchException typeMismatchException) {
        return new ResponseEntity<>(Error.badInput(typeMismatchException.getPropertyName()),
                HttpStatus.BAD_REQUEST);
    }
}
