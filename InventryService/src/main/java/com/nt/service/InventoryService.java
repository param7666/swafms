package com.nt.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.nt.dto.InventoryRequestDTO;
import com.nt.dto.InventoryResponseDTO;
import com.nt.dto.QuantityUpdateDTO;
import com.nt.dto.StockAdjustmentDTO;
import com.nt.dto.StockCheckDTO;
import com.nt.enitity.Inventry;
import com.nt.enitity.Inventry.InventoryStatus;
import com.nt.exception.DuplicateInventoryException;
import com.nt.exception.InsufficientStockException;
import com.nt.exception.InvalidInventoryOperationException;
import com.nt.exception.InventoryNotFoundException;
import com.nt.repository.InventoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class InventoryService implements IInventoryService {
	
	private final InventoryRepository repo;

	
	
	@Override
	public InventoryResponseDTO addInventory(InventoryRequestDTO requestDTO) {
		log.info("InventoryService.addInventory()");
		
		if(repo.existsBySkuCode(requestDTO.getSkuCode())) {
			log.error("Inventory already exists for SKU: " + requestDTO.getSkuCode());
			throw new DuplicateInventoryException("Inventory already exists for SKU: " + requestDTO.getSkuCode());
		}
		
		Inventry inventory=Inventry.builder()
				.skuCode(requestDTO.getSkuCode())
				.productName(requestDTO.getProductName())
				.productId(requestDTO.getProductId())
				.qty(requestDTO.getQuantity())
				.reservedQty(0)
				.wareHouse(requestDTO.getWarehouseLocation())
				.reOrderLevel(requestDTO.getReorderLevel())
				.reservedQty(requestDTO.getReorderQuantity())
				.status(determineStatus(requestDTO.getQuantity(), requestDTO.getReorderLevel()))
				.build();
		
		Inventry savedInventroy=repo.save(inventory);
		log.info("Successfully added into inventory with id{}",inventory.getId());
		
		InventoryResponseDTO res=new InventoryResponseDTO();
		BeanUtils.copyProperties(savedInventroy, res);
		return res;
		
	}

	@Override
	@Transactional(readOnly = true)
	public InventoryResponseDTO getInventoryById(Long id) {
		log.info("Fetching invetory details based on id: {}", id);
		Inventry inventory=repo.findById(id).orElseThrow(()->new InventoryNotFoundException("Inventory not found with ID: " + id));
		InventoryResponseDTO res=new InventoryResponseDTO();
		BeanUtils.copyProperties(inventory, res);
		return res;
	}

	@Override
	@Transactional(readOnly = true)
	public InventoryResponseDTO getInventoryByProductId(Long productId) {
		log.info("Fetching invetory details based on Product id: {}", productId);
		
		Inventry inv=repo.findByProductId(productId).orElseThrow(()->new InventoryNotFoundException("Inventory not found with Product ID: " + productId));
		InventoryResponseDTO res=new InventoryResponseDTO();
		BeanUtils.copyProperties(inv, res);
		return res;
	}

	@Override
	@Transactional(readOnly = true)
	public InventoryResponseDTO getInventoryBySkuCode(String skuCode) {
		log.info("Fetching invetory details based on SKU Code: {}", skuCode);
		Inventry inv=repo.findBySkuCode(skuCode).orElseThrow(()->new InventoryNotFoundException("Inventory not found with skucode: " + skuCode));
		InventoryResponseDTO res=new InventoryResponseDTO();
		BeanUtils.copyProperties(inv, res);
		return res;
	}

	@Override
	@Transactional(readOnly = true)
	public StockCheckDTO checkStockAvailability(String skuCode, Integer quantity) {
		log.info("Checking stock availability for SKU: {}, Quantity: {}", skuCode, quantity);
        
        Inventry inventory = repo.findBySkuCode(skuCode)
                .orElseThrow(() -> new InventoryNotFoundException(
                        "Inventory not found for SKU: " + skuCode));
        
        boolean isAvailable = inventory.getAvailableQuantity() >= quantity;
        String message = isAvailable 
                ? "Stock available" 
                : String.format("Insufficient stock. Available: %d, Requested: %d", 
                        inventory.getAvailableQuantity(), quantity);
        
        return StockCheckDTO.builder()
                .skuCode(skuCode)
                .quantity(quantity)
                .isAvailable(isAvailable)
                .availableQuantity(inventory.getAvailableQuantity())
                .message(message)
                .build();

	}

	@Override
	@Transactional(readOnly = true)
	public List<StockCheckDTO> bulkStockCheck(List<StockCheckDTO> items) {
        log.info("Performing bulk stock check for {} items", items.size());
        
        return items.stream().map(item-> checkStockAvailability(item.getSkuCode(), item.getQuantity()))
        		.collect(Collectors.toList());
	}

	@Override 
	public InventoryResponseDTO updateQuantity(Long id, QuantityUpdateDTO updateDTO) {
		log.info("Updating Quantity of id {}",id);
		
		Inventry in=repo.findById(id).orElseThrow(()-> new InventoryNotFoundException("Inventory not found releted to id "+id) );
		if (updateDTO.getQuantity() < in.getReservedQty()) {
            throw new InvalidInventoryOperationException(
                    String.format("Cannot set quantity below reserved amount. Reserved: %d, Attempted: %d",
                            in.getReservedQty(), updateDTO.getQuantity()));
        }
        
        in.setQty(updateDTO.getQuantity());
        in.setStatus(determineStatus(in.getAvailableQuantity(), 
        		in.getReOrderLevel()));
        
        Inventry updatedInventory = repo.save(in);
        log.info("Successfully updated inventory quantity for ID: {}", id);
        InventoryResponseDTO res=new InventoryResponseDTO();
        BeanUtils.copyProperties(updatedInventory, res);
		return null;
	}

	@Override
	public InventoryResponseDTO reduceStock(StockAdjustmentDTO adjustmentDTO) {
		log.info("Reducing stock for SKU: {}, Quantity: {}", 
                adjustmentDTO.getSkuCode(), adjustmentDTO.getQuantity());
        
        Inventry inventory = repo.findBySkuCodeWithLock(adjustmentDTO.getSkuCode())
                .orElseThrow(() -> new InventoryNotFoundException(
                        "Inventory not found for SKU: " + adjustmentDTO.getSkuCode()));
        
        if (inventory.getAvailableQuantity() < adjustmentDTO.getQuantity()) {
            throw new InsufficientStockException(
                    adjustmentDTO.getSkuCode(),
                    adjustmentDTO.getQuantity(),
                    inventory.getAvailableQuantity());
        }
        
        inventory.setReservedQty(inventory.getReservedQty() + adjustmentDTO.getQuantity());
        inventory.setStatus(determineStatus(inventory.getAvailableQuantity(), 
                inventory.getReOrderLevel()));
        
        Inventry updatedInventory = repo.save(inventory);
        log.info("Successfully reduced stock for SKU: {}", adjustmentDTO.getSkuCode());
        
        InventoryResponseDTO res=new InventoryResponseDTO();
        BeanUtils.copyProperties(updatedInventory, res);
        return res;

	}

	@Override
	public InventoryResponseDTO restoreStock(StockAdjustmentDTO adjustmentDTO) {
		log.info("Restoring stock for SKU: {}, Quantity: {}", 
                adjustmentDTO.getSkuCode(), adjustmentDTO.getQuantity());
        
        Inventry inventory = repo.findBySkuCodeWithLock(adjustmentDTO.getSkuCode())
                .orElseThrow(() -> new InventoryNotFoundException(
                        "Inventory not found for SKU: " + adjustmentDTO.getSkuCode()));
        
        if (inventory.getReservedQty() < adjustmentDTO.getQuantity()) {
            throw new InvalidInventoryOperationException(
                    String.format("Cannot restore more than reserved. Reserved: %d, Attempted: %d",
                            inventory.getReservedQty(), adjustmentDTO.getQuantity()));
        }
        
        inventory.setReservedQty(inventory.getReservedQty() - adjustmentDTO.getQuantity());
        inventory.setStatus(determineStatus(inventory.getAvailableQuantity(), 
                inventory.getReOrderLevel()));
        
        Inventry updatedInventory = repo.save(inventory);
        log.info("Successfully restored stock for SKU: {}", adjustmentDTO.getSkuCode());
        
        InventoryResponseDTO res=new InventoryResponseDTO();
        BeanUtils.copyProperties(updatedInventory, res);
        return res;
	}

	@Override
	public List<InventoryResponseDTO> getAllInventory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InventoryResponseDTO> getLowStockItems() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteInventory(Long id) {
		// TODO Auto-generated method stub
		
	}
	
	
	 private InventoryStatus determineStatus(Integer availableQuantity, Integer reorderLevel) {
	        if (availableQuantity == 0) {
	            return InventoryStatus.OUT_OF_STOCK;
	        } else if (availableQuantity <= reorderLevel) {
	            return InventoryStatus.LOW_STOCK;
	        }
	        return InventoryStatus.ACTIVE;
	    }


}
