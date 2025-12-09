package com.nt.exception;

public class InsufficientStockException extends RuntimeException{

	
	private final String skuCode;
    private final Integer requested;
    private final Integer available;

    public InsufficientStockException(String skuCode, Integer requested, Integer available) {
        super(String.format("Insufficient stock for SKU: %s. Requested: %d, Available: %d", 
              skuCode, requested, available));
        this.skuCode = skuCode;
        this.requested = requested;
        this.available = available;
    }
    
    public String getSkuCode() {
        return skuCode;
    }
    
    public Integer getRequested() {
        return requested;
    }
    
    public Integer getAvailable() {
        return available;
    }

}
