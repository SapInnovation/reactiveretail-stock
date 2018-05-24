package com.sapient.retail.streamkafka.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import com.sapient.retail.stock.common.model.impl.RetailStock;
import com.sapient.retail.streamkafka.service.StockDataService;
import com.sapient.retail.streamkafka.stream.StockDataStreams;

@Service
public class StockDataServiceImpl implements StockDataService<RetailStock> {
    private final StockDataStreams stockdatastreams;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Value(value = "${custom.demandInfoProvider}")
    private String demandInfoProvider;

    @Value(value = "${custom.supplyInfoProvider}")
    private String supplyInfoProvider;
    
    public StockDataServiceImpl(StockDataStreams stockdatastreams) {
        this.stockdatastreams = stockdatastreams;
    }

    /* (non-Javadoc)
	 * @see com.sapient.retail.streamkafka.service.StockDataService#sendStockDataToKafkaTopic(com.sapient.retail.stock.common.model.Stock)
	 */
    @Override
	public void sendStockDataToKafkaTopic(final RetailStock productStock) {
    	logger.info("Sending stock data {}", productStock);

        MessageChannel messageChannel = stockdatastreams.outboundStockData();
        messageChannel.send(MessageBuilder
                .withPayload(productStock)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());
    }

    /* (non-Javadoc)
	 * @see com.sapient.retail.streamkafka.service.StockDataService#evaluateAvailableStock(com.sapient.retail.stock.common.model.Stock, com.sapient.retail.stock.common.model.Stock)
	 */
	@Override
	public void evaluateAvailableStock(RetailStock newStockDetails, RetailStock existingStockDetails) {
		newStockDetails.getStock().forEach((locationId, skuStock) -> {
			if (demandInfoProvider.equals(newStockDetails.getInformationSource())) {
				Long existingSupplyForSku = newStockDetails.getStock().get(locationId).getSupply();
				Long newAvailableSkuStock = existingSupplyForSku - skuStock.getDemand();
				skuStock.setAvailableStock(newAvailableSkuStock<0 ? 0 : newAvailableSkuStock);
				skuStock.setSupply(existingSupplyForSku);
			} else if (supplyInfoProvider.equals(newStockDetails.getInformationSource())) {
				skuStock.setDemand(new Long(0));
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
