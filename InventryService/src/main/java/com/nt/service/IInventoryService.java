package com.nt.service;

import java.util.List;

import com.nt.dto.InventoryRequestDTO;
import com.nt.dto.InventoryResponseDTO;
import com.nt.dto.QuantityUpdateDTO;
import com.nt.dto.StockAdjustmentDTO;
import com.nt.dto.StockCheckDTO;

public interface IInventoryService {

	 public InventoryResponseDTO addInventory(InventoryRequestDTO requestDTO);
	    
	 public InventoryResponseDTO getInventoryById(Long id);
	    
	 public InventoryResponseDTO getInventoryByProductId(Long productId);
	    
	 public InventoryResponseDTO getInventoryBySkuCode(String skuCode);
	    
	 public StockCheckDTO checkStockAvailability(String skuCode, Integer quantity);
	    
	 public List<StockCheckDTO> bulkStockCheck(List<StockCheckDTO> items);
	    
	 public InventoryResponseDTO updateQuantity(Long id, QuantityUpdateDTO updateDTO);
	    
	 public InventoryResponseDTO reduceStock(StockAdjustmentDTO adjustmentDTO);
	    
	 public InventoryResponseDTO restoreStock(StockAdjustmentDTO adjustmentDTO);
	    
	 public List<InventoryResponseDTO> getAllInventory();
	    
	 public List<InventoryResponseDTO> getLowStockItems();
	    
	 public void deleteInventory(Long id);

}
