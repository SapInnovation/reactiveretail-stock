package com.sapient.retail.service.streamkafka.model;


// lombok autogenerates getters, setters, toString() and a builder (see https://projectlombok.org/):
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @Builder
public class StockData {
    private String skuid;
    private String upc;
    private String stock;
}
