package com.sapient.retail.streamkafka.service.impl;

import com.sapient.retail.stock.common.model.Stock;
import com.sapient.retail.streamkafka.stream.StockDataStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

@Service
public class StockDataService {
    private final StockDataStreams stockdatastreams;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value(value = "${custom.demandInfoProvider}")
    private String demandInfoProvider;

    @Value(value = "${custom.supplyInfoProvider}")
    private String supplyInfoProvider;

    public StockDataService(final StockDataStreams stockdatastreams) {
        this.stockdatastreams = stockdatastreams;
    }


    public void sendStockDataToKafkaTopic(final Stock productStock) {
        logger.info("Sending stock data {}", productStock);

        MessageChannel messageChannel = stockdatastreams.outboundStockData();
        messageChannel.send(MessageBuilder
                .withPayload(productStock)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());
    }

    /**
     * Method to evaluate supply, demand, available stock and persist them along with the updates to
     * all objects of Stock as per request including information source.
     *
     * @param newStockDetails      updated stock
     * @param existingStockDetails existing stock
     */
    public void evaluateAvailableStock(final Stock newStockDetails, final Stock existingStockDetails) {
        newStockDetails.getStock().forEach((locationId, skuStock) -> {
            if (demandInfoProvider.equals(newStockDetails.getInformationSource())) {
                Long existingSupplyForSku = existingStockDetails.getStock().get(locationId).getSupply();
                Long newAvailableSkuStock = existingSupplyForSku - skuStock.getDemand();
                skuStock.setAvailableStock(newAvailableSkuStock);
                skuStock.setSupply(existingSupplyForSku);
            } else if (supplyInfoProvider.equals(newStockDetails.getInformationSource())) {
                skuStock.setDemand(0L);
                skuStock.setAvailableStock(skuStock.getSupply());
            }
        });
        logger.debug("Existing Prod Stock Details after filter: {}", existingStockDetails);
        existingStockDetails.getStock().putAll(newStockDetails.getStock());
        existingStockDetails.setInformationSource(newStockDetails.getInformationSource());
        existingStockDetails.setProductId(newStockDetails.getProductId());
        existingStockDetails.setPartNumber(newStockDetails.getPartNumber());
    }
}
