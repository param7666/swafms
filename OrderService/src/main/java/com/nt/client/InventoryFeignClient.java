package com.nt.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nt.dto.InventoryDTO;
import com.nt.payload.ApiResponse;

@FeignClient(name = "inventory-service")
public interface InventoryFeignClient {
    @GetMapping("/api/inventory/product/{productId}")
    ApiResponse<InventoryDTO> getInventoryByProductId(@PathVariable("productId") Long productId);
    
    @PutMapping("/api/inventory/product/{productId}/reduce")
    ApiResponse<InventoryDTO> reduceInventory(@PathVariable("productId") Long productId, 
                                               @RequestParam("quantity") Integer quantity);
}
