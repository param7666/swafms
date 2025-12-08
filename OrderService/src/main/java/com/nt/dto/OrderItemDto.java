package com.nt.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {

	private Long id;
	
	@NotNull(message = "Product Id is Required")
	private Long productId;
	@NotNull(message = "Product Name is Required")
	private String productName;
	@NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
	private Integer qty;
	
	private Double totalAmount;
	
	private Double subTotal;
}
