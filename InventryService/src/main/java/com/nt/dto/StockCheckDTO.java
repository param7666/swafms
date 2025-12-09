package com.nt.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockCheckDTO {
    
    @NotBlank(message = "SKU code is required")
    private String skuCode;
    
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private Integer quantity;
    
    private Boolean isAvailable;
    private Integer availableQuantity;
    private String message;
}
