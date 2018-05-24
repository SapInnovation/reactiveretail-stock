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
        if (demandInfoProvider.equals(newStockDetails.getInformationSource())) {
        	
        	evaluateStockForDemandProvider(newStockDetails, existingStockDetails);
        } else if (supplyInfoProvider.equals(newStockDetails.getInformationSource())) {
        	
        	evaluateStockForSupplyProvider(newStockDetails, existingStockDetails);
        }
        logger.debug("Existing Prod Stock Details after filter: {}", existingStockDetails);
        existingStockDetails.getStock().putAll(newStockDetails.getStock());
        existingStockDetails.setInformationSource(newStockDetails.getInformationSource());
	}
	
	/**
     * Method to evaluate stock and its properties if requested from Supply information source.
     * @param newStockDetails Stock
     * @param existingStockDetails Stock
     */
	private void evaluateStockForSupplyProvider(final RetailStock newStockDetails, final RetailStock existingStockDetails) {
		newStockDetails.getStock().forEach((locationId, skuStock) -> {
			skuStock.setDemand(0L);
		    skuStock.setSupply((null == skuStock.getSupply() 
		    		|| skuStock.getSupply() < 0L) ? 0
		    				: skuStock.getSupply());
		    skuStock.setAvailableStock(skuStock.getSupply());
		    existingStockDetails.setProductId(newStockDetails.getProductId());
		    existingStockDetails.setPartNumber(newStockDetails.getPartNumber());
		});
	}

	/**
     * Method to evaluate stock and its properties if requested from Demand information source.
     * @param newStockDetails Stock
     * @param existingStockDetails Stock
     */
	private void evaluateStockForDemandProvider(final RetailStock newStockDetails, final RetailStock existingStockDetails) {
		newStockDetails.getStock().forEach((locationId, skuStock) -> {
			Long existingSupplyForSku = existingStockDetails.getStock().get(locationId).getSupply();
		    skuStock.setDemand((null == skuStock.getDemand() 
		    		|| skuStock.getDemand() < 0L) ? 0
		    				: skuStock.getDemand());
		    Long newAvailableSkuStock = ((null == existingSupplyForSku 
		    		|| existingSupplyForSku < 0L) ? 0
		    				: (existingSupplyForSku - skuStock.getDemand()));
		    skuStock.setAvailableStock(newAvailableSkuStock);
		    skuStock.setSupply(existingSupplyForSku);
		});
	}

    /**
     * Method to evaluate supply, demand, available stock for new UPC/Location and persist them
     * along with the updates to all objects of Stock as per request including information source.
     * @param newStockDetails Stock
     */
	public void evaluateNewStock(RetailStock newStockDetails) {
		newStockDetails.getStock().forEach((locationId, skuStock) -> {
			skuStock.setSupply((null == skuStock.getSupply() 
            		|| skuStock.getSupply() < 0L) ? 0
            				: skuStock.getSupply());
			if (demandInfoProvider.equals(newStockDetails.getInformationSource())) {
				skuStock.setAvailableStock(0L);
            } else if (supplyInfoProvider.equals(newStockDetails.getInformationSource())) {
                skuStock.setDemand(0L);
                skuStock.setAvailableStock(skuStock.getSupply());
            }
		});
	}
}
