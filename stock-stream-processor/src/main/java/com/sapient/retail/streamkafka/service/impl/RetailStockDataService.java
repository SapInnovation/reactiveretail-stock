package com.sapient.retail.streamkafka.service.impl;

import com.sapient.retail.stock.common.model.impl.RetailStock;
import com.sapient.retail.streamkafka.service.StockDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RetailStockDataService extends StockDataService<RetailStock> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value(value = "${custom.demandInfoProvider}")
    private String demandInfoProvider;

    @Value(value = "${custom.supplyInfoProvider}")
    private String supplyInfoProvider;

    /* (non-Javadoc)
     * @see com.sapient.retail.streamkafka.service.StockDataService#sendStockDataToKafkaTopic(com.sapient.retail.stock.common.model.Stock)
     */


    /* (non-Javadoc)
     * @see com.sapient.retail.streamkafka.service.StockDataService#compareAndMergeStock(
     * com.sapient.retail.stock.common.model.Stock, com.sapient.retail.stock.common.model.Stock)
     */
    @Override
    public void compareAndMergeStock(final RetailStock newStockDetails, final RetailStock existingStockDetails) {
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
     *
     * @param newStockDetails      Stock
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
     *
     * @param newStockDetails      Stock
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
     *
     * @param newStockDetails Stock
     */
    public void validateStock(final RetailStock newStockDetails) {
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
