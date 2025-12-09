package com.nt.dto;

import java.time.LocalDateTime;
import com.nt.enitity.Inventry.InventoryStatus;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryResponseDTO {
    
    private Long id;
    private String skuCode;
    private Long productId;
    private String productName;
    private Integer quantity;
    private Integer reservedQuantity;
    private Integer availableQuantity;
    private String warehouseLocation;
    private Integer reorderLevel;
    private Integer reorderQuantity;
    private InventoryStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
