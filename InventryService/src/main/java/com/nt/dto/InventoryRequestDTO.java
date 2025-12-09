package com.nt.dto;

import jakarta.validation.constraints.*;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryRequestDTO {
    
    @NotBlank(message = "SKU code is required")
    @Size(min = 3, max = 50, message = "SKU code must be between 3 and 50 characters")
    private String skuCode;
    
    @NotNull(message = "Product ID is required")
    @Positive(message = "Product ID must be positive")
    private Long productId;
    
    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 200, message = "Product name must be between 2 and 200 characters")
    private String productName;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;
    
    @NotBlank(message = "Warehouse location is required")
    private String warehouseLocation;
    
    @NotNull(message = "Reorder level is required")
    @Min(value = 0, message = "Reorder level cannot be negative")
    private Integer reorderLevel;
    
    @NotNull(message = "Reorder quantity is required")
    @Positive(message = "Reorder quantity must be positive")
    private Integer reorderQuantity;
}
