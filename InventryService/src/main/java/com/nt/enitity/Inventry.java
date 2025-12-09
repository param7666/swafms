package com.nt.enitity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="Inventry102")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Inventry {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
    @Column(nullable = false, unique = true)
	private String skuCode;
	
    @Column(nullable = false)
	private Long productId;
	
    @Column(nullable = false)
	private  String productName;
	
    @Column(nullable = false)
	private Integer qty;
	
    @Column(nullable = false)
    private Integer availableQuantity;
    
    @Column(nullable = false)
	private Integer reservedQty;
	
    @Column(nullable = false)
	private Integer availableQty;
	
    @Column(nullable = false)
	private String wareHouse;
	
    @Column(nullable = false)
	private Integer reOrderedQty;
	
    @Column(nullable = false)
	private Integer reOrderLevel;
	
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)

	private InventoryStatus status;
	
	
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = InventoryStatus.ACTIVE;
        }
        if (reservedQty == null) {
        	reservedQty = 0;
        }
        calculateAvailableQuantity();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        calculateAvailableQuantity();
    }
    
    private void calculateAvailableQuantity() {
        this.availableQuantity = this.qty - this.reservedQty;
    }
    
    public enum InventoryStatus {
        ACTIVE,
        LOW_STOCK,
        OUT_OF_STOCK,
        DISCONTINUED
    }

}
