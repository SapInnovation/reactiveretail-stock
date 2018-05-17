package com.sapient.retail.service.streamkafka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import com.sapient.retail.stock.common.model.Stock;
import com.sapient.retail.service.streamkafka.stream.StockDataStreams;

@Service
public class StockDataService {
    private final StockDataStreams stockdatastreams;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public StockDataService(StockDataStreams stockdatastreams) {
        this.stockdatastreams = stockdatastreams;
    }

    public void sendStockData(final Stock productStock) {
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
     * @param newStockDetails
     * @param existingStockDetails
     */
	public void evaluateAvailableStock(Stock newStockDetails, Stock existingStockDetails) {
		newStockDetails.getStock().forEach((locationId, skuStock) -> {
			if ("demandInfoProvider".equals(newStockDetails.getInformationSource())) {
				Long existingSupplyForSku = existingStockDetails.getStock().get(locationId).getSupply();
				Long newAvailableSkuStock = existingSupplyForSku - skuStock.getDemand();
				skuStock.setAvailableStock(newAvailableSkuStock<0 ? 0 : newAvailableSkuStock);
				skuStock.setSupply(existingSupplyForSku);
			} else if ("supplyInfoProvider".equals(newStockDetails.getInformationSource())) {
				skuStock.setDemand(new Long(0));
				skuStock.setAvailableStock(skuStock.getSupply());
			}
		});
		logger.debug("Existing Prod Stock Details after filter: {}", existingStockDetails);
		existingStockDetails.getStock().putAll(newStockDetails.getStock());
		existingStockDetails.setInformationSource(newStockDetails.getInformationSource());
	}
}
