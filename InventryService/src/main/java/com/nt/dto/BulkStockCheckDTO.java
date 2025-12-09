package com.nt.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BulkStockCheckDTO {
    
    @NotEmpty(message = "At least one item is required")
    private java.util.List<StockCheckDTO> items;
}
