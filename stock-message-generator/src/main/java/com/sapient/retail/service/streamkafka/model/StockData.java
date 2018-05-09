package com.sapient.retail.service.streamkafka.model;


// lombok autogenerates getters, setters, toString() and a builder (see https://projectlombok.org/):

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class StockData {
    private String skuid;
    private String upc;
    private String stock;

    @java.beans.ConstructorProperties({"skuid", "upc", "stock"})
    StockData(String skuid, String upc, String stock) {
        this.skuid = skuid;
        this.upc = upc;
        this.stock = stock;
    }

    public static StockDataBuilder builder() {
        return new StockDataBuilder();
    }

    public static class StockDataBuilder {
        private String skuid;
        private String upc;
        private String stock;

        StockDataBuilder() {
        }

        public StockDataBuilder skuid(String skuid) {
            this.skuid = skuid;
            return this;
        }

        public StockDataBuilder upc(String upc) {
            this.upc = upc;
            return this;
        }

        public StockDataBuilder stock(String stock) {
            this.stock = stock;
            return this;
        }

        public StockData build() {
            return new StockData(skuid, upc, stock);
        }

        public String toString() {
            return "StockData.StockDataBuilder(skuid=" + this.skuid + ", upc=" + this.upc + ", stock=" + this.stock + ")";
        }
    }
}
