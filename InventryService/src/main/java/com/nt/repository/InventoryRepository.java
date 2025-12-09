package com.nt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nt.enitity.Inventry;

import jakarta.persistence.LockModeType;


public interface InventoryRepository extends JpaRepository<Inventry, Long> {

	Optional<Inventry> findByProductId(Long productId);
	
	Optional<Inventry> findBySkuCode(String skuCode);
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT I FROM Inventry I WHERE I.skuCode= :skuCode")
	Optional<Inventry> findBySkuCodeWithLock(@Param("skuCode")String skuCode);
	
	@Query("SELECT i FROM Inventry i WHERE i.availableQuantity <= i.reOrderLevel")
    List<Inventry> findLowStockItems();
	
	
	@Query("SELECT i FROM Inventry i WHERE i.status = 'ACTIVE' AND i.availableQuantity >= :qty")
    List<Inventry> findAvailableInventry(@Param("qty") Integer qty);
    
    @Modifying
    @Query("UPDATE Inventry i SET i.qty = i.qty - :qty, " +
           "i.reservedQty = i.reservedQty + :qty " +
           "WHERE i.skuCode = :skuCode AND i.availableQuantity >= :qty")
    int reduceStock(@Param("skuCode") String skuCode, @Param("qty") Integer qty);
    
    @Modifying
    @Query("UPDATE Invenry i SET i.qty = i.qty + :qty, " +
           "i.reservedQty = i.reservedQty - :qty " +
           "WHERE i.skuCode = :skuCode")
    int restoreStock(@Param("skuCode") String skuCode, @Param("quantity") Integer quantity);
    
    List<Inventry> findByWareHouse(String wareHouse);
    
    boolean existsBySkuCode(String skuCode);

}
